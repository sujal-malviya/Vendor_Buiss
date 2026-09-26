package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.OrderPolicies;

import java.util.ArrayList;
import java.util.List;

public record OrderPolicyRequest(List<String> modifyOrders, Boolean cancelGracePeriod, String maxAllowedDuration) {

    public void applyTo(OrderPolicies entity)
    {
        entity.setModifyOrders(modifyOrders == null ? new ArrayList<>() : new ArrayList<>(modifyOrders));
        entity.setCancelGracePeriod(cancelGracePeriod);
        entity.setMaxAllowedDuration(maxAllowedDuration);
    }

    public void patch(OrderPolicies entity)
    {
        if (modifyOrders != null) entity.setModifyOrders(new ArrayList<>(modifyOrders));
        if (cancelGracePeriod != null) entity.setCancelGracePeriod(cancelGracePeriod);
        if (maxAllowedDuration != null) entity.setMaxAllowedDuration(maxAllowedDuration);
    }
}
