package com.atguigu.beijingnewstwo_0224.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.activity.PicassoSampleActivity;
import com.atguigu.beijingnewstwo_0224.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：田学伟 on 2017/6/6 18:46
 * QQ：93226539
 * 作用：
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {

    private final Context context;
    private final List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;


    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(context, R.layout.item_photos, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = datas.get(position);
        holder.tvTitle.setText(newsEntity.getTitle());
        String imageUrl = Constants.BASE_URL + newsEntity.getListimage();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageUrl = Constants.BASE_URL + datas.get(getLayoutPosition()).getListimage();
                    Intent intent = new Intent(context, PicassoSampleActivity.class);
                    intent.setData(Uri.parse(imageUrl));
                    context.startActivity(intent);
                }
            });
        }
    }
}
