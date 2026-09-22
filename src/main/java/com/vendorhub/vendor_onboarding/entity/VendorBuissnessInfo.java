package com.vendorhub.vendor_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "vendor_buissness_information")
@Setter
@Getter
public class VendorBuissnessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name="buissness_name")
    private String name;

    @Column(nullable = false,name = "contact_number")
    @NotBlank(message = "contact details are required ")
    private String contactDetail;

    @Column(nullable = false,name = "location_address")
    @NotBlank(message = "address is required ")
    private String address;

    @Column(nullable = false,name = "gst_number")
    @NotBlank(message = "Gst details is required ")
    private String gstNumber;

    @Column(nullable = false,name = "fssai_number")
    @NotBlank(message = "fssai number is required ")
    private String fssaiNwhaumber;

    @Column(nullable = false,name = "years_of_buissness")
    @NotNull(message = "Buissness year is  required ")
    private Integer yearOfBuissness;


    @OneToOne
    @JoinColumn(name = "vendor_id",referencedColumnName = "id",nullable = false)
    @JsonBackReference
    private VendorProfile vendorProfile;
}
