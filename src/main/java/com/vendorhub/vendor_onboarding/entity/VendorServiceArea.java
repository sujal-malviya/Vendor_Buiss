package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vendor_service_area")
@Getter
@Setter
public class VendorServiceArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "max people is required")
    @Max(value = 1000, message = "maximum that can be served is 1000")
    @Column(name = "max_people")
    private Long maxPeople;

    @NotBlank(message = "service PIN is required")
    @Column(name = "service_pin")
    private String servicePin;

    @NotBlank(message = "service city is required")
    @Column(name = "service_city")
    private String serviceCity;

    @NotBlank(message = "service country is required")
    @Column(name = "service_country")
    private String serviceCountry;

    @NotNull(message = "order size is required")
    @Column(name = "order_size")
    private Long orderSize;

    @NotNull(message = "minimum order size is required")
    @Min(value = 200, message = "minimum order size must be at least 200")
    @Column(name = "minimum_order_size")
    private Long minimumOrderSize;

    @NotNull(message = "maximum order size is required")
    @Max(value = 1000, message = "maximum order size cannot be more than 1000")
    @Column(name = "maximum_order_size")
    private Long maximumOrderSize;

    @NotNull(message = "minimum order value is required")
    @Min(value = 250, message = "minimum order value must be at least 250 per plate")
    @Column(name = "minimum_order_value")
    private Long minOrderValue;

    @OneToOne
    @JoinColumn(name = "vendor_id", nullable = false, referencedColumnName = "id")
    private VendorProfile vendorProfile;
}
