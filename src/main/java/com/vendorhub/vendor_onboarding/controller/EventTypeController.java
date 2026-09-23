package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.service.EventTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Shared list: every vendor can read it, only admins can change it (see SecurityConfig)
@RestController
@RequestMapping("/api/vendor/event-types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    EventTypeController(EventTypeService eventTypeService)
    {
        this.eventTypeService = eventTypeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventType createEventType(@Valid @RequestBody EventType eventType)
    {
        return eventTypeService.createEventType(eventType);
    }

    @GetMapping
    public List<EventType> getAllEventTypes()
    {
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("/{id}")
    public EventType getEventTypeById(@PathVariable Long id)
    {
        return eventTypeService.getEventTypeById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventType(@PathVariable Long id)
    {
        eventTypeService.deleteEventType(id);
    }

    @PutMapping("/{id}")
    public EventType updateEventType(@PathVariable Long id, @Valid @RequestBody EventType eventType)
    {
        return eventTypeService.updateEventType(id, eventType);
    }

    @PatchMapping("/{id}")
    public EventType patchEventType(@PathVariable Long id, @RequestBody EventType eventType)
    {
        return eventTypeService.patchEventType(id, eventType);
    }
}
