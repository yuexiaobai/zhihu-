package com.ipcmessenger.example.yuejianchao.zhihu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipcmessenger.example.yuejianchao.zhihu.R;
import com.ipcmessenger.example.yuejianchao.zhihu.activity.MainActivity;
import com.ipcmessenger.example.yuejianchao.zhihu.data.StoriesEntity;
import com.ipcmessenger.example.yuejianchao.zhihu.widget.CircleBitmapDisplayer;
import com.ipcmessenger.example.yuejianchao.zhihu.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuejianchao on 16-3-2.
 */
public class mainNewsAdapter extends BaseAdapter{
    private Context context;
    private List<StoriesEntity> datas;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    public mainNewsAdapter(Context context){
        this.context=context;
        this.datas=new ArrayList<>();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).displayer(new CircleBitmapDisplayer())
                .build();
    }

    //向adapter中添加数据
    public void addData(List<StoriesEntity> list){
        datas.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(viewHolder==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_lv_main_fragment,parent,false);
            viewHolder.ivHead=(ImageView)convertView.findViewById(R.id.item_lv_main_user_fragment_cv_head);
            viewHolder.tvComeFrom=(TextView)convertView.findViewById(R.id.item_lv_main_user_fragment_tv_area);
            viewHolder.tvcommentNum=(TextView)convertView.findViewById(R.id.item_lv_main_fragment_tv_recomand_num);
            viewHolder.tvNewsContent=(TextView)convertView.findViewById(R.id.item_lv_main_fragment_tv_content);
            viewHolder.tvNewsTitle=(TextView)convertView.findViewById(R.id.item_lv_main_fragment_tv_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        StoriesEntity entity = datas.get(position);
        imageLoader.displayImage(entity.getImages().get(0), viewHolder.ivHead, options);
        viewHolder.tvNewsTitle.setText(entity.getTitle());
        //viewHolder.tvNewsContent.setText("暂时还没有获取");
        viewHolder.tvcommentNum.setText("111");
        viewHolder.tvComeFrom.setText("程序员");
        return convertView;
    }

    public static class ViewHolder{
        TextView tvComeFrom;
        TextView tvcommentNum;
        TextView tvNewsTitle;
        TextView tvNewsContent;
        ImageView ivHead;
    }

}
