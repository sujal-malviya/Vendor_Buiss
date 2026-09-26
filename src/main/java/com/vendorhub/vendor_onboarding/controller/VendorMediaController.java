package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.MediaRequest;
import com.vendorhub.vendor_onboarding.dto.MediaResponse;
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
    public MediaResponse createMedia(@Valid @RequestBody MediaRequest request)
    {
        return vendorMediaService.createMedia(request);
    }

    @GetMapping
    public List<MediaResponse> getMyMedia()
    {
        return vendorMediaService.getMyMedia();
    }

    @GetMapping("/{id}")
    public MediaResponse getMediaById(@PathVariable Long id)
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
    public MediaResponse updateMedia(@PathVariable Long id, @Valid @RequestBody MediaRequest request)
    {
        return vendorMediaService.updateMedia(id, request);
    }

    @PatchMapping("/{id}")
    public MediaResponse patchMedia(@PathVariable Long id, @RequestBody MediaRequest request)
    {
        return vendorMediaService.patchMedia(id, request);
    }
}
