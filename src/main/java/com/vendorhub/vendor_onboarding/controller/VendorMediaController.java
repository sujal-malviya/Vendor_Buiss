package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorMedia;
import com.vendorhub.vendor_onboarding.service.VendorMediaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/media")
public class VendorMediaController {

    private final VendorMediaService vendorMediaService;

    VendorMediaController(VendorMediaService vendorMediaService)
    {
        this.vendorMediaService = vendorMediaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorMedia createMedia(@Valid @RequestBody VendorMedia media)
    {
        return vendorMediaService.createMedia(media);
    }

    @GetMapping
    public List<VendorMedia> getMyMedia()
    {
        return vendorMediaService.getMyMedia();
    }

    @GetMapping("/{id}")
    public VendorMedia getMediaById(@PathVariable Long id)
    {
        return vendorMediaService.getMediaById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedia(@PathVariable Long id)
    {
        vendorMediaService.deleteMedia(id);
    }

    @PutMapping("/{id}")
    public VendorMedia updateMedia(@PathVariable Long id, @Valid @RequestBody VendorMedia media)
    {
        return vendorMediaService.updateMedia(id, media);
    }

    @PatchMapping("/{id}")
    public VendorMedia patchMedia(@PathVariable Long id, @RequestBody VendorMedia media)
    {
        return vendorMediaService.patchMedia(id, media);
    }
}
