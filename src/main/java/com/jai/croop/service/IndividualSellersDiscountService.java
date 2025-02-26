package com.jai.croop.service;

import com.jai.croop.model.*;
import com.jai.croop.repository.IndividualSellersDiscountRepository;
import com.jai.croop.repository.IndividualSellersProductsRepository;
import com.jai.croop.repository.IndividualSellersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualSellersDiscountService implements IIndividualSellersDiscountService{
    @Autowired
    private IndividualSellersRepository individualSellersRepository;
    @Autowired
    private IndividualSellersDiscountRepository individualSellersDiscountRepository;
    @Autowired
    private IndividualSellersProductsRepository individualSellersProductsRepository;

    @Override
    public IndividualSellersDiscount addDiscounts(IndividualSellersDiscount individualSellersDiscount, IndividualSellers individualSellers, IndividualSellersProductsInventory individualSellersProductsInventory) {
        if (individualSellersDiscount.getIndividualSellers() == null) {
            throw new IllegalArgumentException("GroupSeller cannot be null in discount");
        }
        if(individualSellersDiscount.getIndividualSellersProductsInventory() == null){
            throw new IllegalArgumentException("Product cannot be null in discount");
        }
        individualSellersDiscount.setIndividualSellers(individualSellers);
        individualSellersDiscount.setIndividualSellersProductsInventory(individualSellersProductsInventory);
        individualSellersDiscount.setProductName(individualSellersProductsInventory.getItemName());
        individualSellersDiscount.setOriginalPrice(individualSellersProductsInventory.getPrice());
        individualSellersDiscount.setSalePrice((int) (individualSellersProductsInventory.getPrice() -(individualSellersProductsInventory.getPrice() * individualSellersDiscount.getDiscountPercent())));
        individualSellersProductsInventory.setIndividualSellersDiscount(individualSellersDiscount);
        return individualSellersDiscountRepository.save(individualSellersDiscount);
    }

    @Override
    public IndividualSellersDiscount getDiscounts(int id) {
        return individualSellersDiscountRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Discount does not exist")
        );
    }

    @Override
    public List<IndividualSellersDiscount> getDiscountsByIndividualSellerID(int id) {
        return individualSellersDiscountRepository.findByIndividualSellersId(id);
    }

    @Override
    public List<IndividualSellersDiscount> findByItemName(String itemName) {
        return individualSellersDiscountRepository.findByItemName(itemName);
    }

    @Override
    public List<IndividualSellersDiscount> getAllDiscounts() {
        return individualSellersDiscountRepository.findAll();
    }

    @Override
    public IndividualSellersDiscount updateDiscounts(int id, IndividualSellersDiscount updated) {
        IndividualSellersDiscount individualSellersDiscount = getDiscounts(id);
        IndividualSellersProductsInventory individualSellersProductsInventory = individualSellersDiscount.getIndividualSellersProductsInventory();
        individualSellersProductsInventory.setIndividualSellersDiscount(individualSellersDiscount);
        individualSellersDiscount.setDiscountPercent(updated.getDiscountPercent());
        individualSellersDiscount.setSalePrice((int) (individualSellersDiscount.getOriginalPrice() - (individualSellersDiscount.getOriginalPrice() * updated.getDiscountPercent())));
        return individualSellersDiscountRepository.save(individualSellersDiscount);
    }

    @Override
    public void deleteDiscounts(int id) {
        individualSellersDiscountRepository.deleteById(id);
    }
}
