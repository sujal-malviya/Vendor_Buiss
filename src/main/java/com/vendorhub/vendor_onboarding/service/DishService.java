package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.Dish;
import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class DishService {

    private DishRepository dishRepository;

    DishService(DishRepository dishRepository)
    {
        this.dishRepository = dishRepository;
    }


    public Dish createDish( Dish dish)
    {
        return dishRepository.save(dish);
    }


    public List<Dish> getAllDish()
    {
        return dishRepository.findAll();
    }


    public Dish getAllDishById(Long id)
    {
        return dishRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found "+id));
    }


    public void deleteDish( Long id)
    {
        if(!dishRepository.existsById(id))
        {
            throw new RuntimeException("id not found : "+id);
        }
        dishRepository.deleteById(id);
    }


    public Dish updateDish(Long id,Dish dish)
    {
        if(!dishRepository.existsById(id))
        {
            throw new RuntimeException("Id not found : "+id);
        }
        dish.setId(id);
        return dishRepository.save(dish);
    }


    public Dish updateDishes(Long id, Dish dish)
    {
        return dishRepository.findById(id).map(existing->{
            existing.setId(dish.getId());
            existing.setName(dish.getName());
            existing.setActive(dish.getActive());
            existing.setCuisine(dish.getCuisine());
            existing.setDescription(dish.getDescription());
            existing.setCategory(dish.getCategory());

            return dishRepository.save(existing);
        }).orElseThrow(()-> new RuntimeException("Id not found : "+id));

    }
}
