package com.jai.croop.service;

import com.jai.croop.model.*;

import java.util.List;

public interface IIndividualSellersOrdersService {
    IndividualSellersOrders addIndividualSellerOrders(IndividualSellersOrders individualSellersOrders, Customer customer, IndividualSellers individualSellers);
    IndividualSellersOrders getIndividualSellerOrders(int id);
    List<IndividualSellersOrders> getAllIndividualSellerOrders();
    List<SellerOrdersDTO>  findByIndividualSellerID(int id);
    IndividualSellersOrders updateIndividualSellerOrders(int id, IndividualSellersOrders individualSellersOrders);
    void deleteIndividualSellerOrders(int id);
}
