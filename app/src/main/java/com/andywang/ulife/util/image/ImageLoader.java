package com.andywang.ulife.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.BaseFragmentAdapter;
import com.andywang.ulife.adapter.WeiChatDetailFragmentAdapter;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.util.cache.DiskLruCacheHelper;
import com.andywang.ulife.util.network.HttpUtils;
import com.andywang.ulife.util.network.NetworkInfo;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.DiskLruCache;

/**
 * Created by parting_soul on 2016/10/5.
 * 用于下载图片
 */

public class ImageLoader {
    /**
     * 异步任务集合
     */
    private Set<LoadImageAsynTask> mTaskSets;


    /**
     * 一级缓存，url和图片的映射
     */
    private LruCache<String, Bitmap> mCache;

    /**
     * 获取当前可用的最大内存，以KB为单位
     */
    int maxMemory = (int) ((Runtime.getRuntime().maxMemory()) / 1024);

    /**
     * 一级缓存的大小,最大值为可用缓存的1/8
     */
    private int mCachesMemory = maxMemory / 8;

    /**
     * SD卡缓存类
     */
    private DiskLruCache mDiskLruCache;

    /**
     * 图片加载类的引用，保证只有一个实例
     */
    private static ImageLoader mImageLoader;

    private Context mContext;

    private Settings mSettings = Settings.newsInstance();

    private ImageLoader(Context context) {
        mTaskSets = new HashSet<LoadImageAsynTask>();
        mCache = new LruCache<String, Bitmap>(mCachesMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 每次存入缓存时调用,返回每张图片的大小
                return value.getByteCount() / 1024;
            }
        };
        mContext = context;
        openDiskLruCache();
        LogUtils.i(CommonInfo.TAG, "ImageLoader-->mCachesMemory " + mCachesMemory / 1024);
    }


    /**
     * 打开硬盘缓存
     */
    public void openDiskLruCache() {
        //得到缓存图片的文件夹对象
        File cacheDir = DiskLruCacheHelper.getCacheFile(mContext,
                CommonInfo.Cache.IMAGE_CACHE_DIR_NAME);
        //若该文件不存在则创建文件夹
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        try {
            //生成硬盘缓存管理类 参数依次为缓存文件对象,app版本号，一个key对应多少个键,缓存容量总大小
            mDiskLruCache = DiskLruCache.open(cacheDir, DiskLruCacheHelper.getVersion(mContext),
                    1, CommonInfo.Cache.IMAGE_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保证只有一个ImageLoader对象不存在就创建，存在就返回
     *
     * @param context 上下文对象
     * @return ImageLoader
     */
    public static ImageLoader newInstance(Context context) {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(context);
        }
        return mImageLoader;
    }

    /**
     * 加载从位置start开始到end之间所有的图片<br>
     * 滚动停止时调用，若存在图片则加载，若不存在则从硬盘获取或网络下载
     *
     * @param start    可见新闻项的开始位置
     * @param end      可见新闻项的结束位置
     * @param listView 对应的ListView
     */
    public void loadImage(int start, int end, ListView listView, BaseFragmentAdapter adapter) {
        LogUtils.d(CommonInfo.TAG, "ImageLoader-->loadImage--->start = " + start + " end = " + end);
        for (int i = start; i < end; i++) {
            //找到对应的图片url地址
            String url = null;
            if (adapter instanceof WeiChatDetailFragmentAdapter) {
                if (listView.getAdapter() instanceof HeaderViewListAdapter) {
                    HeaderViewListAdapter adapter1 = (HeaderViewListAdapter) listView.getAdapter();
                    url = ((BaseFragmentAdapter) adapter1.getWrappedAdapter()).IMAGE_URLS[i];
                    LogUtils.d(CommonInfo.TAG, "WeiChat i" + i + "url");
                }
            } else {
                url = adapter.IMAGE_URLS[i];
            }
            //根据url从一级缓存中找是否有该图片
            Bitmap bitmap = getBitmapFromCache(url);
            //根据图片控件Ta属性上绑定的url从listview中找到图片控件
            ImageView imageView = (ImageView) listView.findViewWithTag(url);
            LogUtils.d(CommonInfo.TAG, "ImageLoader---->loadImage " + imageView + " url = " + url);
            if (bitmap != null && imageView != null) {
                //若成功从缓存中找到图片
                imageView.setImageBitmap(bitmap);
                LogUtils.d(CommonInfo.TAG, "ImageLoader-->loadImage--->bitmap from cache");
            } else {
                //找不到图片则从SD卡或者网络下载图片
                LogUtils.d(CommonInfo.TAG, "LoadImage download");
                downLoadImage(url, imageView);
            }
        }
        LogUtils.d(CommonInfo.TAG, "ImageLoader-->loadimage finished");
    }

    /**
     * 根据url加载图片到指定的图片控件<br>
     * 滚动时调用，若缓存中有该图片，则加载，若没有该图片显示默认图片
     *
     * @param url       图片url地址
     * @param imageView 图片控件的引用
     */
    public void loadImage(String url, ImageView imageView) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            //加载缓存中的图片
            imageView.setImageBitmap(bitmap);
//            LogUtils.d(CommonInfo.TAG, "ImageLoader-->loadImage-->url-->bitmap from cache");
        } else {
            //缓存中没有找到该图片，显示默认图片
            imageView.setImageResource(R.mipmap.imageview_default_bc);
//            LogUtils.d(CommonInfo.TAG, "ImageLoader-->loadImage-->url-->bitmap from default");
        }
    }

    /**
     * 以图片的url为键从内存中找到对应的bitmap值
     *
     * @param url
     * @return Bitmap 找到则返回图片，找不到返回null
     */
    @Nullable
    private Bitmap getBitmapFromCache(String url) {
        return mCache.get(url);
    }

    /**
     * 若一级缓存中没有该图片，则加入缓存
     *
     * @param url    图片的url地址(键)
     * @param bitmap 图片Bitmap对象(值)
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (mCache.get(url) == null) {
            mCache.put(url, bitmap);
        }
    }

    /**
     * 停止所有正在执行的异步任务
     */
    public void cancelAllAsyncTask() {
        Iterator<LoadImageAsynTask> it = mTaskSets.iterator();
        while (it.hasNext()) {
            LoadImageAsynTask task = it.next();
            //           task.cancel(false);
            it.remove();
        }
    }

    /**
     * 启动异步任务加载图片，并将异步任务加入集合
     *
     * @param url       图片路径
     * @param imageView 显示图片的控件
     */
    public synchronized void downLoadImage(final String url, ImageView imageView) {
        LoadImageAsynTask task = new LoadImageAsynTask(url, imageView);
        task.execute(url);
        mTaskSets.add(task);
    }

    /**
     * 异步任务加载图片，若图片在硬盘中找到，则取出加入到内存，并显示
     * 若在硬盘中不存在，则从网络下载图片，并且加入硬盘缓存和内存，显示图片
     */
    class LoadImageAsynTask extends AsyncTask<String, Void, Bitmap> {
        /**
         * 待显示图片的控件
         */
        private ImageView mImageView;
        /**
         * 图片的URL
         */
        private String mUrl;

        public LoadImageAsynTask(String url, ImageView imageView) {
            mImageView = imageView;
            mUrl = url;
            LogUtils.d(CommonInfo.TAG, "LoadImage create async");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LogUtils.d(CommonInfo.TAG, "LoadImage onPre");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            LogUtils.d(CommonInfo.TAG, "LoadImage doInBackground");
            DiskLruCache.Snapshot snapshot = null;
            String imageUrl = params[0];
            if (TextUtils.isEmpty(imageUrl)) return null;
            //将url进行MD5加密
            String key = MD5Utils.hashKeyForDisk(imageUrl);
            InputStream in = null;
            //标记图片下载是否成功
            boolean isSuccess = false;
            boolean isFromSD = true;
            try {
                if (mDiskLruCache.isClosed()) {
                    openDiskLruCache();
                }
                //从硬盘缓存缓存取得该缓存对象封装类
                snapshot = mDiskLruCache.get(key);
                if (snapshot == null && (!Settings.is_no_picture_mode || Settings.is_no_picture_mode
                        && NetworkInfo.isWifiAvailable())) {
                    //若没有在缓存中找到，则需要从网络下载
                    //得到写入缓存操作类
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        //得到出输出流
                        OutputStream out = editor.newOutputStream(0);
                        //下载图片并写入到输出流
                        isSuccess = HttpUtils.HttpGetMethod(imageUrl, out);
                        if (isSuccess) {
                            //成功就提交使得写入生效
                            editor.commit();
                        } else {
                            //放弃此次提交
                            editor.abort();
                        }
                        LogUtils.d(CommonInfo.TAG, "ImageLoader-->doBackground-->bitmap from web");
                    }
                    //重新从硬盘缓存缓存取得该缓存对象封装类
                    snapshot = mDiskLruCache.get(key);
                    isFromSD = false;
                }
                if (snapshot != null) {
                    //得到输入流
                    in = snapshot.getInputStream(0);
                    //先将输入流中的数据转化为字符数组，然后缩放至要求的大小，解析为bitmap对象
                    Bitmap bitmap = ImageZoom.decodeSimpleBitmapFromByte(ImageZoom.getBytes(in), CommonInfo.ImageZoomLeve.
                            REQUEST_IMAGE_WIDTH, CommonInfo.ImageZoomLeve.REQUEST_IMAGE_HEIGHT);
                    if (isFromSD)
                        LogUtils.d(CommonInfo.TAG, "ImageLoader-->doBackground---->from SD卡" + bitmap);
                    if (bitmap != null) {
                        //将该对象加入内存中
                        addBitmapToCache(imageUrl, bitmap);
                        return bitmap;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        in = null;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            LogUtils.d(CommonInfo.TAG, "LoadImage onPostExecute mImageView " + mImageView + " murl " + mUrl + " " + mImageView.getTag());
            if (mImageView != null && mImageView.getTag().equals(mUrl)) {
                LogUtils.d(CommonInfo.TAG, "LoadImage onPostExecute bitmap " + bitmap);
                //判断控件的tag标志是否与url一致
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                } else {
                    if (Settings.is_no_picture_mode && !NetworkInfo.isWifiAvailable() && NetworkInfo.isNetworkAvailable()) {
                        mImageView.setImageResource(R.mipmap.imageview_no_pic_mode);
                    } else {
                        mImageView.setImageResource(R.mipmap.imageview_error_bc);
                    }
                }
            }
            //异步任务图片加载完成，移除该任务
            mTaskSets.remove(this);
        }
    }

    /**
     * 将记录同步到journal中
     */
    public void fluchCache() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到图片缓存的大小，以B为单位
     *
     * @return long 图片缓存大小
     */
    public long getImageCacheSize() {
        long size = 0;
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            size = mDiskLruCache.size();
        }
        return size;
    }

    /**
     * 删除图片缓存
     */
    public boolean deleteImageCache() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.delete();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 关闭硬盘缓存
     */
    public void closeDiskLruCache() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当前硬盘缓存是否关闭
     *
     * @return
     */
    public boolean diskLruCacheIsClosed() {
        return mDiskLruCache.isClosed();
    }
}
