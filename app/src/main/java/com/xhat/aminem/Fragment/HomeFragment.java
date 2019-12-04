package com.xhat.aminem.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.ViewFlipper;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xhat.aminem.Adapter.LostItemAdapter;
import com.xhat.aminem.LoginActivity;
import com.xhat.aminem.LostItemDetailActivity;
import com.xhat.aminem.Model.AlllostitemItem;
import com.xhat.aminem.Model.ResponseLostItem;
import com.xhat.aminem.PostActivity;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.R;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.RecyclerItemClickListener;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ViewFlipper vfBanner;
    RecyclerView rvItem;
    TextView tvProfileName, tvEmpty, tvLastFound;

    ProgressDialog loading;
    ArrayList<String> imageUrl = new ArrayList<>();

    List<AlllostitemItem> alllostitemItemList = new ArrayList<>();
    LostItemAdapter lostItemAdapter;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Dashboard");
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        vfBanner = view.findViewById(R.id.vf_banner);
        rvItem = view.findViewById(R.id.rv_item);
        tvProfileName = view.findViewById(R.id.tv_profile_name);
        tvEmpty = view.findViewById(R.id.tv_empty);
        tvLastFound = view.findViewById(R.id.tv_last_find);

        mContext = getContext();
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        // Set welcome name
        tvProfileName.setText(sessionManager.getSPUserName());

        lostItemAdapter = new LostItemAdapter(mContext, alllostitemItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvItem.setLayoutManager(mLayoutManager);
        rvItem.setItemAnimator(new DefaultItemAnimator());

        // Load data lost item
        getDataItem();

        // Load slider image from server
        getSlider();

        // Get session image slider and set to flipper
        String[] imageSlider = sessionManager.getSPImageSlider().split(",");
        for(String image : imageSlider) {
            setFlipperImage(image);
        }

        return view;
    }


    private void getDataItem(){
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.getLastItemLost(sessionManager.getSPToken())
                .enqueue(new Callback<ResponseLostItem>() {
                    @Override
                    public void onResponse(Call<ResponseLostItem> call, Response<ResponseLostItem> response) {
                        tvLastFound.setVisibility(View.VISIBLE);

                        if (response.isSuccessful()) {
                            loading.dismiss();

                            if (response.body().isError() || response.body() == null) {
                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
                                final List<AlllostitemItem> alllostitemItems = response.body().getAlllostitem();
                                rvItem.setAdapter(new LostItemAdapter(mContext, alllostitemItems));
                                lostItemAdapter.notifyDataSetChanged();

                                initDataIntent(alllostitemItems);
                            }
                        } else {
                            loading.dismiss();
                            Helper.clearSession(mContext);
                            startActivity(new Intent(mContext, LoginActivity.class));
                            Helper.showAlertDialog(mContext,"Error", "Your must login again becaouse session expired");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLostItem> call, Throwable t) {
                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
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
                        String name = alllostitemItemList.get(position).getItemName();
                        String image = alllostitemItemList.get(position).getImage();
                        String found = alllostitemItemList.get(position).getPlaceFound();
                        String save = alllostitemItemList.get(position).getPlaceSaveName();
                        String desc = alllostitemItemList.get(position).getDescription();

                        Intent detailItem = new Intent(mContext, LostItemDetailActivity.class); // sementara
                        detailItem.putExtra(Constant.KEY_ID_ITEM, id);
                        detailItem.putExtra(Constant.KEY_ITEM_NAME, name);
                        detailItem.putExtra(Constant.KEY_ITEM_IMAGE, image);
                        detailItem.putExtra(Constant.KEY_ITEM_FOUND, "Ditemukan di " + found);
                        detailItem.putExtra(Constant.KEY_ITEM_SAVE, "Dapat diambil di " + save);
                        detailItem.putExtra(Constant.KEY_ITEM_DESC, desc);
                        startActivity(detailItem);
                    }
                }));
    }

    private void getSlider() {
        mApiService.getSlider(sessionManager.getSPToken())
                .enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                JSONArray arraySlider = jsonResults.getJSONArray("data");

                                if (arraySlider != null){
                                    for (int i = 0; i < arraySlider.length(); i++) {
                                        JSONObject slider = new JSONObject(arraySlider.get(i).toString());
                                        String image = slider.getString("image");
                                        imageUrl.add(image);
                                    }

                                    String imageUrlList = String.join(",", imageUrl);
                                    sessionManager.saveSPString(SessionManager.SP_IMAGE_SLIDER, imageUrlList);
                                } else {
                                    Helper.showAlertDialog(mContext,"Error", "Failed to load image slider");
                                }
                            } catch (JSONException e) {
                                Helper.showTimeOut(mContext);
                                e.printStackTrace();
                            } catch (IOException e) {
                                Helper.showTimeOut(mContext);
                                e.printStackTrace();
                            }

                            loading.dismiss();
                        } else {
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

    private void setFlipperImage(String res) {
        Log.i("Set Filpper Called", res+"");
        final ImageView image = new ImageView(getContext());
        GlideApp.with(this)
                .load(res)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        image.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        vfBanner.addView(image);
        vfBanner.setFlipInterval(3000);
        vfBanner.setAutoStart(true);

        vfBanner.setInAnimation(getContext(), android.R.anim.slide_in_left);
        vfBanner.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }
}
