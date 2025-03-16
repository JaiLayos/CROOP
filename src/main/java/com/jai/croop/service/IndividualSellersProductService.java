package com.jai.croop.service;

import com.jai.croop.model.*;
import com.jai.croop.repository.IndividualSellersDiscountRepository;
import com.jai.croop.repository.IndividualSellersProductsRepository;
import com.jai.croop.repository.IndividualSellersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualSellersProductService implements IIndividualSellersProductService{
    @Autowired
    private IndividualSellersProductsRepository individualSellersProductsRepository;
    @Autowired
    private IndividualSellersRepository individualSellersRepository;
    @Autowired
    private IndividualSellersDiscountService individualSellersDiscountService;
    @Autowired
    private IndividualSellersDiscountRepository individualSellersDiscountRepository;

    @Override
    public IndividualSellersProductsInventory addItems(IndividualSellersProductsInventory individualSellersProductsInventory, IndividualSellers individualSellers) {
        if (individualSellersProductsInventory.getIndividualSellers() == null) {
            throw new IllegalArgumentException("Individual Seller cannot be null in products");
        }

        individualSellers = individualSellersRepository.findById(individualSellersProductsInventory.getIndividualSellers().getId())
                .orElseThrow(() -> new RuntimeException("GroupSeller does not exist!"));

        individualSellersProductsInventory.setIndividualSellers(individualSellers);
        int remaining = individualSellersProductsInventory.getItemStart() - individualSellersProductsInventory.getItemUsed();
        individualSellersProductsInventory.setItemRemaining(remaining);
        individualSellersProductsInventory = individualSellersProductsRepository.save(individualSellersProductsInventory);

        IndividualSellersDiscount individualSellersDiscount = new IndividualSellersDiscount();
        individualSellersDiscount.setOriginalPrice(individualSellersProductsInventory.getPrice());
        individualSellersDiscount.setDiscountPercent(0.0);
        individualSellersDiscount.setSalePrice(individualSellersProductsInventory.getPrice());
        individualSellersDiscount.setIndividualSellers(individualSellers);
        individualSellersDiscount.setIndividualSellersProductsInventory(individualSellersProductsInventory); // Associate the persisted inventory

        individualSellersDiscountService.addDiscounts(individualSellersDiscount, individualSellers, individualSellersProductsInventory);

        return individualSellersProductsInventory;
    }

    @Override
    public IndividualSellersProductsInventory getItem(int id) {
        return individualSellersProductsRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product does not exist!"));
    }

    @Override
    public List<IndividualSellersProductsInventory> findByItemName(String itemName) {
        List<IndividualSellersProductsInventory> products = individualSellersProductsRepository.findByItemName(itemName);
        return products;
    }

    @Override
    public List<IndividualSellersProductsInventory> findByFirebaseID(String firebaseID) {
        IndividualSellers individualSellers = individualSellersRepository.findByFirebaseID(firebaseID);
        int id = individualSellers.getId();
        return individualSellersProductsRepository.findByIndividualSellersId(id);
    }

    @Override
    public List<IndividualSellersProductsInventory> getAllItems() {
        return individualSellersProductsRepository.findAll();
    }

    @Override
    public List<IndividualSellersProductsInventory> findTop3ByItemStart() {
        PageRequest pageRequest = PageRequest.of(0, 3); // Limit to 3 items
        return individualSellersProductsRepository.findTop3ByItemStart(pageRequest);
    }

    @Override
    public List<IndividualSellersProductsInventory> findTop3ByLowestItemRemaining() {
        PageRequest pageRequest = PageRequest.of(0, 3); // Limit to 3 items
        return individualSellersProductsRepository.findTop3ByLowestItemRemaining(pageRequest);
    }

    @Override
    public boolean findIfSCIsSet(int id) {
        IndividualSellers individualSellers = individualSellersRepository.findById(id).orElseThrow(()-> new RuntimeException("Individual Seller does not exist!"));
        boolean exist = false;
        if(individualSellers.getProduct_inventory_SC() != 0) {
            exist = true;
        }
        return exist;
    }

    @Override
    public boolean findIfMCIsSet(int id) {
        IndividualSellers individualSellers = individualSellersRepository.findById(id).orElseThrow(()-> new RuntimeException("Individual Seller does not exist!"));
        boolean exist = false;
        if(individualSellers.getProduct_inventory_MC() != 0) {
            exist = true;
        }
        return exist;
    }

    @Transactional
    @Override
    public IndividualSellersProductsInventory updateItems(int id, IndividualSellersProductsInventory newIndividualSellersProductsInventory) {
        IndividualSellersProductsInventory individualSellersProductsInventory = getItem(id);
        individualSellersProductsInventory.setPrice(newIndividualSellersProductsInventory.getPrice());
        individualSellersProductsInventory.setItemName(newIndividualSellersProductsInventory.getItemName());
        individualSellersProductsInventory.setItemStart(newIndividualSellersProductsInventory.getItemStart());
        individualSellersProductsInventory.setItemUsed(newIndividualSellersProductsInventory.getItemUsed());
        return individualSellersProductsRepository.save(individualSellersProductsInventory);
    }

    @Transactional
    @Override
    public void deleteItems(int id) {
        IndividualSellersProductsInventory individualSellersProductsInventory = individualSellersProductsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product does not exist!")
        );
        individualSellersDiscountRepository.deleteById(individualSellersProductsInventory.getIndividualSellersDiscount().getId());
        individualSellersProductsRepository.deleteById(id);
    }
}
