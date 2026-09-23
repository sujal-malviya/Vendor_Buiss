package com.vendorhub.vendor_onboarding.service;

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

    public OrderPolicies createOrderPolicy(OrderPolicies orderPolicies)
    {
        VendorProfile profile = currentVendor.profile();
        if (orderPoliciesRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order policies already exist. Use PUT or PATCH to change them.");
        }
        orderPolicies.setId(null);
        orderPolicies.setVendorProfile(profile);
        return orderPoliciesRepository.save(orderPolicies);
    }

    public List<OrderPolicies> getMyOrderPolicies()
    {
        return orderPoliciesRepository.findByVendorProfileVendorId(currentVendor.id());
    }

    public OrderPolicies getOrderPolicyById(Long id)
    {
        return orderPoliciesRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Order policy", id));
    }

    public void deleteOrderPolicy(Long id)
    {
        orderPoliciesRepository.delete(getOrderPolicyById(id));
    }

    public OrderPolicies updateOrderPolicy(Long id, OrderPolicies orderPolicies)
    {
        OrderPolicies existing = getOrderPolicyById(id);
        orderPolicies.setId(existing.getId());
        orderPolicies.setVendorProfile(existing.getVendorProfile());
        return orderPoliciesRepository.save(orderPolicies);
    }

    public OrderPolicies patchOrderPolicy(Long id, OrderPolicies orderPolicies)
    {
        OrderPolicies existing = getOrderPolicyById(id);
        if (orderPolicies.getModifyOrders() != null) existing.setModifyOrders(orderPolicies.getModifyOrders());
        if (orderPolicies.getCancelGracePeriod() != null) existing.setCancelGracePeriod(orderPolicies.getCancelGracePeriod());
        if (orderPolicies.getMaxAllowedDuration() != null) existing.setMaxAllowedDuration(orderPolicies.getMaxAllowedDuration());
        return orderPoliciesRepository.save(existing);
    }
}
