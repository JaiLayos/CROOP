package com.jai.croop.service;

import com.jai.croop.model.GroupSellerDiscount;
import com.jai.croop.model.GroupSellers;
import com.jai.croop.model.GroupSellersOrders;
import com.jai.croop.repository.GroupSellersOrdersRepository;
import com.jai.croop.repository.GroupSellersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GroupSellersService implements IGroupSellersService{
    @Autowired
    GroupSellersRepository groupSellersRepository;
    @Autowired
    GroupSellersOrdersRepository groupSellersOrdersRepository;
    @Override
    public GroupSellers addGroupSellers(GroupSellers groupSellers) {
        return groupSellersRepository.save(groupSellers);
    }

    @Override
    public List<GroupSellers> getAllGroupSellers() {
        return groupSellersRepository.findAll();
    }

    @Override
    public int getGroupSellerIdByFirebaseID(String firebaseID) {
        GroupSellers groupSellers = groupSellersRepository.findByFirebaseID(firebaseID);
        int id = groupSellers.getId();
        return id; // Return the first order in the list
    }


    @Override
    public GroupSellers getGroupSellers(int id) {
        return groupSellersRepository.findById(id).orElseThrow(() -> new RuntimeException("Group Seller Not Found!"));
    }

    @Override
    public int findIDByFirebaseID(String firebaseID) {
        GroupSellers groupSellers = groupSellersRepository.findByFirebaseID(firebaseID);
        return groupSellers.getId();
    }

    @Transactional
    @Override
    public GroupSellers updateGroupSellers(int id, GroupSellers updatedGroupSellers) {
        Date date = new Date();
        GroupSellers groupSellers = getGroupSellers(id);
        groupSellers.setName(updatedGroupSellers.getName());
        groupSellers.setAddress(updatedGroupSellers.getAddress());
        groupSellers.setPhoneNumber(updatedGroupSellers.getPhoneNumber());
        groupSellers.setEmail(updatedGroupSellers.getEmail());
        groupSellers.setBio(updatedGroupSellers.getBio());
        groupSellers.setPosition(updatedGroupSellers.getPosition());
        groupSellers.setGroupName(updatedGroupSellers.getGroupName());
        groupSellers.setRoles(updatedGroupSellers.getRoles());
        groupSellers.setUpdatedAt(date);
        return groupSellersRepository.save(groupSellers);
    }

    @Transactional
    @Override
    public GroupSellers updateGroupSellersByFirebaseID(String firebaseID, GroupSellers updatedGroupSellers) {
        Date date = new Date();
        GroupSellers groupSellers = groupSellersRepository.findByFirebaseID(firebaseID);
        groupSellers.setName(updatedGroupSellers.getName());
        groupSellers.setAddress(updatedGroupSellers.getAddress());
        groupSellers.setPhoneNumber(updatedGroupSellers.getPhoneNumber());
        groupSellers.setEmail(updatedGroupSellers.getEmail());
        groupSellers.setBio(updatedGroupSellers.getBio());
        groupSellers.setPosition(updatedGroupSellers.getPosition());
        groupSellers.setGroupName(updatedGroupSellers.getGroupName());
        groupSellers.setRoles(updatedGroupSellers.getRoles());
        groupSellers.setUpdatedAt(date);
        return groupSellersRepository.save(groupSellers);
    }

    @Override
    public List<GroupSellers> findTop3ByTotalItemStart() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        return groupSellersRepository.findTop3ByTotalItemStart(pageRequest);
    }

    @Override
    public List<GroupSellers> findByGroupName(String groupName) {
        return groupSellersRepository.findByGroupName(groupName);
    }

    @Transactional
    @Override
    public void deleteGroupSellers(int id) {
        groupSellersRepository.deleteById(id);
    }
}
