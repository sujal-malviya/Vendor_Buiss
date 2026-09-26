package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// Named MenuPackage (not Package) so it doesn't clash with java.lang.Package. The table name is unchanged.
@Entity
@Table(name = "package")
@Getter
@Setter
public class MenuPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is required")
    @Column(nullable = false)
    private String name;

    private String description;

    @PositiveOrZero(message = "price cannot be negative")
    private BigDecimal price;

    private String priceUnit;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
}
