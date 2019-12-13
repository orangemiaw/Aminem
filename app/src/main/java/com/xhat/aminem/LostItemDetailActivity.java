package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

    LinearLayout viewDetail, viewPickup, viewError;
    TextView tvItemName, tvItemFound, tvItemSave, tvItemDesc;
    ImageView ivItemDrawable, ivTextDrawable;
    CardView cvCardView;
    Button btnCategory, btnLocation;
    String itemId, itemName, itemImage, itemCategory, itemCategoryId, itemDateFound, itemDesc, itemFound, itemSave, itemSaveId, itemSaveImage, itemStatus;
    boolean isImageFitToScreen;

    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lost_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        Intent intent = getIntent();
        itemId = intent.getStringExtra(Constant.KEY_ID_ITEM);

        viewDetail = findViewById(R.id.view_detail);
        viewPickup = findViewById(R.id.view_pickup);
        viewError = findViewById(R.id.ll_error);
        tvItemName = findViewById(R.id.tv_item_name);
        tvItemFound = findViewById(R.id.tv_item_found);
        tvItemSave = findViewById(R.id.tv_item_save);
        tvItemDesc = findViewById(R.id.tv_item_desc);
        ivItemDrawable = findViewById(R.id.iv_item_drawable);
        ivTextDrawable = findViewById(R.id.iv_text_drawable);
        cvCardView = findViewById(R.id.card_view);
        btnCategory = findViewById(R.id.btn_category);
        btnLocation = findViewById(R.id.btn_navigation);

        // belum bekerja (no error and not work)
        ivItemDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImageFitToScreen) {
                    isImageFitToScreen = false;
                    ivItemDrawable.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ivItemDrawable.setAdjustViewBounds(true);
                } else {
                    isImageFitToScreen = true;
                    ivItemDrawable.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    ivItemDrawable.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });

        // redirection to google maps
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailPlace = new Intent(mContext, PickupLocationActivity.class);
                detailPlace.putExtra(Constant.KEY_ID_PLACE, itemSaveId);
                startActivity(detailPlace);
            }
        });

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
                                    viewPickup.setVisibility(View.VISIBLE);

                                    itemImage = jsonData.getString("image");
                                    itemCategory = jsonData.getString("category");
                                    itemCategoryId = jsonData.getString("_category_id");
                                    itemDesc = jsonData.getString("description");
                                    itemDateFound = jsonData.getString("date_found");
                                    itemFound = jsonData.getString("place_found");
                                    itemSave = jsonData.getString("place_save_name");
                                    itemSaveId = jsonData.getString("_place_save_id");
                                    itemSaveImage = jsonData.getString("place_save_image");
                                    itemStatus = jsonData.getString("status");

                                    tvItemName.setText(itemName);
                                    tvItemFound.setText("Penemuan: " + itemFound);
                                    tvItemSave.setText("Pengambilan: " + itemSave);
                                    btnCategory.setText(itemDateFound);

                                    if(itemDesc.equals("null")) {
                                        tvItemDesc.setText("No description available.");
                                    } else {
                                        tvItemDesc.setText(itemDesc);
                                    }

                                    GlideApp
                                            .with(mContext)
                                            .load(itemImage)
                                            .into(ivItemDrawable);

                                    setIcon(itemCategoryId);

                                } else {
                                    viewError.setVisibility(View.VISIBLE);
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

                                viewError.setVisibility(View.VISIBLE);
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
                        viewError.setVisibility(View.VISIBLE);
                        Helper.showTimeOut(mContext);
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void setIcon(String itemCategoryId) {
        switch (itemCategoryId) {
            case Constant.CATEGORY_ID_CARD:
                ivTextDrawable.setImageResource(R.drawable.ic_lightbulb_outline_black_24dp);
                ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundyello);
                break;
            case Constant.CATEGORY_DOCUMENT:
                ivTextDrawable.setImageResource(R.drawable.ic_attach_file_black_24dp);
                ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundpink);
                break;
            case Constant.CATEGORY_KEY:
                ivTextDrawable.setImageResource(R.drawable.ic_key_white);
                ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundgreen);
                break;
            case Constant.CATEGORY_ELECTRONIC:
                ivTextDrawable.setImageResource(R.drawable.ic_electronic_white);
                ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundgreen);
                break;
            case Constant.CATEGORY_WALLET:
                ivTextDrawable.setImageResource(R.drawable.ic_attach_money_black_24dp);
                ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundyello);
                break;
            case Constant.CATEGORY_OTHER:
                ivTextDrawable.setImageResource(R.drawable.ic_other_white);
                ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundpink);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
