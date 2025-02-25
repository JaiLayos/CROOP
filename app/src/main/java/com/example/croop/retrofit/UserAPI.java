package com.example.croop.retrofit;

import com.example.croop.model.Customer;
import com.example.croop.model.DiscountDTO;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersDiscount;
import com.example.croop.model.GroupSellersItemInventory;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.IndividualSellersDiscount;
import com.example.croop.model.IndividualSellersItemInventory;
import com.example.croop.model.IndividualSellersProductsInventory;
import com.example.croop.model.SellerOrdersDTO;

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
    Call<List<SellerOrdersDTO>> getGroupSellersOrders(@Path("firebaseID") String firebaseID);


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

    //Individual Sellers Controller
    @POST("api/individual-sellers")
    Call<IndividualSellers> addIndividualSellers(@Body IndividualSellers individualSellers);
    @GET("api/individual-sellers/{id}")
    Call<IndividualSellers> getIndividualSellers(@Path("id") int id);
    @GET("api/individual-sellers/id/{firebaseID}")
    Call<Integer> getIndividualSellersID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers/orders/{firebaseID}")
    Call<List<SellerOrdersDTO>> getGroupSellersbyFirebase(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers/details/{firebaseID}")
    Call<IndividualSellers> getIndividualSellersbyFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers")
    Call<List<IndividualSellers>> getAllIndividualSellers();
    @PUT("api/individual-sellers/{id}")
    Call<IndividualSellers> updateGroupSellers(@Path("id") int id, @Body IndividualSellers individualSellers);
    @PUT("api/individual-sellers/firebase/{firebaseID}")
    Call<IndividualSellers> updateGroupSellersByFirebaseID(@Path("firebaseID") String firebaseID, @Body IndividualSellers individualSellers);
    @DELETE("api/individual-sellers/{id}")
    Call<IndividualSellers> deleteGroupSellers(@Path("id") int id);

    //Individual Sellers Item
    @POST("api/individual-sellers-item-inventory/add-item")
    Call<IndividualSellersItemInventory> addIndividualItem(@Body IndividualSellersItemInventory individualSellersItemInventory);
    //Get Item by ID
    @GET("api/individual-sellers-item-inventory/{id}")
    Call<IndividualSellersItemInventory> getIndividualItem(@Path("id") int id);
    @GET("api/individual-sellers-item-inventory/items/{itemName}")
    Call<List<IndividualSellersItemInventory>> getIndividualItemByName(@Path("itemName") String itemName);
    @GET("api/individual-sellers-item-inventory/items/firebase/{firebaseID}")
    Call<List<IndividualSellersItemInventory>> getIndividualItemsByFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers-item-inventory/")
    Call<List<IndividualSellersItemInventory>> getAllIndividualItems();
    @PUT("api/individual-sellers-item-inventory/{id}")
    Call<IndividualSellersItemInventory> updateIndividualItem(@Path("id") int id, @Body IndividualSellersItemInventory individualSellersItemInventory);
    @DELETE("api/individual-sellers-item-inventory/{id}")
    Call<IndividualSellersItemInventory> deleteIndividualItem(@Path("id") int id);

    //Individual Sellers Products
    @POST("api/individual-sellers-products-inventory/add-item")
    Call<IndividualSellersProductsInventory> addIndividualProducts(@Body IndividualSellersProductsInventory individualSellersProductsInventory);
    @GET("api/individual-sellers-products-inventory/{id}")
    Call<IndividualSellersProductsInventory> getIndividualProducts(@Path("id") int id);
    @GET("api/individual-sellers-products-inventory/items/{itemName}")
    Call<List<IndividualSellersProductsInventory>> getIndividualProductsByName(@Path("itemName") String itemName);
    @GET("api/individual-sellers-products-inventory/items/firebase/{firebaseID}")
    Call<List<IndividualSellersProductsInventory>> getIndividualProductsByFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers-products-inventory")
    Call<List<IndividualSellersProductsInventory>> getAllIndividualProducts();
    @PUT("api/individual-sellers-products-inventory/{id}")
    Call<IndividualSellersProductsInventory> updateIndividualProducts(@Path("id") int id, @Body IndividualSellersProductsInventory individualSellersProductsInventory);
    @DELETE("api/individual-sellers-products-inventory/{id}")
    Call<IndividualSellersProductsInventory> deleteIndividualProducts(@Path("id") int id);

    //Individual Sellers Discounts
    @POST("api/individual-seller-discount")
    Call<IndividualSellersDiscount> addIndividualDiscount(@Body IndividualSellersDiscount individualSellersDiscount);
    @GET("api/individual-seller-discount/{id}")
    Call<DiscountDTO> getIndividualDiscount(@Path("id") int id);
    @GET("api/individual-seller-discount/firebase/{firebaseID}")
    Call<List<IndividualSellersDiscount>> getIndividualDiscountbyFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-seller-discount/discount/{itemName}")
    Call<List<IndividualSellersDiscount>> getIndividualDiscountByName(@Path("itemName") String itemName);
    @GET("api/individual-seller-discount")
    Call<List<IndividualSellersDiscount>> getAllIndividualDiscounts();
    @PUT("api/individual-seller-discount/{id}")
    Call<IndividualSellersDiscount> updateIndividualDiscount(@Path("id") int id, @Body IndividualSellersDiscount individualSellersDiscount);
    @DELETE("api/individual-seller-discount/{id}")
    Call<IndividualSellersDiscount> deleteIndividualDiscount(@Path("id") int id);
}
