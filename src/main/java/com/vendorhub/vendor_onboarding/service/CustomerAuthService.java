package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.AuthResponse;
import com.vendorhub.vendor_onboarding.dto.CustomerLoginRequest;
import com.vendorhub.vendor_onboarding.dto.CustomerRegisterRequest;
import com.vendorhub.vendor_onboarding.dto.CustomerResponse;
import com.vendorhub.vendor_onboarding.entity.Customer;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerAuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    CustomerAuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtService jwtService)
    {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(CustomerRegisterRequest request)
    {
        String email = request.email().trim().toLowerCase();
        String phoneNumber = request.phoneNumber().trim();

        if (customerRepository.existsByEmail(email))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        if (customerRepository.existsByPhoneNumber(phoneNumber))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is already registered");
        }

        Customer customer = new Customer();
        customer.setName(request.name().trim());
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setPassword(passwordEncoder.encode(request.password()));

        // Return a token straight away, like vendor registration, so the customer is logged in
        return new AuthResponse(jwtService.createToken(customerRepository.save(customer)));
    }

    // We check the password here instead of using Spring's AuthenticationManager.
    // The AuthenticationManager only knows the vendor UserDetailsService; adding a second
    // UserDetailsService bean would make Spring use neither, and vendor login would break too.
    public AuthResponse login(CustomerLoginRequest request)
    {
        String identifier = request.identifier().trim();
        Customer customer = customerRepository.findByEmail(identifier.toLowerCase())
                .or(() -> customerRepository.findByPhoneNumber(identifier))
                .filter(found -> passwordEncoder.matches(request.password(), found.getPassword()))
                // Same message for "no such customer" and "wrong password", so attackers can't discover accounts
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        return new AuthResponse(jwtService.createToken(customer));
    }

    public CustomerResponse getCustomer(Long id)
    {
        return customerRepository.findById(id)
                .map(CustomerResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }
}
