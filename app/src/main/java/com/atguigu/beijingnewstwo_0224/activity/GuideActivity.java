package com.atguigu.beijingnewstwo_0224.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.beijingnewstwo_0224.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;
    @InjectView(R.id.activity_guide)
    RelativeLayout activityGuide;
    private ArrayList<ImageView>imageViews;

    private int[]ids = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        //初始化数据
        initData();
        vp.setAdapter(new MyPagerAdapter());
    }
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initData() {
        imageViews = new ArrayList<>();
        for (int i = 0;i<ids.length;i++){
            ImageView imageView = new ImageView(this);
            //设置背景
            imageView.setBackgroundResource(ids[i]);
            //添加到集合中去
            imageViews.add(imageView);
        }
    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {
    }
}
