package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.repository.GroupSellersProductsRepository;
import com.jai.croop.repository.GroupSellersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSellersProductService implements IGroupSellersProductInventoryService{
    @Autowired
    private  GroupSellersProductsRepository groupSellersProductsRepository;
    @Autowired
    private GroupSellersRepository groupSellersRepository;
    @Override
    public GroupSellersProductsInventory addItems(GroupSellersProductsInventory groupSellersProductsInventory, GroupSellers groupSellers) {
        if (groupSellersProductsInventory.getGroupSellers() == null) {
            throw new IllegalArgumentException("GroupSeller cannot be null");
        }
        groupSellers = groupSellersRepository.findById(groupSellersProductsInventory.getGroupSellers().getId())
                .orElseThrow(() -> new RuntimeException("GroupSeller does not exist!"));
        groupSellersProductsInventory.setGroupSellers(groupSellers);
        return groupSellersProductsRepository.save(groupSellersProductsInventory);
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
    public List<GroupSellersProductsInventory> getAllItems() {
        return groupSellersProductsRepository.findAll();
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
        groupSellersProductsRepository.deleteById(id);
    }
}
