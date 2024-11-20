package com.example.croop.retrofit;

import com.example.croop.model.Customer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {
    @POST("/add-customer")
    Call<Customer> save(@Body Customer customer);
}
