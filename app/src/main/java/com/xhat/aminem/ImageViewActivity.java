package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.Utils.Constant;

public class ImageViewActivity extends AppCompatActivity {
    private PhotoView mImageView;
    private String imageUrl, imageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra(Constant.TEMP_IMAGE_URL);
        imageTitle = intent.getStringExtra(Constant.TEMP_IMAGE_TITLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(imageTitle);

        mImageView = findViewById(R.id.image);

        GlideApp.with(this)
                .load(imageUrl)
                .into(mImageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
