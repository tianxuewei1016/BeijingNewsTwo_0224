package com.atguigu.beijingnewstwo_0224.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.atguigu.beijingnewstwo_0224.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

public class PicassoSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso_sample);

        Uri uri = getIntent().getData();

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
        Picasso.with(this)
                .load(uri)
                .into(photoView);
    }
}
