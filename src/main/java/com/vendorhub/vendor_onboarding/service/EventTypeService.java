package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.EventTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    EventTypeService(EventTypeRepository eventTypeRepository)
    {
        this.eventTypeRepository = eventTypeRepository;
    }

    public EventType createEventType(EventType eventType)
    {
        eventType.setId(null);
        return eventTypeRepository.save(eventType);
    }

    public List<EventType> getAllEventTypes()
    {
        return eventTypeRepository.findAll();
    }

    public EventType getEventTypeById(Long id)
    {
        return eventTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event type", id));
    }

    public void deleteEventType(Long id)
    {
        eventTypeRepository.delete(getEventTypeById(id));
    }

    public EventType updateEventType(Long id, EventType eventType)
    {
        getEventTypeById(id);
        eventType.setId(id);
        return eventTypeRepository.save(eventType);
    }

    public EventType patchEventType(Long id, EventType eventType)
    {
        EventType existing = getEventTypeById(id);
        if (eventType.getName() != null) existing.setName(eventType.getName());
        if (eventType.getDescription() != null) existing.setDescription(eventType.getDescription());
        if (eventType.getActive() != null) existing.setActive(eventType.getActive());
        return eventTypeRepository.save(existing);
    }
}
