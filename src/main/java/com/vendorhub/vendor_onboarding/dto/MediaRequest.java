package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorMedia;
import jakarta.validation.constraints.NotBlank;

public record MediaRequest(
        @NotBlank(message = "image is required") String image,
        @NotBlank(message = "video is required") String video) {

    public void applyTo(VendorMedia entity)
    {
        entity.setImage(image);
        entity.setVideo(video);
    }

    public void patch(VendorMedia entity)
    {
        if (image != null) entity.setImage(image);
        if (video != null) entity.setVideo(video);
    }
}
