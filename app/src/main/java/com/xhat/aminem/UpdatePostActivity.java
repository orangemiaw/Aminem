package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.xhat.aminem.Model.CategoryItem;
import com.xhat.aminem.Model.PlaceSaveItem;
import com.xhat.aminem.Model.ResponseCategoryItem;
import com.xhat.aminem.Model.ResponsePlaceSaveItem;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class UpdatePostActivity extends AppCompatActivity {
    Button btnUpdate;
    ImageView ivItem;
    EditText etNama, etPlaceFound, etDesc;
    Spinner spinnerCategory, spinnerPlaceSave;
    String itemId;

    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Post");

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        Intent intent = getIntent();
        itemId = intent.getStringExtra(Constant.KEY_ID_ITEM);

        btnUpdate = findViewById(R.id.btn_update);
        ivItem = findViewById(R.id.iv_item);
        etNama = findViewById(R.id.et_nama);
        etPlaceFound = findViewById(R.id.et_place_found);
        etDesc = findViewById(R.id.et_desc);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerPlaceSave = findViewById(R.id.spinner_place_save);

        initSpinnerCategory();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = parent.getItemAtPosition(position).toString();
                Toast.makeText(mContext, "You selected category is " + selectedName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initSpinnerPlaceSave();

        spinnerPlaceSave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = parent.getItemAtPosition(position).toString();
                Toast.makeText(mContext, "You selected place save in " + selectedName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initSpinnerCategory(){
        loading = ProgressDialog.show(mContext, null, "Harap tunggu...", true, false);

        mApiService.getAllItemCategory(sessionManager.getSPToken()).enqueue(new Callback<ResponseCategoryItem>() {
            @Override
            public void onResponse(Call<ResponseCategoryItem> call, Response<ResponseCategoryItem> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    List<CategoryItem> categoryItems = response.body().getCategoryItem();
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < categoryItems.size(); i++){
                        listSpinner.add(categoryItems.get(i).getType());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                } else {
                    loading.dismiss();
                    Helper.showAlertDialog(mContext,"Error", "Failed to get category data.");
                }
            }

            @Override
            public void onFailure(Call<ResponseCategoryItem> call, Throwable t) {
                loading.dismiss();
                Helper.showTimeOut(mContext);
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void initSpinnerPlaceSave(){
        mApiService.getAllPlaceSave(sessionManager.getSPToken()).enqueue(new Callback<ResponsePlaceSaveItem>() {
            @Override
            public void onResponse(Call<ResponsePlaceSaveItem> call, Response<ResponsePlaceSaveItem> response) {
                if (response.isSuccessful()) {
                    List<PlaceSaveItem> placeSaveItems = response.body().getPlaceSaveItem();
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < placeSaveItems.size(); i++){
                        listSpinner.add(placeSaveItems.get(i).getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPlaceSave.setAdapter(adapter);
                } else {
                    Helper.showAlertDialog(mContext,"Error", "Failed to get place save data.");
                }
            }

            @Override
            public void onFailure(Call<ResponsePlaceSaveItem> call, Throwable t) {
                Helper.showTimeOut(mContext);
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
