package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersItemInventory;
import com.jai.croop.model.IndividualSellers;
import com.jai.croop.model.IndividualSellersItemInventory;
import com.jai.croop.repository.IndividualSellersItemRepository;
import com.jai.croop.repository.IndividualSellersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualSellersItemService implements IIndividualSellersItemService{
    @Autowired
    private IndividualSellersItemRepository individualSellersItemRepository;
    @Autowired
    private IndividualSellersRepository individualSellersRepository;

    @Override
    public IndividualSellersItemInventory addItems(IndividualSellersItemInventory individualSellersItemInventory, IndividualSellers individualSellers) {
        if (individualSellersItemInventory.getIndividualSellers() == null) {
            throw new IllegalArgumentException("Individual Seller cannot be null");
        }
        IndividualSellers existing = individualSellersRepository.findById(individualSellersItemInventory.getIndividualSellers().getId())
                .orElseThrow(() -> new RuntimeException("Individual Seller does not exist!"));
        individualSellersItemInventory.setIndividualSellers(existing);
        return individualSellersItemRepository.save(individualSellersItemInventory);
    }

    @Override
    public IndividualSellersItemInventory getItem(int id) {
        return individualSellersItemRepository.findById(id).orElseThrow(()-> new RuntimeException("No items found!"));
    }

    @Override
    public List<IndividualSellersItemInventory> findByItemName(String itemName) {
        List<IndividualSellersItemInventory> items = individualSellersItemRepository.findByItemName(itemName);
        return items;
    }

    @Override
    public List<IndividualSellersItemInventory> findByFirebaseID(String firebaseID) {
        IndividualSellers individualSellers = individualSellersRepository.findByFirebaseID(firebaseID);
        int id = individualSellers.getId();
        return individualSellersItemRepository.findByIndividualSellers_Id(id);
    }

    @Override
    public List<IndividualSellersItemInventory> getAllItems() {
        return individualSellersItemRepository.findAll();
    }

    @Override
    public IndividualSellersItemInventory updateItems(int id, IndividualSellersItemInventory updated) {
        IndividualSellersItemInventory individualSellersItemInventory = getItem(id);
        individualSellersItemInventory.setItemName(updated.getItemName());
        individualSellersItemInventory.setItemStart(updated.getItemStart());
        individualSellersItemInventory.setItemUsed(updated.getItemUsed());
        return individualSellersItemRepository.save(individualSellersItemInventory);
    }

    @Override
    public void deleteItems(int id) {
        individualSellersItemRepository.deleteById(id);
    }
}
