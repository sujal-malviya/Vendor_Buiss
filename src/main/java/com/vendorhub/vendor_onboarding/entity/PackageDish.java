package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "package_dish")
@Getter
@Setter
public class PackageDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "menuPackage is required, e.g. {\"id\": 1}")
    @ManyToOne
    @JoinColumn(name = "package_id")
    private MenuPackage menuPackage;

    @NotNull(message = "dish is required, e.g. {\"id\": 1}")
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Positive(message = "quantity must be more than 0")
    private Integer quantity;
}
