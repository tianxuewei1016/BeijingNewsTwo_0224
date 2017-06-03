package com.atguigu.beijingnewstwo_0224.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.activity.MainActivity;
import com.atguigu.beijingnewstwo_0224.base.BasePager;
import com.atguigu.beijingnewstwo_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnewstwo_0224.detailpager.InteractMenuDetailPager;
import com.atguigu.beijingnewstwo_0224.detailpager.NewsMenuDetailPager;
import com.atguigu.beijingnewstwo_0224.detailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnewstwo_0224.detailpager.TopicMenuDetailPager;
import com.atguigu.beijingnewstwo_0224.detailpager.VoteMenuDetailPager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;
import com.atguigu.beijingnewstwo_0224.fragment.LeftMenuFragment;
import com.atguigu.beijingnewstwo_0224.utils.Constants;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/2 17:58
 * QQ：93226539
 * 作用：
 */

public class NewsPager extends BasePager {
    /**
     * 左侧页面的数据集合
     */
    private List<NewsCenterBean.DataBean> datas;
    /**
     * 左侧菜单详情的页面集合
     */
    private List<MenuDetailBasePager> basePagers;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //把数据绑定到视图上

        //设置标题
        tv_title.setText("新闻");
        ib_menu.setVisibility(View.VISIBLE);

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

        basePagers = new ArrayList<>();
        basePagers.add(new NewsMenuDetailPager(context));//新闻详情页面
        basePagers.add(new TopicMenuDetailPager(context));//专题详情页面
        basePagers.add(new PhotosMenuDetailPager(context));//组图详情页面
        basePagers.add(new InteractMenuDetailPager(context));//互动详情页面
        basePagers.add(new VoteMenuDetailPager(context));//投票详情页面

        //得到左侧菜单Fragment
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(datas);
    }

    /**
     * 根据位置切换到不同的详情页面
     *
     * @param prePosition
     */
    public void swichPager(int prePosition) {
        MenuDetailBasePager basePager = basePagers.get(prePosition);
        View rootView = basePager.rootView;
        fl_content.removeAllViews();//把之前显示的给移除

        fl_content.addView(rootView);
        //调用InitData
        basePager.initData();
    }
}