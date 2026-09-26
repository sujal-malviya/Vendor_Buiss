package com.vendorhub.vendor_onboarding.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// A plain entity: customers log in through CustomerAuthService (not Spring's UserDetailsService),
// so this class does not need to implement UserDetails.
@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "email is required")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "phone number is required")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    // Always a BCrypt hash, never the plain password
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;
}
