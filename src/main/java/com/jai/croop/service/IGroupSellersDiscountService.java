package com.jai.croop.service;

import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersProductsInventory;

import java.util.List;

public interface IGroupSellersDiscountService {
    GroupSellerDiscount addDiscounts(GroupSellerDiscount groupSellerDiscount, GroupSellers groupSellers, GroupSellersProductsInventory groupSellersProductsInventory);
    GroupSellerDiscount getDiscounts(int id);
    List<GroupSellerDiscount> getDiscountsByGroupSellerID(int id);
    List<GroupSellerDiscount> findByItemName(String itemName);
    List<GroupSellerDiscount> getAllDiscounts();
    GroupSellerDiscount updateDiscounts(int id, GroupSellerDiscount groupSellerDiscount);
    void deleteDiscounts(int id);
}
