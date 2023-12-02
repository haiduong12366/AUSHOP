package AUSHOP.Controllers.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import AUSHOP.Model.LoaiSanPhamModel;
import AUSHOP.entity.LoaiSanPham;
import AUSHOP.services.ILoaiSanPhamService;

@Controller
@RequestMapping("admin/loaisanpham")
public class LoaiSanPhamController {

	@Autowired
	ILoaiSanPhamService loaispService;
	
	@RequestMapping("")
	public String list(ModelMap model) {
		
		List<LoaiSanPham> list = loaispService.findAll();
		model.addAttribute("loaisanpham",list);
		return "admin/LoaiSanPham/listLoaiSanPham";
	}
	
	@GetMapping("edit/{maLoaiSP}")
	public ModelAndView edit(ModelMap model, @PathVariable("maLoaiSP") Integer maLoaiSP) {
		Optional<LoaiSanPham> optLoaiSanPham = loaispService.findById(maLoaiSP);
		LoaiSanPhamModel loaispModel = new LoaiSanPhamModel();
		// kiểm tra sự tồn tại của category
		if (optLoaiSanPham.isPresent()) {
			LoaiSanPham entity = optLoaiSanPham.get();
			// copy từ entity sang cateModel
			BeanUtils.copyProperties(entity, loaispModel);
			loaispModel.setIsEdit(true);
			// đẩy dữ liệu ra view
			model.addAttribute("loaisanpham", loaispModel);
			return new ModelAndView("admin/LoaiSanPham/addOrEditLoaiSanPham", model);
		}
		model.addAttribute("message", "Category is not existed!!!!");
		return new ModelAndView("forward:/admin/LoaiSanPham", model);
	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("loaisanpham") LoaiSanPhamModel cateMdoel,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/LoaiSanPham/addOrEditLoaiSanPham");
		}

		LoaiSanPham entity = new LoaiSanPham();
		// copy từ Model sang Entity
		BeanUtils.copyProperties(cateMdoel, entity);
		// gọi hàm save trong service
		loaispService.save(entity);
		// đưa thông báo về cho biến message
		String message = "";
		if (cateMdoel.getIsEdit() == true) {
			message = "Category is Edited!!!!!!!!";
		} else {
			message = "Category is saved!!!!!!!!";
		}
		model.addAttribute("message", message);
		// redirect về URL controller
		return new ModelAndView("forward:/admin/LoaiSanPham", model);
	}
	
	@GetMapping("add")
	public String add(ModelMap model) {
		LoaiSanPhamModel cateModel = new LoaiSanPhamModel();
		cateModel.setIsEdit(false);
		// chuyển dữ liệu từ model vào biến category để đưa lên view
		model.addAttribute("loaisanpham", cateModel);
		return "admin/LoaiSanPham/addOrEditLoaiSanPham";
	}
	
	@GetMapping("delete/{maLoaiSP}")
	public ModelAndView delet(ModelMap model, @PathVariable("maLoaiSP") Integer maLoaiSP) {
		loaispService.deleteById(maLoaiSP);
		model.addAttribute("message", "Category is deleted!!!!");
		return new ModelAndView("forward:/admin/LoaiSanPham", model);
	}
}
