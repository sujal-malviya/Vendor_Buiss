package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.EventTypeRequest;
import com.vendorhub.vendor_onboarding.dto.EventTypeResponse;
import com.vendorhub.vendor_onboarding.service.EventTypeService;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public PageResponse<EventTypeResponse> getAllEventTypes(@ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable)
    {
        return eventTypeService.getAllEventTypes(pageable);
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
