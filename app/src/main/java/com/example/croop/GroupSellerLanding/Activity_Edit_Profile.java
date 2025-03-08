package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Edit_Profile extends AppCompatActivity {
    private EditText userName, userBio, userPosition,
            userStreet, userSubdivision, userCity,
            userRegion, userPostal;
    private ImageView displayPicture;
    private static final int RC_IMAGE_PICKER = 100;
    private Uri imageUri;
    private StorageReference storageRef;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private RetrofitService RetrofitClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group_seller);

        initializeComponents();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri == null) {
                Toast.makeText(this, "Failed to retrieve image URI!", Toast.LENGTH_SHORT).show();
            } else {
                statusPicture(imageUri);
            }
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeComponents() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String collection = prefs.getString("user_collection", null);
        FloatingActionButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, Sign_In_Success_Group_Seller.class);
            startActivity(intent);
        });

        userName = findViewById(R.id.userNameText);
        userBio = findViewById(R.id.userBioText);
        userPosition = findViewById(R.id.userPositionText);
        userStreet = findViewById(R.id.userStreetText);
        userSubdivision = findViewById(R.id.userSubdivisionText);
        userCity = findViewById(R.id.userCityText);
        userRegion = findViewById(R.id.userRegionText);
        userPostal = findViewById(R.id.userPostalText);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(collection).document(user.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    String nameDB = documentSnapshot.getString("Name");
                    String positionDB = documentSnapshot.getString("Position");
                    String bioDB = documentSnapshot.getString("Bio");
                    String cityDB = documentSnapshot.getString("Address.City");
                    String houseDB = documentSnapshot.getString("Address.House_Street_Name");
                    String postDB = documentSnapshot.getString("Address.Postal_Code");
                    String regionDB = documentSnapshot.getString("Address.State_Province_Region");
                    String subdivisionDB = documentSnapshot.getString("Address.Subdivision_Baranggay");

                    userName.setText(nameDB);
                    userBio.setText(bioDB);
                    userPosition.setText(positionDB);
                    userStreet.setText(houseDB);
                    userSubdivision.setText(subdivisionDB);
                    userCity.setText(cityDB);
                    userRegion.setText(regionDB);
                    userPostal.setText(postDB);

                } else {
                    Log.d("FirestoreData", "No such document");
                }
            }
        );

        Button edit = findViewById(R.id.profileEditButton);
        edit.setOnClickListener(v -> {
            String name = userName.getText().toString().trim();
            String bio = userBio.getText().toString().trim();
            String position = userPosition.getText().toString().trim();
            String street = userStreet.getText().toString().trim();
            String subdivision = userSubdivision.getText().toString().trim();
            String city = userCity.getText().toString().trim();
            String region = userRegion.getText().toString().trim();
            String postal = userPostal.getText().toString().trim();
            GroupSellers groupSellers = new GroupSellers();
            groupSellers.setName(name);
            groupSellers.setBio(bio);
            groupSellers.setPersonPosition(position);
            Map<String, String> addressMap = new HashMap<>();
            addressMap.put("City", city);
            addressMap.put("Country", "Philippines");
            addressMap.put("House_Street_Name", street);
            addressMap.put("Postal_Code", postal);
            addressMap.put("State_Province_Region", region);
            addressMap.put("Subdivision_Barangay", subdivision);
            groupSellers.setAddress(addressMap);
            docRef.update(
                    "Address.City", city,
                    "Address.House_Street_Name", street,
                    "Address.Postal_Code", postal,
                    "Address.State_Province_Region", region,
                    "Address.Subdivision_Baranggay", subdivision,
                    "Bio", bio,
                    "Name", name,
                    "Position", position,
                    "Updated At", new Date()
            ).addOnSuccessListener( view -> {
                    Toast.makeText(this, "Na-update na ang user!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Sign_In_Success_Group_Seller.class);
                    startActivity(intent);
                    sendToPG(groupSellers);
                }
            ).addOnFailureListener( e-> {
                Toast.makeText(this, "Hindi na-update ang profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            );
        });

        displayPicture = findViewById(R.id.profilePicture);
        Button profilePicture = findViewById(R.id.editProfileButton);
        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, RC_IMAGE_PICKER);
        });
        String userId = user.getUid();
        storageRef = FirebaseStorage.getInstance().getReference()
                .child("Profile Picture")
                .child(userId)
                .child("Display");

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(displayPicture);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            displayPicture.setImageResource(R.drawable.logo);
        });

    }

    private void statusPicture(Uri imageUri) {
        if (user == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Image URI is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        storageRef.getMetadata()
                .addOnSuccessListener(storageMetadata -> {
                    // File exists, proceed with updating
                    System.out.println("File exists. Updating...");
                    storageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                System.out.println("Update successful!");
                                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    System.out.println("Updated Download URL: " + uri.toString());
                                    Toast.makeText(this, "Picture updated successfully!", Toast.LENGTH_SHORT).show();
                                    storageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                        Glide.with(this)
                                                .load(uri.toString())
                                                .placeholder(R.drawable.logo)
                                                .error(R.drawable.sun)
                                                .into(displayPicture);
                                    }).addOnFailureListener(e -> {
                                        Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
                                        displayPicture.setImageResource(R.drawable.logo);
                                    });
                                }).addOnFailureListener(e -> {
                                    System.err.println("Failed to get download URL: " + e.getMessage());
                                    Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> {
                                System.err.println("Failed to update image: " + e.getMessage());
                                Toast.makeText(this, "Failed to update image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        storageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        storageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                            Glide.with(this)
                                                    .load(uri.toString())
                                                    .placeholder(R.drawable.logo)
                                                    .error(R.drawable.sun)
                                                    .into(displayPicture);
                                        }).addOnFailureListener(e1 -> {
                                            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
                                            displayPicture.setImageResource(R.drawable.logo);
                                        });
                                    }).addOnFailureListener(e1 -> {
                                        Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                })
                                .addOnFailureListener(e1 -> {
                                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Handle other errors
                        System.err.println("Error checking file existence: " + e.getMessage());
                        Toast.makeText(this, "Error checking file existence: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void sendToPG(GroupSellers groupSellers) {
        UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
        Call<GroupSellers> call = userAPI.updateGroupSellersByFirebaseID(user.getUid(), groupSellers);
        call.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_Edit_Profile.this, "Na-update na ang user sa PostgreSQL!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activity_Edit_Profile.this, "Hindi na-update ang profile: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Toast.makeText(Activity_Edit_Profile.this, "Nagkaproblema: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
