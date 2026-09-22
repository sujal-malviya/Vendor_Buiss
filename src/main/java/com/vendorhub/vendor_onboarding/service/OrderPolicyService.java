package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.OrderPolicies;
import com.vendorhub.vendor_onboarding.repository.OrderPoliciesRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class OrderPolicyService {

    private OrderPoliciesRepository orderPoliciesRepository;

    OrderPolicyService(OrderPoliciesRepository orderPoliciesRepository)
    {
        this.orderPoliciesRepository = orderPoliciesRepository;
    }


    public OrderPolicies createOrderPolicy( OrderPolicies orderPolicies)
    {
        return orderPoliciesRepository.save(orderPolicies);
    }

    public List<OrderPolicies> getOrderPolicies()
    {

        return orderPoliciesRepository.findAll();
    }

    public OrderPolicies getOrderPolicyById( Long id)
    {

        return orderPoliciesRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found : "+id));
    }
}
