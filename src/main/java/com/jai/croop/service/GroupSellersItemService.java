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
    private GroupSellersItemRepository GroupSellersItemRepository;
    @Autowired
    private GroupSellersRepository groupSellersRepository;

    @Override
    public GroupSellersItemInventory addItems(GroupSellersItemInventory groupSellersItemInventory, GroupSellers groupSellers) {
        if (groupSellersItemInventory.getGroupSellers() == null) {
            throw new IllegalArgumentException("GroupSeller cannot be null");
        }
        // Retrieve the GroupSellers entity
        GroupSellers existingGroupSeller = groupSellersRepository.findById(groupSellersItemInventory.getGroupSellers().getId())
                .orElseThrow(() -> new RuntimeException("GroupSeller does not exist!"));
        groupSellersItemInventory.setGroupSellers(existingGroupSeller);
        int remaining = groupSellersItemInventory.getItemStart() - groupSellersItemInventory.getItemRemaining();
        groupSellersItemInventory.setItemRemaining(remaining);
        return GroupSellersItemRepository.save(groupSellersItemInventory);
    }

    @Override
    public GroupSellersItemInventory getItem(int id) {
        return GroupSellersItemRepository.findById(id).orElseThrow(()-> new RuntimeException("No items found!"));
    }

    @Override
    public GroupSellersItemInventory findByItemName(String itemName) {
        return GroupSellersItemRepository.findByItemName(itemName);
    }

    @Override
    public List<GroupSellersItemInventory> getAllItems() {
        return GroupSellersItemRepository.findAll();
    }


    @Transactional
    @Override
    public GroupSellersItemInventory updateItems(int id, GroupSellersItemInventory newGroupSellersItemInventory) {
        GroupSellersItemInventory groupSellersItemInventory = getItem(id);
        groupSellersItemInventory.setItemName(newGroupSellersItemInventory.getItemName());
        groupSellersItemInventory.setItemStart(newGroupSellersItemInventory.getItemStart());
        groupSellersItemInventory.setItemUsed(newGroupSellersItemInventory.getItemUsed());
        return GroupSellersItemRepository.save(groupSellersItemInventory);
    }

    @Transactional
    @Override
    public void deleteItems(int id) {
        GroupSellersItemRepository.deleteById(id);
    }
}
