package com.jai.croop.service;

import ch.qos.logback.classic.Logger;
import com.jai.croop.model.*;
import com.jai.croop.repository.IndividualSellersDiscountRepository;
import com.jai.croop.repository.IndividualSellersProductsRepository;
import com.jai.croop.repository.IndividualSellersRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IndividualSellersProductService implements IIndividualSellersProductService{
    @Autowired
    private IndividualSellersProductsRepository individualSellersProductsRepository;
    @Autowired
    private IndividualSellersRepository individualSellersRepository;
    @Autowired
    private IndividualSellersDiscountService individualSellersDiscountService;
    @Autowired
    private IndividualSellersDiscountRepository individualSellersDiscountRepository;
    private static final Logger log = (Logger) LoggerFactory.getLogger(GroupSellersProductService.class);


    @Override
    public IndividualSellersProductsInventory addItems(IndividualSellersProductsInventory individualSellersProductsInventory, IndividualSellers individualSellers) {
        if (individualSellersProductsInventory.getIndividualSellers() == null) {
            throw new IllegalArgumentException("Individual Seller cannot be null in products");
        }

        individualSellers = individualSellersRepository.findById(individualSellersProductsInventory.getIndividualSellers().getId())
                .orElseThrow(() -> new RuntimeException("GroupSeller does not exist!"));

        individualSellersProductsInventory.setIndividualSellers(individualSellers);
        int remaining = individualSellersProductsInventory.getItemStart() - individualSellersProductsInventory.getItemUsed();
        individualSellersProductsInventory.setItemRemaining(remaining);
        individualSellersProductsInventory = individualSellersProductsRepository.save(individualSellersProductsInventory);

        IndividualSellersDiscount individualSellersDiscount = new IndividualSellersDiscount();
        individualSellersDiscount.setOriginalPrice(individualSellersProductsInventory.getPrice());
        individualSellersDiscount.setDiscountPercent(0.0);
        individualSellersDiscount.setSalePrice(individualSellersProductsInventory.getPrice());
        individualSellersDiscount.setIndividualSellers(individualSellers);
        individualSellersDiscount.setIndividualSellersProductsInventory(individualSellersProductsInventory); // Associate the persisted inventory

        individualSellersDiscountService.addDiscounts(individualSellersDiscount, individualSellers, individualSellersProductsInventory);

        return individualSellersProductsInventory;
    }

    @Override
    public IndividualSellersProductsInventory getItem(int id) {
        return individualSellersProductsRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product does not exist!"));
    }

    @Override
    public List<IndividualSellersProductsInventory> findByItemName(String itemName) {
        List<IndividualSellersProductsInventory> products = individualSellersProductsRepository.findByItemName(itemName);
        return products;
    }

    @Override
    public List<IndividualSellersProductsInventory> findByFirebaseID(String firebaseID) {
        IndividualSellers individualSellers = individualSellersRepository.findByFirebaseID(firebaseID);
        int id = individualSellers.getId();
        return individualSellersProductsRepository.findByIndividualSellersId(id);
    }

    @Override
    public List<IndividualSellersProductsInventory> getAllItems() {
        return individualSellersProductsRepository.findAll();
    }

    @Override
    public List<IndividualSellersProductsInventory> findTop3ByItemStart() {
        PageRequest pageRequest = PageRequest.of(0, 3); // Limit to 3 items
        return individualSellersProductsRepository.findTop3ByItemStart(pageRequest);
    }

    @Override
    public List<IndividualSellersProductsInventory> findTop3ByLowestItemRemaining() {
        PageRequest pageRequest = PageRequest.of(0, 3); // Limit to 3 items
        return individualSellersProductsRepository.findTop3ByLowestItemRemaining(pageRequest);
    }

    @Override
    public boolean findIfSCIsSet(int id) {
        IndividualSellers individualSellers = individualSellersRepository.findById(id).orElseThrow(()-> new RuntimeException("Individual Seller does not exist!"));
        boolean exist = false;
        if(individualSellers.getProduct_inventory_SC() != 0) {
            exist = true;
        }
        return exist;
    }

    @Override
    public boolean findIfMCIsSet(int id) {
        IndividualSellers individualSellers = individualSellersRepository.findById(id).orElseThrow(()-> new RuntimeException("Individual Seller does not exist!"));
        boolean exist = false;
        if(individualSellers.getProduct_inventory_MC() != 0) {
            exist = true;
        }
        return exist;
    }

    @Override
    public boolean shouldRestock(List<Integer> demandForecast, int id, int remaining) {
        log.info("Checking restock necessity for sellerId: {}, remaining stock: {}", id, remaining);

        IndividualSellers individualSellers = individualSellersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group Seller Doesn't Exist"));

        int setupCost = individualSellers.getProduct_inventory_SC();
        int holdingCost = individualSellers.getProduct_inventory_MC();

        int reorderPoint = calculateOptimalReorderPoint(demandForecast, setupCost, holdingCost, remaining);

        log.info("Calculated reorder point: {}. Remaining stock: {}.", reorderPoint, remaining);

        return remaining <= reorderPoint;
    }

    @Override
    public boolean shouldDiscount(int id, int shelfLifeDays) {
        IndividualSellersProductsInventory individualSellersProductsInventory = getItem(id);
        LocalDate setDate = individualSellersProductsInventory.getLocalDate();
        LocalDate shelfLifeThreshold = setDate.plusDays(shelfLifeDays);
        LocalDate triggerThreshold = shelfLifeThreshold.minusDays(7);
        LocalDate now = LocalDate.now();
        return now.isAfter(triggerThreshold);
    }

    private int calculateOptimalReorderPoint(List<Integer> demandForecast, int setupCost, int holdingCost, int currentStock) {
        int periods = demandForecast.size();
        int[] cost = new int[periods + 1];
        int[] orderQty = new int[periods + 1];
        for (int i = 0; i <= periods; i++) {
            cost[i] = Integer.MAX_VALUE;
        }
        cost[0] = 0;
        for (int t = 1; t <= periods; t++) {
            int totalDemand = 0;
            for (int j = t; j >= 1; j--) {
                totalDemand += demandForecast.get(j - 1);
                int totalCost = (j > 1 ? cost[j - 1] : 0) + setupCost + (holdingCost * totalDemand);
                if (totalCost < cost[t]) {
                    cost[t] = totalCost;
                    orderQty[t] = totalDemand;
                }
            }
        }
        return orderQty[periods] > currentStock ? orderQty[periods] : 0;
    }

    @Transactional
    @Override
    public IndividualSellersProductsInventory updateItems(int id, IndividualSellersProductsInventory newIndividualSellersProductsInventory) {
        IndividualSellersProductsInventory individualSellersProductsInventory = getItem(id);
        individualSellersProductsInventory.setPrice(newIndividualSellersProductsInventory.getPrice());
        individualSellersProductsInventory.setItemName(newIndividualSellersProductsInventory.getItemName());
        individualSellersProductsInventory.setItemStart(newIndividualSellersProductsInventory.getItemStart());
        individualSellersProductsInventory.setItemUsed(newIndividualSellersProductsInventory.getItemUsed());
        return individualSellersProductsRepository.save(individualSellersProductsInventory);
    }

    @Transactional
    @Override
    public void deleteItems(int id) {
        IndividualSellersProductsInventory individualSellersProductsInventory = individualSellersProductsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product does not exist!")
        );
        individualSellersDiscountRepository.deleteById(individualSellersProductsInventory.getIndividualSellersDiscount().getId());
        individualSellersProductsRepository.deleteById(id);
    }
}
