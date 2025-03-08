package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGroupSellersService {
    GroupSellers addGroupSellers(GroupSellers groupSellers);
    List<GroupSellers> getAllGroupSellers();
    int getGroupSellerIdByFirebaseID(String firebaseID);
    GroupSellers getGroupSellers(int id);
    int findIDByFirebaseID(String firebaseID);
    GroupSellers updateGroupSellers(int id, GroupSellers updatedGroupSellers);
    GroupSellers updateGroupSellersByFirebaseID(String firebaseID, GroupSellers updatedGroupSellers);
    List<GroupSellers> findTop3ByTotalItemStart();
    List<GroupSellers> findByGroupName(String groupName);
    void deleteGroupSellers(int id);
}
