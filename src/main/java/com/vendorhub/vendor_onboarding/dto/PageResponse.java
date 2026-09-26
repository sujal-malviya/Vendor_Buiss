package com.vendorhub.vendor_onboarding.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * One page of a list, e.g. GET /api/dishes?page=0&size=20&sort=name
 *
 * {
 *   "content": [ ...items... ],
 *   "page": 0, "size": 20, "totalElements": 57, "totalPages": 3
 * }
 *
 * page starts at 0. We return this small record instead of Spring's Page class,
 * so the JSON stays the same even if Spring changes how Page is serialized.
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <E, T> PageResponse<T> from(Page<E> page, Function<E, T> mapper)
    {
        return new PageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
