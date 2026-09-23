package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.service.JwtService;
import com.vendorhub.vendor_onboarding.repository.VendorRepository;
import com.vendorhub.vendor_onboarding.entity.Vendor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	// FIX: uses the top-level VendorRepository.java (the copy nested in this class was removed)
	private final VendorRepository vendorRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthController(
		VendorRepository vendorRepository,
		PasswordEncoder passwordEncoder,
		AuthenticationManager authenticationManager,
		JwtService jwtService) {
		this.vendorRepository = vendorRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@PostMapping("/register")
	ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		// FIX: treat "" the same as missing, so {"email": ""} is rejected too
		String email = isBlank(request.email()) ? null : request.email().trim();
		String phone = isBlank(request.phone()) ? null : request.phone().trim();

		if (email == null && phone == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or phone is required");
		}
		if (email != null && vendorRepository.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
		}
		if (phone != null && vendorRepository.existsByPhone(phone)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone is already registered");
		}

		Vendor vendor = vendorRepository.save(
			new Vendor(email, phone, passwordEncoder.encode(request.password())));
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new AuthResponse(jwtService.createToken(vendor)));
	}

	@PostMapping("/login")
	AuthResponse login(@Valid @RequestBody LoginRequest request) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
				UsernamePasswordAuthenticationToken.unauthenticated(request.identifier().trim(), request.password()));
		}
		catch (AuthenticationException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}
		return new AuthResponse(jwtService.createToken((Vendor) authentication.getPrincipal()));
	}



	private static boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	public record RegisterRequest(
		@Email(message = "email is not valid") String email,
		@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "phone must be 10 to 15 digits") String phone,
		@NotBlank @Size(min = 8, message = "password must be at least 8 characters") String password) {
	}

	public record LoginRequest(@NotBlank String identifier, @NotBlank String password) {
	}

	public record AuthResponse(String token) {
	}
}
