package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhat.aminem.Adapter.LogItemAdapter;
import com.xhat.aminem.Adapter.VoucherItemAdapter;
import com.xhat.aminem.Fragment.HistoryFragment;
import com.xhat.aminem.Model.AlllostitemItem;
import com.xhat.aminem.Model.LogItem;
import com.xhat.aminem.Model.ResponseLogItem;
import com.xhat.aminem.Model.ResponseVoucherItem;
import com.xhat.aminem.Model.VoucherItem;
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

public class VoucherActivity extends AppCompatActivity {
    RecyclerView rvVoucher;
    LinearLayout llError, llEmpty;
    ProgressDialog loading;

    Context mContext;
    List<VoucherItem> voucherItemList = new ArrayList<>();
    VoucherItemAdapter voucherItemAdapter;
    BaseApiService mApiService;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voucher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vouchers");

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        rvVoucher = findViewById(R.id.rv_voucher);
        llError = findViewById(R.id.ll_error);
        llEmpty = findViewById(R.id.ll_empty);

        if (!sessionManager.getSPSudahLogin()){
            startActivity(new Intent(mContext, LoginActivity.class));
        }

        voucherItemAdapter = new VoucherItemAdapter(mContext, voucherItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvVoucher.setLayoutManager(mLayoutManager);
        rvVoucher.setItemAnimator(new DefaultItemAnimator());

        getVoucherActivity();
    }

    private void getVoucherActivity() {
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);

        mApiService.getVoucherActivity(sessionManager.getSPToken())
                .enqueue(new Callback<ResponseVoucherItem>() {
                    @Override
                    public void onResponse(Call<ResponseVoucherItem> call, Response<ResponseVoucherItem> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();

                            if (response.body().isError() || response.body() == null) {
                                llError.setVisibility(View.VISIBLE);
                            } else {
                                final List<VoucherItem> voucherItemList = response.body().getVoucherItem();
                                rvVoucher.setAdapter(new VoucherItemAdapter(mContext, voucherItemList));
                                voucherItemAdapter.notifyDataSetChanged();

                                initVoucherIntent(voucherItemList);
                            }
                        } else {
                            loading.dismiss();
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseVoucherItem> call, Throwable t) {
                        loading.dismiss();
                        llError.setVisibility(View.VISIBLE);
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Helper.showTimeOut(mContext);
                    }
                });

    }

    private void initVoucherIntent(final List<VoucherItem> voucherItemList){

        rvVoucher.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                mContext);

                        // set title dialog
                        alertDialogBuilder.setTitle("Buy Voucher");

                        // set pesan dari dialog
                        alertDialogBuilder
                                .setMessage("Are you sure want to trade your poin with this voucher?")
                                .setIcon(R.drawable.ic_help)
                                .setCancelable(true)
                                .setNeutralButton("Yes, Buy Voucher",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        buyVoucher(voucherItemList.get(position).getId());
                                    }
                                })
                                .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog actionDialog = alertDialogBuilder.create();
                        actionDialog.show();
                    }
                }));
    }

    private void buyVoucher(String voucherId) {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.purchaseVoucher(sessionManager.getSPToken(), voucherId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            Helper.showAlertDialog(mContext,"Success", "Your voucher has been send to your Amikom student email, follow instruction in the email to redem your voucher.");
                            startActivity(new Intent(mContext, HistoryFragment.class));
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
