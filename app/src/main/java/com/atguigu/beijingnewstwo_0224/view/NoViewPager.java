package com.atguigu.beijingnewstwo_0224.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：田学伟 on 2017/6/3 09:46
 * QQ：93226539
 * 作用：
 */

public class NoViewPager extends ViewPager{
    public NoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
