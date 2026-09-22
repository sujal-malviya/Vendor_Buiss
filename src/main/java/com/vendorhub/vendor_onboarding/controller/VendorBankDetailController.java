package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;
import com.vendorhub.vendor_onboarding.repository.VendorBankRepository;
import com.vendorhub.vendor_onboarding.service.VendorBankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/bank-detail")
public class VendorBankDetailController {

    private VendorBankService vendorBankService;
    VendorBankDetailController(VendorBankService vendorBankService)
    {
        this.vendorBankService=vendorBankService;
    }
    @PostMapping
    public VendorBankDetail createVendorBankDetail(@RequestBody VendorBankDetail vendorBankDetail)
    {
        return vendorBankService.createVendorBankDetail(vendorBankDetail);
    }

    @GetMapping
    public List<VendorBankDetail> getVendorBankDetails()
    {
        return  vendorBankService.getVendorBankDetails();
    }

    @GetMapping("/{id}")
    public VendorBankDetail getVendorBankDetailById(@PathVariable Long id)
    {
        return  vendorBankService.getVendorBankDetailById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorBankDetail(@PathVariable Long id)
    {
        vendorBankService.deleteVendorBankDetail(id);
    }

    @PutMapping("/{id}")
    public VendorBankDetail updateBankDetail(@PathVariable Long id ,@RequestBody VendorBankDetail vendorBankDetail)
    {
        return vendorBankService.updateBankDetail(id,vendorBankDetail);
    }

    @PatchMapping("/{id}")
    public VendorBankDetail updateBankDetails(@PathVariable Long id , @RequestBody VendorBankDetail vendorBankDetail)
    {
        return vendorBankService.updateBankDetails(id,vendorBankDetail);
    }
}
