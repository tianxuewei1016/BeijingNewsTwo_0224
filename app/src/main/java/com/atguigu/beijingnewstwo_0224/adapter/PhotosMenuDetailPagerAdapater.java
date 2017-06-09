package com.atguigu.beijingnewstwo_0224.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.activity.PicassoSampleActivity;
import com.atguigu.beijingnewstwo_0224.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnewstwo_0224.utils.BitmapCacheUtils;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.atguigu.beijingnewstwo_0224.utils.NetCachUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
    private RecyclerView recyclerview;
    private DisplayImageOptions options;

    /**
     * 做图片三级缓存
     * 1.内存缓存
     * 2.本地缓存
     * 3.网络缓存
     */
    private BitmapCacheUtils bitmapCacheUtils;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCachUtils.SUCESS://图片请求成功
                    //位置
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (recyclerview.isShown()) {
                        ImageView ivIcon = (ImageView) recyclerview.findViewWithTag(position);
                        if (ivIcon != null && bitmap != null) {
                            Log.e("TAG", "网络缓存图片显示成功" + position);
                            ivIcon.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCachUtils.FAIL://图片请求失败
                    position = msg.arg1;
                    Log.e("TAG", "网络缓存失败" + position);
                    break;
            }
        }
    };

    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas, RecyclerView recyclerview) {
        this.context = context;
        this.datas = datas;
        this.recyclerview = recyclerview;
        //把Hanlder传入构造方法
        bitmapCacheUtils = new BitmapCacheUtils(handler);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.news_pic_default)
                .showImageForEmptyUri(R.drawable.news_pic_default)
                .showImageOnFail(R.drawable.news_pic_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //设置矩形圆角图片
                .displayer(new RoundedBitmapDisplayer(10))
                .build();
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
//        Glide.with(context)
//                .load(imageUrl)
//                .placeholder(R.drawable.news_pic_default)
//                .error(R.drawable.news_pic_default)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.ivIcon);

        //使用自定义方式请求图片
//        Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl, position);
//        //图片对应的Tag就是位置
//        holder.ivIcon.setTag(position);
//        if (bitmap != null) {//来自内存和本地,不包括网络的
//            holder.ivIcon.setImageBitmap(bitmap);
//        }

        ImageLoader.getInstance().displayImage(Constants.BASE_URL+newsEntity.getListimage(), holder.ivIcon, options);
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
