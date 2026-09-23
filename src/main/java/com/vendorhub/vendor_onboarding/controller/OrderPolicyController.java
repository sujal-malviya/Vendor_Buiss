package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.OrderPolicies;
import com.vendorhub.vendor_onboarding.service.OrderPolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/order-policies")
public class OrderPolicyController {

    private final OrderPolicyService orderPolicyService;

    OrderPolicyController(OrderPolicyService orderPolicyService)
    {
        this.orderPolicyService = orderPolicyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderPolicies createOrderPolicy(@Valid @RequestBody OrderPolicies orderPolicies)
    {
        return orderPolicyService.createOrderPolicy(orderPolicies);
    }

    @GetMapping
    public List<OrderPolicies> getMyOrderPolicies()
    {
        return orderPolicyService.getMyOrderPolicies();
    }

    @GetMapping("/{id}")
    public OrderPolicies getOrderPolicyById(@PathVariable Long id)
    {
        return orderPolicyService.getOrderPolicyById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderPolicy(@PathVariable Long id)
    {
        orderPolicyService.deleteOrderPolicy(id);
    }

    @PutMapping("/{id}")
    public OrderPolicies updateOrderPolicy(@PathVariable Long id, @Valid @RequestBody OrderPolicies orderPolicies)
    {
        return orderPolicyService.updateOrderPolicy(id, orderPolicies);
    }

    @PatchMapping("/{id}")
    public OrderPolicies patchOrderPolicy(@PathVariable Long id, @RequestBody OrderPolicies orderPolicies)
    {
        return orderPolicyService.patchOrderPolicy(id, orderPolicies);
    }
}
