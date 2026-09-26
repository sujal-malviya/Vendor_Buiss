package com.vendorhub.vendor_onboarding.service;

import java.time.Instant;

import com.vendorhub.vendor_onboarding.entity.Customer;
import com.vendorhub.vendor_onboarding.entity.Vendor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final JwtEncoder jwtEncoder;
	private final long expirationSeconds;

	public JwtService(
		JwtEncoder jwtEncoder,
		@Value("${jwt.expiration}") long expirationSeconds) {
		this.jwtEncoder = jwtEncoder;
		this.expirationSeconds = expirationSeconds;
	}

	public String createToken(Vendor vendor) {
		// Spring turns "scope": "ADMIN" into the authority SCOPE_ADMIN, which SecurityConfig checks
		return createToken(vendor.getId(), vendor.getUsername(), vendor.getRole().name());
	}

	// Customer and vendor ids both start at 1, so the "scope" claim is what tells them apart.
	// SecurityConfig only lets SCOPE_CUSTOMER tokens into /api/customer/** and never into /api/vendor/**.
	public String createToken(Customer customer) {
		return createToken(customer.getId(), customer.getEmail(), "CUSTOMER");
	}

	private String createToken(Long subjectId, String identifier, String scope) {
		Instant issuedAt = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
			.subject(subjectId.toString())
			.claim("identifier", identifier)
			.claim("scope", scope)
			.issuedAt(issuedAt)
			.expiresAt(issuedAt.plusSeconds(expirationSeconds))
			.build();

		// FIX: state the algorithm (HS256) so it matches the secret key used by the encoder and decoder
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}
}
