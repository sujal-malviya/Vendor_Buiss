package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.OrderPolicyRequest;
import com.vendorhub.vendor_onboarding.dto.OrderPolicyResponse;
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
    public OrderPolicyResponse createOrderPolicy(@Valid @RequestBody OrderPolicyRequest request)
    {
        return orderPolicyService.createOrderPolicy(request);
    }

    @GetMapping
    public List<OrderPolicyResponse> getMyOrderPolicies()
    {
        return orderPolicyService.getMyOrderPolicies();
    }

    @GetMapping("/{id}")
    public OrderPolicyResponse getOrderPolicyById(@PathVariable Long id)
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
    public OrderPolicyResponse updateOrderPolicy(@PathVariable Long id, @Valid @RequestBody OrderPolicyRequest request)
    {
        return orderPolicyService.updateOrderPolicy(id, request);
    }

    @PatchMapping("/{id}")
    public OrderPolicyResponse patchOrderPolicy(@PathVariable Long id, @RequestBody OrderPolicyRequest request)
    {
        return orderPolicyService.patchOrderPolicy(id, request);
    }
}
