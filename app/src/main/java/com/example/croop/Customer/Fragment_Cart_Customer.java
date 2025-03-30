package com.example.croop.Customer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croop.R;
import com.example.croop.model.CartDTO;
import com.example.croop.model.CartGroupedResponseDTO;
import com.example.croop.model.Customer;
import com.example.croop.model.CustomerOrdersForGroupSellers;
import com.example.croop.model.CustomerOrdersForIndivSellers;
import com.example.croop.model.GroupSellerCartDTO;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.IndividualSellerCartDTO;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Cart_Customer extends Fragment implements EachItemAdapter.OnUpdateClickListener{
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;
    private RecyclerView recyclerView;
    private int priceOverall, returnCustomer;
    private Map<String, Integer> orderList;
    private CustomerOrdersForGroupSellers customerOrdersForGroupSellers;
    private CustomerOrdersForIndivSellers customerOrdersForIndivSellers;
    private Customer getCustomer;
    private GroupSellers getGroupSeller;
    private UserAPI userAPI;
    private FirebaseUser user;
    private String collection, buyerAddress;
    private int id;



    public Fragment_Cart_Customer() {
    }

    public void refreshCartData() {
        fetchCartData(); // Re-fetch the cart data
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user != null) {
            DocumentReference docRef = db.collection(collection).document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> address_map = (Map<String, Object>) document.get("Address");

                            // Access individual fields
                            String city = (String) address_map.get("City");
                            String country = (String) address_map.get("Country");
                            String streetName = (String) address_map.get("House_Street_Name");
                            String postalCode = (String) address_map.get("Postal_Code");
                            String state = (String) address_map.get("State_Province_Region");
                            String subdivision = (String) address_map.get("Subdivision_Baranggay");

                            buyerAddress = (streetName + ", " + subdivision + ", " + city + ", " + state + ", " + postalCode + ", " + country);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        fetchCartData(); // This will refresh data every time fragment becomes visible
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customer_cart, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String role = prefs.getString("user_role", null);
        collection = getCollection(role);

        recyclerView = view.findViewById(R.id.holderOfGroupedItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        userAPI = RetrofitClient.getClient().create(UserAPI.class);

        return view;
    }

    private void fetchCartData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        Call<Customer> customerIDcall = userAPI.getCustomerByFirebaseID(user.getUid());
        customerIDcall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                Customer customer = response.body();
                 id = customer.getId();
                getGroupedCart(id);
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Getting Customer Error: ", t.getMessage());
            }
        });

    }

    private void getGroupedCart(int id) {
        Call<CartGroupedResponseDTO> getGroupedCartCall = userAPI.getGroupedCart(id);
        getGroupedCartCall.enqueue(new Callback<CartGroupedResponseDTO>() {
            @Override
            public void onResponse(Call<CartGroupedResponseDTO> call, Response<CartGroupedResponseDTO> response) {
                if(response.isSuccessful() && response != null){
                    priceOverall = 0;
                    orderList = new HashMap<>();
                    CartGroupedResponseDTO cartGroupedResponseDTO = response.body();
                    CartGroupedAdapter adapter = new CartGroupedAdapter(
                            requireContext(),
                            cartGroupedResponseDTO,
                            getActivity(),
                            (sellerId, isGroup) -> {
                                if (isGroup) {
                                    cartForGroup(cartGroupedResponseDTO, id, sellerId);
                                } else {
                                    cartForIndividual(cartGroupedResponseDTO, id, sellerId);
                                }
                            }
                    );
                    adapter.setUpdateListener(Fragment_Cart_Customer.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CartGroupedResponseDTO> call, Throwable t) {
                Log.e("Getting Grouped Cart Error: ", t.getMessage());
            }
        });
    }

    private void cartForIndividual(CartGroupedResponseDTO cartGroupedResponseDTO, int id, int sellerId) {
        List<IndividualSellerCartDTO> individualSellers = cartGroupedResponseDTO.getIndividualSellers();
        for(IndividualSellerCartDTO individualSeller : individualSellers){
            if(individualSeller.getId() == sellerId){
                List<CartDTO> carts = individualSeller.getCartItems();
                for(CartDTO cart : carts){
                    customerOrdersForIndivSellers = new CustomerOrdersForIndivSellers();
                    orderList.put(cart.getCropName(),cart.getQuantity());
                    priceOverall = (int) (priceOverall + cart.getPrice() + (cart.getPrice()*0.01));
                    customerOrdersForIndivSellers.setOrderList(orderList);
                    customerOrdersForIndivSellers.setOrderPrice(priceOverall);
                    customerOrdersForIndivSellers.setOrderStatus("Pending");
                    customerOrdersForIndivSellers.setOrderType("Cash-On-Delivery");
                    customerOrdersForIndivSellers.setBuyerLocation(buyerAddress);
                    customerOrdersForIndivSellers.setSellerLocation("");
                    customerOrdersForIndivSellers.setSellerType("individual");
                    deleteCart(cart.getId());
                }
            }
        }
        Call<CustomerOrdersForIndivSellers> createIndividualOrder = userAPI.createIndividualOrder(id,
                sellerId,customerOrdersForIndivSellers);
        createIndividualOrder.enqueue(new Callback<CustomerOrdersForIndivSellers>() {
            @Override
            public void onResponse(Call<CustomerOrdersForIndivSellers> call, Response<CustomerOrdersForIndivSellers> response) {
                if(response.isSuccessful() && response!= null){

                    Toast.makeText(getActivity(), "Order Added to the Group", Toast.LENGTH_SHORT).show();
                    Log.e("Add Order to Individual", "Success" + response.message());
                }
            }

            @Override
            public void onFailure(Call<CustomerOrdersForIndivSellers> call, Throwable t) {
                Log.e("Add Order to Individual", "Error" + t.getMessage());
            }
        });
    }

    private void cartForGroup(CartGroupedResponseDTO cartGroupedResponseDTO, int id, int sellerId) {
        List<GroupSellerCartDTO> groupSellers = cartGroupedResponseDTO.getGroupSellers();
        for(GroupSellerCartDTO groupSeller : groupSellers){
            if(groupSeller.getId() == sellerId){
                List<CartDTO> carts = groupSeller.getCartItems();
                for(CartDTO cart : carts){
                    customerOrdersForGroupSellers = new CustomerOrdersForGroupSellers();
                    orderList.put(cart.getCropName(),cart.getQuantity());
                    priceOverall = priceOverall + cart.getPrice();
                    customerOrdersForGroupSellers.setOrderList(orderList);
                    customerOrdersForGroupSellers.setOrderPrice(priceOverall);
                    customerOrdersForGroupSellers.setOrderStatus("Pending");
                    customerOrdersForGroupSellers.setOrderType("Cash-On-Delivery");
                    customerOrdersForGroupSellers.setBuyerLocation(buyerAddress);
                    customerOrdersForGroupSellers.setSellerLocation("");
                    customerOrdersForGroupSellers.setSellerType("group");
                    deleteCart(cart.getId());
                }
            }
        }
        Call<CustomerOrdersForGroupSellers> createGroupOrder = userAPI.createGroupOrder(id,
                sellerId,customerOrdersForGroupSellers);
        createGroupOrder.enqueue(new Callback<CustomerOrdersForGroupSellers>() {
            @Override
            public void onResponse(Call<CustomerOrdersForGroupSellers> call, Response<CustomerOrdersForGroupSellers> response) {
                if(response.isSuccessful() && response!= null){

                    Toast.makeText(getActivity(), "Order Added to the Group", Toast.LENGTH_SHORT).show();
                    Log.e("Add Order to Group", "Success" + response.message());
                }
            }

            @Override
            public void onFailure(Call<CustomerOrdersForGroupSellers> call, Throwable t) {
                Log.e("Add Order to Group", "Error" + t.getMessage());
            }
        });
    }

    private void deleteCart(int id) {
        Call<Void> deleteCall = userAPI.deleteCart(id);
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(), "Processed!", Toast.LENGTH_SHORT).show();
                fetchCartData();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Delete Call Error: ", t.getMessage());
            }
        });
    }

    private GroupSellers getGroupSeller(int sellerId) {
        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(sellerId);
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                getGroupSeller = response.body();
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                getGroupSeller = null;
            }
        });
        return getGroupSeller;
    }

    private Customer getCustomer(int id) {
        Call<Customer> customerCall = userAPI.getCustomer(id);
        customerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                getCustomer = response.body();
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                getCustomer = null;
            }
        });
        return getCustomer;
    }
    private String getCollection(String role) {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String collection;

        switch (role) {
            case "Group Business User (Association)":
                collection = "Farming Association";
                break;
            case "Group Business User (Cooperative)":
                collection = "Farming Cooperatives";
                break;
            case "Individual Business User":
                collection = "Individual Sellers";
                break;
            case "Customer User":
                collection = "Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }

        editor.putString("user_collection", collection).apply();
        return collection;
    }

    @Override
    public void onUpdateClicked(CartDTO cartItem) {
        UpdateCartItemBottomSheet bottomSheet = new UpdateCartItemBottomSheet(cartItem, id);
        bottomSheet.show(getChildFragmentManager(), "UpdateCartItemBottomSheet");
    }
}
