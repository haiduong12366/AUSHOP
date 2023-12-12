package AUSHOP.Controllers;

import AUSHOP.Model.CartItem;
import AUSHOP.Model.ChangePasswordModel;
import AUSHOP.Model.KhachHangModel;
import AUSHOP.entity.AppRole;
import AUSHOP.entity.ChiTietDonHang;
import AUSHOP.entity.DonHang;
import AUSHOP.entity.KhachHang;
import AUSHOP.entity.SanPham;
import AUSHOP.entity.UserRole;
import AUSHOP.repository.AppRoleRepository;
import AUSHOP.repository.ChiTietDonHangRepository;
import AUSHOP.repository.DonHangRepository;
import AUSHOP.repository.KhachHangRepository;
import AUSHOP.repository.SanPhamRepository;
import AUSHOP.repository.UserRoleRepository;
import AUSHOP.services.SendMailService;
import AUSHOP.services.ShoppingCartService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerSiteController {
	
	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	KhachHangRepository khachHangRepository;
	
	@Autowired
	SanPhamRepository sanPhamRepository;

	@Autowired
	AppRoleRepository appRoleRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	DonHangRepository donHangRepository;
	
	@Autowired
	ChiTietDonHangRepository chiTietDonHangRepository;
	
	@Autowired
	SendMailService sendMailService;

	@Autowired
	HttpSession session;

	@GetMapping("/login")
	public ModelAndView loginForm(ModelMap model, @RequestParam("error") Optional<String> error) {
		String errorString = error.orElse("false");
		if (errorString.equals("true")) {
			model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
		}
		return new ModelAndView("/site/login", model);
	}

	@RequestMapping("/logout")
	public String login() {

		return "redirect:/home";
	}

	@GetMapping("/register")
	public ModelAndView registerForm(ModelMap model) {
		model.addAttribute("KhachHang", new KhachHangModel());
		return new ModelAndView("/site/register", model);
	}

	@PostMapping("/register")
	public String register(ModelMap model, @Valid @ModelAttribute("KhachHang") KhachHangModel dto, BindingResult result,
						   @RequestParam("passwd") String passwd) {
		if (result.hasErrors()) {
			return "/site/register";
		}
		if (!checkEmail(dto.getEmail())) {
			model.addAttribute("error", "Email này đã được sử dụng!");
			return "/site/register";
		}
		session.removeAttribute("otp");
		int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
		session.setAttribute("otp", random_otp);
		String body = "<div>\r\n" + "        <h3>Mã OTP của bạn là: <span style=\"color:red; font-weight: bold;\">"
				+ random_otp + "</span></h3>\r\n" + "    </div>";
		sendMailService.queue(dto.getEmail(), "Đăng kí tài khoản", body);

		model.addAttribute("KhachHang", dto);
		model.addAttribute("message", "Mã OTP đã được gửi tới Email, hãy kiểm tra Email của bạn!");

		return "/site/confirmOtpRegister";
	}

	@PostMapping("/confirmOtpRegister")
	public ModelAndView confirmRegister(ModelMap model, @ModelAttribute("KhachHang") KhachHangModel dto,
										@RequestParam("passwd") String passwd, @RequestParam("otp") String otp) {
		if (otp.equals(String.valueOf(session.getAttribute("otp")))) {
			dto.setPasswd(bCryptPasswordEncoder.encode(passwd));
			KhachHang kh = new KhachHang();
			BeanUtils.copyProperties(dto, kh);
			kh.setNgayDangKy(new Date());
			kh.setIs_admin(false);
			kh.setHinhanhKH("user.png");
			khachHangRepository.save(kh);
			Optional<AppRole> a = appRoleRepository.findById(2);
			UserRole urole = new UserRole(0, kh, a.get());
			userRoleRepository.save(urole);

			session.removeAttribute("otp");
			model.addAttribute("message", "Đăng kí thành công");
			return new ModelAndView("/site/login");
		}

		model.addAttribute("KhachHang", dto);
		model.addAttribute("error", "Mã OTP không đúng, hãy thử lại!");
		return new ModelAndView("/site/confirmOtpRegister", model);
	}

	@GetMapping("/forgotPassword")
	public ModelAndView forgotFrom() {
		return new ModelAndView("/site/forgotPassword");
	}

	@PostMapping("/forgotPassword")
	public ModelAndView forgotPassowrd(ModelMap model, @RequestParam("email") String email) {
		List<KhachHang> listKh = khachHangRepository.findAll();
		for (KhachHang kh : listKh) {
			if (email.trim().equals(kh.getEmail())) {
				session.removeAttribute("otp");
				int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
				session.setAttribute("otp", random_otp);
				String body = "<div>\r\n"
						+ "        <h3>Mã OTP của bạn là: <span style=\"color:red; font-weight: bold;\">" + random_otp
						+ "</span></h3>\r\n" + "    </div>";
				sendMailService.queue(email, "Quên mật khẩu?", body);

				model.addAttribute("email", email);
				model.addAttribute("message", "Mã OTP đã được gửi tới Email, hãy kiểm tra Email của bạn!");
				return new ModelAndView("/site/confirmOtp", model);
			}
		}
		model.addAttribute("error", "Email này không tồn tại trong hệ thống!");
		return new ModelAndView("/site/forgotPassword", model);
	}

	@PostMapping("/confirmOtp")
	public ModelAndView confirm(ModelMap model, @RequestParam("otp") String otp, @RequestParam("email") String email) {
		if (otp.equals(String.valueOf(session.getAttribute("otp")))) {
			model.addAttribute("email", email);
			model.addAttribute("newPasswd", "");
			model.addAttribute("confirmPasswd", "");
			model.addAttribute("changePassword", new ChangePasswordModel());
			return new ModelAndView("/site/changePassword", model);
		}
		model.addAttribute("error", "Mã OTP không trùng, vui lòng kiểm tra lại!");
		return new ModelAndView("/site/confirmOtp", model);
	}

	@PostMapping("/changePassword")
	public ModelAndView changeForm(ModelMap model,
								   @Valid @ModelAttribute("changePassword") ChangePasswordModel changePassword, BindingResult result,
								   @RequestParam("email") String email, @RequestParam("newPasswd") String newPasswd,
								   @RequestParam("confirmPasswd") String confirmPasswd) {
		if (result.hasErrors()) {

			model.addAttribute("newPasswd", newPasswd);
			model.addAttribute("newPasswd", confirmPasswd);
//			model.addAttribute("changePasswd", changePasswd);
			model.addAttribute("email", email);
			return new ModelAndView("/site/changePassword", model);
		}

		if (!changePassword.getNewPasswd().equals(changePassword.getConfirmPasswd())) {

			model.addAttribute("newPasswd", newPasswd);
			model.addAttribute("newPasswd", confirmPasswd);
//			model.addAttribute("changePasswd", changePasswd);
			model.addAttribute("error", "error");
			model.addAttribute("email", email);
			return new ModelAndView("/site/changePassword", model);
		}
		KhachHang kh = khachHangRepository.findByEmail(email).get();
		kh.setPasswd(bCryptPasswordEncoder.encode(newPasswd));
		khachHangRepository.save(kh);
		model.addAttribute("message", "Đổi mật khẩu thành công!");
		model.addAttribute("email", "");
		session.removeAttribute("otp");
		return new ModelAndView("/site/changePassword", model);
	}

	@GetMapping("/customer/editProfile")
	public ModelAndView editForm(ModelMap model, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> kh = khachHangRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(kh.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/KhachHang", model);
			}
		}

		model.addAttribute("user", khachHangRepository.findByEmail(principal.getName()).get());

		// model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return new ModelAndView("/khachhang/edit");
	}

	@PostMapping("/customer/editProfile")
	public ModelAndView edit(ModelMap model, @Valid @ModelAttribute("KhachHang") KhachHangModel dto,
							 BindingResult result, @RequestParam("photo") MultipartFile photo, Principal principal) throws IOException {
		/*
		 * if (result.hasErrors()) { model.addAttribute("totalCartItems",
		 * shoppingCartService.getCount()); return new ModelAndView("/site/editProfile",
		 * model); }
		 */
		KhachHang kh = khachHangRepository.findByEmail(principal.getName()).get();
		if (!photo.getOriginalFilename().equals("")) {
			upload(photo, "uploads/customers");
			kh.setHinhanhKH(photo.getOriginalFilename());
		}
		kh.setHoTen(dto.getHoTen());
		kh.setGioiTinh(dto.isGioiTinh());
		kh.setSdt(dto.getSdt());
		kh.setDiaChi(dto.getDiaChi());

		khachHangRepository.save(kh);

		return new ModelAndView("forward:/khachhang/thongtin");
	}

	@RequestMapping("/customer/info")
	public ModelAndView info(ModelMap model, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> kh = khachHangRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(kh.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		Optional<KhachHang> c = khachHangRepository.findByEmail(principal.getName());

		Page<DonHang> listO0 = donHangRepository.findByMaKhachHang1(c.get().getMaKhachHang(), 0,
				PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "maDH")));
		model.addAttribute("orders0", listO0);

		Page<DonHang> listO1 = donHangRepository.findByMaKhachHang1(c.get().getMaKhachHang(), 1,
				PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "maDH")));
		model.addAttribute("orders1", listO1);

		Page<DonHang> listO2 = donHangRepository.findByMaKhachHang1(c.get().getMaKhachHang(), 2,
				PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "maDH")));
		model.addAttribute("orders2", listO2);

		Page<DonHang> listO3 = donHangRepository.findByMaKhachHang1(c.get().getMaKhachHang(), 3,
				PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "maDH")));
		model.addAttribute("orders3", listO3);

		model.addAttribute("user", c.get());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return new ModelAndView("/site/infomation", model);
	}

//	@GetMapping("/customer/changePassword")
//	public ModelAndView changeForm(ModelMap model, Principal principal) {
//
//		boolean isLogin = false;
//		if (principal != null) {
//			isLogin = true;
//		}
//		model.addAttribute("isLogin", isLogin);
//
//		model.addAttribute("email", principal.getName());
//		model.addAttribute("totalCartItems", shoppingCartService.getCount());
//		return new ModelAndView("/site/changePassword", model);
//	}
//
//	@PostMapping("/customer/changePassword")
//	public ModelAndView changePassword(ModelMap model, Principal principal,
//			@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
//			@RequestParam("confirm") String confirm) {
//
//		boolean isLogin = false;
//		if (principal != null) {
//			isLogin = true;
//		}
//		model.addAttribute("isLogin", isLogin);
//		KhachHang c = khachHangRepository.findByEmail(principal.getName()).get();
////		String password = bCryptPasswordEncoder.encode(currentPassword);
//		if (bCryptPasswordEncoder.encode(currentPassword).equals(c.getPasswd())) {
//			System.out.println("trung");
//		} else {
//			System.out.println("ko trung");
//		}
//
//		model.addAttribute("totalCartItems", shoppingCartService.getCount());
//		return new ModelAndView("forward:/customer/info", model);
//	}

	@RequestMapping("/customer/checkout")
	public ModelAndView checkout(Principal principal) {
		Collection<CartItem> listItem = shoppingCartService.getCartItems();
		KhachHang c = khachHangRepository.findByEmail(principal.getName()).get();
		DonHang o = new DonHang();
		o.setTongTien(shoppingCartService.getAmount());
		o.setNgayDatHang(new Date());
		o.setMaKhachHang(c);
		o.setTinhTrang(0);
		donHangRepository.save(o);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"<h3>Xin chào " + c.getHoTen() + "!</h3>\r\n" + "    <h4>Bạn có 1 đơn hàng từ AUSHOP</h4>\r\n"
						+ "    <table style=\"border: 1px solid gray;\">\r\n"
						+ "        <tr style=\"width: 100%; border: 1px solid gray;\">\r\n"
						+ "            <th style=\"border: 1px solid gray;\">STT</th>\r\n"
						+ "            <th style=\"border: 1px solid gray;\">Tên sản phẩm</th>\r\n"
						+ "            <th style=\"border: 1px solid gray;\">Số lượng</th>\r\n"
						+ "            <th style=\"border: 1px solid gray;\">Đơn giá</th>\r\n" + "        </tr>");
//		Set<OrderDetail> set = null;
		for (CartItem i : listItem) {
			Optional<SanPham> opt = sanPhamRepository.findById(i.getProductId());
			if(opt.isPresent()) {
				SanPham p = opt.get();
				ChiTietDonHang od = new ChiTietDonHang();
				od.setSoLuong(i.getQuantity());
				od.setDonGia(i.getPrice());
				od.setMaSP(p);
				od.setMaDH(o);
				chiTietDonHangRepository.save(od);
				SanPham sp = opt.get();
				sp.setSlTonKho(opt.get().getSlTonKho()-od.getSoLuong());
				sanPhamRepository.save(sp);
			}
		}

		sendMailAction(o, "Bạn đã đặt thành công 1 đơn hàng từ AUSHOP!", "Chúng tôi sẽ sớm giao hàng cho bạn!",
				"Thông báo đặt hàng thành công!");

		shoppingCartService.clear();
		return new ModelAndView("forward:/customer/info");
	}

	
	@RequestMapping("/customer/checkoutVNPAY")
	public ModelAndView checkoutVNPAY(Principal principal) {
		Collection<CartItem> listItem = shoppingCartService.getCartItems();
		KhachHang c = khachHangRepository.findByEmail(principal.getName()).get();
		DonHang o = new DonHang();
		o.setTongTien(shoppingCartService.getAmount());
		o.setNgayDatHang(new Date());
		o.setMaKhachHang(c);
		o.setTinhTrang(0);
		donHangRepository.save(o);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"<h3>Xin chào " + c.getHoTen() + "!</h3>\r\n" + "    <h4>Bạn có 1 đơn hàng từ AUSHOP</h4>\r\n"
						+ "    <table style=\"border: 1px solid gray;\">\r\n"
						+ "        <tr style=\"width: 100%; border: 1px solid gray;\">\r\n"
						+ "            <th style=\"border: 1px solid gray;\">STT</th>\r\n"
						+ "            <th style=\"border: 1px solid gray;\">Tên sản phẩm</th>\r\n"
						+ "            <th style=\"border: 1px solid gray;\">Số lượng</th>\r\n"
						+ "            <th style=\"border: 1px solid gray;\">Đơn giá</th>\r\n" + "        </tr>");
//		Set<OrderDetail> set = null;
		for (CartItem i : listItem) {
			Optional<SanPham> opt = sanPhamRepository.findById(i.getProductId());
			if(opt.isPresent()) {
				SanPham p = opt.get();
				ChiTietDonHang od = new ChiTietDonHang();
				od.setSoLuong(i.getQuantity());
				od.setDonGia(i.getPrice());
				od.setMaSP(p);
				od.setMaDH(o);
				chiTietDonHangRepository.save(od);
				SanPham sp = opt.get();
				sp.setSlTonKho(opt.get().getSlTonKho()-od.getSoLuong());
				sanPhamRepository.save(sp);
			}
		}

		sendMailAction(o, "Bạn đã đặt thành công 1 đơn hàng từ KeyBoard Shop!", "Chúng tôi sẽ sớm giao hàng cho bạn!",
				"Thông báo đặt hàng thành công!");

		shoppingCartService.clear();
		return new ModelAndView("redirect:/VNPAY?tongtien="+o.getTongTien()+"&maDH="+o.getMaDH());
	}
	
	@RequestMapping("/customer/cancel/{id}")
	public ModelAndView cancel(ModelMap model, @PathVariable("id") int id, Principal principal) {
		Optional<DonHang> o = donHangRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("forward:/customer/info");
		}
		DonHang oReal = o.get();
		oReal.setTinhTrang((short) 3);
		donHangRepository.save(oReal);

		sendMailAction(oReal, "Bạn đã huỷ 1 đơn hàng từ AUSHOP!",
				"Chúng tôi rất tiếc vì không làm hài lòng bạn!", "Thông báo huỷ đơn hàng thành công!");

		return new ModelAndView("forward:/customer/info");
	}
	
	@RequestMapping("/customer/detail/{id}")
	public ModelAndView detail(ModelMap model, @PathVariable("id") int id, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> kh = khachHangRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(kh.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		
		 List<ChiTietDonHang> listO = chiTietDonHangRepository.findByMaDH(id);
		 
		 model.addAttribute("amount", donHangRepository.findById(id).get().getTongTien());
		 model.addAttribute("orderDetail", listO); model.addAttribute("orderId", id);
		 model.addAttribute("totalCartItems", shoppingCartService.getCount()); return
		 new ModelAndView("/site/detail", model);
		 
	}

	@GetMapping("customer/seen")
	public ModelAndView seen(ModelMap model, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> kh = khachHangRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(kh.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		Optional<KhachHang> c = khachHangRepository.findByEmail(principal.getName());
		List<SanPham> listSeen = sanPhamRepository.findSanPhamById(c.get().getMaKhachHang());

		model.addAttribute("hangDaXem", listSeen);
		return new ModelAndView("/khachhang/daxem", model);

	}
	@RequestMapping("/403")
	public String error() {

		return "/site/error";
	}

	// check email
	public boolean checkEmail(String email) {
		List<KhachHang> list = khachHangRepository.findAll();
		for (KhachHang c : list) {
			if (c.getEmail().equalsIgnoreCase(email)) {
				return false;
			}
		}
		return true;
	}

	// save file
	public void upload(MultipartFile file, String dir) throws IOException {
		Path path = Paths.get(dir);
		InputStream inputStream = file.getInputStream();
		Files.copy(inputStream, path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
	}

	// format currency
	public String format(String number) {
		DecimalFormat formatter = new DecimalFormat("###,###,###.##");

		return formatter.format(Double.valueOf(number)) + " VNĐ";
	}

	public void sendMailAction(DonHang oReal, String status, String cmt, String notifycation) {
		List<ChiTietDonHang> list = chiTietDonHangRepository.findByMaDH(oReal.getMaDH());

		StringBuilder stringBuilder = new StringBuilder();
		int index = 0;
		stringBuilder.append("<h3>Xin chào " + oReal.getMaKhachHang().getHoTen() + "!</h3>\r\n" + "    <h4>" + status
				+ "</h4>\r\n" + "    <table style=\"border: 1px solid gray;\">\r\n"
				+ "        <tr style=\"width: 100%; border: 1px solid gray;\">\r\n"
				+ "            <th style=\"border: 1px solid gray;\">STT</th>\r\n"
				+ "            <th style=\"border: 1px solid gray;\">Tên sản phẩm</th>\r\n"
				+ "            <th style=\"border: 1px solid gray;\">Số lượng</th>\r\n"
				+ "            <th style=\"border: 1px solid gray;\">Đơn giá</th>\r\n" + "        </tr>");
		for (ChiTietDonHang oD : list) {
			index++;
			stringBuilder.append("<tr>\r\n" + "            <td style=\"border: 1px solid gray;\">" + index + "</td>\r\n"
					+ "            <td style=\"border: 1px solid gray;\">" + oD.getMaSP().getTenSP() + "</td>\r\n"
					+ "            <td style=\"border: 1px solid gray;\">" + oD.getSoLuong() + "</td>\r\n"
					+ "            <td style=\"border: 1px solid gray;\">" + format(String.valueOf(oD.getDonGia()))
					+ "</td>\r\n" + "        </tr>");
		}
		stringBuilder.append("\r\n" + "    </table>\r\n" + "    <h3>Tổng tiền: "
				+ format(String.valueOf(oReal.getTongTien())) + "</h3>\r\n" + "    <hr>\r\n" + "    <h5>" + cmt
				+ "</h5>\r\n" + "    <h5>Chúc bạn 1 ngày tốt lành!</h5>");

		sendMailService.queue(oReal.getMaKhachHang().getEmail().trim(), notifycation, stringBuilder.toString());
	}

}
