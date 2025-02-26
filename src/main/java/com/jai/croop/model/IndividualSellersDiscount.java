package com.jai.croop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "individual_sellers_discount")
public class IndividualSellersDiscount extends BaseDiscount{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "individual_seller_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("individual-discounts")
    private IndividualSellers individualSellers;

    @ManyToOne
    @JoinColumn(name = "individual_products_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("individual-products-discounted")
    private IndividualSellersProductsInventory individualSellersProductsInventory;

    private String productName;
    public IndividualSellersDiscount(){

    }

    public IndividualSellersDiscount(int id, IndividualSellers individualSellers, IndividualSellersProductsInventory individualSellersProductsInventory, int originalPrice, int discountPercent, int salePrice){
        super(originalPrice, discountPercent, salePrice);
        this.id = id;
        this.individualSellers = individualSellers;
        this.individualSellersProductsInventory = individualSellersProductsInventory;
    }

    public int getId() {
        return id;
    }

    public void setIndividualSellers(IndividualSellers individualSellers) {
        this.individualSellers = individualSellers;
    }
    public IndividualSellers getIndividualSellers() {
        return individualSellers;
    }

    public IndividualSellersProductsInventory getIndividualSellersProductsInventory() {
        return individualSellersProductsInventory;
    }
    public void setIndividualSellersProductsInventory(IndividualSellersProductsInventory individualSellersProductsInventory) {
        this.individualSellersProductsInventory = individualSellersProductsInventory;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
