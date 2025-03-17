package com.jai.croop.utility;

import java.util.Map;

public class OrderUtils {

    public static int calculateTotalQuantity(Map<String, Integer> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return 0; // Return 0 if the order list is empty or null
        }

        // Sum up all the quantities in the order list
        return orderList.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
