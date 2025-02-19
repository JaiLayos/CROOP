package com.example.croop.retrofit;

import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersItemInventory;
import com.example.croop.model.GroupSellersOrders;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {
    @POST("api/group-sellers")
    Call<Void> sendGroupSellers(@Body GroupSellers groupSellers);
    @PUT("firebase/{firebaseID}")
    Call<GroupSellers> updateGroupSellersByFirebaseID(@Path("firebaseID") String firebaseID, @Body GroupSellers groupSellers);
    @GET("api/group-sellers/id/{firebaseID}")
    Call<Integer> getGroupSellersID(@Path("firebaseID") String firebaseID);

    //ORDERS
    @GET("api/group-sellers/orders/{firebaseID}")
    Call<List<GroupSellersOrders>> getGroupSellersOrders(@Path("firebaseID") String firebaseID);


    //Group Sellers Inventory
    @POST("api/group-sellers-item-inventory/add-item")
    Call<GroupSellersItemInventory> addItem(@Body GroupSellersItemInventory groupSellersItemInventory);
    @GET("api/group-sellers-item-inventory/items/firebase/{firebaseID}")
    Call<List<GroupSellersItemInventory>> getItemsByFirebaseID(@Path("firebaseID")String firebaseID);
    @GET("api/group-sellers-item-inventory/items/{itemName}")
    Call<List<GroupSellersItemInventory>> getItemByName(@Path("itemName")String itemName);
    @PUT("api/group-sellers-item-inventory/{id}")
    Call<GroupSellersItemInventory> updateItem(@Path("id") int id, @Body GroupSellersItemInventory groupSellersItemInventory);
    @DELETE("api/group-sellers-item-inventory/{id}")
    Call<GroupSellersItemInventory> deleteItem(@Path("id") int id);
}
