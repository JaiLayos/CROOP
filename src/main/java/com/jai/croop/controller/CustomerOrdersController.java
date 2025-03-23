package com.jai.croop.controller;

import ch.qos.logback.classic.Logger;
import com.jai.croop.model.*;
import com.jai.croop.repository.GroupSellersProductsRepository;
import com.jai.croop.repository.IndividualSellersProductsRepository;
import com.jai.croop.repository.IndividualSellersRepository;
import com.jai.croop.service.*;
import org.aspectj.weaver.ast.Not;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer-orders")
public class CustomerOrdersController {
    @Autowired
    private ICustomerOrdersService customerOrdersService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IGroupSellersProductInventoryService groupSellersProductInventoryService;
    @Autowired
    private GroupSellersProductsRepository groupSellersProductsRepository;
    @Autowired
    private IIndividualSellersProductService individualSellersProductService;
    @Autowired
    private IndividualSellersProductsRepository individualSellersProductsRepository;
    @Autowired
    private IIndividualSellersService individualSellersService;
    @Autowired
    private IGroupSellersService groupSellersService;
    @Autowired
    private INotificationsService notificationsService;
    private static final Logger log = (Logger) LoggerFactory.getLogger(CustomerOrdersController.class);

    // Group Order Endpoints
    @PostMapping("/group/{customerId}/{groupSellerId}")
    public ResponseEntity<CustomerOrdersForGroupSellers> createGroupOrder(
            @PathVariable int customerId,
            @PathVariable int groupSellerId,
            @RequestBody CustomerOrdersForGroupSellers order) {

        CustomerOrdersForGroupSellers savedOrder = customerOrdersService.addCustomerOrdersToGroupOrders(
                order, customerId, groupSellerId);
        Map<String, Integer> orderList = savedOrder.getOrderList();
        if (orderList != null && !orderList.isEmpty()) {
            for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
                String productName = entry.getKey(); // Product name
                int quantity = entry.getValue();     // Quantity
                List<GroupSellersProductsInventory> productsInventories = groupSellersProductInventoryService.findByItemName(productName);
                for(GroupSellersProductsInventory productsInventory:productsInventories){
                    int productID = productsInventory.getId();
                    int initialUsed = productsInventory.getItemUsed();
                    int used = quantity + initialUsed;
                    int currentStock = productsInventory.getItemStart();
                    int remaining = currentStock - used;
                    productsInventory.setItemUsed(used);
                    productsInventory.setItemRemaining(remaining);
                    groupSellersProductInventoryService.updateItems(productID, productsInventory);
                    checkGroupStockBasedOnDemand(groupSellerId,productID, remaining);

                }
            }
        }
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("group/demand-threshold/{groupSellerId}/{productId}")
    public void checkGroupStockBasedOnDemand(@PathVariable int groupSellerId, @PathVariable int productId, int remaining) {
        log.info("Checking stock for groupSellerId: {}, productId: {}, remaining: {}", groupSellerId, productId, remaining);

        GroupSellersProductsInventory productInventory = groupSellersProductsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Cannot find the product to check the stock."));

        List<Integer> demandForecast = customerOrdersService.getPastOrderQuantities(groupSellerId, productId);
        log.info("Demand forecast for productId {}: {}", productId, demandForecast);

        boolean needsRestock = groupSellersProductInventoryService.shouldRestock(demandForecast, groupSellerId, remaining);
        GroupSellersProductsInventory product = groupSellersProductInventoryService.getItem(productId);
        String productName = product.getItemName();

        GroupSellers groupSellers = groupSellersService.getGroupSellers(groupSellerId);
        String userName = groupSellers.getGroupName();

        if (needsRestock) {
            Notifications notifications = new Notifications();
            notifications.setUserID(groupSellerId);
            notifications.setUserName(userName);
            notifications.setUserType("Group Seller");
            notifications.setMessage("Product: "+productName+ " hit the demand threshold for its inventory. " +
                    "Consider restocking the product.");
            notifications.setDate(new java.sql.Date(System.currentTimeMillis()));
            notifications.setAbout("Inventory");
            notificationsService.addNotification(notifications);
        } else {
            log.info("Restock Error");
        }
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<CustomerOrdersForGroupSellers> getGroupOrder(@PathVariable int id) {
        return ResponseEntity.ok(customerOrdersService.getCustomerOrdersFromGroup(id));
    }

    @GetMapping("/group/customer/{id}")
    public ResponseEntity<List<SellerOrdersDTO>> getGroupOrderByCustomer(@PathVariable int id) {
        List<CustomerOrdersForGroupSellers> groupSellers = customerOrdersService.groupSellersFindByCustomerId(id);
        List<SellerOrdersDTO> ordersDTOS = new ArrayList<>();
        for(CustomerOrdersForGroupSellers groupSeller : groupSellers){
            SellerOrdersDTO ordersDTO = new SellerOrdersDTO();
            ordersDTO.setId(groupSeller.getId());
            ordersDTO.setCustomerId(groupSeller.getCustomerId());
            ordersDTO.setCustomerName(groupSeller.getCustomer().getName());
            ordersDTO.setOrderList(groupSeller.getOrderList());
            ordersDTO.setOrderPrice(groupSeller.getOrderPrice());
            ordersDTO.setOrderStatus(groupSeller.getOrderStatus());
            ordersDTO.setOrderType(groupSeller.getOrderType());
            ordersDTO.setOrderDate(groupSeller.getOrderDate());
            ordersDTOS.add(ordersDTO);
        }
        return ResponseEntity.ok(ordersDTOS);
    }

    @GetMapping("/group/seller/{id}")
    public ResponseEntity<List<SellerOrdersDTO>> getGroupOrderByGroupSellerId(@PathVariable int id) {
        List<CustomerOrdersForGroupSellers> groupSellers = customerOrdersService.findByGroupSellerId(id);
        List<SellerOrdersDTO> ordersDTOS = new ArrayList<>();
        for(CustomerOrdersForGroupSellers groupSeller : groupSellers){
            SellerOrdersDTO ordersDTO = new SellerOrdersDTO();
            ordersDTO.setId(groupSeller.getId());
            ordersDTO.setCustomerId(groupSeller.getCustomerId());
            ordersDTO.setCustomerName(groupSeller.getCustomer().getName());
            ordersDTO.setOrderList(groupSeller.getOrderList());
            ordersDTO.setOrderPrice(groupSeller.getOrderPrice());
            ordersDTO.setOrderStatus(groupSeller.getOrderStatus());
            ordersDTO.setOrderType(groupSeller.getOrderType());
            ordersDTO.setOrderDate(groupSeller.getOrderDate());
            ordersDTOS.add(ordersDTO);
        }
        return ResponseEntity.ok(ordersDTOS);
    }

    @GetMapping("/individual/seller/{id}")
    public ResponseEntity<List<SellerOrdersDTO>> getIndividualOrderByGroupSellerId(@PathVariable int id) {
        List<CustomerOrdersForIndivSellers> groupSellers = customerOrdersService.findByindividualSellerId(id);
        List<SellerOrdersDTO> ordersDTOS = new ArrayList<>();
        for(CustomerOrdersForIndivSellers groupSeller : groupSellers){
            SellerOrdersDTO ordersDTO = new SellerOrdersDTO();
            ordersDTO.setId(groupSeller.getId());
            ordersDTO.setCustomerId(groupSeller.getCustomerId());
            ordersDTO.setCustomerName(groupSeller.getCustomer().getName());
            ordersDTO.setOrderList(groupSeller.getOrderList());
            ordersDTO.setOrderPrice(groupSeller.getOrderPrice());
            ordersDTO.setOrderStatus(groupSeller.getOrderStatus());
            ordersDTO.setOrderType(groupSeller.getOrderType());
            ordersDTO.setOrderDate(groupSeller.getOrderDate());
            ordersDTOS.add(ordersDTO);
        }
        return ResponseEntity.ok(ordersDTOS);
    }

    @GetMapping("/group")
    public ResponseEntity<List<CustomerOrdersForGroupSellers>> getAllGroupOrders() {
        return ResponseEntity.ok(customerOrdersService.getAllCustomerOrdersFromGroup());
    }

    @PutMapping("/group/{id}")
    public ResponseEntity<CustomerOrdersForGroupSellers> updateGroupOrder(
            @PathVariable int id,
            @RequestBody CustomerOrdersForGroupSellers updatedOrder) {
        return ResponseEntity.ok(customerOrdersService.updateCustomerOrdersFromGroup(id, updatedOrder));
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteGroupOrder(@PathVariable int id) {
        customerOrdersService.deleteGroupCustomerOrders(id);
        return ResponseEntity.noContent().build();
    }

    // Individual Order Endpoints
    @PostMapping("/individual/{customerId}/{sellerId}")
    public ResponseEntity<CustomerOrdersForIndivSellers> createIndividualOrder(
            @PathVariable int customerId,
            @PathVariable int sellerId,
            @RequestBody CustomerOrdersForIndivSellers order) {

        CustomerOrdersForIndivSellers savedOrder = customerOrdersService.addCustomerOrdersToIndividualOrders(
                order, customerId, sellerId);
        Map<String, Integer> orderList = savedOrder.getOrderList();
        if (orderList != null && !orderList.isEmpty()) {
            for (Map.Entry<String, Integer> entry : orderList.entrySet()) {
                String productName = entry.getKey(); // Product name
                int quantity = entry.getValue();     // Quantity
                List<IndividualSellersProductsInventory> productsInventories = individualSellersProductsRepository.findByItemName(productName);
                for(IndividualSellersProductsInventory productsInventory:productsInventories){
                    int productID = productsInventory.getId();
                    int initialUsed = productsInventory.getItemUsed();
                    int used = quantity + initialUsed;
                    int currentStock = productsInventory.getItemStart();
                    int remaining = currentStock - used;
                    productsInventory.setItemUsed(used);
                    productsInventory.setItemRemaining(remaining);
                    individualSellersProductService.updateItems(productID, productsInventory);
                    checkIndividualStockBasedOnDemand(sellerId, productID, remaining);

                }
            }
        }
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("individual/demand-threshold/{individualID}/{productId}")
    public void checkIndividualStockBasedOnDemand(@PathVariable int individualID, @PathVariable int productId, int remaining) {
        log.info("Checking stock for individual seller ID: {}, productId: {}, remaining: {}", individualID, productId, remaining);

        IndividualSellersProductsInventory productInventory = individualSellersProductsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Cannot find the product to check the stock."));

        List<Integer> demandForecast = customerOrdersService.getPastOrderQuantities(individualID, productId);
        log.info("Demand forecast for productId {}: {}", productId, demandForecast);

        boolean needsRestock = individualSellersProductService.shouldRestock(demandForecast, individualID, remaining);
        IndividualSellersProductsInventory product = individualSellersProductService.getItem(productId);
        String productName = product.getItemName();

        IndividualSellers individualSellers = individualSellersService.getIndividualSellers(individualID);
        String userName = individualSellers.getName();

        if (needsRestock) {
            Notifications notifications = new Notifications();
            notifications.setUserID(individualID);
            notifications.setUserName(userName);
            notifications.setUserType("Individual Seller");
            notifications.setMessage("Product: "+productName+ " hit the demand threshold for its inventory. " +
                    "Consider restocking the product.");
            notifications.setDate(new java.sql.Date(System.currentTimeMillis()));
            notifications.setAbout("Inventory");
            notificationsService.addNotification(notifications);
        } else {
            log.info("Restock Error");
        }
    }

    @GetMapping("/individual/{id}")
    public ResponseEntity<CustomerOrdersForIndivSellers> getIndividualOrder(@PathVariable int id) {
        return ResponseEntity.ok(customerOrdersService.getCustomerOrdersFromIndividual(id));
    }

    @GetMapping("/individual/customer/{id}")
    public ResponseEntity<List<SellerOrdersDTO>> getIndividualOrderByCustomer(@PathVariable int id) {
        List<CustomerOrdersForIndivSellers> indivSellers = customerOrdersService.individualSellersFindByCustomerId(id);
        List<SellerOrdersDTO> ordersDTOS = new ArrayList<>();
        for(CustomerOrdersForIndivSellers indivSeller : indivSellers){
            SellerOrdersDTO ordersDTO = new SellerOrdersDTO();
            ordersDTO.setId(indivSeller.getId());
            ordersDTO.setCustomerId(indivSeller.getCustomerId());
            ordersDTO.setCustomerName(indivSeller.getCustomer().getName());
            ordersDTO.setOrderList(indivSeller.getOrderList());
            ordersDTO.setOrderPrice(indivSeller.getOrderPrice());
            ordersDTO.setOrderStatus(indivSeller.getOrderStatus());
            ordersDTO.setOrderType(indivSeller.getOrderType());
            ordersDTO.setOrderDate(indivSeller.getOrderDate());
            ordersDTOS.add(ordersDTO);
        }
        return ResponseEntity.ok(ordersDTOS);
    }

    @GetMapping("/individual")
    public ResponseEntity<List<CustomerOrdersForIndivSellers>> getAllIndividualOrders() {
        return ResponseEntity.ok(customerOrdersService.getAllCustomerOrdersFromIndividual());
    }

    @PutMapping("/individual/{id}")
    public ResponseEntity<CustomerOrdersForIndivSellers> updateIndividualOrder(
            @PathVariable int id,
            @RequestBody CustomerOrdersForIndivSellers updatedOrder) {
        return ResponseEntity.ok(customerOrdersService.updateCustomerOrdersFromIndividual(id, updatedOrder));
    }

    @DeleteMapping("/individual/{id}")
    public ResponseEntity<Void> deleteIndividualOrder(@PathVariable int id) {
        customerOrdersService.deleteIndividualCustomerOrders(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group/sales/{sellerId}")
    public ResponseEntity<List<DailySalesDTO>> getDailySales(
            @PathVariable int sellerId) {

        List<DailySalesDTO> salesData = customerOrdersService
                .getDailySalesForGroupSeller(sellerId);

        return ResponseEntity.ok(salesData);
    }
}
