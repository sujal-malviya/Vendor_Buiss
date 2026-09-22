package com.vendorhub.vendor_onboarding.controller;

import java.util.List;
import java.util.Map;

import com.vendorhub.vendor_onboarding.entity.Vendor;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.service.Vendorservice;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// NEW: a protected API to test your token. Without a valid token it returns 401.
@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private Vendorservice vendorservice;
    VendorController(Vendorservice vendorservice)
    {
        this.vendorservice=vendorservice;
    }

	@GetMapping("/me")
	Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
		return Map.of(
			"vendorId", jwt.getSubject(),
			"identifier", jwt.getClaimAsString("identifier"),
			"tokenExpiresAt", jwt.getExpiresAt().toString());
	}

    @PostMapping("/profile")
    public VendorProfile createVendor(@Valid @RequestBody VendorProfile vendorprofile)
    {
        return vendorservice.createVendor(vendorprofile);
    }

    @GetMapping("/profile")
    public List<VendorProfile> getAllVendor()
    {
        return vendorservice.getAllVendor();
    }

    @GetMapping("/profile/{id}")
    public VendorProfile getVendorProfileById(@PathVariable Long id)
    {
        return vendorservice.getVendorProfileById(id);
    }

    @DeleteMapping("/profile/{id}")
    public void deleteVendorProfileById(@PathVariable Long id)
    {
        vendorservice.deleteVendorProfileById(id);
    }

    @PutMapping("/profile/{id}")
    public VendorProfile updateVendorProfile(@PathVariable Long id,@RequestBody VendorProfile vendorProfile)
    {
        return vendorservice.updateVendorProfile(id,vendorProfile);
    }

    @PatchMapping("profile/{id}")
    public VendorProfile updatedVendorProfile(@PathVariable Long id , @RequestBody VendorProfile vendorProfile)
    {
        return vendorservice.updatedVendorProfile(id,vendorProfile);
    }

}
