package com.reload.petsstore.items;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.reload.petsstore.R;
import com.reload.petsstore.common.ApiService;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.common.WebServiceClient;
import com.reload.petsstore.databinding.ActivityItemsBinding;
import com.reload.petsstore.fav.Model.
        AddFav;
import com.reload.petsstore.items.Model.ItemResponse;
import com.reload.petsstore.items.Model.ItemResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsActivity extends AppCompatActivity implements ItemAdapter.changeFav {

    ArrayList<ItemResult> mList;
    ItemAdapter mAdapter;
    String mRecivedCatId = "";
    SessionMangment mSessionMangment;

    ActivityItemsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_items);
        mSessionMangment = new SessionMangment(this);
        initViews();
    }

    private void initViews() {

        Intent intent = getIntent();
        binding.pageTitle.setText(intent.getStringExtra("catName") + "");
        mRecivedCatId = intent.getStringExtra("CatID" + "");
        mList = new ArrayList<>();
        refresh();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ItemsActivity.this, RecyclerView.VERTICAL, false);
        binding.itemRv.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callItemByCatId(mSessionMangment.getUserDetails().get(SessionMangment.KEY_ID), mRecivedCatId);

    }

    void refresh() {
        mAdapter = new ItemAdapter(mList, ItemsActivity.this, this);
        binding.itemRv.setAdapter(mAdapter);
    }

    void callItemByCatId(String userID, String CatId) {
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<ItemResponse> call = apiService.getItemsByCat(CatId, "en", userID);
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                mList.add((ItemResult) response.body().getResult());
                refresh();
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void change(int hasFav,String itemId) {
        if (hasFav == 1) {
            addFav(itemId);
        } else {
            removeFav(itemId);
        }
    }

    private void removeFav(String itemId) {
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<AddFav> call = apiService.removeFav(mSessionMangment.getUserDetails().get(SessionMangment.KEY_ID), itemId);
        call.enqueue(new Callback<AddFav>() {
            @Override
            public void onResponse(Call<AddFav> call, Response<AddFav> response) {
                Toast.makeText(ItemsActivity.this, "Remove Fav", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddFav> call, Throwable t) {

            }
        });

    }

    private void addFav(String itemId) {
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<AddFav> call = apiService.addFav(mSessionMangment.getUserDetails().get(SessionMangment.KEY_ID), itemId);
        call.enqueue(new Callback<AddFav>() {
            @Override
            public void onResponse(Call<AddFav> call, Response<AddFav> response) {
                Toast.makeText(ItemsActivity.this, "Add Fav", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddFav> call, Throwable t) {

            }
        });

    }


}
