package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.OrderPolicies;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.service.OrderPolicyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/order-policies")
public class OrderPolicyController {

    private OrderPolicyService orderPolicyService;

    OrderPolicyController(OrderPolicyService orderPolicyService)
    {
        this.orderPolicyService = orderPolicyService;
    }

    @PostMapping
    public OrderPolicies createOrderPolicy(@Valid @RequestBody OrderPolicies orderPolicies)
    {
        return orderPolicyService.createOrderPolicy(orderPolicies);
    }

    @GetMapping
    public List<OrderPolicies> getOrderPolicies()
    {

        return orderPolicyService.getOrderPolicies();
    }

    @GetMapping("/{id}")
    public OrderPolicies getOrderPolicyById(@PathVariable Long id)
    {

        return orderPolicyService.getOrderPolicyById(id);
    }
}
