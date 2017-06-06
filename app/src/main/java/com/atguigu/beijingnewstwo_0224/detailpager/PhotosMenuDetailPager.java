package com.atguigu.beijingnewstwo_0224.detailpager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.adapter.PhotosMenuDetailPagerAdapater;
import com.atguigu.beijingnewstwo_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;
import com.atguigu.beijingnewstwo_0224.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/3 14:18
 * QQ：93226539
 * 作用：组图详情页面的内容
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean dataBean;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private String url;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;
    private PhotosMenuDetailPagerAdapater adapter;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        //实例视图
        View view = View.inflate(context, R.layout.pager_photos_menu_detail, null);
        ButterKnife.inject(this, view);
        //设置下拉刷新的监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(url);
            }
        });
        //设置滑动多少距离有效果
//        refreshLayout.setDistanceToTriggerSync(100);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + dataBean.getUrl();
        getDataFromNet(url);
    }

    /**
     * 联网请求
     *
     * @param url
     */
    private void getDataFromNet(String url) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        Log.e("TAG", "图组请求失败==" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("TAG", "图组请求成功==" + response);
                        processData(response);
                    }
                });
    }

    /**
     * 解析数据
     *
     * @param json
     */
    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
        datas = bean.getData().getNews();
        if (datas != null && datas.size() > 0) {
            progressbar.setVisibility(View.GONE);
            adapter = new PhotosMenuDetailPagerAdapater(context, datas);
            recyclerview.setAdapter(adapter);
            //设置布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        } else {
            //没有数据
            progressbar.setVisibility(View.VISIBLE);
        }
        //隐藏刷新进度的效果
        refreshLayout.setRefreshing(false);
    }

    /**
     * true:显示List效果
     * false:显示Grid
     */
    private boolean isShowList = true;

    /**
     * 设置List和Grid风格的切换和按钮的设置
     *
     * @param iv
     */
    public void swichListAndGrid(ImageButton iv) {
        if (isShowList) {
            recyclerview.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
            isShowList = false;
            iv.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false));
            isShowList = true;
            iv.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }
}
