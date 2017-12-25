package com.andywang.ulife.view.fragment.weichat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.WeiChatDetailFragmentAdapter;
import com.andywang.ulife.callback.CollectionCheckStateNotifiyCallBack;
import com.andywang.ulife.customview.LoadMoreItemListView;
import com.andywang.ulife.customview.LoadingPager;
import com.andywang.ulife.entity.calendar.bean.WeiChat;
import com.andywang.ulife.ui.MainActivity;
import com.andywang.ulife.ui.MessageActivity;
import com.andywang.ulife.util.cache.database.DBManager;
import com.andywang.ulife.util.network.HttpUtils;
import com.andywang.ulife.util.network.JsonParseTool;
import com.andywang.ulife.util.support.CollectionCheckStateManager;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.ListSerializableUtils;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.view.fragment.support.BaseFragment;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import static com.andywang.ulife.util.network.HttpUtils.HttpPostMethod;

/**
 * Created by parting_soul on 2016/11/1.
 */

public class WeiChatFragment extends BaseFragment<WeiChat> implements PullToRefreshView.OnRefreshListener
        , LoadMoreItemListView.LoadMoreItemListener, AdapterView.OnItemClickListener, CollectionCheckStateNotifiyCallBack {
    public static final String NAME = "weichatfragment";

    private List<WeiChat> mLists;

    private String mParams;

    private int mCurrentPage = 1;

    private int mRequestPage = 1;

    private LoadMoreItemListView mListView;

    private PullToRefreshView mPullToRefreshView;

    private WeiChatDetailFragmentAdapter mWeiChatDetailFragmentAdapter;

    public static int REQUEST_ITEM_NUMS = 30;

    public static int REQUEST_MAX_PAGE_NUMS = 17;

    public WeiChat mCurrentSelectedWeiChat;

    public DBManager mDBManager;

    private CollectionCheckStateManager mCollectionCheckStateManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.weichat);
        mParams = initRequestUrlParam();
        mDBManager = DBManager.getDBManager(getActivity());
        mCollectionCheckStateManager = CollectionCheckStateManager.newInstance();
        LogUtils.d(CommonInfo.TAG, "--->111" + "onCreate() " + mParams);
    }

    @Override
    protected String initRequestUrlParam() {
        StringBuilder params = new StringBuilder();
        params.append(CommonInfo.WeiChatAPI.Params.REQUEST_PAGE_NO_NAME).append("=").append(mRequestPage)
                .append("&").append(CommonInfo.WeiChatAPI.Params.REQUEST_NUMS).append("=")
                .append(REQUEST_ITEM_NUMS).append("&").append(CommonInfo.WeiChatAPI.Params.REQUEST_DATATYPE).append("=")
                .append(CommonInfo.WeiChatAPI.Params.REQUEST_TYPE).append("&").append(CommonInfo.WeiChatAPI.Params.REQUEST_KEY_NAME)
                .append("=").append(CommonInfo.WeiChatAPI.Params.REQUEST_KEY_VALUE);
        LogUtils.d(CommonInfo.TAG, "-->" + params.toString());
        return params.toString();
    }

    @Override
    public View createSuccessPage() {
        View view = View.inflate(getActivity(), R.layout.weichat_fragment_layout, null);
        mListView = view.findViewById(R.id.list_view);
        mPullToRefreshView = view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        LogUtils.d(CommonInfo.TAG, "--->111" + "createSuccess()");
        return view;
    }

    @Override
    public void updataUI() {
        //将数据源绑定到适配器
        mWeiChatDetailFragmentAdapter = new WeiChatDetailFragmentAdapter(getActivity(), mLists, mListView);
        //设置可以加载图片
        mWeiChatDetailFragmentAdapter.setIsCanLoadImage(true);
        //为listview设置适配器
        mListView.setAdapter(mWeiChatDetailFragmentAdapter);
        mListView.setOnLoadMoreListener(this);
        mWeiChatDetailFragmentAdapter.notifyDataSetChanged();

        LogUtils.d(CommonInfo.TAG, "-->lists 1 " + mLists.size());
    }

    @Override
    public LoadingPager.LoadState loadData() {
        LogUtils.d(CommonInfo.TAG, "--->111" + "loadData()");
        List<WeiChat> lists = mDBManager.readWeiChatCacheFromDatabase(mRequestPage);
        if (lists == null) {
            String result = HttpPostMethod(CommonInfo.WeiChatAPI.Params.REQUEST_URL,
                    mParams, CommonInfo.ENCODE_TYPE);
            Log.d(CommonInfo.TAG, "-->is from web " + lists + " " + result);
            lists = parseJsonData(result);
            addToDataBase(lists);
        }
        mLists = lists;
        return getCheckResult(mLists);
    }

    @Override
    public List<WeiChat> parseJsonData(String result) {
        return JsonParseTool.parseWeiChatJsonWidthJSONObject(result);
    }

    @Override
    public void onRefresh() {
        mRequestPage = 1;
        mParams = initRequestUrlParam();
        new LoadDataAsync(true).execute();
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }

    private void loadMore() {
        ++mRequestPage;
        mParams = initRequestUrlParam();
        LogUtils.d(CommonInfo.TAG, "weichat aa mparam" + mParams);
        new LoadDataAsync(false).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //当前fragment可见时设置回调接口
        mCollectionCheckStateManager.setNotifyVisibleNewsFragmentCallBack(this);
        mCurrentSelectedWeiChat = mLists.get(position);
        MessageActivity.startActivity(getActivity(), mCurrentSelectedWeiChat.getUrl(),
                mCurrentSelectedWeiChat.getTitle(), mCurrentSelectedWeiChat.is_collected(), CollectionCheckStateManager.FROM_WEICHATFRAGMENT);
    }

    @Override
    public void collectedStateChange(boolean isChange) {
        mCurrentSelectedWeiChat.setIs_collected(isChange);
        mWeiChatDetailFragmentAdapter.notifyDataSetChanged();
    }

    class LoadDataAsync extends AsyncTask<Void, Void, List<WeiChat>> {
        /**
         * 异步任务来自下拉刷新
         */
        private boolean mIsFromPullDownRefresh;

        public LoadDataAsync(boolean fromPullDownRefresh) {
            mIsFromPullDownRefresh = fromPullDownRefresh;
        }

        @Override
        protected List<WeiChat> doInBackground(Void... params) {
            List<WeiChat> lists = mDBManager.readWeiChatCacheFromDatabase(mRequestPage);
            if (lists == null) {
                String result = HttpUtils.HttpPostMethod(CommonInfo.WeiChatAPI.Params.REQUEST_URL,
                        mParams, CommonInfo.ENCODE_TYPE);
                lists = parseJsonData(result);
                LogUtils.d(CommonInfo.TAG, "weichat from web");
                addToDataBase(lists);
            }
            return lists;
        }

        @Override
        protected void onPostExecute(List<WeiChat> result) {
            if (result != null && result.size() != 0) {
                LogUtils.d(CommonInfo.TAG, "weichat aa result size " + result.size());
                if (mIsFromPullDownRefresh) {
                    mLists.clear();
                    mLists.addAll(result);
                    mPullToRefreshView.setRefreshing(false);
                } else {
                    mLists.addAll(result);

                }
                mWeiChatDetailFragmentAdapter.getPicUrl();
            } else {
                LogUtils.d(CommonInfo.TAG, "weichat aa result null ");
                if (mIsFromPullDownRefresh) {
                    mPullToRefreshView.setRefreshing(false);
                }
            }
            mWeiChatDetailFragmentAdapter.notifyDataSetChanged();
            mListView.setLoadCompleted();
        }
    }

    /**
     * 将数据加入数据库
     *
     * @param lists
     */
    public void addToDataBase(final List<WeiChat> lists) {
        final List<WeiChat> data = ListSerializableUtils.copyList(lists);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //将数据写入数据库
                mDBManager.addWeiChatCaCheToDataBase(data);
            }
        }).start();
    }
}
