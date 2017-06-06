package com.atguigu.beijingnewstwo_0224.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;
import com.atguigu.beijingnewstwo_0224.domain.TabDetailPagerBean;
import com.atguigu.beijingnewstwo_0224.utils.CacheUtils;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.atguigu.beijingnewstwo_0224.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
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
    public static final String READ_ID_ARRAY = "read_id_array";
    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llPointGroup;
    ListView lv;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshList;
    private List<TabDetailPagerBean.DataEntity.TopnewsEntity> topnews;

    private int prePosition = 0;
    private List<TabDetailPagerBean.DataEntity.NewsEntity> newsBeanList;
    private ListAdapter adapter;

    private String moreUrl;
    private boolean isLoadingMore = false;
    private String url;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);

        //得到ListView
        lv = pullRefreshList.getRefreshableView();

        /**
         * 增加下拉刷新的声音
         */
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pullRefreshList.setOnPullEventListener(soundListener);
        //顶部视图
        View viewTopNews = View.inflate(context, R.layout.tab_detail_topnews, null);
        viewpager = (HorizontalScrollViewPager) viewTopNews.findViewById(R.id.viewpager);
        tvTitle = (TextView) viewTopNews.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) viewTopNews.findViewById(R.id.ll_point_group);

        //把顶部的部分以添加头的方式加入ListView中
        lv.addHeaderView(viewTopNews);
        //设置监听ViewPager页面的变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //把之前的设置默认
                llPointGroup.getChildAt(prePosition).setEnabled(false);
                //当前的设置true
                llPointGroup.getChildAt(position).setEnabled(true);
                //记录当前值
                prePosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                String title = topnews.get(position).getTitle();
                tvTitle.setText(title);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置下拉和上拉刷新的监听
        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadingMore = false;
                getDataFromNet(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!TextUtils.isEmpty(moreUrl)) {
                    isLoadingMore = true;
                    getDataFromNet(moreUrl);
                } else {
                    Toast.makeText(context, "没有更多数据了...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPosition = position - 2;
                TabDetailPagerBean.DataEntity.NewsEntity newsEntity = newsBeanList.get(realPosition);
//                Log.e("TAG", "" + newsBean.getId() + "--------" + newsBean.getTitle());
                //获取
                String idArray = CacheUtils.getString(context, READ_ID_ARRAY);
                //判断是否存在
                if (!idArray.contains(newsEntity.getId() + "")) {
                    idArray = idArray + newsEntity.getId() + ",";
                    CacheUtils.putString(context, READ_ID_ARRAY, idArray);
                    //刷新适配器
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        url = Constants.BASE_URL + childrenBean.getUrl();
        getDataFromNet(url);
    }

    /**
     * 联网请求
     */
    private void getDataFromNet(String url) {
        OkHttpUtils.get()
                .url(url)
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
                        //结束下拉和上拉刷新
                        pullRefreshList.onRefreshComplete();
                    }
                });
    }

    private void processData(String response) {
        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);
//        Log.e("TAG", "" + bean.getData().getNews().get(0).getTitle());

        String more = bean.getData().getMore();
        if (!TextUtils.isEmpty(more)) {
            moreUrl = Constants.BASE_URL + more;
        }

        if (!isLoadingMore) {
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
        } else {
            isLoadingMore = false;
            newsBeanList.addAll(bean.getData().getNews());//把新的数据集合加入到原来集合中，而不是覆盖
            adapter.notifyDataSetChanged();
        }
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

            //判断是否已经被点击了
            String idArray = CacheUtils.getString(context, READ_ID_ARRAY);
            if (idArray.contains(newsEntity.getId() + "")) {
                //灰色
                viewHolder.tvDesc.setTextColor(Color.GRAY);
            } else {
                //黑色
                viewHolder.tvDesc.setTextColor(Color.BLACK);
            }
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
