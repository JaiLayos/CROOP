package com.example.croop.retrofit;

import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersOrders;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {
    @POST("api/group-sellers")
    Call<Void> sendGroupSellers(@Body GroupSellers groupSellers);

    @GET("api/group-sellers/orders/{firebaseID}")
    Call<List<GroupSellersOrders>> getGroupSellersOrders(@Path("firebaseID") String firebaseID);
}
