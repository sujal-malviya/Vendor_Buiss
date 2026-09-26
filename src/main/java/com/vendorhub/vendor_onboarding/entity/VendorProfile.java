package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vendorprofile")
@Setter
@Getter
public class VendorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_name", nullable = false)
    @NotBlank(message = "name is required")
    private String name;

    @Column(name = "vendor_address", nullable = false)
    @NotBlank(message = "address is required")
    private String address;

    // The login account that owns this profile. The server sets it from the JWT, never from the request body.
    @OneToOne
    @JoinColumn(name = "vendor_id", unique = true)
    private Vendor vendor;

    @OneToOne(mappedBy = "vendorProfile", cascade = CascadeType.ALL)
    @Valid
    private VendorBusinessInfo businessInfo;
}
