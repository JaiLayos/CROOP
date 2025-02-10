package com.jai.croop.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "group_business_users")
public class GroupSellers extends BaseUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String groupName;
    private String position;

    @OneToMany(mappedBy = "groupSeller", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GroupSellersOrders> groupSellerOrders;

    public GroupSellers() {}

    public GroupSellers(String firebaseID, String name, Map<String, String> address, String phoneNumber,
                        String email, String bio, Date createdAt, Date updatedAt, String roles,
                        String groupName, String position) {
        super(firebaseID, name, address, phoneNumber, email, bio, createdAt, updatedAt, roles);
        this.groupName = groupName;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<GroupSellersOrders> getGroupSellerOrders() {
        return groupSellerOrders;
    }

    public void setGroupSellerOrders(List<GroupSellersOrders> groupSellerOrders) {
        this.groupSellerOrders = groupSellerOrders;
    }
}
