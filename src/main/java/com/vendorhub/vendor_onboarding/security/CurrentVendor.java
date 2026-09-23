package com.vendorhub.vendor_onboarding.security;

import com.vendorhub.vendor_onboarding.entity.Vendor;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorProfileRepository;
import com.vendorhub.vendor_onboarding.repository.VendorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Tells services who is making the request. The vendor id comes from the "sub" claim of the JWT,
 * which the server signed itself, so unlike the request body it cannot be faked by the client.
 */
@Component
public class CurrentVendor {

    private final VendorRepository vendorRepository;
    private final VendorProfileRepository vendorProfileRepository;

    CurrentVendor(VendorRepository vendorRepository, VendorProfileRepository vendorProfileRepository)
    {
        this.vendorRepository = vendorRepository;
        this.vendorProfileRepository = vendorProfileRepository;
    }

    public Long id()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication)
        {
            return Long.valueOf(jwtAuthentication.getToken().getSubject());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
    }

    public Vendor vendor()
    {
        return vendorRepository.findById(id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vendor account no longer exists"));
    }

    public VendorProfile profile()
    {
        return vendorProfileRepository.findByVendorId(id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "You have no vendor profile yet. Create one with POST /api/vendor/profile first."));
    }
}
