package com.jai.croop.service;

import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.repository.GroupSellersDiscountRepository;
import com.jai.croop.repository.GroupSellersProductsRepository;
import com.jai.croop.repository.GroupSellersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupSellersProductService implements IGroupSellersProductInventoryService{
    @Autowired
    private  GroupSellersProductsRepository groupSellersProductsRepository;
    @Autowired
    private GroupSellersRepository groupSellersRepository;
    @Autowired
    private GroupSellersDiscountRepository groupSellersDiscountRepository;
    @Autowired
    private GroupSellerDiscountService groupSellersDiscountService;

    @Override
    public GroupSellersProductsInventory addItems(GroupSellersProductsInventory groupSellersProductsInventory, GroupSellers groupSellers) {
        // Validate that GroupSellers is not null
        if (groupSellersProductsInventory.getGroupSellers() == null) {
            throw new IllegalArgumentException("GroupSeller cannot be null in products");
        }

        groupSellers = groupSellersRepository.findById(groupSellersProductsInventory.getGroupSellers().getId())
                .orElseThrow(() -> new RuntimeException("GroupSeller does not exist!"));

        groupSellersProductsInventory.setGroupSellers(groupSellers);
        int remaining = groupSellersProductsInventory.getItemStart() - groupSellersProductsInventory.getItemUsed();
        groupSellersProductsInventory.setItemRemaining(remaining);
        groupSellersProductsInventory = groupSellersProductsRepository.save(groupSellersProductsInventory);

        GroupSellerDiscount groupSellerDiscount = new GroupSellerDiscount();
        groupSellerDiscount.setOriginalPrice(groupSellersProductsInventory.getPrice());
        groupSellerDiscount.setDiscountPercent(0.0);
        groupSellerDiscount.setSalePrice(groupSellersProductsInventory.getPrice());
        groupSellerDiscount.setGroupSellers(groupSellers);
        groupSellerDiscount.setGroupSellersProductsInventory(groupSellersProductsInventory); // Associate the persisted inventory

        groupSellersDiscountService.addDiscounts(groupSellerDiscount, groupSellers, groupSellersProductsInventory);

        return groupSellersProductsInventory;
    }

    @Override
    public GroupSellersProductsInventory getItem(int id) {
        return groupSellersProductsRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product does not exist!"));
    }

    @Override
    public List<GroupSellersProductsInventory> findByItemName(String itemName) {
        List<GroupSellersProductsInventory> products = groupSellersProductsRepository.findByItemName(itemName);
        return products;
    }

    @Override
    public List<GroupSellersProductsInventory> findByItemNameAndGroupSellers_Id(String itemName, int sellerId) {
        return groupSellersProductsRepository.findByItemNameAndGroupSellers_Id(itemName, sellerId);
    }

    @Override
    public List<GroupSellersProductsInventory> findByFirebaseID(String firebaseID) {
        GroupSellers groupSellers = groupSellersRepository.findByFirebaseID(firebaseID);
        int id = groupSellers.getId();
        return groupSellersProductsRepository.findByGroupSellersId(id);
    }

    @Override
    public List<GroupSellersProductsInventory> getAllItems() {
        return groupSellersProductsRepository.findAll();
    }

    @Override
    public List<GroupSellersProductsInventory> getTop3ProductsByItemStart() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        return groupSellersProductsRepository.findTop3ByItemStart(pageRequest);
    }

    @Override
    public List<GroupSellersProductsInventory> findTop3ByLowestItemRemaining() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        return groupSellersProductsRepository.findTop3ByLowestItemRemaining(pageRequest);
    }

    @Transactional
    @Override
    public GroupSellersProductsInventory updateItems(int id, GroupSellersProductsInventory newGroupSellersProductsInventory) {
        GroupSellersProductsInventory groupSellersProductsInventory = getItem(id);
        groupSellersProductsInventory.setPrice(newGroupSellersProductsInventory.getPrice());
        groupSellersProductsInventory.setItemName(newGroupSellersProductsInventory.getItemName());
        groupSellersProductsInventory.setItemStart(newGroupSellersProductsInventory.getItemStart());
        groupSellersProductsInventory.setItemUsed(newGroupSellersProductsInventory.getItemUsed());
        return groupSellersProductsRepository.save(groupSellersProductsInventory);
    }

    @Transactional
    @Override
    public void deleteItems(int id) {
        GroupSellersProductsInventory groupSellersProductsInventory = groupSellersProductsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product does not exist!")
        );
        groupSellersDiscountRepository.deleteById(groupSellersProductsInventory.getGroupSellerDiscounts().getId());
        groupSellersProductsRepository.deleteById(id);
    }
}
