package com.reload.petsstore.homecategory.homeFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reload.petsstore.common.ApiService;
import com.reload.petsstore.common.WebServiceClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<HomeCatResponse> responseMutableLiveData;

    public HomeViewModel(){
        responseMutableLiveData = new MutableLiveData<>();
        getData();
    }

    private void getData(){
        ApiService apiService = WebServiceClient.getRetrofit().create(ApiService.class);
        Call<HomeCatResponse> call = apiService.getAllCat();


        call.enqueue(new Callback<HomeCatResponse>() {
            @Override
            public void onResponse(Call<HomeCatResponse> call, Response<HomeCatResponse> response) {
               responseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeCatResponse> call, Throwable t) {

            }
        });
    }

    public LiveData<HomeCatResponse> getCatResponse(){
        return responseMutableLiveData;
    }
}
