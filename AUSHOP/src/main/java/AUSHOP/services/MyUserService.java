package AUSHOP.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import AUSHOP.entity.AppRole;
import AUSHOP.entity.KhachHang;

public class MyUserService implements UserDetails {

	private static final long serialVersionUID = 1L;
	private KhachHang user;

	public MyUserService(KhachHang user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<AppRole> roles = user.getUserRole();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (AppRole role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPasswd();
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
		// TODO Auto-generated method stub
		return true;
	}


}
