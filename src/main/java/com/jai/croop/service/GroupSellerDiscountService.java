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
            throw new IllegalArgumentException("GroupSeller cannot be null in discount");
        }
        if(groupSellerDiscount.getGroupSellersProductsInventory() == null){
            throw new IllegalArgumentException("Product cannot be null in discount");
        }
        groupSellerDiscount.setGroupSellers(groupSellers);
        groupSellerDiscount.setGroupSellersProductsInventory(groupSellersProductsInventory);
        groupSellerDiscount.setProductName(groupSellersProductsInventory.getItemName());
        groupSellerDiscount.setOriginalPrice(groupSellersProductsInventory.getPrice());
        groupSellerDiscount.setSalePrice((int) (groupSellersProductsInventory.getPrice() -(groupSellersProductsInventory.getPrice() * groupSellerDiscount.getDiscountPercent())));
        groupSellersProductsInventory.setGroupSellerDiscounts(groupSellerDiscount);
        return groupSellersDiscountRepository.save(groupSellerDiscount);
    }

    @Override
    public GroupSellerDiscount getDiscounts(int id) {
        return groupSellersDiscountRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Discount does not exist")
        );
    }

    @Override
    public List<GroupSellerDiscount> getDiscountsByGroupSellerID(int id) {
        return groupSellersDiscountRepository.findByGroupSellersId(id);
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
        GroupSellersProductsInventory groupSellersProductsInventory = groupSellerDiscount.getGroupSellersProductsInventory();
        groupSellersProductsInventory.setGroupSellerDiscounts(groupSellerDiscount);
        groupSellerDiscount.setDiscountPercent(newGroupSellerDiscount.getDiscountPercent());
        groupSellerDiscount.setSalePrice((int) (groupSellerDiscount.getOriginalPrice() - (groupSellerDiscount.getOriginalPrice() * newGroupSellerDiscount.getDiscountPercent())));
        return groupSellersDiscountRepository.save(groupSellerDiscount);
    }

    @Override
    public void deleteDiscounts(int id) {
        groupSellersDiscountRepository.deleteById(id);
    }
}
