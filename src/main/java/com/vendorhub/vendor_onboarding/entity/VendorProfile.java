package com.vendorhub.vendor_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import lombok.Setter;


@Entity
@Table(name="vendorprofile")
@Setter
@Getter
public class VendorProfile {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="vendor_name",nullable = false)
    @NotBlank(message = "name is required")
    private String name;


    @Column(name="vendor_address",nullable = false)
    @NotBlank(message = "address is required")
    private String address;

    @OneToOne(mappedBy = "vendorProfile",cascade = CascadeType.ALL)
    @JsonManagedReference
    private VendorBuissnessInfo buissnessInfo;
}
