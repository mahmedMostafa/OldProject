package com.reload.petsstore.itemdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.reload.petsstore.R;
import com.reload.petsstore.common.ApiService;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.common.WebServiceClient;
import com.reload.petsstore.databinding.ActivityItemDetailsBinding;
import com.reload.petsstore.itemdetails.Model.ItemDetailsResponse;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {
    ArrayList<String> mList = new ArrayList<>();

    SliderAdapterExample adapter;
    SessionMangment mSessionMangment;
    String mRecivedName, mRecivedID;

    ActivityItemDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_item_details);
        mSessionMangment = new SessionMangment(this);
        Intent intent = getIntent();
        mRecivedID = intent.getStringExtra("itemId");
        mRecivedName = intent.getStringExtra("itemName");

        initSlider();
    }

    private void initSlider(){
        adapter = new SliderAdapterExample(this, mList);
        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM);
        //set indicator animation by using SliderLayout.IndicatorAnimations.
        // :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        binding.imageSlider.startAutoCycle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        callItemDetilsApi();
    }

    void callItemDetilsApi() {
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<ItemDetailsResponse> call = apiService.getItemDetails(mRecivedID, "en", mSessionMangment.getUserDetails().get(SessionMangment.KEY_ID));
        call.enqueue(new Callback<ItemDetailsResponse>() {
            @Override
            public void onResponse(Call<ItemDetailsResponse> call, Response<ItemDetailsResponse> response) {
                mList = (ArrayList<String>) response.body().getResult().get(0).getImages();
                adapter = new SliderAdapterExample(ItemDetailsActivity.this, mList);
                binding.imageSlider.setSliderAdapter(adapter);
                binding.itemTitle.setText(response.body().getResult().get(0).getName());
                binding.itemDesc.setText(response.body().getResult().get(0).getDescription());
            }

            @Override
            public void onFailure(Call<ItemDetailsResponse> call, Throwable t) {

            }
        });
    }
}
