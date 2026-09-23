package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.service.EventTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/event-types")
public class EventTypeController {

    private EventTypeService eventTypeService;

    EventTypeController(EventTypeService eventTypeService)
    {
        this.eventTypeService = eventTypeService;
    }

    @PostMapping
    public EventType createEvent(@RequestBody EventType eventType)
    {
        return eventTypeService.createEvent(eventType);
    }

    @GetMapping
    public List<EventType> getAllEvent()
    {
        return eventTypeService.getAllEvent();
    }

    @GetMapping("/{id}")
    public EventType getAllEventById(@PathVariable Long id)
    {
        return eventTypeService.getAllEventById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEventById(@PathVariable Long id)
    {
         eventTypeService.deleteEventById(id);
    }

    @PutMapping("/{id}")
    public EventType updateEvent(@PathVariable Long id,@RequestBody EventType eventType)
    {
        return eventTypeService.updateEvent(id,eventType);
    }

    @PatchMapping("/{id}")
    public EventType updateEvents(@PathVariable Long id,@RequestBody EventType eventType)
    {
        return eventTypeService.updateEvents(id,eventType);
    }




}
