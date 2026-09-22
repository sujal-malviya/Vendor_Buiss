package com.vendorhub.vendor_onboarding.service;

import java.time.Instant;

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
		Instant issuedAt = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
			.subject(vendor.getId().toString())
			.claim("identifier", vendor.getUsername())
			.issuedAt(issuedAt)
			.expiresAt(issuedAt.plusSeconds(expirationSeconds))
			.build();

		// FIX: state the algorithm (HS256) so it matches the secret key used by the encoder and decoder
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}
}
