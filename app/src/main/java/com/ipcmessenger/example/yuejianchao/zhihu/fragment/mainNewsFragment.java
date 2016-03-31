package com.ipcmessenger.example.yuejianchao.zhihu.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ipcmessenger.example.yuejianchao.zhihu.R;
import com.ipcmessenger.example.yuejianchao.zhihu.Utils.Constant;
import com.ipcmessenger.example.yuejianchao.zhihu.Utils.HttpUtils;
import com.ipcmessenger.example.yuejianchao.zhihu.adapter.mainNewsAdapter;
import com.ipcmessenger.example.yuejianchao.zhihu.data.Latest;
import com.ipcmessenger.example.yuejianchao.zhihu.data.StoriesEntity;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class mainNewsFragment extends Fragment {
    public static final String TAG = mainNewsFragment.class.getSimpleName();
    public static Boolean isrefresh = false;
    private ListView newListView;
    private boolean isLoading = false;
    private mainNewsAdapter adapter;
    private List<StoriesEntity> list;
    private LinearLayout rlrefresh;
    private TextView tvrefresh;
    private StoriesEntity storiesEntity = new StoriesEntity();
    private Latest latest;
    private SwipeRefreshLayout refreshLayout;

    public mainNewsFragment() {
    }

    public mainNewsFragment(SwipeRefreshLayout s) {
        this.refreshLayout = s;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_news, container, false);
        newListView = (ListView) view.findViewById(R.id.fragment_main_news_lv);
        newListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(newListView != null && newListView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = newListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = newListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshLayout.setEnabled(enable);

            }
        });
        rlrefresh = (LinearLayout) view.findViewById(R.id.fragment_main_news_rl_refresh);
        tvrefresh = (TextView) view.findViewById(R.id.fragment_main_news_tv_refresh);
        adapter = new mainNewsAdapter(getActivity());
        newListView.setAdapter(adapter);
        list = new ArrayList<>();
        newListView.setHorizontalScrollBarEnabled(false);
        newListView.setVerticalScrollBarEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadContent();
    }

    //首次进入时加载数据
    public void loadContent() {
        isLoading = true;
        refreshLayout.setRefreshing(true);
        final List<String> images = new ArrayList<>();
        //如果有网络链接
        if (HttpUtils.isNetworkConnected(getActivity())) {
            HttpUtils.get(Constant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, org.apache.http.Header[] headers, String s, Throwable throwable) {
                    refreshLayout.setRefreshing(false);
                    showRefreshLayout();
                }

                @Override
                public void onSuccess(int i, org.apache.http.Header[] headers, String s) {
                    refreshLayout.setRefreshing(false);
                    parseJson(s);
                    storiesEntity.setId(1);
                    storiesEntity.setTitle("123");
                    storiesEntity.setType(1);
                    images.add("http://pic3.zhimg.com/fe27abc8f094510f2d3b4f3706108b56.jpg");
                    storiesEntity.setImages(images);
                    list.add(storiesEntity);
                    list.addAll(latest.getStories());
                    adapter.addData(list);

                }
            });
        } else {
            //如果没有网络连接
            refreshLayout.setRefreshing(false);
            showRefreshLayout();
        }
    }

    //如果没有数据或者没有联网就调用这个函数
    public void showRefreshLayout() {
        rlrefresh.setVisibility(View.VISIBLE);
        tvrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlrefresh.setVisibility(View.GONE);
                loadContent();
            }
        });
    }


    public void parseJson(String response) {

        Gson gson = new Gson();
        latest = gson.fromJson(response, Latest.class);
    }


}
