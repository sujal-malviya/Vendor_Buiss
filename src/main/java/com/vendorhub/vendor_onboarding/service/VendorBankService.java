package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;
import com.vendorhub.vendor_onboarding.repository.VendorBankRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class VendorBankService {

    private VendorBankRepository vendorBankRepository;
    VendorBankService(VendorBankRepository vendorBankRepository)
    {
        this.vendorBankRepository=vendorBankRepository;

    }

    public VendorBankDetail createVendorBankDetail(VendorBankDetail vendorBankDetail)
    {
        return vendorBankRepository.save(vendorBankDetail);
    }

    public List<VendorBankDetail> getVendorBankDetails()
    {
        return  vendorBankRepository.findAll();
    }

    public VendorBankDetail getVendorBankDetailById( Long id)
    {
        return  vendorBankRepository.findById(id).orElseThrow(()-> new RuntimeException("id not found : "+id));
    }

    public void deleteVendorBankDetail( Long id)
    {
        if (!vendorBankRepository.existsById(id))
        {
            throw new RuntimeException("id not found : "+id);
        }
        vendorBankRepository.deleteById(id);
    }

    public VendorBankDetail updateBankDetail(Long id ,VendorBankDetail vendorBankDetail)
    {
        if(!vendorBankRepository.existsById(id))
        {
            throw new RuntimeException("id not found : "+id);
        }
        vendorBankDetail.setId(id);
        return vendorBankRepository.save(vendorBankDetail);
    }

    public VendorBankDetail updateBankDetails( Long id , VendorBankDetail vendorBankDetail)
    {

        return vendorBankRepository.findById(id).map(existing ->
        {
            existing.setIFSC_Code(vendorBankDetail.getIFSC_Code());
            existing.setVendorProfile(vendorBankDetail.getVendorProfile());
            existing.setAccountNumber(vendorBankDetail.getAccountNumber());
            existing.setAccountHolderName(vendorBankDetail.getAccountHolderName());

            return vendorBankRepository.save(existing);
        }
        ).orElseThrow(()-> new RuntimeException("id not found : "+id));
    }
}
