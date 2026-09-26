package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.OrderPolicyRequest;
import com.vendorhub.vendor_onboarding.dto.OrderPolicyResponse;
import com.vendorhub.vendor_onboarding.entity.OrderPolicies;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.OrderPoliciesRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderPolicyService {

    private final OrderPoliciesRepository orderPoliciesRepository;
    private final CurrentVendor currentVendor;

    OrderPolicyService(OrderPoliciesRepository orderPoliciesRepository, CurrentVendor currentVendor)
    {
        this.orderPoliciesRepository = orderPoliciesRepository;
        this.currentVendor = currentVendor;
    }

    public OrderPolicyResponse createOrderPolicy(OrderPolicyRequest request)
    {
        VendorProfile profile = currentVendor.profile();
        if (orderPoliciesRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order policies already exist. Use PUT or PATCH to change them.");
        }
        OrderPolicies orderPolicies = new OrderPolicies();
        request.applyTo(orderPolicies);
        orderPolicies.setVendorProfile(profile);
        return OrderPolicyResponse.from(orderPoliciesRepository.save(orderPolicies));
    }

    public List<OrderPolicyResponse> getMyOrderPolicies()
    {
        return orderPoliciesRepository.findByVendorProfileVendorId(currentVendor.id()).stream()
                .map(OrderPolicyResponse::from)
                .toList();
    }

    public OrderPolicyResponse getOrderPolicyById(Long id)
    {
        return OrderPolicyResponse.from(findOwned(id));
    }

    public void deleteOrderPolicy(Long id)
    {
        orderPoliciesRepository.delete(findOwned(id));
    }

    public OrderPolicyResponse updateOrderPolicy(Long id, OrderPolicyRequest request)
    {
        OrderPolicies existing = findOwned(id);
        request.applyTo(existing);
        return OrderPolicyResponse.from(orderPoliciesRepository.save(existing));
    }

    public OrderPolicyResponse patchOrderPolicy(Long id, OrderPolicyRequest request)
    {
        OrderPolicies existing = findOwned(id);
        request.patch(existing);
        return OrderPolicyResponse.from(orderPoliciesRepository.save(existing));
    }

    private OrderPolicies findOwned(Long id)
    {
        return orderPoliciesRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Order policy", id));
    }
}
