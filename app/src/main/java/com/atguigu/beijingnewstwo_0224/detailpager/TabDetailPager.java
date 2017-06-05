package com.atguigu.beijingnewstwo_0224.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;
import com.atguigu.beijingnewstwo_0224.domain.TabDetailPagerBean;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/5 11:04
 * QQ：93226539
 * 作用：
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;

    ViewPager viewpager;
    TextView tvTitle;
    LinearLayout llPointGroup;
    @InjectView(R.id.lv)
    ListView lv;
    private List<TabDetailPagerBean.DataEntity.TopnewsEntity> topnews;

    private int prePosition = 0;
    private List<TabDetailPagerBean.DataEntity.NewsEntity> newsBeanList;
    private ListAdapter adapter;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);

        //顶部视图
        View viewTopNews = View.inflate(context, R.layout.tab_detail_topnews, null);
        viewpager = (ViewPager) viewTopNews.findViewById(R.id.viewpager);
        tvTitle = (TextView) viewTopNews.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) viewTopNews.findViewById(R.id.ll_point_group);

        //把顶部的部分以添加头的方式加入ListView中
        lv.addHeaderView(viewTopNews);
        //设置监听ViewPager页面的变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = topnews.get(position).getTitle();
                tvTitle.setText(title);
                //把之前的设置默认
                llPointGroup.getChildAt(prePosition).setEnabled(false);
                //当前的设置true
                llPointGroup.getChildAt(position).setEnabled(true);
                //记录当前值
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        getDataFromNet();
    }

    /**
     * 联网请求
     */
    private void getDataFromNet() {
        OkHttpUtils.get()
                .url(Constants.BASE_URL + childrenBean.getUrl())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        Log.e("TAG", "请求失败==" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("TAG", "请求成功==" + response);
                        processData(response);
                    }
                });
    }

    private void processData(String response) {
        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);
//        Log.e("TAG", "" + bean.getData().getNews().get(0).getTitle());
        topnews = bean.getData().getTopnews();
        //设置适配器
        viewpager.setAdapter(new MyPagerAdapter());
        tvTitle.setText(topnews.get(prePosition).getTitle());
        //把之前的移除
        llPointGroup.removeAllViews();
        //添加指示点
        for (int i = 0; i < topnews.size(); i++) {
            ImageView point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            point.setLayoutParams(params);

            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 8;
            }
            //添加到线性布局中
            llPointGroup.addView(point);
        }
        //------------ListView的---------------
        newsBeanList = bean.getData().getNews();
        adapter = new ListAdapter();
        lv.setAdapter(adapter);
    }

    class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return newsBeanList == null ? 0 : newsBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tab_detail, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataEntity.NewsEntity newsEntity = newsBeanList.get(position);
            viewHolder.tvDesc.setText(newsEntity.getTitle());
            viewHolder.tvTime.setText(newsEntity.getPubdate());

            String imageUrl = Constants.BASE_URL + newsEntity.getListimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);
            return convertView;
        }
    }

    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_desc)
        TextView tvDesc;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.news_pic_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(context)
                    .load(Constants.BASE_URL + topnews.get(position).getTopimage())
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
