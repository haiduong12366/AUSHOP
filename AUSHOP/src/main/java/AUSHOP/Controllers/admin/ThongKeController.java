package AUSHOP.Controllers.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import AUSHOP.repository.SanPhamRepository;

@Controller
@RequestMapping("/admin")
public class ThongKeController {

	@Autowired
	SanPhamRepository sanphamRepository;
	
	@RequestMapping("/hangtonkho")
	public ModelAndView hangtonkho(ModelMap model) {
		model.addAttribute("hangtonkho", sanphamRepository.getTonKhoByLoaiSanPham());
		
		model.addAttribute("menuI", "menu");
		return new ModelAndView("/admin/hangtonkho",model);
	}
	
	@RequestMapping("/baocao")
	public ModelAndView doanhthu(ModelMap model, @RequestParam("reports") Optional<Integer> reports) {
		int report = reports.orElse(0);
		if (report == 0) {
			model.addAttribute("report", report);
			return new ModelAndView("forward:/admin/baocao/thongke", model);
		}else if (report == 1) {		
			model.addAttribute("report", report);
			return new ModelAndView("forward:/admin/baocao/loai-san-pham-ban-chay", model);
		}else if (report == 2) {
			model.addAttribute("report", report);
			return new ModelAndView("forward:/admin/baocao/san-pham-ban-chay", model);
		}else if (report == 3) {
			model.addAttribute("report", report);
			return new ModelAndView("forward:/admin/baocao/khach-hang-mua-nhieu", model);
		}
		model.addAttribute("report", report);
		return new ModelAndView("forward:/admin/loai-san-pham-ban-chay", model);
	}
	
	@RequestMapping("/baocao/loai-san-pham-ban-chay")
	public ModelAndView loaisanphambanchay(ModelMap model) {
		List<Object[]> dsloaisanphambanchay = sanphamRepository.getLoaiSanPhamBanChay();

		model.addAttribute("list", dsloaisanphambanchay);
		// set active front-end
		model.addAttribute("menuR", "menu");
		return new ModelAndView("/admin/loai-san-pham-ban-chay");
	}

	@RequestMapping("/baocao/san-pham-ban-chay")
	public ModelAndView sanphambanchay(ModelMap model) {
		List<Object[]> listBestSellingProduct = sanphamRepository.getSanPhamBanChay();

		model.addAttribute("list1", listBestSellingProduct);
		// set active front-end
		model.addAttribute("menuR", "menu");
		return new ModelAndView("/admin/san-pham-ban-chay");
	}

	@RequestMapping("/baocao/khach-hang-mua-nhieu")
	public ModelAndView bestBuyer(ModelMap model) {
		List<Object[]> listBestBuyer = sanphamRepository.getNguoiMuaNhieuNhat();

		model.addAttribute("listBestBuyer", listBestBuyer);
		// set active front-end
		model.addAttribute("menuR", "menu");
		return new ModelAndView("/admin/khach-hang-mua-nhieu");
	}
	
	@RequestMapping("/baocao/thongke")
	public ModelAndView statistical(ModelMap model, @RequestParam("statisticalId") Optional<Integer> statisticalId) {
		int idStatistical = statisticalId.orElse(0);
		if(idStatistical==0) {
			model.addAttribute("statisticalId", idStatistical);
			return new ModelAndView("forward:/admin/baocao/thongke/ngay", model);
		} else if(idStatistical==1) {
			model.addAttribute("statisticalId", idStatistical);
			return new ModelAndView("forward:/admin/baocao/thongke/thang", model);			
		} else if(idStatistical==2) {
			model.addAttribute("statisticalId", idStatistical);
			return new ModelAndView("forward:/admin/baocao/thongke/nam", model);			
		}
		model.addAttribute("statisticalId", idStatistical);
		// set active front-end
		model.addAttribute("menuR", "menu");
		return new ModelAndView("/admin/statistical-day");
	}
	
	@RequestMapping("/baocao/thongke/ngay")
	public ModelAndView statisticalByDay(ModelMap model) {
		List<Object[]> statistical = sanphamRepository.getThongKeTheoNgay();
		
		model.addAttribute("statistical", statistical);
		// set active front-end
		model.addAttribute("menuR", "menu");
		return new ModelAndView("/admin/thongke-ngay");
	}
	
	@RequestMapping("/baocao/thongke/thang")
	public ModelAndView statisticalByMonth(ModelMap model) {
		List<Object[]> statistical = sanphamRepository.getThongKeTheoThang();
		
		model.addAttribute("statistical", statistical);
		// set active front-end
		model.addAttribute("menuR", "menu");
		return new ModelAndView("/admin/thongke-thang");
	}
	
	@RequestMapping("/baocao/thongke/nam")
	public ModelAndView statisticalByYear(ModelMap model) {
		List<Object[]> statistical = sanphamRepository.getThongKeTheoNam();
		
		model.addAttribute("statistical", statistical);
		// set active front-end
		model.addAttribute("menuR", "menu");
		return new ModelAndView("/admin/thongke-nam");
	}
}
