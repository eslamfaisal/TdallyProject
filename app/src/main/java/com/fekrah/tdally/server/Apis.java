package com.fekrah.tdally.server;

import com.fekrah.tdally.models.AllAdsResponse;
import com.fekrah.tdally.models.BanksResponse;
import com.fekrah.tdally.models.BunnersResponse;
import com.fekrah.tdally.models.CategoriesResponse;
import com.fekrah.tdally.models.LoginResponse;
import com.fekrah.tdally.models.RegisterResponse;
import com.fekrah.tdally.models.Response;
import com.fekrah.tdally.models.TermsResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Apis {

    @Multipart
    @POST("register")
    Call<RegisterResponse> registerClient(
            @Part("api_token") RequestBody api_token,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("address") RequestBody address,
            @Part("phone") RequestBody mobile,
            @Part("country") RequestBody country,
            @Part("city") RequestBody city,
            @Part("location") RequestBody location,
            @Part MultipartBody.Part image

    );

    // log in user privider
    @FormUrlEncoded
    @POST("categories")
    Call<CategoriesResponse> getCategory(@Field("api_token") String api_token, @Field("lang") String lang);

    // get all cities
    @GET("banners")
    Call<BunnersResponse> getBunners();

    // get all cities
    @GET("bank")
    Call<BanksResponse> getBanks();


    // log in user privider
    @FormUrlEncoded
    @POST("adBySubCategories")
    Call<AllAdsResponse> getAllAds(@Field("sub_category_id") String sub_category_id, @Field("type") String type);

    // log in user privider
    @FormUrlEncoded
    @POST("adBySubCategories")
    Call<AllAdsResponse> getAdsBySub(@Field("sub_category_id") String sub_category_id, @Field("type") String type);


    // log in user privider
    @FormUrlEncoded
    @POST("adBySubCategories")
    Call<AllAdsResponse> getMyAds(@Field("user_token") String user_token, @Field("type") String type);


    // log in user privider
    @FormUrlEncoded
    @POST("adSearch")
    Call<AllAdsResponse> getSearch(@Field("search") String search);


    // log in user privider
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> logIn(@Field("api_token") String api_token, @Field("username") String username, @Field("password") String password);

    @Multipart
    @POST("addAds")
    Call<Response> adAdd(
            @Part("user_token") RequestBody user_token,
            @Part("name_ad") RequestBody name_ad,
            @Part("address") RequestBody address,
            @Part("category_id") RequestBody category_id,
            @Part("sub_category_id") RequestBody sub_category_id,
            @Part("price") RequestBody price,
            @Part("type_ad") RequestBody type_ad,
            @Part("phone") RequestBody phone,
            @Part("description") RequestBody description,
            @Part("location") RequestBody location,
            @Part MultipartBody.Part image

    );


    // log in user privider
    @FormUrlEncoded
    @POST("contactUs")
    Call<TermsResponse> contactUs(@Field("api_token") String api_token, @Field("lang") String lang, @Field("type") String password);


}
