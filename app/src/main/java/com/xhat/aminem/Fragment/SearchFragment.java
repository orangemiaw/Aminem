package com.xhat.aminem.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.xhat.aminem.Adapter.LostItemAdapter;
import com.xhat.aminem.LoginActivity;
import com.xhat.aminem.LostItemDetailActivity;
import com.xhat.aminem.Model.AlllostitemItem;
import com.xhat.aminem.Model.ResponseLostItem;
import com.xhat.aminem.R;
import com.xhat.aminem.Utils.Api.BaseApiService;
import com.xhat.aminem.Utils.Api.RetrofitClient;
import com.xhat.aminem.Utils.Api.UtilsApi;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.RecyclerItemClickListener;
import com.xhat.aminem.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class SearchFragment extends Fragment {
    private MenuItem menuItemSearch;
    private RecyclerView rvItem;
    LinearLayout llError, llEmpty;

    private ProgressDialog loading;
    private List<AlllostitemItem> alllostitemItemList = new ArrayList<>();
    private LostItemAdapter lostItemAdapter;
    private Context mContext;
    private BaseApiService mApiService;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        getActivity().setTitle(" Search");
        setHasOptionsMenu(true);

        mContext = getContext();
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(mContext);

        rvItem = view.findViewById(R.id.rv_item);
        llError = view.findViewById(R.id.ll_error);
        llEmpty = view.findViewById(R.id.ll_empty);

        lostItemAdapter = new LostItemAdapter(mContext, alllostitemItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvItem.setLayoutManager(mLayoutManager);
        rvItem.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu,menu);
        menuItemSearch = menu.findItem(R.id.searchMenu);
        ((SearchView)menuItemSearch.getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getActivity().setTitle("");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        setupSearchBox();
    }


    private void setupSearchBox(){
        ((SearchView) menuItemSearch.getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                llEmpty.setVisibility(View.GONE);
                rvItem.setVisibility(View.VISIBLE);

                // Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                getDataItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getDataItem(String query){
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.searchItem(sessionManager.getSPToken(), query)
                .enqueue(new Callback<ResponseLostItem>() {
                    @Override
                    public void onResponse(Call<ResponseLostItem> call, Response<ResponseLostItem> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            if (response.body().isError() || response.body() == null) {
                                rvItem.setVisibility(View.GONE);
                                llEmpty.setVisibility(View.VISIBLE);
                            } else {
                                llEmpty.setVisibility(View.GONE);
                                llError.setVisibility(View.GONE);

                                final List<AlllostitemItem> alllostitemItems = response.body().getAlllostitem();
                                rvItem.setAdapter(new LostItemAdapter(mContext, alllostitemItems));
                                lostItemAdapter.notifyDataSetChanged();

                                initDataIntent(alllostitemItems);

                                rvItem.setVisibility(View.VISIBLE);
                            }
                        } else {
                            loading.dismiss();
                            rvItem.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLostItem> call, Throwable t) {
                        loading.dismiss();
                        rvItem.setVisibility(View.GONE);
                        llError.setVisibility(View.VISIBLE);
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private void initDataIntent(final List<AlllostitemItem> alllostitemItemList){
        rvItem.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id = alllostitemItemList.get(position).getId();

                        Intent detailItem = new Intent(mContext, LostItemDetailActivity.class);
                        detailItem.putExtra(Constant.KEY_ID_ITEM, id);
                        startActivity(detailItem);
                    }
                }));
    }

}
