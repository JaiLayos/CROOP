package com.jai.croop.model;

import java.util.List;

public class CartGroupedResponseDTO {
    private List<GroupSellerCartDTO> groupSellers;
    private List<IndividualSellerCartDTO> individualSellers;

    public CartGroupedResponseDTO(){

    }

    public CartGroupedResponseDTO(List<GroupSellerCartDTO> groupSellers,
                                  List<IndividualSellerCartDTO> individualSellers){
        this.groupSellers = groupSellers;
        this.individualSellers = individualSellers;
    }

    public List<GroupSellerCartDTO> getGroupSellers() {
        return groupSellers;
    }

    public void setGroupSellers(List<GroupSellerCartDTO> groupSellers) {
        this.groupSellers = groupSellers;
    }

    public List<IndividualSellerCartDTO> getIndividualSellers() {
        return individualSellers;
    }
    public void setIndividualSellers(List<IndividualSellerCartDTO> individualSellers) {
        this.individualSellers = individualSellers;
    }
}
