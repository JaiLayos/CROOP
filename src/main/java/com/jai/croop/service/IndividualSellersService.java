package com.jai.croop.service;

import com.jai.croop.model.IndividualSellers;
import com.jai.croop.repository.IndividualSellersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IndividualSellersService implements IIndividualSellersService{
    @Autowired
    IndividualSellersRepository individualSellersRepository;
    @Override
    public IndividualSellers addIndividualSellers(IndividualSellers individualSellers) {
        return individualSellersRepository.save(individualSellers);
    }

    @Override
    public List<IndividualSellers> getAllIndividualSellers() {
        return individualSellersRepository.findAll();
    }

    @Override
    public int getIndividualSellerIdByFirebaseID(String firebaseID) {
        IndividualSellers individualSellers = individualSellersRepository.findByFirebaseID(firebaseID);
        int id = individualSellers.getId();
        return id;
    }

    @Override
    public IndividualSellers getIndividualSellers(int id) {
        return individualSellersRepository.findById(id).orElseThrow(() -> new RuntimeException("Individual Seller Not Found!"));
    }

    @Override
    public List<IndividualSellers> findByName(String name) {
        return individualSellersRepository.findByName(name);
    }

    @Override
    public int findIDByFirebaseID(String firebaseID) {
        IndividualSellers individualSellers = individualSellersRepository.findByFirebaseID(firebaseID);
        return individualSellers.getId();
    }

    @Transactional
    @Override
    public IndividualSellers updateIndividualSellers(int id, IndividualSellers updated) {
        Date date = new Date();
        IndividualSellers individualSellers = getIndividualSellers(id);
        individualSellers.setName(updated.getName());
        individualSellers.setAddress(updated.getAddress());
        individualSellers.setPhoneNumber(updated.getPhoneNumber());
        individualSellers.setEmail(updated.getEmail());
        individualSellers.setBio(updated.getBio());
        individualSellers.setRoles(updated.getRoles());
        individualSellers.setUpdatedAt(date);
        return individualSellersRepository.save(individualSellers);
    }

    @Transactional
    @Override
    public IndividualSellers updateIndividualSellersByFirebaseID(String firebaseID, IndividualSellers updated) {
        Date date = new Date();
        IndividualSellers individualSellers = individualSellersRepository.findByFirebaseID(firebaseID);
        individualSellers.setName(updated.getName());
        individualSellers.setAddress(updated.getAddress());
        individualSellers.setPhoneNumber(updated.getPhoneNumber());
        individualSellers.setEmail(updated.getEmail());
        individualSellers.setBio(updated.getBio());
        individualSellers.setRoles(updated.getRoles());
        individualSellers.setUpdatedAt(date);
        return individualSellersRepository.save(individualSellers);
    }

    @Override
    public List<IndividualSellers> findTop3ByTotalItemStart() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        return individualSellersRepository.findTop3ByTotalItemStart(pageRequest);
    }

    @Override
    public void deleteIndividualSellers(int id) {
        individualSellersRepository.deleteById(id);
    }
}
