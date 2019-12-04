package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.Utils.Constant;

public class LostItemDetailActivity extends AppCompatActivity {

    TextView tvItemName, tvItemFound, tvItemSave, tvItemDesc;
    ImageView ivItemDrawable;

    String itemId, itemName, itemImage, itemFound, itemSave, itemDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lost_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");

        Intent intent = getIntent();
        itemId = intent.getStringExtra(Constant.KEY_ID_ITEM);
        itemName = intent.getStringExtra(Constant.KEY_ITEM_NAME);
        itemImage = intent.getStringExtra(Constant.KEY_ITEM_IMAGE);
        itemFound = intent.getStringExtra(Constant.KEY_ITEM_FOUND);
        itemSave = intent.getStringExtra(Constant.KEY_ITEM_SAVE);
        itemDesc = intent.getStringExtra(Constant.KEY_ITEM_DESC);

        tvItemName = findViewById(R.id.tv_item_name);
        tvItemFound = findViewById(R.id.tv_item_found);
        tvItemSave = findViewById(R.id.tv_item_save);
        tvItemDesc = findViewById(R.id.tv_item_desc);
        ivItemDrawable = findViewById(R.id.iv_item_drawable);

        Log.d("INTEN_ITEM_ID", itemId);
        Log.d("INTEN_ITEM_NAME", itemName);
        Log.d("INTEN_ITEM_IMAGE", itemImage);
        Log.d("INTEN_ITEM_FOUND", itemFound);
        tvItemName.setText(itemName);
        tvItemFound.setText(itemFound);
        tvItemSave.setText(itemSave);
        if(itemDesc != null) {
            tvItemDesc.setText(itemDesc);
        } else {
            tvItemDesc.setText("No description available.");
        }

        GlideApp
                .with(this)
                .load(itemImage)
                .into(ivItemDrawable);
    }
}
