package com.vendorhub.vendor_onboarding.controller;

import java.util.List;
import java.util.Map;

import com.vendorhub.vendor_onboarding.entity.VendorProfile;
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
	Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
		return Map.of(
			"vendorId", jwt.getSubject(),
			"identifier", jwt.getClaimAsString("identifier"),
			"tokenExpiresAt", jwt.getExpiresAt().toString());
	}

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorProfile createProfile(@Valid @RequestBody VendorProfile vendorProfile)
    {
        return vendorProfileService.createProfile(vendorProfile);
    }

    @GetMapping("/profile")
    public List<VendorProfile> getMyProfiles()
    {
        return vendorProfileService.getMyProfiles();
    }

    @GetMapping("/profile/{id}")
    public VendorProfile getProfileById(@PathVariable Long id)
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
    public VendorProfile updateProfile(@PathVariable Long id, @Valid @RequestBody VendorProfile vendorProfile)
    {
        return vendorProfileService.updateProfile(id, vendorProfile);
    }

    @PatchMapping("/profile/{id}")
    public VendorProfile patchProfile(@PathVariable Long id, @RequestBody VendorProfile vendorProfile)
    {
        return vendorProfileService.patchProfile(id, vendorProfile);
    }
}
