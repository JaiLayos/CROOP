package com.jai.croop.service;

import com.jai.croop.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IIndividualSellersDiscountService {
    IndividualSellersDiscount addDiscounts(IndividualSellersDiscount individualSellersDiscount, IndividualSellers individualSellers, IndividualSellersProductsInventory individualSellersProductsInventory);
    IndividualSellersDiscount getDiscounts(int id);
    List<IndividualSellersDiscount> getDiscountsByIndividualSellerID(int id);

    List<IndividualSellersDiscount> findByItemName(String itemName);
    List<IndividualSellersDiscount> getAllDiscounts();
    IndividualSellersDiscount updateDiscounts(int id, IndividualSellersDiscount individualSellersDiscount);
    void deleteDiscounts(int id);
}
