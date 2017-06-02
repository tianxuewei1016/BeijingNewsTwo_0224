package com.atguigu.beijingnewstwo_0224.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.beijingnewstwo_0224.R;
import com.atguigu.beijingnewstwo_0224.utils.CacheUtils;
import com.atguigu.beijingnewstwo_0224.utils.DensityUtil;

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
    private ArrayList<ImageView> imageViews;

    private int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    /**
     * 两点的间距
     */
    private int leftMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        //初始化数据
        initData();
        vp.setAdapter(new MyPagerAdapter());
        //设置监听ViewPager滑动位置的变化
        vp.addOnPageChangeListener(new MyOnPageChangeListener());
        //计算两个点之间的距离
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //取消监听
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                leftMargin = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
            }
        });
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当滑到的时候回调
         *
         * @param position
         * @param positionOffset
         * @param positionOffsetPixels
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            红点移动距离：间距 = 手滑动的距离：屏幕宽 = 屏幕滑动的百分比
//            红点移动距离 = 间距 * 屏幕滑动的百分比
            float left = leftMargin * (position + positionOffset);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin = (int) left;
            ivRedPoint.setLayoutParams(params);
        }

        /**
         * 当选中某个页面的时候回调
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                //最后一个页面就显示
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                //其他的都隐藏
                btnStartMain.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyPagerAdapter extends PagerAdapter {

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
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            //设置背景
            imageView.setBackgroundResource(ids[i]);
            //添加到集合中去
            imageViews.add(imageView);

            //添加三个灰色的点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.guide_point_noemal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(GuideActivity.this,10), DensityUtil.dip2px(GuideActivity.this,10));
            point.setLayoutParams(params);
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(GuideActivity.this,10);
            }
            //添加到线性布局中区
            llPointGroup.addView(point);
        }
    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {
        //保存记录已经进入到主页面
        CacheUtils.putBoolean(this,"start_main",true);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
    }
}
