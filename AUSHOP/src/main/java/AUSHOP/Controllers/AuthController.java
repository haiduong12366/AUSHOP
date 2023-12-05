package AUSHOP.Controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AUSHOP.Model.LoginDto;
import AUSHOP.Model.SignUpDto;
import AUSHOP.entity.AppRole;
import AUSHOP.entity.KhachHang;
import AUSHOP.repository.RoleRepository;
import AUSHOP.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signin")
	public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){
		Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
				loginDto.getUsernameOrEmail(), loginDto.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<>("Vo roi em",HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
		
		//kiểm tra username tồn tại chưa
		if (userRepository.existsByUsername(signUpDto.getUsername())) {
			return new ResponseEntity<>("ten tai khoan co r cung",HttpStatus.BAD_REQUEST);
		}
		
		// kiểm tra email
		if (userRepository.existsByEmail(signUpDto.getEmail())) {
			return new ResponseEntity<>("email co r cung",HttpStatus.BAD_REQUEST);
		}
		
		// lưu acc mới
		KhachHang user = new KhachHang();
		user.setHoTen(signUpDto.getName());

		user.setEmail(signUpDto.getEmail());
		user.setEnabled(true);
		user.setPasswd(passwordEncoder.encode(signUpDto.getPassword()));
		
		AppRole roles = roleRepository.findByName("USER").get();
		user.setUserRole(Collections.singleton(roles));
		
		userRepository.save(user);
		
		return new ResponseEntity<>("dang ky duoc r e", HttpStatus.OK);
		
	}
}
