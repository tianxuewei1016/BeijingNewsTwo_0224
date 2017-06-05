package com.atguigu.beijingnewstwo_0224.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;

/**
 * 作者：田学伟 on 2017/6/5 11:04
 * QQ：93226539
 * 作用：
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    private TextView textView;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        textView.setText(childrenBean.getTitle());
    }
}
