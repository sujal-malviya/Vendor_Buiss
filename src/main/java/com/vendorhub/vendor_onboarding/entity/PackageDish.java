package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "package_dish")
public class PackageDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package packageEntity;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Integer quantity;
}