package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.xhat.aminem.Adapter.LostItemAdapter;
import com.xhat.aminem.Model.AlllostitemItem;
import com.xhat.aminem.Model.ResponseLostItem;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.RecyclerItemClickListener;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LostItemByCategoryActivity extends AppCompatActivity {
    RecyclerView rvItem;
    LinearLayout llError, llEmpty;
    String categoryId, categoryName, categoryItemName, totalLost, totalReturned;
    TextView tvStatusLost, tvStatusReturned;
    CardView cvStatus;
    ImageView ivStatusIcon;

    ProgressDialog loading;
    ArrayList<String> imageUrl = new ArrayList<>();

    List<AlllostitemItem> alllostitemItemList = new ArrayList<>();
    LostItemAdapter lostItemAdapter;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lost_item_by_category);
        Intent intent = getIntent();
        categoryId = intent.getStringExtra(Constant.CATEGORY_ID);
        categoryName = intent.getStringExtra(Constant.CATEGORY_NAME);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(categoryName);

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        rvItem = findViewById(R.id.rv_item);
        cvStatus = findViewById(R.id.cv_status);
        ivStatusIcon = findViewById(R.id.iv_status_icon);
        llError = findViewById(R.id.ll_error);
        llEmpty = findViewById(R.id.ll_empty);
        tvStatusLost = findViewById(R.id.tv_status_lost);
        tvStatusReturned = findViewById(R.id.tv_status_returned);

        if (!sessionManager.getSPSudahLogin()){
            startActivity(new Intent(mContext, LoginActivity.class));
        }

        lostItemAdapter = new LostItemAdapter(mContext, alllostitemItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvItem.setLayoutManager(mLayoutManager);
        rvItem.setItemAnimator(new DefaultItemAnimator());

        // Change status icon
        createStatusCard(categoryId);

        // Load lost item summary in category
        lostItemSummary();

        // Load data lost item
        getDataItem();
    }

    private void createStatusCard(String categoryId) {
        switch (categoryId) {
            case Constant.CATEGORY_ID_CARD:
                cvStatus.setCardBackgroundColor(getResources().getColor(R.color.yello));
                ivStatusIcon.setImageResource(R.drawable.category_card_id);
                break;
            case Constant.CATEGORY_DOCUMENT:
                cvStatus.setCardBackgroundColor(getResources().getColor(R.color.pink));
                ivStatusIcon.setImageResource(R.drawable.ic_attach_file_black_24dp);
                break;
            case Constant.CATEGORY_KEY:
                cvStatus.setCardBackgroundColor(getResources().getColor(R.color.green));
                ivStatusIcon.setImageResource(R.drawable.ic_key_white);
                break;
            case Constant.CATEGORY_ELECTRONIC:
                cvStatus.setCardBackgroundColor(getResources().getColor(R.color.green));
                ivStatusIcon.setImageResource(R.drawable.ic_electronic_white);
                break;
            case Constant.CATEGORY_WALLET:
                cvStatus.setCardBackgroundColor(getResources().getColor(R.color.yello));
                ivStatusIcon.setImageResource(R.drawable.ic_attach_money_black_24dp);
                break;
            case Constant.CATEGORY_OTHER:
                cvStatus.setCardBackgroundColor(getResources().getColor(R.color.pink));
                ivStatusIcon.setImageResource(R.drawable.ic_other_white);
                break;
        }
    }

    private void lostItemSummary() {
        mApiService.getCategoryStatus(sessionManager.getSPToken(), categoryId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                JSONObject jsonData = jsonResults.getJSONObject("data");
                                categoryItemName = jsonData.getString("category");

                                if (!categoryItemName.equals("null")){
                                    totalLost = jsonData.getString("total_lost");
                                    totalReturned = jsonData.getString("total_returned");

                                    tvStatusLost.setText(totalLost + " items lost in this category");
                                    tvStatusReturned.setText("And " + totalReturned + " items returned to the owner");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject jsonResults = new JSONObject(response.errorBody().string());
                                Integer error_code = jsonResults.getInt("code");

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
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private void getDataItem(){
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.getItemByCategory(sessionManager.getSPToken(), categoryId)
                .enqueue(new Callback<ResponseLostItem>() {
                    @Override
                    public void onResponse(Call<ResponseLostItem> call, Response<ResponseLostItem> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();

                            if (response.body().isError() || response.body() == null) {
                                llError.setVisibility(View.VISIBLE);
                            } else {
                                // CardView Status
                                //cvStatus.setVisibility(View.VISIBLE);

                                final List<AlllostitemItem> alllostitemItems = response.body().getAlllostitem();
                                rvItem.setAdapter(new LostItemAdapter(mContext, alllostitemItems));
                                lostItemAdapter.notifyDataSetChanged();

                                initDataIntent(alllostitemItems);
                            }
                        } else {
                            loading.dismiss();
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLostItem> call, Throwable t) {
                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        llError.setVisibility(View.VISIBLE);
                        Helper.showTimeOut(mContext);
                    }
                });
    }

    private void initDataIntent(final List<AlllostitemItem> alllostitemItemList){
        rvItem.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id = alllostitemItemList.get(position).getId();

                        Intent detailItem = new Intent(mContext, LostItemDetailActivity.class);
                        detailItem.putExtra(Constant.KEY_ID_ITEM, id);
                        startActivity(detailItem);
                    }
                }));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
