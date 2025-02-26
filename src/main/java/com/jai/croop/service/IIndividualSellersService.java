package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.IndividualSellers;

import java.util.List;

public interface IIndividualSellersService {
    IndividualSellers addIndividualSellers(IndividualSellers individualSellers);
    List<IndividualSellers> getAllIndividualSellers();
    int getIndividualSellerIdByFirebaseID(String firebaseID);
    IndividualSellers getIndividualSellers(int id);
    int findIDByFirebaseID(String firebaseID);
    IndividualSellers updateIndividualSellers(int id, IndividualSellers updated);
    IndividualSellers updateIndividualSellersByFirebaseID(String firebaseID, IndividualSellers updated);
    void deleteIndividualSellers(int id);
}
