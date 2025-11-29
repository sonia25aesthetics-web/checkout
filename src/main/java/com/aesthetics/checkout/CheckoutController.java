package com.aesthetics.checkout;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CheckoutController {

    private static final Map<String, Double> PRODUCT_PRICES = new HashMap<>();
    static {
        PRODUCT_PRICES.put("15281", 229.0);
        PRODUCT_PRICES.put("15282", 349.0);
        PRODUCT_PRICES.put("15283", 499.0);
    }

    @GetMapping("/checkout")
    public Map<String, Object> checkout(
            @RequestParam String products,
            @RequestParam(required = false) String coupon) {

        Map<String, Integer> productQuantities = new HashMap<>();
        for (String productEntry : products.split(",")) {
            String[] parts = productEntry.split(":");
            productQuantities.put(parts[0], Integer.parseInt(parts[1]));
        }

        double subtotal = 0.0;
        List<Map<String, Object>> items = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
            double price = PRODUCT_PRICES.getOrDefault(productId, 0.0);
            double lineTotal = price * quantity;

            subtotal += lineTotal;

            Map<String, Object> item = new HashMap<>();
            item.put("productId", productId);
            item.put("quantity", quantity);
            item.put("unitPrice", price);
            item.put("lineTotal", lineTotal);
            items.add(item);
        }

        double discount = 0.0;
        if (coupon != null && coupon.equalsIgnoreCase("DIWALI2025")) {
            discount = subtotal * 0.10;
        }

        double grandTotal = subtotal - discount;

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("subtotal", subtotal);
        result.put("discount", discount);
        result.put("grandTotal", grandTotal);
        result.put("coupon", coupon != null ? coupon : "No coupon applied");

        return result;
    }
}
