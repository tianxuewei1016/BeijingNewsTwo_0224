package com.atguigu.beijingnewstwo_0224.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.base.BasePager;

/**
 * 作者：田学伟 on 2017/6/2 17:56
 * QQ：93226539
 * 作用：
 */

public class SettingPager extends BasePager {

    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //把数据绑定到视图上

        //设置标题
        tv_title.setText("设置");

        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setText("设置页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        fl_content.addView(textView);
    }
}