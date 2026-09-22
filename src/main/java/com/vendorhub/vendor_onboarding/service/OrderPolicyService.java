package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.repository.OrderPoliciesRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderPolicyService {

    private OrderPoliciesRepository orderPoliciesRepository;

    OrderPolicyService(OrderPoliciesRepository orderPoliciesRepository)
    {
        this.orderPoliciesRepository = orderPoliciesRepository;
    }

}
