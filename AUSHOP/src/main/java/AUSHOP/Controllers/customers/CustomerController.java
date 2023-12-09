package AUSHOP.Controllers.customers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import AUSHOP.Model.KhachHangDtoModel;
import AUSHOP.entity.ChiTietDonHang;
import AUSHOP.entity.DonHang;
import AUSHOP.entity.KhachHang;
import AUSHOP.repository.ChiTietDonHangRepository;
import AUSHOP.repository.DonHangRepository;
import AUSHOP.repository.KhachHangRepository;



@Controller
public class CustomerController {

	@Autowired
	KhachHangRepository khachhangRepository;
	
	@Autowired
	DonHangRepository donhangRepository;
	
	@Autowired
	BCryptPasswordEncoder bcryptPass;
	
	@Autowired
	ChiTietDonHangRepository chitietRepository;
	
	@RequestMapping("/khachhang/thongtin")
	public ModelAndView thongtin (ModelMap model, Principal principal) {
		
		Optional<KhachHang> kh = khachhangRepository.findByEmail(principal.getName());
		model.addAttribute("user", kh.get());
		
		Page<DonHang> list0 = donhangRepository.findByMaKhachHang(kh.get().getMaKhachHang(), 0, PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "madh")));
		model.addAttribute("orders0", list0);
		
		Page<DonHang> list1 = donhangRepository.findByMaKhachHang(kh.get().getMaKhachHang(), 1, PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "madh")));
		model.addAttribute("orders1", list1);

		
		Page<DonHang> list2 = donhangRepository.findByMaKhachHang(kh.get().getMaKhachHang(), 2, PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "madh")));
		model.addAttribute("orders2", list2);
		
		Page<DonHang> list3 = donhangRepository.findByMaKhachHang(kh.get().getMaKhachHang(), 3, PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "madh")));
		model.addAttribute("orders3", list3);
		
		return new ModelAndView("/khachhang/thongtin",model);
	}
	
	@RequestMapping("/khachhang/chitiet/{id}")
	public ModelAndView chitiet (ModelMap model, @PathVariable("id") int id, Principal principal) {
		
		List<ChiTietDonHang> list = chitietRepository.findByMaDH(id);
		
		model.addAttribute("orderDetail", list);
		model.addAttribute("orderId", id);
		model.addAttribute("amount", donhangRepository.findById(id).get().getTongTien());
		
		return new ModelAndView("/khachhang/chitiet");
	}
	
	@RequestMapping("khachhang/huy/{id}")
	public ModelAndView huy (ModelMap model,@PathVariable("id") int id, Principal principal) {
		
		Optional<DonHang> dh = donhangRepository.findById(id);
		
		if (dh.isEmpty()) {
			return new ModelAndView("forward:/khachhang/thongtin");
		}
		
		DonHang dh1 = dh.get();
		dh1.setTinhTrang((String)"3");
		donhangRepository.save(dh1);
		
		return new ModelAndView("forward:/khachhang/thongtin");
	}
	
	@GetMapping("/khachhang/edit")
	public ModelAndView getEdit (ModelMap model, Principal principal) {
		
		KhachHang kh = khachhangRepository.findByEmail(principal.getName()).get();
		model.addAttribute("user", kh);
		
		return new ModelAndView("/khachhang/edit");
	}
	
	@PostMapping("/khachhang/edit")
	public ModelAndView postEdit(ModelMap model, @Valid @ModelAttribute("customer") KhachHangDtoModel dto, BindingResult result,
			@RequestParam("photo") MultipartFile photo, Principal principal)throws IOException {
		
		if (result.hasErrors()) {
			System.out.println("Lỗi!!!!!!!!!"); 
			//return new ModelAndView("/admin/", model);
		}
		
		KhachHang kh = khachhangRepository.findByEmail(principal.getName()).get();
		if (!photo.getOriginalFilename().equals("")) {
			upload(photo,"uploads/customers");
			kh.setHinhanhKH(photo.getOriginalFilename());
		}
		kh.setHoTen(dto.getHoTen());
		kh.setGioiTinh(dto.isGioiTinh());
		kh.setSdt(dto.getSdt());
		kh.setDiaChi(dto.getDiaChi());
		
		khachhangRepository.save(kh);
		
		return new ModelAndView("forward:/khachhang/thongtin");
	}
	
	public void upload(MultipartFile file, String dir) throws IOException {
		Path path = Paths.get(dir);
		InputStream inputStream = file.getInputStream();
		Files.copy(inputStream, path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
	}
}
