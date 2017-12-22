package com.andywang.ulife.view.fragment.news;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.NewsInfoAdapter;
import com.andywang.ulife.callback.CollectionCheckStateNotifiyCallBack;
import com.andywang.ulife.entity.calendar.bean.News;
import com.andywang.ulife.entity.calendar.bean.Settings;
import com.andywang.ulife.ui.MessageActivity;
import com.andywang.ulife.util.cache.database.DBManager;
import com.andywang.ulife.util.image.ImageLoader;
import com.andywang.ulife.util.network.AbstractDownLoadHandler;
import com.andywang.ulife.util.network.HttpUtils;
import com.andywang.ulife.util.network.JsonParseTool;
import com.andywang.ulife.util.network.NetworkInfo;
import com.andywang.ulife.util.support.CollectionCheckStateManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.view.fragment.support.BaseMulFragment;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import static com.andywang.ulife.customview.LoadingPager.LoadState;
import static com.andywang.ulife.util.support.CommonInfo.TAG;


/**
 * Created by parting_soul on 2016/10/4.
 * 新闻碎片类
 */

public class NewsDetailFragment extends BaseMulFragment<News> implements AdapterView.OnItemClickListener
        , PullToRefreshView.OnRefreshListener, CollectionCheckStateNotifiyCallBack {
    /**
     * 第三方下拉刷新类
     */
    private PullToRefreshView mPullToRefreshView;
    /**
     * 存放新闻项的listview
     */
    private ListView mListView;
    /**
     * 新闻信息适配器
     */
    private NewsInfoAdapter mNewsInfoAdapter;
    /**
     * 新闻数组
     */
    private List<News> mLists;
    /**
     * 请求地址中的新闻类别参数
     */
    private String mNewTypeParam;
    /**
     * 请求地址的所有参数
     */
    private String mParams;

    /**
     * 数据库管理类
     */
    private DBManager manager;

    /**
     * 旧状态时第一个ListView可见项
     */
    private int oldFirstVisibleItem;
    /**
     * 第一个listview item距离listview的位置
     */
    private int top;

    /**
     * 图片加载类
     */
    private ImageLoader mImageLoader;

    /**
     * 设置对象类
     */
    private Settings mSettings = Settings.newsInstance();

    /**
     * 当前被选中的新闻项
     */
    private News mCurrentSelectedNews;

    /**
     * 收藏状态回调管理类
     */
    private CollectionCheckStateManager mCollectionCheckStateManager;

    /**
     * 消息处理类
     */
    private AbstractDownLoadHandler mHandler = new AbstractDownLoadHandler() {

        /**
         * 该方法在主线程调用
         * @param msg Looper从消息队列取出的消息
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommonInfo.LoaderStatus.DOWNLOAD_FINISH_MSG:
                    updateUI(msg);
                    break;
                default:

            }
            mPullToRefreshView.setRefreshing(false);
        }

        /**
         * 下载错误时要处理的逻辑代码段
         */
        @Override
        protected void showError() {

        }

        /**
         * 下载正确则更新相应的UI
         * @param msg
         */
        @Override
        protected void updateUI(Message msg) {
            //数据下载完成
            if (msg.obj != null) {
                mLists = (List<News>) msg.obj;
                //将数据源绑定到适配器
                mNewsInfoAdapter = new NewsInfoAdapter(getActivity(), mLists, mListView);
                //设置可以加载图片
                mNewsInfoAdapter.setIsCanLoadImage(true);
                //为listview设置适配器
                mListView.setAdapter(mNewsInfoAdapter);
                mNewsInfoAdapter.notifyDataSetChanged();

                LogUtils.d(CommonInfo.TAG, "-->lists 1" + mLists.size());


//                if (msg.what == CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH) {
//                    //恢复原来的位置
//                    mListView.setSelectionFromTop(oldFirstVisibleItem, top);
//                }
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.d(TAG, "+DEBUG onAttach -->fragment");
    }

    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        if (mNewsInfoAdapter != null) {
            //当前页面不可见就设置为不加载图片
            mNewsInfoAdapter.setIsCanLoadImage(false);
            //保存侧滑前listview中第一个可见项目的位置
            oldFirstVisibleItem = mListView.getFirstVisiblePosition();
            View view = mListView.getChildAt(0);
            if (view != null) {
                top = view.getTop();
            }
        }
        LogUtils.d(CommonInfo.TAG, "InvisibletoUser " + oldFirstVisibleItem + " " + top + " " + mNewTypeParam);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewTypeParam = getArguments().getString(CommonInfo.NewsAPI.Params.REQUEST_TYPE_PARAM_NAME);
        //得到所有请求参数
        mParams = initRequestUrlParam();
        //实例化数据库管理类
        manager = DBManager.getDBManager(getActivity());
        //得到图片加载类
        mImageLoader = ImageLoader.newInstance(getActivity());
        mCollectionCheckStateManager = CollectionCheckStateManager.newInstance();
        if (mImageLoader.diskLruCacheIsClosed()) {
            mImageLoader.openDiskLruCache();
        }
        LogUtils.d(TAG, "+onCreate -->fragment " + mNewTypeParam + " " + this);
    }


    /**
     * 拼接http url的地址参数
     *
     * @return String 返回拼接后的参数
     */
    @Override
    protected String initRequestUrlParam() {
        StringBuilder params = new StringBuilder();
        params.append(CommonInfo.NewsAPI.Params.REQUEST_URL_TYPR_NAME).append("=").append(mNewTypeParam)
                .append("&").append(CommonInfo.NewsAPI.Params.REQUEST_URL_KEY_NAME).append("=")
                .append(CommonInfo.NewsAPI.Params.REQUEST_URL_KEY_VALUE);
        return params.toString();
    }

    /**
     * 创建显示数据的界面
     *
     * @return View
     */
    @Override
    public View createSuccessPage() {
        View view = null;
        if (getActivity() != null) {
            view = View.inflate(getActivity(), R.layout.news_detail_fragment, null);
            mListView = (ListView) view.findViewById(R.id.news_lists);
            mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
            mListView.setOnItemClickListener(this);
            mPullToRefreshView.setOnRefreshListener(this);
        }
        Log.d(TAG, "+DEBUG createSuccessPage -->fragment " + mNewTypeParam);
        return view;
    }

    @Override
    public void updataUI() {
        //将数据源绑定到适配器
        mNewsInfoAdapter = new NewsInfoAdapter(getActivity(), mLists, mListView);
        //设置可以加载图片
        mNewsInfoAdapter.setIsCanLoadImage(true);
        //为listview设置适配器
        mListView.setAdapter(mNewsInfoAdapter);
        mNewsInfoAdapter.notifyDataSetChanged();

        LogUtils.d(CommonInfo.TAG, "-->lists 1" + mLists.size());

//        if (msg.what == CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH) {
//            //恢复原来的位置
//            mListView.setSelectionFromTop(oldFirstVisibleItem, top);
//        }
    }

    /**
     * 从网络或数据库加载数据，工作在子线程，不需另外开辟线程<br>
     * 若有网络，则从网络获取数据，若没有则从数据库加载
     */
    @Override
    public LoadState loadData() {
        List<News> lists = null;
        if (!NetworkInfo.isNetworkAvailable()) {
            lists = manager.readNewsCacheFromDatabase(mNewTypeParam);
            LogUtils.d(CommonInfo.TAG, "network unavailable " + mNewTypeParam);
        } else {
            String result = HttpUtils.HttpPostMethod(CommonInfo.NewsAPI.Params.REQUEST_URL,
                    mParams, CommonInfo.ENCODE_TYPE);
            Log.d(CommonInfo.TAG, "-->" + result);
            lists = parseJsonData(result);
        }
        mLists = lists;
        return getCheckResult(mLists);
    }

    /**
     * 解析下载的内容，并把数据缓存入数据库
     *
     * @param result 带解析的网络数据
     * @return List<News>
     */
    @Override
    public List<News> parseJsonData(String result) {
        List<News> lists = null;
        //解析下载的数据
        lists = JsonParseTool.parseNewsJsonWidthJSONObject(result);

        List<News> collection = manager.readCollectionNews();
        updataCollectionData(lists, collection);
        addToDataBase(lists);
//        lists = manager.readNewsCacheFromDatabase(mNewTypeParam);
        return lists;
    }

    /**
     * 将数据加入数据库
     *
     * @param lists
     */
    public void addToDataBase(final List<News> lists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //将数据写入数据库
                manager.updataNewsCacheToDatabase(lists, mNewTypeParam);
            }
        }).start();
    }

    /**
     * 更新收藏的新闻
     *
     * @param lists
     * @param collection
     */
    private void updataCollectionData(List<News> lists, List<News> collection) {
        if (lists != null && collection != null) {
            for (int i = 0; i < lists.size(); i++) {
                for (int j = 0; j < collection.size(); j++) {
                    if (lists.get(i).getTitle().equals(collection.get(j).getTitle())) {
                        lists.get(i).setIs_collected(collection.get(j).is_collected());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, "+DEBUG onActivityCreated -->fragment " + mNewTypeParam);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "+DEBUG onResume -->fragment " + mNewTypeParam);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "+DEBUG onPause -->fragment " + mNewTypeParam);
        if (mImageLoader != null) {
            //将图片同步记录写入journal文件
            mImageLoader.fluchCache();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "+DEBUG onDestroyView -->fragment " + mNewTypeParam);
        //退出当前pager，停止所有加载图片的异步任务
        if (mImageLoader != null) {
            mImageLoader.cancelAllAsyncTask();
        }
        //UI被销毁，item位置重新初始化
        top = 0;
        oldFirstVisibleItem = 0;
        mNewsInfoAdapter = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //当前fragment可见时设置回调接口
        mCollectionCheckStateManager.setNotifyVisibleNewsFragmentCallBack(this);
        mCurrentSelectedNews = mLists.get(position);
        MessageActivity.startActivity(getActivity(), mCurrentSelectedNews.getUrl(),
                mCurrentSelectedNews.getTitle(), mCurrentSelectedNews.is_collected(), CollectionCheckStateManager.FROM_NEWSFRAGMENT);
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> lists = null;
                String result = HttpUtils.HttpPostMethod(CommonInfo.NewsAPI.Params.REQUEST_URL,
                        mParams, CommonInfo.ENCODE_TYPE);
                lists = parseJsonData(result);
                Message msg = Message.obtain();
                msg.obj = lists;
                msg.what = CommonInfo.LoaderStatus.DOWNLOAD_FINISH_MSG;
                mHandler.sendMessage(msg);
            }
        }).start();
    }


    @Override
    public void collectedStateChange(boolean isChange) {
        mCurrentSelectedNews.setIs_collected(isChange);
        mNewsInfoAdapter.notifyDataSetChanged();
        LogUtils.d(CommonInfo.TAG, "---->is changes" + isChange);
    }
}
