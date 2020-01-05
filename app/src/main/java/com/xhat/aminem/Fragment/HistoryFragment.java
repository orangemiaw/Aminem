package com.xhat.aminem.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xhat.aminem.Adapter.HistoryAdapter;
import com.xhat.aminem.LoginActivity;
import com.xhat.aminem.LostItemByCategoryActivity;
import com.xhat.aminem.LostItemDetailActivity;
import com.xhat.aminem.MainActivity;
import com.xhat.aminem.Model.AlllostitemItem;
import com.xhat.aminem.Model.ResponseLostItem;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.R;
import com.xhat.aminem.UpdatePostActivity;
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

public class HistoryFragment extends Fragment {
    private RecyclerView rvItem;
    LinearLayout llContent, llError, llEmpty;

    private ProgressDialog loading;

    private List<AlllostitemItem> alllostitemItemList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private Context mContext;
    private BaseApiService mApiService;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        getActivity().setTitle("History");
        setHasOptionsMenu(true);

        mContext = getContext();
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        rvItem = view.findViewById(R.id.rv_item);
        llContent = view.findViewById(R.id.ll_content);
        llError = view.findViewById(R.id.ll_error);
        llEmpty = view.findViewById(R.id.ll_empty);

        historyAdapter = new HistoryAdapter(mContext, alllostitemItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvItem.setLayoutManager(mLayoutManager);
        rvItem.setItemAnimator(new DefaultItemAnimator());

        // Load data lost item
        getDataItem();

        return view;
    }

    private void getDataItem(){
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.getItemHistory(sessionManager.getSPToken(), sessionManager.getSPUserId(), "DESC")
                .enqueue(new Callback<ResponseLostItem>() {
                    @Override
                    public void onResponse(Call<ResponseLostItem> call, Response<ResponseLostItem> response) {
                        loading.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body().isError() || response.body() == null) {
                                llEmpty.setVisibility(View.VISIBLE);
                            } else {
                                llContent.setVisibility(View.VISIBLE);

                                final List<AlllostitemItem> alllostitemItems = response.body().getAlllostitem();
                                rvItem.setAdapter(new HistoryAdapter(mContext, alllostitemItems));
                                historyAdapter.notifyDataSetChanged();

                                initDataIntent(alllostitemItems);
                            }
                        } else {
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLostItem> call, Throwable t) {
                        loading.dismiss();
                        llError.setVisibility(View.VISIBLE);
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Helper.showTimeOut(mContext);
                    }
                });
    }

    private void initDataIntent(final List<AlllostitemItem> alllostitemItemList){

        rvItem.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        String id = alllostitemItemList.get(position).getId();
                        final Intent detailItem = new Intent(mContext, LostItemDetailActivity.class);
                        detailItem.putExtra(Constant.KEY_ID_ITEM, id);

                        final Intent updateItem = new Intent(mContext, UpdatePostActivity.class);
                        updateItem.putExtra(Constant.KEY_ID_ITEM, id);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                mContext);

                        // set title dialog
                        alertDialogBuilder.setTitle("Action");

                        // set pesan dari dialog
                        alertDialogBuilder
                                .setMessage("Select an action what do you want to this post.")
                                .setIcon(R.drawable.ic_help)
                                .setCancelable(true)
                                .setNeutralButton("Detail",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(detailItem);
                                    }
                                })
                                .setNegativeButton("Update",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(updateItem);
                                    }
                                })
                                .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteItem(alllostitemItemList.get(position).getId());
                                    }
                                });

                        AlertDialog actionDialog = alertDialogBuilder.create();
                        actionDialog.show();
                    }
                }));
    }

    private void deleteItem(String itemId) {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.deleteItem(sessionManager.getSPToken(), itemId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            Helper.showAlertDialog(mContext,"Success", "Post has been deleted.");
                            startActivity(new Intent(mContext, MainActivity.class));
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
