package AUSHOP.services;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import AUSHOP.entity.AppRole;
import AUSHOP.entity.KhachHang;
import AUSHOP.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		KhachHang user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(()-> new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswd(), mapRolesToAuthorities(user.getUserRole()));
	}
	
	private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<AppRole> set){
		return set.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}