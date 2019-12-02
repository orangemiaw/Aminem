package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.FormValidation;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etNIM, etPassword;
    TextView tvHelp;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        // hide the action bar
        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(this);

        tvHelp = findViewById(R.id.tv_help);
        etNIM = findViewById(R.id.et_nim);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                if(TextUtils.isEmpty(etNIM.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
                    Helper.showAlertDialog(mContext,"Error", "NIM or password can't be empty");
                    loading.dismiss();
                } else if(!FormValidation.isValidNPM(etNIM.getText().toString())) {
                    Helper.showAlertDialog(mContext,"Error", "You submited invalid NIM");
                    loading.dismiss();
                } else if(!FormValidation.isValidPassword(etPassword.getText().toString())) {
                    Helper.showAlertDialog(mContext,"Error", "You submited invalid password");
                    loading.dismiss();
                } else {
                    requestLogin();
                }
            }
        });

        // Code berikut berfungsi untuk mengecek session, Jika session true ( sudah login )
        // maka langsung memulai MainActivity.
        if (sessionManager.getSPSudahLogin()){
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void requestLogin(){
        mApiService.loginRequest(etNIM.getText().toString(), etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                String AuthToken = jsonRESULTS.getJSONObject("data").getString("access_token");
                                String AuthTokenType = jsonRESULTS.getJSONObject("data").getString("token_type");

                                if (AuthToken != null){
                                    // Toast.makeText(mContext, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    String UserId = jsonRESULTS.getJSONObject("data").getString("access_id");
                                    String UserNim = jsonRESULTS.getJSONObject("data").getJSONObject("profile_info").getString("nim");
                                    String UserName = jsonRESULTS.getJSONObject("data").getJSONObject("profile_info").getString("name");

                                    Log.d("USER_NIM", UserNim);
                                    Log.d("USER_NAME", UserName);

                                    sessionManager.saveSPString(SessionManager.SP_USER_ID, UserId);
                                    sessionManager.saveSPString(SessionManager.SP_USER_NAME, UserName);
                                    sessionManager.saveSPString(SessionManager.SP_TOKEN, AuthTokenType + " " + AuthToken);
                                    sessionManager.saveSPBoolean(SessionManager.SP_SUDAH_LOGIN, true);

                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Helper.showAlertDialog(mContext,"Error", "Login failed, something when wrong");
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

    private void showHelpDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Help");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Aplikasi ini terhubung langsung dengan akun mhs.amikom.ac.id. Kamu hanya perlu memasukan NIM dan Password akun Amikom untuk menggunakan aplikasi ini. Mohon maaf aplikasi ini hanya untuk anak Amikom.")
                .setIcon(R.drawable.ic_help)
                .setCancelable(false)
                .setNeutralButton("Learn More",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan mengarahkan ke website amikom
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://amikom.ac.id"));
                        startActivity(viewIntent);
                    }
                })
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog helpDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        helpDialog.show();
    }
}
