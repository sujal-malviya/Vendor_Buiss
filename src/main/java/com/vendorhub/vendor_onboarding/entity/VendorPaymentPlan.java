package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vendor_payment_plan")
@Getter
@Setter
public class VendorPaymentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "post payment is required")
    @Column(nullable = false, name = "post_payment")
    private String postPayment;

    @NotBlank(message = "pre payment is required")
    @Column(nullable = false, name = "pre_payment")
    private String prePayment;

    @NotBlank(message = "advance payment is required")
    @Column(nullable = false, name = "advance_payment")
    private String advancePayment;

    @OneToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    private VendorProfile vendorProfile;
}
