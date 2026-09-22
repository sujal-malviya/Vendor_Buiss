package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.service.OrderPolicyService;

public class OrderPolicyController {

    private OrderPolicyService orderPolicyService;

    OrderPolicyController(OrderPolicyService orderPolicyService)
    {
        this.orderPolicyService = orderPolicyService;
    }


}
