package com.vendorhub.vendor_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vendor_bank_detail")
public class VendorBankDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "account number is mandatory.")
    @Column(name = "account_number")
    private String AccountNumber;

    @NotBlank(message = "account_holder_name field is required")
    @Column(name = "account_holder_name")
    private String AccountHolderName;


    @NotBlank(message = "IFSC CODE is required.")
    @Column(name = "ifsc_code")
    private String IFSC_Code;

    @OneToOne
    @JoinColumn(name = "vendor_id",referencedColumnName = "id",nullable = false)
    @JsonBackReference
    private VendorProfile vendorProfile;


}
