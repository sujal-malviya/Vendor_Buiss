package com.vendorhub.vendor_onboarding.controller;

import java.util.List;

import com.vendorhub.vendor_onboarding.dto.MeResponse;
import com.vendorhub.vendor_onboarding.dto.VendorProfileRequest;
import com.vendorhub.vendor_onboarding.dto.VendorProfileResponse;
import com.vendorhub.vendor_onboarding.service.VendorProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private final VendorProfileService vendorProfileService;

    VendorController(VendorProfileService vendorProfileService)
    {
        this.vendorProfileService = vendorProfileService;
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal Jwt jwt)
    {
        return MeResponse.from(jwt);
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorProfileResponse createProfile(@Valid @RequestBody VendorProfileRequest request)
    {
        return vendorProfileService.createProfile(request);
    }

    @GetMapping("/profile")
    public List<VendorProfileResponse> getMyProfiles()
    {
        return vendorProfileService.getMyProfiles();
    }

    @GetMapping("/profile/{id}")
    public VendorProfileResponse getProfileById(@PathVariable Long id)
    {
        return vendorProfileService.getProfileById(id);
    }

    @DeleteMapping("/profile/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable Long id)
    {
        vendorProfileService.deleteProfile(id);
    }

    @PutMapping("/profile/{id}")
    public VendorProfileResponse updateProfile(@PathVariable Long id, @Valid @RequestBody VendorProfileRequest request)
    {
        return vendorProfileService.updateProfile(id, request);
    }

    @PatchMapping("/profile/{id}")
    public VendorProfileResponse patchProfile(@PathVariable Long id, @RequestBody VendorProfileRequest request)
    {
        return vendorProfileService.patchProfile(id, request);
    }
}
