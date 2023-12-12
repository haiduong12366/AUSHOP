package AUSHOP.Controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import AUSHOP.Model.KhachHangModel;
import AUSHOP.entity.AppRole;
import AUSHOP.entity.ChiTietDonHang;
import AUSHOP.entity.DonHang;
import AUSHOP.entity.KhachHang;
import AUSHOP.entity.ThanhToan;
import AUSHOP.entity.UserRole;
import AUSHOP.repository.ChiTietDonHangRepository;
import AUSHOP.repository.DonHangRepository;
import AUSHOP.repository.KhachHangRepository;
import AUSHOP.repository.RoleRepository;
import AUSHOP.repository.ThanhToanRepository;
import AUSHOP.repository.UserRoleRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
@RequestMapping("/admin/customers")
public class KhachHangController {
	@Autowired
	KhachHangRepository khachHangRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	RoleRepository appRoleRepository;

	@Autowired
	DonHangRepository donHangRepository;

	@Autowired
	ChiTietDonHangRepository chiTietDonHangRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping("")
	public ModelAndView form(ModelMap model) {
		Pageable pageable = PageRequest.of(0, 5);
		Page<KhachHang> list = khachHangRepository.findAllwithIsdelete(0,pageable);

		model.addAttribute("customers", list);
		// set active front-end
		model.addAttribute("menuC", "menu");
		return new ModelAndView("/admin/manageCustomer", model);
	}

	@GetMapping("/add")
	public ModelAndView add(ModelMap model) {
		model.addAttribute("customer", new KhachHangModel());
		model.addAttribute("photo", "user.png");

		// set active front-end
		model.addAttribute("menuC", "menu");
		return new ModelAndView("/admin/addCustomer", model);
	}

	@PostMapping("/add")
	public ModelAndView addd(ModelMap model, @Valid @ModelAttribute("customer") KhachHangModel dto,
			BindingResult result, @RequestParam("hinhanhKH") String image, @RequestParam("photo") MultipartFile photo,
			@RequestParam("passwd") String passwd) throws IOException {
		dto.setPasswd(bCryptPasswordEncoder.encode(passwd));
		if (dto.isEdit()) {
			if (passwd.equals(khachHangRepository.findById(dto.getMaKhachHang()).get().getPasswd())) {
				System.out.println("trung   ajaja");
				dto.setPasswd(passwd);
			}
		}
		if (result.hasErrors()) {
			if (dto.isEdit()) {
				model.addAttribute("photo", image);
				dto.setHinhanhKH(image);
			} else {
				model.addAttribute("photo", "user.png");
			}
			// set active front-end
			model.addAttribute("menuC", "menu");
			return new ModelAndView("/admin/addCustomer", model);
		}

		if (!checkEmail(dto.getEmail()) && !dto.isEdit()) {
			model.addAttribute("photo", "user.png");

			model.addAttribute("error", "Email này đã được sử dụng!");
			// set active front-end
			model.addAttribute("menuC", "menu");
			return new ModelAndView("/admin/addCustomer", model);
		}

		KhachHang c = new KhachHang();
		BeanUtils.copyProperties(dto, c);
		c.setNgayDangKy(new Date());
		c.setDelete(false);
		if (photo.getOriginalFilename().equals("")) {
			if (image.equals("")) {
				c.setHinhanhKH("user.png");
			} else {
				c.setHinhanhKH(image);
			}
		} else {
			c.setHinhanhKH(photo.getOriginalFilename());
			upload(photo, "uploads/customers");
		}

		khachHangRepository.save(c);

		if (dto.isEdit()) {
			model.addAttribute("message", "Sửa thành công !");

		} else {
			model.addAttribute("message", "Thêm thành công !");
		}

		// set active front-end
		model.addAttribute("menuC", "menu");
		return new ModelAndView("forward:/admin/customers", model);
	}

	@RequestMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Integer id) {

		Optional<KhachHang> opt = khachHangRepository.findById(id);
		if (opt.isPresent()) {
			KhachHangModel dto = new KhachHangModel();
			BeanUtils.copyProperties(opt.get(), dto);
			dto.setEdit(true);
			model.addAttribute("customer", dto);
			model.addAttribute("photo", dto.getHinhanhKH());
			// set active front-end
			model.addAttribute("menuC", "menu");
			return new ModelAndView("/admin/addCustomer", model);
		}

		model.addAttribute("error", "Người dùng này không tồn tại!");
		// set active front-end
		model.addAttribute("menuC", "menu");
		return new ModelAndView("forward:/admin/customers", model);
	}

	@RequestMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Integer id) {

		Optional<KhachHang> opt = khachHangRepository.findById(id);
		if (opt.isPresent()) {
			KhachHang c = opt.get();
			c.setDelete(true);
			khachHangRepository.isDelete(c.getMaKhachHang());
			model.addAttribute("message", "Xoá thành công!");
		} else {
			model.addAttribute("error", "Người dùng này không tồn tại!");
		}

		// set active front-end
		model.addAttribute("menuC", "menu");
		return new ModelAndView("forward:/admin/customers", model);
	}

	@RequestMapping("/page")
	public ModelAndView page(ModelMap model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("name") String name, @RequestParam("size") Optional<Integer> size,
			@RequestParam("filter") Optional<Integer> filter) {
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(5);
		int filterPage = filter.orElse(0);
		if (name.equalsIgnoreCase("null")) {
			name = "";
		}
		Pageable pageable = PageRequest.of(currentPage, pageSize);

		if (filterPage == 0) {
			pageable = PageRequest.of(currentPage, pageSize);
		} else if (filterPage == 1) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "hoTen"));
		} else if (filterPage == 2) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "hoTen"));
		} else if (filterPage == 3) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "ngayDangKy"));
		} else if (filterPage == 4) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "ngayDangKy"));
		}

		Page<KhachHang> list = khachHangRepository.findByHoTenContaining(name, pageable);

		model.addAttribute("customers", list);
		model.addAttribute("name", name);
		model.addAttribute("filter", filterPage);
		// set active front-end
		model.addAttribute("menuC", "menu");
		return new ModelAndView("/admin/manageCustomer", model);
	}

	@RequestMapping("/search")
	public ModelAndView search(ModelMap model, @RequestParam("name") String name,
			@RequestParam("size") Optional<Integer> size, @RequestParam("filter") Optional<Integer> filter) {
		int filterPage = filter.orElse(0);
		int pageSize = size.orElse(5);
		Pageable pageable = PageRequest.of(0, pageSize);

		if (filterPage == 0) {
			pageable = PageRequest.of(0, pageSize);
		} else if (filterPage == 1) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, "hoTen"));
		} else if (filterPage == 2) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "hoTen"));
		} else if (filterPage == 3) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, "ngayDangKy"));
		} else if (filterPage == 4) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "ngayDangKy"));
		}

		Page<KhachHang> list = khachHangRepository.findByHoTenContaining(name, pageable);

		model.addAttribute("name", name);
		model.addAttribute("filter", filterPage);
		model.addAttribute("customers", list);
		// set active front-end
		model.addAttribute("menuC", "menu");
		return new ModelAndView("/admin/manageCustomer", model);
	}

	// save file
	public void upload(MultipartFile file, String dir) throws IOException {
		Path path = Paths.get(dir);
		InputStream inputStream = file.getInputStream();
		Files.copy(inputStream, path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
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
}
