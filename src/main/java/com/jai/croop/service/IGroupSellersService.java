package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;

import java.util.List;

public interface IGroupSellersService {
    GroupSellers addGroupSellers(GroupSellers groupSellers);
    List<GroupSellers> getAllGroupSellers();
    List<GroupSellersOrders> getGroupSellerIdByFirebaseID(String firebaseID);
    GroupSellers getGroupSellers(int id);
    int findIDByFirebaseID(String firebaseID);
    GroupSellers updateGroupSellers(int id, GroupSellers updatedGroupSellers);
    GroupSellers updateGroupSellersByFirebaseID(String firebaseID, GroupSellers updatedGroupSellers);
    void deleteGroupSellers(int id);
}
