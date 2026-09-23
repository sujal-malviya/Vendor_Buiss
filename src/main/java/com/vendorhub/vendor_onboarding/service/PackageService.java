package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.Package;
import com.vendorhub.vendor_onboarding.repository.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Service
public class PackageService {

    private PackageRepository packageRepository;

    PackageService(PackageRepository packageRepository)
    {
        this.packageRepository = packageRepository;
    }


    public Package createPackage(Package packagee)
    {
        return packageRepository.save(packagee);
    }


    public List<Package> getAllPackage()
    {
        return packageRepository.findAll();
    }


    public Package getAllPackageById(Long id)
    {
        return packageRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found : "+id));
    }


    public void deletePackage(Long id)
    {
        if(!packageRepository.existsById(id))
        {
            throw new RuntimeException("Id not found : "+id);
        }
        packageRepository.deleteById(id);
    }


    public Package updatePackage( Long id, Package packagee)
    {
        if(!packageRepository.existsById(id))
        {
            throw new RuntimeException("Id not found : "+id);
        }
        packagee.setId(id);
        return packageRepository.save(packagee);
    }


    public Package updatePackages(Long id,  Package packagee)
    {
        return packageRepository.findById(id).map(existing->{
            existing.setId(packagee.getId());
            existing.setName(packagee.getName());
            existing.setPrice(packagee.getPrice());
            existing.setActive(packagee.getActive());
            existing.setVendor(packagee.getVendor());
            existing.setDescription(packagee.getDescription());
            existing.setPriceUnit(packagee.getPriceUnit());

            return packageRepository.save(existing);
        }).orElseThrow(()->new RuntimeException("Id not found : "+id));
    }
}
