package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.UserEntity;

public class CustomUserDetails implements UserDetails {
	
	private UserEntity userEntity;
	
	public CustomUserDetails(UserEntity userEntity) {
		
		this.userEntity = userEntity; // 초기화
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collection = new ArrayList<>();
		
		collection.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				
				return userEntity.getRole();
			}
		});
		
		return collection;
	}

	@Override
	public String getPassword() {

		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {

		return userEntity.getUsername();
	}

	// 이 아래로는 디비에 설정해 주지 않았기 때문에 그냥 다 트루 설정
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
		
		return true;
	}

}
