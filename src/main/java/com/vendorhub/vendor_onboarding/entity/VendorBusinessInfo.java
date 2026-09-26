package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

// Table and column names keep the old "buissness" spelling so existing rows in the database are not lost.
@Entity
@Table(name = "vendor_buissness_information")
@Setter
@Getter
public class VendorBusinessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "buissness_name")
    @NotBlank(message = "business name is required")
    private String name;

    @Column(nullable = false, name = "contact_number")
    @NotBlank(message = "contact details are required")
    private String contactDetail;

    @Column(nullable = false, name = "location_address")
    @NotBlank(message = "address is required")
    private String address;

    @Column(nullable = false, name = "gst_number")
    @NotBlank(message = "GST number is required")
    private String gstNumber;

    @Column(nullable = false, name = "fssai_number")
    @NotBlank(message = "FSSAI number is required")
    private String fssaiNumber;

    @Column(nullable = false, name = "years_of_buissness")
    @NotNull(message = "years in business is required")
    @PositiveOrZero(message = "years in business cannot be negative")
    private Integer yearsInBusiness;

    @OneToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    private VendorProfile vendorProfile;
}
