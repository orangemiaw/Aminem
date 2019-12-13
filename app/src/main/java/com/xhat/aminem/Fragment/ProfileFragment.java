package com.xhat.aminem.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.xhat.aminem.ChangePasswordActivity;
import com.xhat.aminem.LoginActivity;
import com.xhat.aminem.LostItemDetailActivity;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.R;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;
import com.xhat.aminem.VoucherActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProfileFragment extends Fragment {
    ImageView ivPhoto;
    TextView tvPoin, tvName, tvNim, tvEmail, tvEmailPassword, tvGeneration, tvLastLogin, tvChangePassword;
    ScrollView svProfile;
    LinearLayout llError;
    Button btnRedem;

    String profileName, profileNim, profileImage, profileGeneration, profileEmail, profileEmailPassword, profilePoin, profileLogin;

    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout ;

    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;
    ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Profile");
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = getContext();
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();

        svProfile = view.findViewById(R.id.sv_profile);
        llError = view.findViewById(R.id.ll_error);
        ivPhoto = view.findViewById(R.id.iv_photo);
        tvPoin = view.findViewById(R.id.tv_poin);
        tvName = view.findViewById(R.id.tv_profile_name);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvEmailPassword = view.findViewById(R.id.tv_profile_email_password);
        tvNim = view.findViewById(R.id.tv_profile_nim);
        tvGeneration = view.findViewById(R.id.tv_profile_generation);
        tvLastLogin = view.findViewById(R.id.tv_profile_last_login);
        tvChangePassword = view.findViewById(R.id.tv_change_password);
        constraintLayout = view.findViewById(R.id.constraint_layout);
        btnRedem = view.findViewById(R.id.btn_redem);
        layout2.clone(mContext, R.layout.profile_expanded);
        layout1.clone(constraintLayout);

        getProfile();

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpen) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = !isOpen ;
                } else {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout1.applyTo(constraintLayout);
                    isOpen = !isOpen ;
                }
            }
        });

        btnRedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, VoucherActivity.class));
            }
        });

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePassword = new Intent(mContext, ChangePasswordActivity.class);
                changePassword.putExtra(Constant.PROFILE_IMAGE, profileImage);
                startActivity(changePassword);
            }
        });

        return view;
    }

    private void getProfile() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.getProfile(sessionManager.getSPToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                JSONObject jsonData = jsonResults.getJSONObject("data");
                                profileName = jsonData.getString("fullname");

                                if (profileName != null){
                                    svProfile.setVisibility(View.VISIBLE);

                                    profileNim = jsonData.getString("nim");
                                    profileImage = jsonData.getString("profile_image");
                                    profileGeneration = jsonData.getString("generation");
                                    profileEmail = jsonData.getString("amikom_email");
                                    profileEmailPassword = jsonData.getString("amikom_email_password");
                                    profilePoin = jsonData.getString("reward_points");
                                    profileLogin = jsonData.getString("last_login");

                                    if(profilePoin.equals("null")) {
                                        profilePoin = "0";
                                    }

                                    tvPoin.setText(profilePoin + " TKN");
                                    tvName.setText(profileName);
                                    tvNim.setText(profileNim);
                                    tvEmail.setText(profileEmail);
                                    tvGeneration.setText("Angkatan " + profileGeneration);
                                    tvLastLogin.setText("Last login at " + profileLogin);

                                    String firstCharPassword = profileEmailPassword.substring(0,1);
                                    tvEmailPassword.setText(firstCharPassword + "*****");

                                    GlideApp
                                            .with(mContext)
                                            .load(profileImage)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(ivPhoto);

                                } else {
                                    llError.setVisibility(View.VISIBLE);
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
                                llError.setVisibility(View.VISIBLE);

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
                        llError.setVisibility(View.VISIBLE);
                        Helper.showTimeOut(mContext);
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }
}
