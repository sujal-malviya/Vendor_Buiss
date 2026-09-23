package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;
import com.vendorhub.vendor_onboarding.service.VendorBankService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/bank-details")
public class VendorBankDetailController {

    private final VendorBankService vendorBankService;

    VendorBankDetailController(VendorBankService vendorBankService)
    {
        this.vendorBankService = vendorBankService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorBankDetail createBankDetail(@Valid @RequestBody VendorBankDetail bankDetail)
    {
        return vendorBankService.createBankDetail(bankDetail);
    }

    @GetMapping
    public List<VendorBankDetail> getMyBankDetails()
    {
        return vendorBankService.getMyBankDetails();
    }

    @GetMapping("/{id}")
    public VendorBankDetail getBankDetailById(@PathVariable Long id)
    {
        return vendorBankService.getBankDetailById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBankDetail(@PathVariable Long id)
    {
        vendorBankService.deleteBankDetail(id);
    }

    @PutMapping("/{id}")
    public VendorBankDetail updateBankDetail(@PathVariable Long id, @Valid @RequestBody VendorBankDetail bankDetail)
    {
        return vendorBankService.updateBankDetail(id, bankDetail);
    }

    @PatchMapping("/{id}")
    public VendorBankDetail patchBankDetail(@PathVariable Long id, @RequestBody VendorBankDetail bankDetail)
    {
        return vendorBankService.patchBankDetail(id, bankDetail);
    }
}
