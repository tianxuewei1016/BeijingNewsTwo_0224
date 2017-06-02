package com.atguigu.beijingnewstwo_0224.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.R;

/**
 * 作者：田学伟 on 2017/6/2 18:35
 * QQ：93226539
 * 作用：
 */

public class BasePager {

    public Context context;
    /**
     * +     * 代表一整个页面
     * +
     */
    public View rootView;
    public TextView tv_title;
    public ImageButton ib_menu;

    /**
     * 子类的视图添加到该布局上
     */
    public FrameLayout fl_content;

    public BasePager(Context context) {
        this.context = context;

        rootView = View.inflate(context, R.layout.base_pager, null);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) rootView.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);
    }

    /**
     * 子类绑定数据的时候重写
     */
    public void initData() {

    }
}
