package com.vendorhub.vendor_onboarding.entity;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "vendors")
public class Vendor implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String phone;

	private String password;

	// Accounts created before this column existed have null here; getRole() treats that as VENDOR.
	@Enumerated(EnumType.STRING)
	private Role role;

	protected Vendor() {
	}

	public Vendor(String email, String phone, String password) {
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = Role.VENDOR;
	}

	public Long getId() {
		return id;
	}

	public Role getRole() {
		return role != null ? role : Role.VENDOR;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email != null ? email : phone;
	}
}
