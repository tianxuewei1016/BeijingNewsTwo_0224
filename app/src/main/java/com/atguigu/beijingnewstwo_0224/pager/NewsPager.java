package com.atguigu.beijingnewstwo_0224.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.activity.MainActivity;
import com.atguigu.beijingnewstwo_0224.base.BasePager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;
import com.atguigu.beijingnewstwo_0224.fragment.LeftMenuFragment;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/2 17:58
 * QQ：93226539
 * 作用：
 */

public class NewsPager extends BasePager {
    private List<NewsCenterBean.DataBean> datas;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //把数据绑定到视图上

        //设置标题
        tv_title.setText("新闻");

        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        fl_content.addView(textView);

        //联网请求
        getDataForNet();
    }

    private void getDataForNet() {
        OkHttpUtils
                .get()
                .url(Constants.NEWSCENTER_PAGER_URL)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        Log.e("TAG", "联网失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("TAG", "联网成功" + response);
                        processData(response);
                    }
                });
    }

    private void processData(String json) {
        NewsCenterBean newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);
        Log.e("TAG", "解析成功了哦==" + newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
        datas = newsCenterBean.getData();
        //传到左侧菜单
        MainActivity mainActivity = (MainActivity) context;
        //得到左侧菜单Fragment
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(datas);
    }
}