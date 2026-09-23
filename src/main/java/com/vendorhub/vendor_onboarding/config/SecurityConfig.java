package com.vendorhub.vendor_onboarding.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.vendorhub.vendor_onboarding.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	private static final String[] SHARED_LISTS = {
		"/api/dishes/**", "/api/vendor/event-types/**", "/api/vendor/required-items/**"};

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/register", "/api/auth/login", "/error",
					"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				// Shared lists: any logged-in vendor can read them, only admins can change them.
				// SCOPE_ADMIN comes from the "scope" claim that JwtService puts in the token.
				.requestMatchers(HttpMethod.GET, SHARED_LISTS).authenticated()
				.requestMatchers(SHARED_LISTS).hasAuthority("SCOPE_ADMIN")
				// FIX: was denyAll(), which blocked every other API even with a valid token
				.anyRequest().authenticated())
			// FIX: this is what reads "Authorization: Bearer <token>" and checks the token.
			// Without it, the token from login was never checked anywhere.
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
			.build();
	}

	@Bean
	UserDetailsService userDetailsService(VendorRepository vendorRepository) {
		// Login works with either email or phone
		return identifier -> vendorRepository.findByEmail(identifier)
			.or(() -> vendorRepository.findByPhone(identifier))
			.orElseThrow(() -> new UsernameNotFoundException("Vendor not found"));
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// One shared key: the encoder SIGNS tokens with it, the decoder CHECKS them with it
	@Bean
	SecretKey jwtSecretKey(@Value("${jwt.secret}") String secret) {
		return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	}

	@Bean
	JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
		return NimbusJwtEncoder.withSecretKey(jwtSecretKey).build();
	}

	// FIX: new. Needed so incoming tokens can be verified.
	@Bean
	JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
		return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
			.macAlgorithm(MacAlgorithm.HS256)
			.build();
	}
}
