package com.vendorhub.vendor_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_policies")
@Getter
@Setter
public class OrderPolicies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;


    @Column(name = "modify_orders")
    private List<String> modifyOrders = new ArrayList<>();

    @Column(name = "cancel_grace_period")
    private Boolean CancelGracePeriod ;

    @Column(name = "max_allowed_duration")
    private String Max_allowed_duration ;


    @OneToOne
    @JoinColumn(name = "vendor_id" , nullable = false , referencedColumnName = "id")
    @JsonBackReference
    private VendorProfile vendorProfile;
}
