package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.PackageDish;
import com.vendorhub.vendor_onboarding.repository.PackageDishRepository;
import com.vendorhub.vendor_onboarding.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class PackageDishService {

    private PackageDishRepository packageDishRepository;

    PackageDishService(PackageDishRepository packageDishRepository)
    {
        this.packageDishRepository=packageDishRepository;
    }


    public PackageDish createPackageDish( PackageDish packageDish)
    {
        return packageDishRepository.save(packageDish);
    }


    public List<PackageDish> getAllPackageDish()
    {
        return packageDishRepository.findAll();
    }


    public PackageDish getAllPackageDishById( Long id)
    {
        return packageDishRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found"));
    }


    public void deletePackageDish(Long id)
    {
        if(!packageDishRepository.existsById(id))
        {
            throw new RuntimeException("Id not found : "+id);
        }
        packageDishRepository.deleteById(id);
    }


    public PackageDish updatePackageDish(Long id , PackageDish packageDish)
    {
        if(!packageDishRepository.existsById(id))
        {
            throw new RuntimeException("Id not found : "+id);
        }
        packageDish.setId(id);
        return packageDishRepository.save(packageDish);
    }


    public PackageDish updatePackageDishes(@PathVariable Long id,@RequestBody PackageDish packageDish)
    {
        return packageDishRepository.findById(id).map(existing->{
            existing.setId(packageDish.getId());
            existing.setDish(packageDish.getDish());
            existing.setPackageEntity(packageDish.getPackageEntity());
            existing.setQuantity(packageDish.getQuantity());

            return packageDishRepository.save(existing);
        }).orElseThrow(()->new RuntimeException("Id not found : "+id));
    }
}
