package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.exception.VendorNotFoundException;
import com.vendorhub.vendor_onboarding.repository.EventTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class EventTypeService {

    private EventTypeRepository eventTypeRepository;

    EventTypeService(EventTypeRepository eventTypeRepository)
    {
        this.eventTypeRepository =eventTypeRepository;
    }


    public EventType createEvent( EventType eventType)
    {
        return eventTypeRepository.save(eventType);
    }


    public List<EventType> getAllEvent()
    {
        return eventTypeRepository.findAll();
    }


    public EventType getAllEventById( Long id)
    {
        return eventTypeRepository.findById(id).orElseThrow(()-> new VendorNotFoundException(id));
    }


    public void deleteEventById( Long id)
    {
        if(!eventTypeRepository.existsById(id))
        {
            throw new VendorNotFoundException(id);
        }
        eventTypeRepository.deleteById(id);
    }


    public EventType updateEvent( Long id, EventType eventType)
    {
        if(!eventTypeRepository.existsById(id))
        {
            throw new VendorNotFoundException(id);
        }
        eventType.setId(id);
        return eventTypeRepository.save(eventType);
    }


    public EventType updateEvents(Long id,EventType eventType)
    {
        return eventTypeRepository.findById(id).map(existing->{
            existing.setName(eventType.getName());
            existing.setDescription(eventType.getDescription());
            existing.setActive(eventType.getActive());

            return eventTypeRepository.save(existing);
        }).orElseThrow(()->new VendorNotFoundException(id));
    }
}
