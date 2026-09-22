package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorMedia;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.service.VendorMediaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/media")
public class VendorMediaController {

    private VendorMediaService vendorMediaService;

    VendorMediaController(VendorMediaService vendorMediaService)
    {
        this.vendorMediaService =vendorMediaService;
    }

    @PostMapping
    public VendorMedia createVendorMedia(@Valid @RequestBody VendorMedia vendorMedia)
    {
        return vendorMediaService.createVendorMedia(vendorMedia);
    }

    @GetMapping
    public List<VendorMedia> getAllVendorMedia()
    {

        return vendorMediaService.getAllVendorMedia();
    }

    @GetMapping("/{id}")
    public VendorMedia getVendorProfileMediaById(@PathVariable Long id)
    {
        return vendorMediaService.getVendorProfileMediaById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorMedia(@PathVariable Long id)
    {
        vendorMediaService.deleteVendorMedia(id);
    }

    @PutMapping("/{id}")
    public VendorMedia updateVendorMedia(@PathVariable Long id,@RequestBody VendorMedia vendorMedia)
    {
        return vendorMediaService.updateVendorMedia(id,vendorMedia);
    }

    @PatchMapping("/{id}")
    public VendorMedia updatedVendorMedias(@PathVariable Long id , @RequestBody VendorMedia vendorMedia)
    {
        return vendorMediaService.updatedVendorMedias(id,vendorMedia);
    }
}
