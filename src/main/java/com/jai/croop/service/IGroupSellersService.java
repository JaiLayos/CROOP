package com.jai.croop.service;

import com.jai.croop.model.GroupSellers;

import java.util.List;
import java.util.Optional;

public interface IGroupSellersService {
    GroupSellers addGroupSellers(GroupSellers groupSellers);
    List<GroupSellers> getAllGroupSellers();
    GroupSellers getGroupSellers(int id);
    GroupSellers updateGroupSellers(int id, GroupSellers updatedGroupSellers);
    void deleteGroupSellers(int id);
}
