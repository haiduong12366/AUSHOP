package AUSHOP.oauth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import AUSHOP.entity.AppRole;
import AUSHOP.entity.KhachHang;
import AUSHOP.entity.UserRole;
import AUSHOP.repository.KhachHangRepository;
import AUSHOP.repository.RoleRepository;
import AUSHOP.repository.UserRoleRepository;


@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	KhachHangRepository customerRepository;
	
	@Autowired
	RoleRepository appRoleRepository;
	
	@Autowired
	UserRoleRepository	 userRoleRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
		String email = oauth2User.getName();
		System.out.println(email);
		Optional<KhachHang> cus = customerRepository.findByEmail(email);
		if(cus.isEmpty()) {
			KhachHang c = new KhachHang();
			c.setHinhanhKH("user.png");
			c.setHoTen(oauth2User.getNameReal());
			c.setEmail(email);
			c.setGioiTinh(true);
			c.setPasswd(bCryptPasswordEncoder.encode("123@ABCxyzalualua"));
			c.setDiaChi("Chưa có");
			c.setSdt("");
			customerRepository.save(c);
			Optional<AppRole> a = appRoleRepository.findById(2);
			UserRole urole = new UserRole(0, c, a.get());
			userRoleRepository.save(urole);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
