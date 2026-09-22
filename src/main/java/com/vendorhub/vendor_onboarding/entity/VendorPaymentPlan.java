package com.vendorhub.vendor_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @NotBlank(message = "PostPayment is required .")
    @Column(nullable = false , name = "post_payment")
    private String PostPayment;


    @NotBlank(message = "Prepayment is required .")
    @Column(nullable = false ,  name ="pre_payment")
    private String PrePayement;

    @NotBlank(message = "Advance Payemnt is required .")
    @Column(nullable = false , name = "advance_payment")
    private String AdvancePayment;

    @OneToOne
    @JoinColumn(name = "vendor_id",referencedColumnName = "id",nullable = false)
    @JsonBackReference
    private VendorProfile vendorProfile;
}
