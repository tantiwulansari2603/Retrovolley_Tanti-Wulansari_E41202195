package com.example.retrovolley;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MethodHTTP {

    //get all user
    @GET("User_Registration.php")
    Call<UserResponse> getUser();

    //get specific using username and password
    @GET("volley/Login.php?")
    Call<UserResponse> login(@Query("email") String email, @Query("password") String password);

    //get specific using id
    @GET("volley/User.php?")
    Call<UserResponse> getUserByID(@Query("id") int id);

    @POST("User_Registration.php")
    Call<Request> sendUser(@Body User user);

    //update user
    @POST("volley/User.php")
    Call<Request> updateUser(@Body User user);

    @DELETE("volley/User.php?")
    Call<Request> deleteUser(@Query("id") int id);
}
