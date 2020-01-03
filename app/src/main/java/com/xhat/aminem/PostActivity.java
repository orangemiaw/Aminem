package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.xhat.aminem.Adapter.LostItemAdapter;
import com.xhat.aminem.Fragment.HistoryFragment;
import com.xhat.aminem.Model.AlllostitemItem;
import com.xhat.aminem.Model.CategoryItem;
import com.xhat.aminem.Model.PlaceSaveItem;
import com.xhat.aminem.Model.ResponseCategoryItem;
import com.xhat.aminem.Model.ResponsePlaceSaveItem;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    Button btnImage, btnPost;
    ImageView ivItem;
    EditText etNama, etPlaceFound, etDesc;
    Spinner spinnerCategory, spinnerPlaceSave;
    String mediaPath;
    String[] mediaColumns = { MediaStore.Video.Media._ID };

    private ProgressDialog loading;
    private Context mContext;
    private BaseApiService mApiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Posting");

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        btnImage = findViewById(R.id.btn_image);
        btnPost = findViewById(R.id.btn_post);
        ivItem = findViewById(R.id.iv_item);
        etNama = findViewById(R.id.et_nama);
        etPlaceFound = findViewById(R.id.et_place_found);
        etDesc = findViewById(R.id.et_desc);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerPlaceSave = findViewById(R.id.spinner_place_save);

        // validate permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        initSpinnerCategory();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = parent.getItemAtPosition(position).toString();
                Toast.makeText(mContext, "You selected category " + selectedName, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "You selected place save " + selectedName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                ivItem.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    // Uploading Image/Video
    private void addItem() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        // RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), etNama.getText().toString());
        RequestBody category = RequestBody.create(MediaType.parse("text/plain"), spinnerCategory.getSelectedItem().toString());
        RequestBody place_found = RequestBody.create(MediaType.parse("text/plain"), etPlaceFound.getText().toString());
        RequestBody place_save = RequestBody.create(MediaType.parse("text/plain"), spinnerPlaceSave.getSelectedItem().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), etDesc.getText().toString());

        // API call here
        mApiService.addItem(sessionManager.getSPToken(), fileToUpload, filename, category, place_found, place_save, description)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                JSONObject jsonData = jsonResults.getJSONObject("data");
                                String itemName = jsonData.getString("name");

                                if (itemName != null){
                                    Helper.showAlertDialog(mContext,"Success", "Post successfully created.");
                                    startActivity(new Intent(mContext, MainActivity.class));
                                } else {
                                    Helper.showAlertDialog(mContext,"Error", "Failed to create post.");
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
                    Toast.makeText(mContext, "Gagal mengambil data kategori", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCategoryItem> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "Gagal mengambil data tempat pengumpulan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePlaceSaveItem> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
            {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do something you want
                } else {
                    // permission denied
                    Toast.makeText(mContext, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
