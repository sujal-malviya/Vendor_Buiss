package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
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

    @ManyToOne
    private Dish dish;

    private BigDecimal price;

    private Boolean available;
}