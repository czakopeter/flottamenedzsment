package com.flotta.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.flotta.entity.Role;
import com.flotta.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 3185970362329652822L;

	private User user;

	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if(user.getPasswordRenewerKey() == null) {
		  Set<Role> roles = user.getRoles();
	    for (Role role : roles) {
	      authorities.add(new SimpleGrantedAuthority(role.getRole()));
	    }
		} else {
		  authorities.add(new SimpleGrantedAuthority("PASSWORD"));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}
	
	public String getFullName() {
	  return user.getFullName();
	}
	
	public boolean isPasswordExpired() {
	  return user.isPasswordChangeRequired();
	}
	
	public boolean hasRole(String role) {
	  return user.hasRole(role);
	}
	
	public long getId() {
	  return user.getId();
	}
}
