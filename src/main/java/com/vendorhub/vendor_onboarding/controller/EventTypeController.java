package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.EventTypeRequest;
import com.vendorhub.vendor_onboarding.dto.EventTypeResponse;
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
    public EventTypeResponse createEventType(@Valid @RequestBody EventTypeRequest request)
    {
        return eventTypeService.createEventType(request);
    }

    @GetMapping
    public List<EventTypeResponse> getAllEventTypes()
    {
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("/{id}")
    public EventTypeResponse getEventTypeById(@PathVariable Long id)
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
    public EventTypeResponse updateEventType(@PathVariable Long id, @Valid @RequestBody EventTypeRequest request)
    {
        return eventTypeService.updateEventType(id, request);
    }

    @PatchMapping("/{id}")
    public EventTypeResponse patchEventType(@PathVariable Long id, @RequestBody EventTypeRequest request)
    {
        return eventTypeService.patchEventType(id, request);
    }
}
