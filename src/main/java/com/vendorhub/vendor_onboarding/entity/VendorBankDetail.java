package com.vendorhub.vendor_onboarding.entity;

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

    @NotBlank(message = "account number is required")
    @Column(name = "account_number")
    private String accountNumber;

    @NotBlank(message = "account holder name is required")
    @Column(name = "account_holder_name")
    private String accountHolderName;

    @NotBlank(message = "IFSC code is required")
    @Column(name = "ifsc_code")
    private String ifscCode;

    @OneToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    private VendorProfile vendorProfile;
}
