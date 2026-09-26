package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.OrderPolicies;

import java.util.List;

public record OrderPolicyResponse(Long id, List<String> modifyOrders, Boolean cancelGracePeriod, String maxAllowedDuration) {

    public static OrderPolicyResponse from(OrderPolicies entity)
    {
        return new OrderPolicyResponse(
                entity.getId(),
                List.copyOf(entity.getModifyOrders() == null ? List.of() : entity.getModifyOrders()),
                entity.getCancelGracePeriod(),
                entity.getMaxAllowedDuration());
    }
}
