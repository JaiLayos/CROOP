package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.repository.GroupSellersItemRepository;
import com.jai.croop.repository.GroupSellersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSellersItemService implements IGroupSellersItemInventoryService {
    @Autowired
    private GroupSellersItemRepository groupSellersItemRepository;
    @Autowired
    private GroupSellersRepository groupSellersRepository;

    @Override
    public GroupSellersItemInventory addItems(GroupSellersItemInventory groupSellersItemInventory, GroupSellers groupSellers) {
        if (groupSellersItemInventory.getGroupSellers() == null) {
            throw new IllegalArgumentException("GroupSeller cannot be null");
        }
        GroupSellers existingGroupSeller = groupSellersRepository.findById(groupSellersItemInventory.getGroupSellers().getId())
                .orElseThrow(() -> new RuntimeException("GroupSeller does not exist!"));
        groupSellersItemInventory.setGroupSellers(existingGroupSeller);
        return groupSellersItemRepository.save(groupSellersItemInventory);
    }

    @Override
    public GroupSellersItemInventory getItem(int id) {
        return groupSellersItemRepository.findById(id).orElseThrow(()-> new RuntimeException("No items found!"));
    }

    @Override
    public List<GroupSellersItemInventory> findByItemNameAndGroupSellers_Id(String itemName, int sellerId) {
        return groupSellersItemRepository.findByItemNameAndGroupSeller_Id(itemName, sellerId);
    }

    @Override
    public List<GroupSellersItemInventory> findByItemName(String itemName) {
        List<GroupSellersItemInventory> items = groupSellersItemRepository.findByItemName(itemName);
        return items;
    }

    @Override
    public List<GroupSellersItemInventory> findByFirebaseID(String firebaseID) {
        GroupSellers groupSellers = groupSellersRepository.findByFirebaseID(firebaseID);
        int id = groupSellers.getId();
        return groupSellersItemRepository.findByGroupSellerId(id);
    }

    @Override
    public List<GroupSellersItemInventory> getAllItems() {
        return groupSellersItemRepository.findAll();
    }


    @Transactional
    @Override
    public GroupSellersItemInventory updateItems(int id, GroupSellersItemInventory newGroupSellersItemInventory) {
        GroupSellersItemInventory groupSellersItemInventory = getItem(id);
        groupSellersItemInventory.setItemName(newGroupSellersItemInventory.getItemName());
        groupSellersItemInventory.setItemStart(newGroupSellersItemInventory.getItemStart());
        groupSellersItemInventory.setItemUsed(newGroupSellersItemInventory.getItemUsed());
        return groupSellersItemRepository.save(groupSellersItemInventory);
    }

    @Transactional
    @Override
    public void deleteItems(int id) {
        groupSellersItemRepository.deleteById(id);
    }
}
