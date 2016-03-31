package com.ipcmessenger.example.yuejianchao.zhihu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    private ListView newListView;
    private boolean isLoading = false;
    private mainNewsAdapter adapter;
    private List<StoriesEntity> list;
    private RelativeLayout rlrefresh;
    private ImageView ivrefresh;
    private StoriesEntity storiesEntity = new StoriesEntity();
    private Latest latest;
    private SwipeRefreshLayout refreshLayout;

    public mainNewsFragment(){}
    public mainNewsFragment(SwipeRefreshLayout s){
        this.refreshLayout=s;
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
        rlrefresh = (RelativeLayout) view.findViewById(R.id.fragment_main_news_rl_refresh);
        ivrefresh = (ImageView) view.findViewById(R.id.fragment_main_news_iv_refresh);
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
    public void showRefreshLayout(){
        rlrefresh.setVisibility(View.VISIBLE);
        ivrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.news_refresh_anim);
                ivrefresh.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rlrefresh.setVisibility(View.GONE);
                        loadContent();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
    }


    public void parseJson(String response) {

        Gson gson = new Gson();
        latest = gson.fromJson(response, Latest.class);
    }


}
