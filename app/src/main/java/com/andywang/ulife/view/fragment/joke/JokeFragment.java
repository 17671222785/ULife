package com.andywang.ulife.view.fragment.joke;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.andywang.ulife.R;
import com.andywang.ulife.adapter.JokeFragmentAdapter;
import com.andywang.ulife.callback.CollectionCallBack;
import com.andywang.ulife.customview.LoadingPager;
import com.andywang.ulife.entity.calendar.bean.Joke;
import com.andywang.ulife.ui.MainActivity;
import com.andywang.ulife.util.cache.database.CollectionJokeThread;
import com.andywang.ulife.util.cache.database.DBManager;
import com.andywang.ulife.util.network.HttpUtils;
import com.andywang.ulife.util.network.JsonParseTool;
import com.andywang.ulife.util.network.NetworkInfo;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.ListSerializableUtils;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.view.fragment.support.BaseFragment;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by parting_soul on 2016/11/1.
 */

public class JokeFragment extends BaseFragment<Joke> implements PullToRefreshView.OnRefreshListener,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, JokeFragmentAdapter.JokeCollectionCallBack
        , CollectionCallBack<Joke>{
    public static final String NAME = "JokeFragment";

    private JokeFragmentAdapter mJokeFragmentAdapter;

    private ListView mListView;

    private PullToRefreshView mPullToRefreshView;

    private List<Joke> mLists;

    private int mRequestPage = 1;

    private DBManager mDBManager;

    private String mParams;

    private static final int REQUEST_ITEM_NUMS = 20;

    private LinearLayout mLoadMore;

    private int mTotalItemNums;

    private Set<LoadDataAsync> mSets;

    private boolean isLoadingMore;

    private CollectionJokeThread mCollectionJokeThread;

    private CheckBox mCurrentCheckBox;

    private boolean checkBoxState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.funny);
        mDBManager = DBManager.getDBManager(getActivity());
        mSets = new HashSet<LoadDataAsync>();
        mCollectionJokeThread = new CollectionJokeThread();
    }

    @Override
    public View createSuccessPage() {
        View view = View.inflate(getActivity(), R.layout.funny_fragment_layout, null);
        mListView = view.findViewById(R.id.list_view);
        //      mListView.setOnLoadMoreListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mPullToRefreshView = view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void updataUI() {
        mJokeFragmentAdapter = new JokeFragmentAdapter(getActivity(), mLists);
        mJokeFragmentAdapter.setJokeCollectionCallBack(this);
        mListView.setAdapter(mJokeFragmentAdapter);
        mJokeFragmentAdapter.notifyDataSetChanged();
        LogUtils.d(CommonInfo.TAG, "-->lists  " + NAME + " " + mLists.size());
    }


    @Override
    protected String initRequestUrlParam() {
        StringBuilder params = new StringBuilder();
        params.append(CommonInfo.JokeApI.Param.JOKE_REQUEST_PAGE).append("=").append(mRequestPage)
                .append("&").append(CommonInfo.JokeApI.Param.JOKE_REQUEST_PAGE_SIZE).append("=")
                .append(REQUEST_ITEM_NUMS).append("&").append(CommonInfo.JokeApI.Param.JOKE_REQUEST_KEY)
                .append("=").append(CommonInfo.JokeApI.Param.JOKE_REQUEST_KEY_VALUE);
        LogUtils.d(CommonInfo.TAG, "-->" + params.toString());
        return params.toString();
    }


    @Override
    public LoadingPager.LoadState loadData() {
        List<Joke> lists = null;
        if (!NetworkInfo.isNetworkAvailable()) {
            lists = mDBManager.readJokeCacheFromDataBase(mRequestPage);
        } else {
            String json = HttpUtils.HttpPostMethod(CommonInfo.JokeApI.Param.JOKR_REQUEST_URL,
                    initRequestUrlParam(), CommonInfo.ENCODE_TYPE);
            lists = parseJsonData(json);
            List<Joke> collection = mDBManager.readJokeCacheFromDataBase(1);
            updataCollectionData(lists, collection);
            addToDataBase(lists, false);
        }
        mLists = lists;
        return getCheckResult(mLists);
    }


    @Override
    public List<Joke> parseJsonData(String result) {
        return JsonParseTool.parseJokeJsonWidthJSONObject(result, mRequestPage);
    }

    /**
     * 更新收藏的段子
     *
     * @param lists
     * @param collection
     */
    private void updataCollectionData(List<Joke> lists, List<Joke> collection) {
        if (lists != null && collection != null) {
            for (int i = 0; i < lists.size(); i++) {
                for (int j = 0; j < collection.size(); j++) {
                    if (lists.get(i).getHashId().equals(collection.get(j).getHashId())) {
                        lists.get(i).setIs_collected(collection.get(j).is_collected());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        mRequestPage = 1;
        mParams = initRequestUrlParam();
        LoadDataAsync asyn = new LoadDataAsync(true);
        asyn.execute();
        mSets.add(asyn);
    }


    public void onLoadMore() {
        if (!isLoadingMore) {
            mRequestPage++;
            mParams = initRequestUrlParam();
            LoadDataAsync asyn = new LoadDataAsync(false);
            asyn.execute();
            mSets.add(asyn);
        }
    }

    /**
     * 将数据加入数据库
     *
     * @param lists
     */
    public void addToDataBase(final List<Joke> lists, final boolean isPulltoRefresh) {
        final List<Joke> data = ListSerializableUtils.copyList(lists);
        LogUtils.d(CommonInfo.TAG, "---data>" + data.size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //将数据写入数据库
                if (!isPulltoRefresh) {
                    mDBManager.addJokeCaCheToDataBase(data);
                } else {
                    mDBManager.updateJokeCacheFromDataBase(data);
                }
            }
        }).start();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getLastVisiblePosition() == mTotalItemNums - 1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mLoadMore = (LinearLayout) view.getChildAt(view.getLastVisiblePosition() - view.getFirstVisiblePosition()).findViewById(R.id.load_more);
            mLoadMore.setVisibility(View.VISIBLE);
            onLoadMore();
            isLoadingMore = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemNums = totalItemCount;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "on click ", Toast.LENGTH_SHORT).show();
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.is_collectedBox);
        if (checkBox.isChecked()) {
            Toast.makeText(getActivity(), "on click ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void setJokeCollectedState(View v, String hashID, int position, boolean isSelected) {
        mCurrentCheckBox = (CheckBox) v;
        checkBoxState = isSelected;
        if (isSelected) {
            mCollectionJokeThread.setCollectionJoke(this, hashID);
        } else {
            mCollectionJokeThread.cancelCollectionJoke(this, hashID);
        }
    }

    @Override
    public void getResult(List<Joke> lists) {

    }

    @Override
    public void isSuccess(boolean isSuccess) {
        if (!isSuccess) {
            mCurrentCheckBox.setChecked(!checkBoxState);
        }
    }

    class LoadDataAsync extends AsyncTask<Void, Void, List<Joke>> {
        /**
         * 异步任务来自下拉刷新
         */
        private boolean mIsFromPullDownRefresh;

        public LoadDataAsync(boolean fromPullDownRefresh) {
            mIsFromPullDownRefresh = fromPullDownRefresh;
        }

        @Override
        protected List<Joke> doInBackground(Void... params) {
            List<Joke> lists = null;
            if (NetworkInfo.isNetworkAvailable()) {
                String result = HttpUtils.HttpPostMethod(CommonInfo.JokeApI.Param.JOKR_REQUEST_URL,
                        mParams, CommonInfo.ENCODE_TYPE);
                lists = parseJsonData(result);
                addToDataBase(lists, mIsFromPullDownRefresh);
            }
            if (!NetworkInfo.isNetworkAvailable() || lists == null) {
                LogUtils.d(CommonInfo.TAG, "from database");
                lists = mDBManager.readJokeCacheFromDataBase(mRequestPage);
            }
            List<Joke> data = mDBManager.readCollectionJokes();
            updataCollectionData(lists, data);
            return lists;
        }

        @Override
        protected void onPostExecute(List<Joke> result) {
            if (result != null && result.size() != 0) {
                LogUtils.d(CommonInfo.TAG, "joke aa result size " + result.size());
                if (mIsFromPullDownRefresh) {
                    mLists.clear();
                    mLists.addAll(result);
                    mPullToRefreshView.setRefreshing(false);
                } else {
                    mLists.addAll(result);

                }
            } else {
                LogUtils.d(CommonInfo.TAG, "joke aa result null ");
                if (mIsFromPullDownRefresh) {
                    mPullToRefreshView.setRefreshing(false);
                }
            }
            if (mLoadMore != null) {
                mLoadMore.setVisibility(View.GONE);
                isLoadingMore = false;
            }
            mJokeFragmentAdapter.init_is_collected();
            mJokeFragmentAdapter.notifyDataSetChanged();
            if (mSets != null) {
                mSets.remove(this);
            }
            //          mListView.setLoadCompleted();
        }
    }

    public void cancelAllAsync() {
        if (mSets != null) {
            for (AsyncTask task : mSets) {
                task.cancel(false);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelAllAsync();
    }
}
