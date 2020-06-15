package com.reload.petsstore.common;


import com.reload.petsstore.auth.AuthModel.SignUpResponse;
import com.reload.petsstore.fav.Model.AddFav;
import com.reload.petsstore.homecategory.homeFragment.HomeCatResponse;
import com.reload.petsstore.itemdetails.Model.ItemDetailsResponse;
import com.reload.petsstore.items.Model.ItemResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("register")
    Call<SignUpResponse> reigister(@Body String fName,
                                   @Body String LName,
                                   @Body String phone,
                                   @Body String email,
                                   @Body String pass);







    @FormUrlEncoded
    @POST("login")
    Call<SignUpResponse> login(@Field("email") String email, @Field("password") String pass);


    @GET("categories")
    Call<HomeCatResponse> getAllCat();


    @GET("category/{category_id}/items")
    Call<ItemResponse> getItemsByCat(@Path(value = "category_id", encoded = true) String category_id,
                                     @Query("lang") String lang,
                                     @Query("user") String user);


    @GET("item/{item_id}")
    Call<ItemDetailsResponse> getItemDetails(@Path(value = "item_id", encoded = true) String itemID,
                                             @Query("lang") String lang,
                                             @Query("user") String user);


    @FormUrlEncoded
    @POST("favorite/add")
    Call<AddFav> addFav(@Field("user_id") String userId, @Field("item_id") String itemId);


    @FormUrlEncoded
    @POST("/fav/remove")
    Call<AddFav> removeFav(@Field("user_id") String userId, @Field("item_id") String itemId);



    @GET("user/{user_id}/favorites")
    Call<ItemResponse> getUserFav(@Path(value = "user_id" , encoded = true) String userID);


    @FormUrlEncoded
    @POST("user/{user_id}/edit")
    Call<SignUpResponse> editProfile(@Path(value = "user_id", encoded = true) String userID,
                                     @Field("first_name") String fName,
                                     @Field("last_name") String LName,
                                     @Field("phone") String phone,
                                     @Field("email") String email);



    @Multipart
    @POST("user/{user_id}/upload-image")
    Call<AddFav> uploadImage(@Path(value = "user_id", encoded = true) String userID,
                              @Part MultipartBody.Part file);
}

