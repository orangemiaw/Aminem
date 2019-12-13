package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.xhat.aminem.Fragment.ProfileFragment;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.FormValidation;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnChangePassword, btnCancel;
    EditText etPassword, etConfirmPassword;
    ImageView ivPhoto;
    String profileImage;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        profileImage = intent.getStringExtra(Constant.PROFILE_IMAGE);

        ivPhoto = findViewById(R.id.iv_photo);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnCancel = findViewById(R.id.btn_cancel);

        GlideApp
                .with(mContext)
                .load(profileImage)
                .apply(RequestOptions.circleCropTransform())
                .into(ivPhoto);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                if(TextUtils.isEmpty(etPassword.getText().toString()) || TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
                    Helper.showAlertDialog(mContext,"Error", "Password and confirm password can't be empty");
                    loading.dismiss();
                } else if(!FormValidation.isValidPassword(etPassword.getText().toString()) || !FormValidation.isValidPassword(etConfirmPassword.getText().toString())) {
                    Helper.showAlertDialog(mContext,"Error", "You submited invalid password");
                    loading.dismiss();
                } else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    Helper.showAlertDialog(mContext,"Error", "Password doesn't match");
                    loading.dismiss();
                } else {
                    requestChangePassword();
                }
            }
        });
    }

    private void requestChangePassword() {
        mApiService.changePassword(sessionManager.getSPToken(), etPassword.getText().toString(), etConfirmPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                JSONObject jsonData = jsonResults.getJSONObject("data");
                                String userId = jsonData.getString("_id");

                                if (userId != null){
                                    Helper.showAlertDialog(mContext,"Success", "Password has been changed successfully. You must relogin to continue use this app.");
                                } else {
                                    Helper.showAlertDialog(mContext,"Error", "Change password failed, something when wrong");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject jsonResults = new JSONObject(response.errorBody().string());
                                String error_message = jsonResults.getString("message");
                                Helper.showAlertDialog(mContext,"Error", error_message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            loading.dismiss();
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
