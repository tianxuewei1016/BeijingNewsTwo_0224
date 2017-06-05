package com.atguigu.beijingnewstwo_0224.detailpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;
import com.atguigu.beijingnewstwo_0224.domain.TabDetailPagerBean;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.lv)
    ListView lv;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);
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
        Log.e("TAG", "" + bean.getData().getNews().get(0).getTitle());
    }
}
