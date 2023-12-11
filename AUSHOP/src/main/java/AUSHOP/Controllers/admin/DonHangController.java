package AUSHOP.Controllers.admin;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;

import AUSHOP.entity.ChiTietDonHang;
import AUSHOP.entity.DonHang;

import AUSHOP.entity.SanPham;
import AUSHOP.repository.ChiTietDonHangRepository;
import AUSHOP.repository.DonHangRepository;
import AUSHOP.repository.SanPhamRepository;
import AUSHOP.services.HoaDonServiceImpl;
import AUSHOP.services.SendMailService;


@Controller
@RequestMapping("/admin/donhang")
public class DonHangController {

	@Autowired
	SanPhamRepository sanphamRepository;

	@Autowired
	DonHangRepository donhangRepository;

	@Autowired
	ChiTietDonHangRepository chitietdonhangRepository;

	@Autowired
	SendMailService sendmailService;
	
	@Autowired
	HoaDonServiceImpl hoadonService;

	@RequestMapping("")
	public ModelAndView donhang(ModelMap model) {

		Page<DonHang> list1 = donhangRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "maDH")));
		model.addAttribute("orders", list1);
		model.addAttribute("menuO", "menu");
		return new ModelAndView("/admin/donhang");

	}

	@RequestMapping("/page")
	public ModelAndView page(ModelMap model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("filter") Optional<Integer> filter) {

		int currentPage = page.orElse(0);
		int pageSize = size.orElse(5);
		int filterPage = filter.orElse(0);

		Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "maDH"));
		Page<DonHang> list1 = null;
		if (filterPage == 0) {
			list1 = donhangRepository.findAll(pageable);
		} else if (filterPage == 1) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "maDH"));
			list1 = donhangRepository.findByStatus(0, pageable);
		} else if (filterPage == 2) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "maDH"));
			list1 = donhangRepository.findByStatus(1, pageable);
		} else if (filterPage == 3) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "maDH"));
			list1 = donhangRepository.findByStatus(2, pageable);
		} else if (filterPage == 4) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "maDH"));
			list1 = donhangRepository.findByStatus(3, pageable);
		} else if (filterPage == 5) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "tongTien"));
			list1 = donhangRepository.findAll(pageable);
		}

		model.addAttribute("filter", filterPage);
		model.addAttribute("page", currentPage);
		model.addAttribute("orders", list1);
		model.addAttribute("menuO", "menu");

		return new ModelAndView("/admin/donhang");
	}

	@RequestMapping("/timkiem")
	public ModelAndView timkiem(ModelMap model, @RequestParam("id") String id) {
		Page<DonHang> listDH = null;
		if (id == null || id.equals("") || id.equalsIgnoreCase("null")) {
			listDH = donhangRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "maDH")));
		} else {
			listDH = donhangRepository.findByMaDH(Integer.valueOf(id), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "maDH")));
		}

		model.addAttribute("id", id);
		model.addAttribute("orders", listDH);
		// set active front-end
		model.addAttribute("menuO", "menu");
		return new ModelAndView("/admin/donhang");
	}

	@RequestMapping("/chitiet/{order-id}")
	public ModelAndView chitiet(ModelMap model, @PathVariable("order-id") int id) {

		List<ChiTietDonHang> listO = chitietdonhangRepository.findByMaDH(id);
		
		DonHang donHang = donhangRepository.findById(id).orElse(null); 

		if (donHang != null) {
		    int tinhTrang = donHang.getTinhTrang();  
		    model.addAttribute("tinhTrang", tinhTrang); 
		}

		model.addAttribute("amount", donhangRepository.findById(id).get().getTongTien());
		model.addAttribute("orderDetail", listO);
		model.addAttribute("orderId", id);
		// set active front-end
		model.addAttribute("menuO", "menu");
		return new ModelAndView("/admin/chitiet", model);
	}

	public String format(String number) {
		DecimalFormat formatter = new DecimalFormat("###,###,###.##");

		return formatter.format(Double.valueOf(number)) + " VNĐ";
	}

	public void sendMailAction(DonHang oReal, String status, String cmt, String notifycation) {
		List<ChiTietDonHang> list = chitietdonhangRepository.findByMaDH(oReal.getMaDH());
		System.out.println(oReal.getMaDH());

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

		sendmailService.queue(oReal.getMaKhachHang().getEmail().trim(), notifycation, stringBuilder.toString());
	}

	@RequestMapping("/huy/{order-id}")
	public ModelAndView huy(ModelMap model, @PathVariable("order-id") int id) {

		Optional<DonHang> o = donhangRepository.findById(id);

		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/donhang", model);
		}

		DonHang dh = o.get();
		dh.setTinhTrang(3);
		donhangRepository.save(dh);

		sendMailAction(dh, "Bạn đã bị huỷ 1 đơn hàng từ AUSHOP!", "Chúng tôi rất tiếc về vấn đề này!",
				"THÔNG BÁO HỦY ĐƠN HÀNG");

		return new ModelAndView("forward:/admin/donhang", model);
	}

	@RequestMapping("/xacnhan/{order-id}")
	public ModelAndView xacnhan(ModelMap model, @PathVariable("order-id") int id) {

		Optional<DonHang> o = donhangRepository.findById(id);

		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/donhang", model);
		}

		DonHang dh = o.get();
		dh.setTinhTrang(1);
		donhangRepository.save(dh);
		
		sendMailAction(dh, "Bạn có 1 đơn hàng ở AUSHOP đã được xác nhận!",
				"Đơn hàng của bạn sẽ sớm được vận chuyển!", "XÁC NHẬN ĐƠN HÀNG THÀNH CÔNG");
		
		return new ModelAndView("forward:/admin/donhang", model);
	}
	
	@RequestMapping("/dagiaohang/{order-id}")
	public ModelAndView dagiaohang (ModelMap model, @PathVariable("order-id") int id) {
		
		Optional<DonHang> o = donhangRepository.findById(id);

		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/donhang", model);
		}

		DonHang dh = o.get();
		dh.setTinhTrang(2);
		donhangRepository.save(dh);
		
		SanPham sp = null;
		List<ChiTietDonHang> list = chitietdonhangRepository.findByMaDH(id);
		for (ChiTietDonHang ct : list) {
			sp = ct.getMaSP();
			ct.setSoLuong(sp.getSlTonKho() - ct.getSoLuong());
			sanphamRepository.save(sp);
		}
		
		sendMailAction(dh, "Bạn có 1 đơn hàng ở AUSHOP đã thanh toán thành công!",
				"Rất mong bạn sẽ tiếp tục ủng hộ chúng tôi trong thời gian tới", "THANH TOÁN THÀNH CÔNG");
	
		
		return new ModelAndView("forward:/admin/donhang", model);
	}
	
	@RequestMapping("/chinhsua/{order-id}")
	public ModelAndView chinhsua(ModelMap model, @PathVariable("order-id") int id) {
	       
	    List<ChiTietDonHang> listO = chitietdonhangRepository.findByMaDH(id);

		model.addAttribute("amount", donhangRepository.findById(id).get().getTongTien());
		model.addAttribute("orderDetail", listO);
		model.addAttribute("orderId", id);
	    return new ModelAndView("/admin/chinhsua_donhang", model); 
	
	}

	
	@RequestMapping("/chinhsua/xoa/{id}")
	public ModelAndView xoa(@PathVariable("id") Integer id, ModelMap model) {
	    Optional<ChiTietDonHang> opt = chitietdonhangRepository.findById(id);

	    if (opt.isPresent()) {
	        ChiTietDonHang chitiet = opt.get();
	        int orderId = chitiet.getMaDH().getMaDH();
	        
	        chitietdonhangRepository.deleteById(id);
	        model.addAttribute("message", "Xoá thành công!");
	        
	        // Set lại orderId trước khi redirect
	        model.addAttribute("orderId", orderId);
	        
	        return new ModelAndView("redirect:/admin/donhang/chinhsua/{orderId}", model);
	    } else {
	        model.addAttribute("error", "Sản phẩm này không tồn tại!");
	        return new ModelAndView("redirect:/admin/donhang", model); // Chuyển hướng về trang nếu không tìm thấy ChiTietDonHang
	    }
	}


	@PostMapping("/chinhsua/capnhat/{id}")
	public ModelAndView chinhsuaCapNhat(@PathVariable("id") Integer id, @RequestParam("soLuong") int soLuong, ModelMap model) {
	    Optional<ChiTietDonHang> opt = chitietdonhangRepository.findById(id);

	    if (opt.isPresent()) {
	        ChiTietDonHang chitiet = opt.get();
	        chitiet.setSoLuong(soLuong);
	        chitietdonhangRepository.save(chitiet);
	    	
	        int orderId = chitiet.getMaDH().getMaDH();
	    	model.addAttribute("orderId", orderId);
	    	return new ModelAndView("redirect:/admin/donhang/chinhsua/{orderId}", model);
	    }

	    return new ModelAndView("redirect:/admin/donhang/chinhsua/{orderId}", model);
	}
	
}
