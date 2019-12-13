package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PickupLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;

    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;

    CardView cvPlace;
    ImageView ivPlace;
    TextView tvPlaceName, tvPlaceDesc;
    String placeId, placeName, placeImage, placeDesc, placeStatus;
    double placeLon, placeLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_location);

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pickup Location");

        Intent intent = getIntent();
        placeId = intent.getStringExtra(Constant.KEY_ID_PLACE);

        // Get data froms erver
        placeSaveDetail();

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();

        tvPlaceName = findViewById(R.id.tv_place_name);
        tvPlaceDesc = findViewById(R.id.tv_place_desc);
        ivPlace = findViewById(R.id.iv_place);
        cvPlace = findViewById(R.id.cv_place);
        constraintLayout = findViewById(R.id.constrain_place);
        layout2.clone(mContext, R.layout.pickup_location_expanded);
        layout1.clone(constraintLayout);

        ivPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ACTION INFO", "Image clicked");
                if (!isOpen) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = !isOpen;
                } else {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout1.applyTo(constraintLayout);
                    isOpen = !isOpen;
                }
            }
        });
    }

    private void placeSaveDetail() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.getPlaceDetail(sessionManager.getSPToken(), placeId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                JSONObject jsonData = jsonResults.getJSONObject("data");
                                placeName = jsonData.getString("name");

                                if (!placeName.equals("null")){
                                    placeImage = jsonData.getString("image");
                                    placeLat = jsonData.getDouble("latitude");
                                    placeLon = jsonData.getDouble("longitude");
                                    placeStatus = jsonData.getString("status");
                                    placeDesc = "Latitude: " + placeLat + " - Longitude: " + placeLon;

                                    tvPlaceName.setText(placeName);
                                    tvPlaceDesc.setText(placeDesc);

                                    GlideApp
                                            .with(mContext)
                                            .load(placeImage)
                                            .into(ivPlace);

                                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                            .findFragmentById(R.id.map);
                                    mapFragment.getMapAsync((OnMapReadyCallback) mContext);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng TO_DESTINATION  = new LatLng(placeLat, placeLon);
        mMap.addMarker(new MarkerOptions().position(TO_DESTINATION).title(placeName)
                .snippet(placeDesc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TO_DESTINATION, 19));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
