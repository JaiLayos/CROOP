package com.example.croop.retrofit;

import com.example.croop.model.Cart;
import com.example.croop.model.CartDTO;
import com.example.croop.model.CartGroupedResponseDTO;
import com.example.croop.model.Customer;
import com.example.croop.model.CustomerOrdersForGroupSellers;
import com.example.croop.model.CustomerOrdersForIndivSellers;
import com.example.croop.model.DailySalesDTO;
import com.example.croop.model.DiscountDTO;
import com.example.croop.model.FeaturedSellersDTO;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersDiscount;
import com.example.croop.model.GroupSellersItemInventory;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.IndividualSellersDiscount;
import com.example.croop.model.IndividualSellersItemInventory;
import com.example.croop.model.IndividualSellersProductsInventory;
import com.example.croop.model.Notifications;
import com.example.croop.model.ProductDTO;
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
    @PUT("api/group-sellers/firebase/{firebaseID}")
    Call<GroupSellers> updateGroupSellersByFirebaseID(@Path("firebaseID") String firebaseID, @Body GroupSellers groupSellers);
    @GET("api/group-sellers/id/{firebaseID}")
    Call<Integer> getGroupSellersID(@Path("firebaseID") String firebaseID);
    @GET("api/group-sellers/{id}")
    Call<GroupSellers> getGroupSellers(@Path("id") int id);
    @GET("api/group-sellers/group-name/{groupName}")
    Call<List<FeaturedSellersDTO>> getGroupSellerByGroupName(@Path("groupName") String groupName);
    @GET("api/group-sellers/details/{firebaseID}")
    Call<GroupSellers> getGroupSellersbyFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/group-sellers/featured")
    Call<List<FeaturedSellersDTO>> getFeaturedGroup();
    @GET("api/group-sellers/dto")
    Call<List<FeaturedSellersDTO>> getAllGroupSellers();

    //CUSTOMERS
    @GET("api/customers/{id}")
    Call<Customer> getCustomer(@Path("id") int id);
    @GET("api/customers/name/{id}")
    Call<String> getCustomerName(@Path("id") int id);
    @GET("api/customers")
    Call<List<Customer>> getAllCustomers();
    @GET("api/customers/firebase/{firebaseID}")
    Call<Customer> getCustomerByFirebaseID(@Path("firebaseID") String firebaseID);
    @POST("api/customers")
    Call<Customer> addCustomer(@Body Customer customer);
    @PUT("api/customers/{id}")
    Call<Customer> updateCustomer(@Path("id") int id, @Body Customer customer);
    @DELETE("api/customers/{id}")
    Call<Void> deleteCustomer(@Path("id") int id);

    //ORDERS
    @GET("api/group-sellers/orders/{firebaseID}")
    Call<List<SellerOrdersDTO>> getGroupSellersOrders(@Path("firebaseID") String firebaseID);

    //Group Order Endpoint
    @POST("api/customer-orders/group/{customerId}/{groupSellerId}")
    Call<CustomerOrdersForGroupSellers> createGroupOrder(
            @Path("customerId") int customerId,
            @Path("groupSellerId") int groupSellerId,
            @Body CustomerOrdersForGroupSellers order
    );
    @GET("api/customer-orders/group/{id}")
    Call<CustomerOrdersForGroupSellers> getGroupOrder(@Path("id") int id);
    @GET("api/customer-orders/group")
    Call<List<CustomerOrdersForGroupSellers>> getAllGroupOrders();
    @GET("api/customer-orders/group/seller/{id}")
    Call<List<SellerOrdersDTO>> getGroupOrderByGroupSellerId(@Path("id") int id);
    @GET("api/customer-orders/group/customer/{id}")
    Call<List<SellerOrdersDTO>> getGroupOrderByCustomer(@Path("id") int id);
    @PUT("api/customer-orders/group/{id}")
    Call<CustomerOrdersForGroupSellers> updateGroupOrder(
            @Path("id") int id,
            @Body CustomerOrdersForGroupSellers updatedOrder
    );

    @GET("api/customer-orders/group/sales/{sellerId}")
    Call<List<DailySalesDTO>> getDailySales(
            @Path("sellerId") int sellerId);
    @DELETE("api/customer-orders/group/{id}")
    Call<Void> deleteGroupOrder(@Path("id") int id);

    // Individual Order Endpoints
    @POST("api/customer-orders/individual/{customerId}/{sellerId}")
    Call<CustomerOrdersForIndivSellers> createIndividualOrder(
            @Path("customerId") int customerId,
            @Path("sellerId") int sellerId,
            @Body CustomerOrdersForIndivSellers order
    );
    @GET("api/customer-orders/individual/{id}")
    Call<CustomerOrdersForIndivSellers> getIndividualOrder(@Path("id") int id);
    @GET("api/customer-orders/individual/customer/{id}")
    Call<List<SellerOrdersDTO>> getIndividualOrderByCustomer(@Path("id") int id);
    @GET("api/customer-orders/individual")
    Call<List<CustomerOrdersForIndivSellers>> getAllIndividualOrders();
    @PUT("api/customer-orders/individual/{id}")
    Call<CustomerOrdersForIndivSellers> updateIndividualOrder(
            @Path("id") int id,
            @Body CustomerOrdersForIndivSellers updatedOrder
    );
    @GET("api/customer-orders/individual/seller/{id}")
    Call<List<SellerOrdersDTO>> getIndividualOrderByGroupSellerId(@Path("id") int id);
    @DELETE("api/customer-orders/individual/{id}")
    Call<Void> deleteIndividualOrder(@Path("id") int id);

    //Group Sellers Item Inventory
    @POST("api/group-sellers-item-inventory/add-item")
    Call<GroupSellersItemInventory> addItem(@Body GroupSellersItemInventory groupSellersItemInventory);
    @GET("api/group-sellers-item-inventory/item/{itemName}/{id}")
    Call<List<GroupSellersItemInventory>> getItemByNameByIndividualID(@Path("itemName") String itemName,
                                                                 @Path("id") int id);
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
    @GET("api/group-sellers-products/product/{id}")
    Call<ProductDTO> getGroupProductDTO(@Path("id") int id);
    @GET("api/group-sellers-products/products/{itemName}/{id}")
    Call<List<GroupSellersProductsInventory>> getItemByNameByGroupID(@Path("itemName") String itemName,
                                                                                      @Path("id") int id);
    @GET("api/group-sellers-products/products/{itemName}")
    Call<List<GroupSellersProductsInventory>> getProductsByName(@Path("itemName")String itemName);
    @GET("api/group-sellers-products/productDTO/{itemName}")
    Call<List<ProductDTO>> getGroupProductDTOByName(@Path("itemName") String itemName);
    @GET("api/group-sellers-products/items/firebase/{firebaseID}")
    Call<List<GroupSellersProductsInventory>> getProductsByFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/group-sellers-products")
    Call<List<ProductDTO>> getAllProducts();
    @GET("api/group-sellers-products/productsDTO/firebase/{firebaseID}")
    Call<List<ProductDTO>> getGroupProductsDTOsByFirebaseID(@Path("firebaseID") String firebaseID);
    @PUT("api/group-sellers-products/{id}")
    Call<GroupSellersProductsInventory> updateProducts(@Path("id") int id, @Body GroupSellersProductsInventory groupSellersProductsInventory);
    @DELETE("api/group-sellers-products/{id}")
    Call<GroupSellersProductsInventory> deleteProducts(@Path("id") int id);
    @GET("api/group-sellers-products/in-season")
    Call<List<ProductDTO>> getInSeasonGroupProducts();
    @GET("api/group-sellers-products/in-demand")
    Call<List<ProductDTO>> getInDemandGroupProducts();
    @GET("api/group-sellers-products/checkSC/{id}")
    Call<Boolean> checkGroupSellerSC(@Path("id") int id);
    @GET("api/group-sellers-products/checkMC/{id}")
    Call<Boolean> checkGroupSellerMC(@Path("id") int id);

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
    @GET("api/group-seller-discount")
    Call<List<DiscountDTO>> getAllDiscounts();


    //Individual Sellers Controller
    @POST("api/individual-sellers")
    Call<IndividualSellers> addIndividualSellers(@Body IndividualSellers individualSellers);
    @GET("api/individual-sellers/{id}")
    Call<IndividualSellers> getIndividualSellers(@Path("id") int id);
    @GET("api/individual-sellers/id/{firebaseID}")
    Call<Integer> getIndividualSellersID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers/orders/{firebaseID}")
    Call<List<SellerOrdersDTO>> getIndividualSellersbyFirebase(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers/details/{firebaseID}")
    Call<IndividualSellers> getIndividualSellersbyFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers")
    Call<List<IndividualSellers>> getAllIndividualSellers();
    @GET("api/individual-sellers/individual-seller/{name}")
    Call<List<FeaturedSellersDTO>> findIndividualSellerByName(@Path("name") String name);
    @GET("api/individual-sellers/dto")
    Call<List<FeaturedSellersDTO>> getAllIndividualSellersDTO();
    @GET("api/individual-sellers/featured")
    Call<List<FeaturedSellersDTO>> getFeaturedIndividual();
    @PUT("api/individual-sellers/{id}")
    Call<IndividualSellers> updateIndividualSellers(@Path("id") int id, @Body IndividualSellers individualSellers);
    @PUT("api/individual-sellers/firebase/{firebaseID}")
    Call<IndividualSellers> updateIndividualSellersByFirebaseID(@Path("firebaseID") String firebaseID, @Body IndividualSellers individualSellers);
    @DELETE("api/individual-sellers/{id}")
    Call<IndividualSellers> deleteIndividualSellers(@Path("id") int id);


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
    @POST("api/individual-sellers-products/add-item")
    Call<IndividualSellersProductsInventory> addIndividualProducts(@Body IndividualSellersProductsInventory individualSellersProductsInventory);
    @GET("api/individual-sellers-products/{id}")
    Call<IndividualSellersProductsInventory> getIndividualProducts(@Path("id") int id);
    @GET("api/individual-sellers-products/product/{id}")
    Call<ProductDTO> getIndividualProductDTO(@Path("id") int id);
    @GET("api/individual-sellers-products/items/{itemName}")
    Call<List<IndividualSellersProductsInventory>> getIndividualProductsByName(@Path("itemName") String itemName);
    @GET("api/individual-sellers-products/productDTO/{itemName}")
    Call<List<ProductDTO>> getIndividualProductDTOByName(@Path("itemName") String itemName);
    @GET("api/individual-sellers-products/products/firebase/{firebaseID}")
    Call<List<ProductDTO>> getIndividualProductDTOsByFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers-products/items/firebase/{firebaseID}")
    Call<List<IndividualSellersProductsInventory>> getIndividualProductsByFirebaseID(@Path("firebaseID") String firebaseID);
    @GET("api/individual-sellers-products")
    Call<List<ProductDTO>> getAllIndividualProducts();
    @PUT("api/individual-sellers-products/{id}")
    Call<IndividualSellersProductsInventory> updateIndividualProducts(@Path("id") int id, @Body IndividualSellersProductsInventory individualSellersProductsInventory);
    @DELETE("api/individual-sellers-products/{id}")
    Call<IndividualSellersProductsInventory> deleteIndividualProducts(@Path("id") int id);
    @GET("api/individual-sellers-products/in-season")
    Call<List<ProductDTO>> getInSeasonIndividualProducts();
    @GET("api/individual-sellers-products/in-demand")
    Call<List<ProductDTO>> getInDemandIndividualProducts();
    @GET("api/individual-sellers-products/checkSC/{id}")
    Call<Boolean> checkIndividualSellerSC(@Path("id") int id);
    @GET("api/individual-sellers-products/checkMC/{id}")
    Call<Boolean> checkIndividualSellerMC(@Path("id") int id);


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
    Call<List<DiscountDTO>> getAllIndividualDiscounts();
    @PUT("api/individual-seller-discount/{id}")
    Call<IndividualSellersDiscount> updateIndividualDiscount(@Path("id") int id, @Body IndividualSellersDiscount individualSellersDiscount);
    @DELETE("api/individual-seller-discount/{id}")
    Call<IndividualSellersDiscount> deleteIndividualDiscount(@Path("id") int id);

    ///Cart
    @POST("api/cart")
    Call<Cart> addCart(@Body Cart cart);
    @GET("api/cart/grouped/{customerId}")
    Call<CartGroupedResponseDTO> getGroupedCart(@Path("customerId") int customerId);
    @GET("api/cart/from-group")
    Call<List<CartDTO>> getAllCartFromGroup();
    @GET("api/cart/from-individual")
    Call<List<CartDTO>> getAllCartFromIndividual();
    @GET("api/cart/from-group/{id}")
    Call<CartDTO> getCartFromGroup(@Path("id") int id);
    @GET("api/cart/from-individual/{id}")
    Call<CartDTO> getCartFromIndividual(@Path("id") int id);
    @GET("api/cart/from-group/seller/{id}")
    Call<List<CartDTO>> getGroupCartBySellerID(@Path("id") int sellerId);
    @GET("api/cart/from-individual/seller/{id}")
    Call<List<CartDTO>> getIndividualCartBySellerID(@Path("id") int sellerId);
    @PUT("api/cart/{id}")
    Call<Cart> updateCart(@Path("id") int id, @Body Cart cart);
    @DELETE("api/cart/{id}")
    Call<Void> deleteCart(@Path("id") int id);

    ///Notifications
    @POST("api/group-sellers-products/notification/shelf-life-threshold/{id}")
    Call<String> checkGroupSellerShelfLife(@Path("id") int id);
    @GET("api/notifications/{userID}/{userType}")
    Call<List<Notifications>> getNotificationOfUser(@Path("userID") int userID, @Path("userType") String userType);
    @POST("api/individual-sellers-products/notification/shelf-life-threshold/{id}")
    Call<String> checkIndividualSellerShelfLife(@Path("id") int id);

}
