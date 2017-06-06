package com.atguigu.beijingnewstwo_0224.detailpager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
    private String url;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        //实例视图
        View view = View.inflate(context, R.layout.pager_photos_menu_detail, null);
        ButterKnife.inject(this, view);
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
     * @param json
     */
    private void processData(String json) {

    }
}
