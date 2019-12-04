package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.TextView;

import com.xhat.aminem.Adapter.LogItemAdapter;
import com.xhat.aminem.Model.LogItem;
import com.xhat.aminem.Model.ResponseLogItem;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    RecyclerView rvLog;
    TextView tvEmpty, tvErrorSorry, tvErrorHi, tvInfo;
    ImageView ivError;
    ProgressDialog loading;

    Context mContext;
    List<LogItem> logItemList = new ArrayList<>();
    LogItemAdapter logItemAdapter;
    BaseApiService mApiService;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_log);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Log Activity");

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        rvLog = findViewById(R.id.rv_log);
        ivError = findViewById(R.id.iv_error);
        tvEmpty = findViewById(R.id.tv_empty);
        tvErrorHi = findViewById(R.id.tv_error_hi);
        tvErrorSorry = findViewById(R.id.tv_error_sorry);
        tvInfo = findViewById(R.id.tv_error_info);

        if (!sessionManager.getSPSudahLogin()){
            startActivity(new Intent(mContext, LoginActivity.class));
        }

        logItemAdapter = new LogItemAdapter(mContext, logItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvLog.setLayoutManager(mLayoutManager);
        rvLog.setItemAnimator(new DefaultItemAnimator());

        getLogActivity();
    }

    private void getLogActivity() {
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);

        mApiService.getLogActivity(sessionManager.getSPToken())
                .enqueue(new Callback<ResponseLogItem>() {
                    @Override
                    public void onResponse(Call<ResponseLogItem> call, Response<ResponseLogItem> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();

                            if (response.body().isError() || response.body() == null) {
                                ivError.setVisibility(View.VISIBLE);
                                tvErrorHi.setVisibility(View.VISIBLE);
                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
                                final List<LogItem> logItemLists = response.body().getLogitem();
                                rvLog.setAdapter(new LogItemAdapter(mContext, logItemLists));
                                logItemAdapter.notifyDataSetChanged();
                            }
                        } else {
                            loading.dismiss();
                            Helper.clearSession(mContext);
                            startActivity(new Intent(mContext, LoginActivity.class));
                            Helper.showAlertDialog(mContext,"Error", "Your must login again becaouse session expired");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLogItem> call, Throwable t) {
                        loading.dismiss();
                        ivError.setVisibility(View.VISIBLE);
                        tvErrorSorry.setVisibility(View.VISIBLE);
                        tvInfo.setVisibility(View.VISIBLE);

                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Helper.showTimeOut(mContext);
                    }
                });

    }
}
