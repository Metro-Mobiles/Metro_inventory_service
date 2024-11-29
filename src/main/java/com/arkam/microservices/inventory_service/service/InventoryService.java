package com.arkam.microservices.inventory_service.service;

import com.arkam.microservices.inventory_service.model.Inventory;
import com.arkam.microservices.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        try{
            log.info(" Start -- Received request to check stock for skuCode {}, with quantity {}", skuCode, quantity);
            boolean isInStock = inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
            log.info(" End -- Product with skuCode {}, and quantity {}, is in stock - {}", skuCode, quantity, isInStock);
            return isInStock;
        } catch (Exception e){
            throw new RuntimeException("Server Error:"+e.getMessage(),e);
        }
    }

    public void updateStock(String skuCode, Integer newQuantity){
        try {
            if(inventoryRepository.existsBySkuCode(skuCode)){
                Optional<Inventory> inventory = inventoryRepository.findBySkuCode(skuCode);

                Inventory updatedInventory = inventory.get();
                updatedInventory.setQuantity(updatedInventory.getQuantity() + newQuantity);

                inventoryRepository.save(updatedInventory);
            } else {
                throw new RuntimeException("Can't found the inventory");
            }
        }catch (Exception e){
            throw new RuntimeException("Server Error:"+e.getMessage(),e);
        }
    }

    public void removeStock(String skuCode){
        try {
            if(inventoryRepository.existsBySkuCode(skuCode)){
                Optional<Inventory> inventory = inventoryRepository.findBySkuCode(skuCode);
                inventoryRepository.delete(inventory.get());
            } else {
                throw new RuntimeException("Inventory is already removed");
            }
        }catch (Exception e){
            throw new RuntimeException("Error on finding skuCode");
        }
    }

    public ResponseEntity<?> checkQuantity(String skuCode){
        try {
            Optional<Inventory> inventory = inventoryRepository.findBySkuCode(skuCode);
            if(inventory.isPresent()){
                Inventory optionalInventory = inventory.get();
                return ResponseEntity.ok().body(optionalInventory.getQuantity());
            } else {
                return ResponseEntity.badRequest().body("Inventory not present");
            }
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Server error on finding inventory");
        }
    }
    public void addStock(Inventory inventory){
        try {
            if(!inventoryRepository.existsBySkuCode(inventory.getSkuCode())) {
                inventoryRepository.save(inventory);
            } else {
                updateStock(inventory.getSkuCode(),inventory.getQuantity());
            }
        } catch (Exception e){
            throw new RuntimeException("Server error:"+e.getMessage(),e);
        }
    }

    public void updateByOrder(String skuCode,Integer quantityThatWantToReduce){
        try {
            Optional<Inventory> inventory = inventoryRepository.findBySkuCode(skuCode);
            if (inventory.isPresent()) {
                Inventory presentedInventory = inventory.get();
                Integer quantity = presentedInventory.getQuantity();
                presentedInventory.setQuantity(quantity - quantityThatWantToReduce);

                inventoryRepository.save(presentedInventory);
            } else {
                throw new RuntimeException("Inventory is not presented");
            }
        } catch (Exception e){
            throw new RuntimeException("Server error:"+e.getMessage(),e);
        }
    }

}