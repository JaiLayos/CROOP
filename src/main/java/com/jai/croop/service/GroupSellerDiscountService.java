package com.jai.croop.service;

import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.repository.GroupSellersDiscountRepository;
import com.jai.croop.repository.GroupSellersProductsRepository;
import com.jai.croop.repository.GroupSellersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSellerDiscountService implements IGroupSellersDiscountService {

    @Autowired
    private GroupSellersRepository groupSellersRepository;
    @Autowired
    private GroupSellersDiscountRepository groupSellersDiscountRepository;
    @Autowired
    private GroupSellersProductsRepository groupSellersProductsRepository;
    @Override
    public GroupSellerDiscount addDiscounts(GroupSellerDiscount groupSellerDiscount,
                                            GroupSellers groupSellers,
                                            GroupSellersProductsInventory groupSellersProductsInventory) {
        if (groupSellerDiscount.getGroupSellers() == null) {
            throw new IllegalArgumentException("GroupSeller cannot be null");
        }
        groupSellers = groupSellersRepository.findById(groupSellerDiscount.getGroupSellers().getId()).orElseThrow(()->
                new RuntimeException("Group Seller Doesn't Exist"));
        if(groupSellerDiscount.getGroupSellersProductsInventory() == null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        groupSellersProductsInventory = groupSellersProductsRepository.findById(groupSellerDiscount.getGroupSellersProductsInventory().getId()).
                orElseThrow(()-> new RuntimeException("Product Doesn't Exist"));
        groupSellerDiscount.setGroupSellers(groupSellers);
        groupSellerDiscount.setGroupSellersProductsInventory(groupSellersProductsInventory);
        groupSellerDiscount.setProductName(groupSellersProductsInventory.getItemName());
        return groupSellersDiscountRepository.save(groupSellerDiscount);
    }

    @Override
    public GroupSellerDiscount getDiscounts(int id) {
        return groupSellersDiscountRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Discount does not exist")
        );
    }

    @Override
    public List<GroupSellerDiscount> findByItemName(String itemName) {
        return groupSellersDiscountRepository.findByItemName(itemName);
    }

    @Override
    public List<GroupSellerDiscount> getAllDiscounts() {
        return groupSellersDiscountRepository.findAll();
    }

    @Override
    public GroupSellerDiscount updateDiscounts(int id, GroupSellerDiscount newGroupSellerDiscount) {
        GroupSellerDiscount groupSellerDiscount = getDiscounts(id);
        groupSellerDiscount.setGroupSellers(newGroupSellerDiscount.getGroupSellers());
        groupSellerDiscount.setGroupSellersProductsInventory(newGroupSellerDiscount.getGroupSellersProductsInventory());
        groupSellerDiscount.setOriginalPrice(newGroupSellerDiscount.getOriginalPrice());
        groupSellerDiscount.setDiscountPercent(newGroupSellerDiscount.getDiscountPercent());
        groupSellerDiscount.setSalePrice(newGroupSellerDiscount.getSalePrice());
        return groupSellersDiscountRepository.save(groupSellerDiscount);
    }

    @Override
    public void deleteDiscounts(int id) {
        groupSellersDiscountRepository.deleteById(id);
    }
}
