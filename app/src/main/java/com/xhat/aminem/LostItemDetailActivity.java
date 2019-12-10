package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class LostItemDetailActivity extends AppCompatActivity {

    LinearLayout viewDetail;
    TextView tvItemName, tvItemFound, tvItemSave, tvItemDesc;
    ImageView ivItemDrawable, ivTextDrawable;
    Button btnCategory;
    String itemId, itemName, itemImage, itemCategory, itemDesc, itemFound, itemSave, itemSaveImage, itemStatus;

    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;

    public String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };

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

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        Intent intent = getIntent();
        itemId = intent.getStringExtra(Constant.KEY_ID_ITEM);

        viewDetail = findViewById(R.id.view_detail);
        tvItemName = findViewById(R.id.tv_item_name);
        tvItemFound = findViewById(R.id.tv_item_found);
        tvItemSave = findViewById(R.id.tv_item_save);
        tvItemDesc = findViewById(R.id.tv_item_desc);
        ivItemDrawable = findViewById(R.id.iv_item_drawable);
        ivTextDrawable = findViewById(R.id.iv_text_drawable);
        btnCategory = findViewById(R.id.btn_category);

        lostItemDetail();
    }

    private void lostItemDetail() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.getLostItemDetail(sessionManager.getSPToken(), itemId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                JSONObject jsonData = jsonResults.getJSONObject("data");
                                itemName = jsonData.getString("name");

                                if (itemName != null){
                                    viewDetail.setVisibility(View.VISIBLE);

                                    itemImage = jsonData.getString("image");
                                    itemCategory = jsonData.getString("category");
                                    itemDesc = jsonData.getString("description");
                                    itemFound = jsonData.getString("place_found");
                                    itemSave = jsonData.getString("place_save_name");
                                    itemSaveImage = jsonData.getString("place_save_image");
                                    itemStatus = jsonData.getString("status");

                                    tvItemName.setText(itemName);
                                    tvItemFound.setText("Penemuan: " + itemFound);
                                    tvItemSave.setText("Pengambilan: " + itemSave);
                                    btnCategory.setText(itemCategory);

                                    if(itemDesc.equals("null")) {
                                        tvItemDesc.setText("No description available.");
                                    } else {
                                        tvItemDesc.setText(itemDesc);
                                    }

                                    GlideApp
                                            .with(mContext)
                                            .load(itemImage)
                                            .into(ivItemDrawable);

                                    String firstCharNamaMatkul = itemName.substring(0,1);
                                    TextDrawable drawable = TextDrawable.builder()
                                            .buildRound(firstCharNamaMatkul, getColor());
                                    ivTextDrawable.setImageDrawable(drawable);

                                } else {
                                    Helper.showAlertDialog(mContext,"Error", "Request failed, something when wrong");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.errorBody().string());
                                String error_message = jsonResults.getString("message");
                                Integer error_code = jsonResults.getInt("code");

                                Helper.showAlertDialog(mContext,"Error", error_message);

                                if(error_code == 401) {
                                    Helper.clearSession(mContext);
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Helper.showTimeOut(mContext);
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    public int getColor() {
        String color;

        // Randomly select a fact
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);

        color = mColors[randomNumber];
        int colorAsInt = Color.parseColor(color);

        return colorAsInt;
    }
}
