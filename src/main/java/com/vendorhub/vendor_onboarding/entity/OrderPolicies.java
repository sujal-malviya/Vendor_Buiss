package com.vendorhub.vendor_onboarding.entity;

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
    private Long id;

    @Column(name = "modify_orders")
    private List<String> modifyOrders = new ArrayList<>();

    @Column(name = "cancel_grace_period")
    private Boolean cancelGracePeriod;

    @Column(name = "max_allowed_duration")
    private String maxAllowedDuration;

    @OneToOne
    @JoinColumn(name = "vendor_id", nullable = false, referencedColumnName = "id")
    private VendorProfile vendorProfile;
}
