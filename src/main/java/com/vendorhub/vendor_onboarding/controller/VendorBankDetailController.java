package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.BankDetailRequest;
import com.vendorhub.vendor_onboarding.dto.BankDetailResponse;
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
    public BankDetailResponse createBankDetail(@Valid @RequestBody BankDetailRequest request)
    {
        return vendorBankService.createBankDetail(request);
    }

    @GetMapping
    public List<BankDetailResponse> getMyBankDetails()
    {
        return vendorBankService.getMyBankDetails();
    }

    @GetMapping("/{id}")
    public BankDetailResponse getBankDetailById(@PathVariable Long id)
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
    public BankDetailResponse updateBankDetail(@PathVariable Long id, @Valid @RequestBody BankDetailRequest request)
    {
        return vendorBankService.updateBankDetail(id, request);
    }

    // No @Valid: PATCH may send only some fields, the rest arrive as null and are left unchanged
    @PatchMapping("/{id}")
    public BankDetailResponse patchBankDetail(@PathVariable Long id, @RequestBody BankDetailRequest request)
    {
        return vendorBankService.patchBankDetail(id, request);
    }
}
