package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.IndividualSellers;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IIndividualSellersService {
    IndividualSellers addIndividualSellers(IndividualSellers individualSellers);
    List<IndividualSellers> getAllIndividualSellers();
    int getIndividualSellerIdByFirebaseID(String firebaseID);
    IndividualSellers getIndividualSellers(int id);
    List<IndividualSellers> findByName(String name);
    int findIDByFirebaseID(String firebaseID);
    IndividualSellers updateIndividualSellers(int id, IndividualSellers updated);
    IndividualSellers updateIndividualSellersByFirebaseID(String firebaseID, IndividualSellers updated);
    List<IndividualSellers> findTop3ByTotalItemStart();
    void deleteIndividualSellers(int id);
}
