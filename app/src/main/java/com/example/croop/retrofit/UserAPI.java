package com.example.croop.retrofit;

import com.example.croop.model.Customer;
import com.example.croop.model.DiscountDTO;
import com.example.croop.model.GroupSellerOrdersDTO;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersDiscount;
import com.example.croop.model.GroupSellersItemInventory;
import com.example.croop.model.GroupSellersProductsInventory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {
    //Group Sellers
    @POST("api/group-sellers")
    Call<Void> sendGroupSellers(@Body GroupSellers groupSellers);
    @PUT("firebase/{firebaseID}")
    Call<GroupSellers> updateGroupSellersByFirebaseID(@Path("firebaseID") String firebaseID, @Body GroupSellers groupSellers);
    @GET("api/group-sellers/id/{firebaseID}")
    Call<Integer> getGroupSellersID(@Path("firebaseID") String firebaseID);
    @GET("api/group-sellers/{id}")
    Call<GroupSellers> getGroupSellers(@Path("id") int id);
    @GET("api/group-sellers/details/{firebaseID}")
    Call<GroupSellers> getGroupSellersbyFirebaseID(@Path("firebaseID") String firebaseID);

    //CUSTOMERS
    @GET("api/customers/{id}")
    Call<Customer> getCustomer(@Path("id") int id);
    @GET("api/customers/name/{id}")
    Call<String> getCustomerName(@Path("id") int id);
    //ORDERS
    @GET("api/group-sellers/orders/{firebaseID}")
    Call<List<GroupSellerOrdersDTO>> getGroupSellersOrders(@Path("firebaseID") String firebaseID);


    //Group Sellers Item Inventory
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

    //Group Sellers Products Inventory
    @POST("api/group-sellers-products")
    Call<GroupSellersProductsInventory> addProduct(@Body GroupSellersProductsInventory groupSellersProductsInventory);
    @GET("api/group-sellers-products/{id}")
    Call<GroupSellersProductsInventory> getProduct(@Path("id") int id);
    @GET("api/group-sellers-products/products/{itemName}")
    Call<List<GroupSellersProductsInventory>> getProductsByName(@Path("itemName")String itemName);
    @GET("api/group-sellers-products/items/firebase/{firebaseID}")
    Call<List<GroupSellersProductsInventory>> getProductsByFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/group-sellers-products")
    Call<List<GroupSellersProductsInventory>> getAllProducts();
    @PUT("api/group-sellers-products/{id}")
    Call<GroupSellersProductsInventory> updateProducts(@Path("id") int id, @Body GroupSellersProductsInventory groupSellersProductsInventory);
    @DELETE("api/group-sellers-products/{id}")
    Call<GroupSellersProductsInventory> deleteProducts(@Path("id") int id);

    //Group Sellers Discount
    @POST("api/group-seller-discount")
    Call<GroupSellersDiscount> addDiscount(@Body GroupSellersDiscount groupSellerDiscount);
    @GET("api/group-seller-discount/firebase/{firebaseID}")
    Call<List<GroupSellersDiscount>> getDiscountbyFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/group-seller-discount/discount/{itemName}")
    Call<List<GroupSellersDiscount>> getDiscountByName(@Path("itemName") String itemName);
    @GET("api/group-seller-discount/{id}")
    Call<DiscountDTO> getDiscount(@Path("id") int id);
    @PUT("api/group-seller-discount/{id}")
    Call<GroupSellersDiscount> updateDiscount(@Path("id") int id, @Body GroupSellersDiscount groupSellerDiscount);
    //////

}
