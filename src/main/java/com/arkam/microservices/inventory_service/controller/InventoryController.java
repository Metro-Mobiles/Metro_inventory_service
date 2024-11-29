package com.arkam.microservices.inventory_service.controller;

import com.arkam.microservices.inventory_service.model.Inventory;
import com.arkam.microservices.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/checkStock")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }
    @PostMapping("/addNewStock")
    @ResponseStatus(HttpStatus.CREATED)
    public String addStock(@RequestBody Inventory inventory) {
        inventoryService.addStock(inventory);
        return "Add new stock successfully!";
    }

    @DeleteMapping("/removeStock")
    public String dltStock(@RequestParam String skuCode) {
        inventoryService.removeStock(skuCode);
        return "Delete stock successfully";
    }

    @PutMapping("/updateStock")
    public String updateStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        inventoryService.updateStock(skuCode, quantity);
        return "Update the existing stock successfully";
    }

    @GetMapping("/availableQuantity")
    public ResponseEntity<?> checkQuantity(@RequestParam String skuCode){
        return ResponseEntity.ok().body(inventoryService.checkQuantity(skuCode));
    }

    @PutMapping("/reduceQuantity")
    public String deleteByOrder(@RequestParam String skuCode, @RequestParam Integer quantity) {
        inventoryService.updateByOrder(skuCode, quantity);
        return "Quantity reduced as order quantity from the existing stock successfully";
    }
}