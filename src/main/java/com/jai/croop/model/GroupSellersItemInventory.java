package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "group_sellers_items")
@JsonIgnoreProperties({"groupSellers"})
public class GroupSellersItemInventory extends BaseInventory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "group_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("group-inventory")
    private GroupSellers groupSeller;

    public GroupSellersItemInventory(){

    }

    public GroupSellersItemInventory(GroupSellers groupSeller, String itemName, int itemStart, int itemUsed,
                                     int itemRemaining){
        super(itemName, itemStart, itemUsed,itemRemaining);
        this.groupSeller = groupSeller;
    }

    public int getId() {
        return id;
    }

    public GroupSellers getGroupSellers() {
        return groupSeller;
    }

    public void setGroupSellers(GroupSellers groupSeller) {
        this.groupSeller = groupSeller;
    }
}
