package AUSHOP.Controllers.customers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
		model.addAttribute("oders1", list1);
		
		
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
}
