package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "vendor_dish")
@Getter
@Setter
public class VendorDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendor vendor;

    @NotNull(message = "dish is required")
    @ManyToOne
    private Dish dish;

    @PositiveOrZero(message = "price cannot be negative")
    private BigDecimal price;

    private Boolean available;
}
