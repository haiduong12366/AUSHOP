package AUSHOP.Controllers.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import AUSHOP.Model.NhaCungCapModel;
import AUSHOP.entity.LoaiSanPham;
import AUSHOP.entity.NhaCungCap;
import AUSHOP.repository.LoaiSanPhamRepository;
import AUSHOP.repository.NhaCungCapRepository;

@Controller
@RequestMapping("/admin/categories")
public class NhaCungCapController {

	@Autowired
	NhaCungCapRepository capRepository;

	@Autowired
	LoaiSanPhamRepository loaiSanPhamRepository;
	
	@GetMapping("/add")
	public String add(ModelMap model) {
		NhaCungCapModel n = new NhaCungCapModel();
		n.setEdit(false);
		List<LoaiSanPham> l =loaiSanPhamRepository.findAll();
		model.addAttribute("menuCa", "menu");
		
		model.addAttribute("category", n);
		
		return "/admin/addCategory";
	}

	@PostMapping("/reset")
	public String reset(ModelMap model, @Valid @ModelAttribute("category") NhaCungCapModel dto,
			BindingResult result) {
		model.addAttribute("menuCa", "menu");
		if (result.hasErrors()) {
			return"admin/addCategory";
		}
		if(dto.isEdit()) {
			Optional<NhaCungCap> opt = capRepository.findById(dto.getMaNhaCC());
			NhaCungCap entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setEdit(true);
			model.addAttribute("category", dto);
			return "/admin/addCategory";
		}
		else {
			NhaCungCapModel n = new NhaCungCapModel();
			n.setEdit(false);
			model.addAttribute("category", n);
			return "/admin/addCategory";
		}
	}
	boolean checkCategory(int MaNhaCC, String tenNhaCC) {
		List<NhaCungCap> list = capRepository.findAll();
		for (NhaCungCap item : list) {
			if (item.getTenNhaCC().equalsIgnoreCase(tenNhaCC) && item.getMaNhaCC() != MaNhaCC) {
				return false;
			}
		}
		return true;
	}
	
	boolean checkCategory(String tenNhaCC) {
		List<NhaCungCap> list = capRepository.findAll();
		for (NhaCungCap item : list) {
			if (item.getTenNhaCC().equalsIgnoreCase(tenNhaCC)) {
				return false;
			}
		}
		return true;
	}

	@PostMapping("/add")
	public ModelAndView addd(ModelMap model, @Valid @ModelAttribute("category") NhaCungCapModel dto,
			BindingResult result) {
		if (result.hasErrors()) {

			return new ModelAndView("admin/addCategory");
		}
		if(dto.isEdit()) {
			if (!checkCategory(dto.getTenNhaCC())) {
				model.addAttribute("error", "Nhãn hiệu này đã tồn tại!");

				// set active front-end
				model.addAttribute("menuCa", "menu");
				return new ModelAndView("admin/addCategory", model);
			}
		}
		else {
			if (!checkCategory(dto.getMaNhaCC(),dto.getTenNhaCC())) {
				model.addAttribute("error", "Nhãn hiệu này đã tồn tại!");

				// set active front-end
				model.addAttribute("menuCa", "menu");
				return new ModelAndView("admin/addCategory", model);
			}
		}
		
		NhaCungCap c = new NhaCungCap();
		BeanUtils.copyProperties(dto, c);
		capRepository.save(c);
		if (dto.isEdit()) {
			model.addAttribute("message", "Sửa thành công!");
		} else {
			model.addAttribute("message", "Thêm thành công!");
		}
		model.addAttribute("menuCa", "menu");
		return new ModelAndView("forward:/admin/categories", model);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id, ModelMap model) {
		Optional<NhaCungCap> opt = capRepository.findById(id);
		model.addAttribute("menuCa", "menu");
		NhaCungCapModel dto = new NhaCungCapModel();
		if (opt.isPresent()) {
			NhaCungCap entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setEdit(true);

			model.addAttribute("category", dto);
			
			return new ModelAndView("/admin/addCategory", model);
		}
		
		model.addAttribute("error", "Không tồn tại thương hiệu này!");
		return new ModelAndView("forward:/admin/addCategories", model);
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id, ModelMap model) {
		Optional<NhaCungCap> opt = capRepository.findById(id);
		if (opt.isPresent()) {

			capRepository.delete(opt.get());
			model.addAttribute("message", "Xoá thành công!");

		} else {
			model.addAttribute("error", "Thương hiệu này không tồn tại!");
		}

		model.addAttribute("menuCa", "menu");
		return new ModelAndView("forward:/admin/categories", model);
	}
	
	@GetMapping("/search")
	public String search(ModelMap model, @RequestParam(name = "tenNhaCC", required = false) String tenNhaCC,
			@RequestParam("size") Optional<Integer> size) {
		int pageSize = size.orElse(5);
		Pageable pageable = PageRequest.of(0, pageSize);
		Page<NhaCungCap> list = capRepository.findBytenNhaCCContaining(tenNhaCC, pageable);
		model.addAttribute("categories", list);
		model.addAttribute("tenNhaCC", tenNhaCC);
		
		model.addAttribute("menuCa", "menu");
		return "admin/nhacungcap";
	}

	@RequestMapping("/page")
	public String page(ModelMap model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam(name = "tenNhaCC", required = false) String tenNhaCC) {
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(5);
		if(tenNhaCC.equalsIgnoreCase("null")) {
			tenNhaCC = "";
		}
		Pageable pageable = PageRequest.of(currentPage, pageSize);
		Page<NhaCungCap> list = capRepository.findBytenNhaCCContaining(tenNhaCC, pageable);
		model.addAttribute("categories", list);
		model.addAttribute("tenNhaCC", tenNhaCC);
		
		model.addAttribute("menuCa", "menu");
		return "admin/nhacungcap";
	}

	@RequestMapping("")
	public String list(ModelMap model, @RequestParam("size") Optional<Integer> size) {
		int pageSize = size.orElse(5);
		Pageable pageable = PageRequest.of(0, pageSize);
		Page<NhaCungCap> list = capRepository.findAll(pageable);
		model.addAttribute("categories", list);
		
		model.addAttribute("menuCa", "menu");
		return "admin/nhacungcap";
	}

}
