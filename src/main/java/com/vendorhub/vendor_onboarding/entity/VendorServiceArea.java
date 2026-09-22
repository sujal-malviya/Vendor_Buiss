package com.vendorhub.vendor_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "vendor_service_area")
@Getter
@Setter
public class VendorServiceArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NonNull
    @Column(name = "max_people")
    @Max(value = 1000 , message = "Maximum can be served = 1000 .")
    private Long MaxPeople;

    @NotBlank(message = "Service PIN is required .")
    @Column(name = "service_pin")
    private String ServicePIN;

    @NotBlank(message = "Service City is required .")
    @Column(name = "service_city")
    private String ServiceCity;

    @NotBlank(message = "Service Country is required .")
    @Column(name = "service_country")
    private String ServiceCountry;

    @NotNull
    @Column(name = "order_size")
    private Long OrderSize;

    @Min(value = 200 , message = "minimum order band is 200 .")
    @NotNull
    @Column(name = "minimum_order_size")
    private Long MinimumOrderSize;

    @Max(value = 1000 , message = "maximum order band is 1000 ")
    @NotNull
    @Column(name = "maximum_order_size")
    private Long MaximumOrderSize;

    @NotNull
    @Min(value = 250 , message = "250/plate")
    @Column(name = "minimum_order_value")
    private Long MinOrderValue;

    @OneToOne
    @JoinColumn(name = "vendor_id" , nullable = false ,referencedColumnName = "id")
    @JsonBackReference
    private VendorProfile vendorProfile;
}
