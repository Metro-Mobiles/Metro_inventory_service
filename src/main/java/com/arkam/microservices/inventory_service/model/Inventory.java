package com.arkam.microservices.inventory_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "t_inventory")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Skucode must be included")
    private String skuCode;

    @NotBlank(message = "Enter the quantity")
    @PositiveOrZero(message = "Quantity must be zero or a positive value")
    private Integer quantity;
}