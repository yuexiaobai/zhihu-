package com.ipcmessenger.example.yuejianchao.zhihu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.ipcmessenger.example.yuejianchao.zhihu.R;
import com.ipcmessenger.example.yuejianchao.zhihu.Utils.Constant;
import com.ipcmessenger.example.yuejianchao.zhihu.Utils.HttpUtils;
import com.ipcmessenger.example.yuejianchao.zhihu.adapter.mainNewsAdapter;
import com.ipcmessenger.example.yuejianchao.zhihu.data.StoriesEntity;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

public class mainNewsFragment extends Fragment {
    public static final String TAG=mainNewsFragment.class.getSimpleName();
    private ListView newListView;
    private boolean isLoading=false;
    private mainNewsAdapter adapter;
    private List<StoriesEntity> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main_news, container, false);
        newListView=(ListView)view.findViewById(R.id.fragment_main_news_lv);
        adapter=new mainNewsAdapter(getActivity());
        newListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadContent();
        adapter.addData(list);
    }

    //首次进入时加载数据
    public void loadContent() {
        isLoading = true;
        //如果有网络链接
        if(HttpUtils.isNetworkConnected(getActivity())){
            HttpUtils.get(Constant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, org.apache.http.Header[] headers, String s, Throwable throwable) {

                }

                @Override
                public void onSuccess(int i, org.apache.http.Header[] headers, String s) {
                    Log.w(TAG,s);
                }
            });
        }else{
            //如果没有网络连接

        }
    }

    public void parseJson(){

    }



}
