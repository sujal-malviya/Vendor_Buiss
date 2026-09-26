package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.EventTypeRequest;
import com.vendorhub.vendor_onboarding.dto.EventTypeResponse;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.EventTypeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    EventTypeService(EventTypeRepository eventTypeRepository)
    {
        this.eventTypeRepository = eventTypeRepository;
    }

    public EventTypeResponse createEventType(EventTypeRequest request)
    {
        EventType eventType = new EventType();
        request.applyTo(eventType);
        return EventTypeResponse.from(eventTypeRepository.save(eventType));
    }

    public PageResponse<EventTypeResponse> getAllEventTypes(Pageable pageable)
    {
        return PageResponse.from(eventTypeRepository.findAll(pageable), EventTypeResponse::from);
    }

    public EventTypeResponse getEventTypeById(Long id)
    {
        return EventTypeResponse.from(findEventType(id));
    }

    public void deleteEventType(Long id)
    {
        eventTypeRepository.delete(findEventType(id));
    }

    public EventTypeResponse updateEventType(Long id, EventTypeRequest request)
    {
        EventType existing = findEventType(id);
        request.applyTo(existing);
        return EventTypeResponse.from(eventTypeRepository.save(existing));
    }

    public EventTypeResponse patchEventType(Long id, EventTypeRequest request)
    {
        EventType existing = findEventType(id);
        request.patch(existing);
        return EventTypeResponse.from(eventTypeRepository.save(existing));
    }

    private EventType findEventType(Long id)
    {
        return eventTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event type", id));
    }
}
