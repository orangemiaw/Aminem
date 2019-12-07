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
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.xhat.aminem.LoginActivity;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.R;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProfileFragment extends Fragment {
    ImageView ivPhoto;
    TextView tvPoin, tvName, tvNim, tvEmail, tvEmailPassword;

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

        ivPhoto = view.findViewById(R.id.iv_photo);
        tvPoin = view.findViewById(R.id.tv_poin);
        tvName = view.findViewById(R.id.tv_profile_name);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvEmailPassword = view.findViewById(R.id.tv_profile_email_password);
        tvNim = view.findViewById(R.id.tv_profile_nim);
        constraintLayout = view.findViewById(R.id.constraint_layout);
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
//                                    viewDetail.setVisibility(View.VISIBLE);

                                    profileNim = jsonData.getString("nim");
                                    profileImage = jsonData.getString("profile_image");
                                    profileGeneration = jsonData.getString("generation");
                                    profileEmail = jsonData.getString("amikom_email");
                                    profileEmailPassword = jsonData.getString("amikom_email_password");
                                    profilePoin = jsonData.getString("reward_points");
                                    profileLogin = jsonData.getString("last_login");

                                    tvPoin.setText(profilePoin);
                                    tvName.setText(profileName);
                                    tvNim.setText(profileNim);
                                    tvEmail.setText(profileEmail);

                                    String firstCharPassword = profileEmailPassword.substring(0,1);
                                    tvEmailPassword.setText(firstCharPassword + "*****");


                                    GlideApp
                                            .with(mContext)
                                            .load(profileImage)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(ivPhoto);

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
}
