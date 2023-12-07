package AUSHOP.services;

import AUSHOP.entity.AppRole;
import AUSHOP.entity.KhachHang;
import AUSHOP.entity.UserRole;
import AUSHOP.repository.AppRoleRepository;
import AUSHOP.repository.KhachHangRepository;
import AUSHOP.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private KhachHangRepository khachHangRepository;

	@Autowired
	private AppRoleRepository appRoleRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<KhachHang> userOpt = khachHangRepository.findByEmail(email.trim());

		if (userOpt.isEmpty()) {
			System.out.println("Email not found! " + email);
			throw new UsernameNotFoundException("Email: " + email + " was not found in the database");
		}

		System.out.println("Found User: " + userOpt.get());

		Optional<UserRole> urole = userRoleRepository
				.findByMaKhachHang(Integer.valueOf(userOpt.get().getMaKhachHang()));

		Optional<AppRole> arole = appRoleRepository.findById(urole.get().getRoleId().getRoleId());

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority(arole.get().getTen());
		grantList.add(authority);

		System.out.println(arole.get().getTen());

		UserDetails userDetails = (UserDetails) new User(userOpt.get().getEmail(), userOpt.get().getPasswd().trim(),
				grantList);

		return userDetails;
	}
}
