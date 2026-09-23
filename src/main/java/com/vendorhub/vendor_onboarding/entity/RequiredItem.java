package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "required_item")
@Getter
@Setter
public class RequiredItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "name is required")
    private String name;

    private String description;

    private String unit;

    private Boolean active;
}
