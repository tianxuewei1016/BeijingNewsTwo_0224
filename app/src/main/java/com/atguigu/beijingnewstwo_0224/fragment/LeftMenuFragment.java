package com.atguigu.beijingnewstwo_0224.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnewstwo_0224.base.BaseFragment;
import com.atguigu.beijingnewstwo_0224.domain.NewsCenterBean;

import java.util.List;

/**
 * 作者：田学伟 on 2017/6/2 18:21
 * QQ：93226539
 * 作用：
 */

public class LeftMenuFragment extends BaseFragment {
    private TextView textView;
    /**
     * 传入的数据
     */
    private List<NewsCenterBean.DataBean> datas;

    @Override
    protected View initView() {
        textView = new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("左侧菜单");
    }

    public void setData(List<NewsCenterBean.DataBean> datas) {
        this.datas = datas;
        for (int i = 0; i < datas.size(); i++) {
            Log.e("TAG", "" + datas.get(i).getTitle());
        }
    }
}
