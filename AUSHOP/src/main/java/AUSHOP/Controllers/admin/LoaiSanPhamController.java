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

import AUSHOP.Model.LoaiSanPhamModel;
import AUSHOP.Model.NhaCungCapModel;
import AUSHOP.entity.LoaiSanPham;
import AUSHOP.entity.NhaCungCap;
import AUSHOP.repository.LoaiSanPhamRepository;
import AUSHOP.services.ILoaiSanPhamService;

@Controller
@RequestMapping("/admin/loaisanpham")
public class LoaiSanPhamController {

	@Autowired
	LoaiSanPhamRepository loaiSanPhamRepository;

	@GetMapping("/add")
	public String add(ModelMap model) {
		LoaiSanPhamModel n = new LoaiSanPhamModel();
		model.addAttribute("menuLa", "menu");

		model.addAttribute("category", n);

		return "/admin/addLoaiSanPham";
	}

	@PostMapping("/reset")
	public String reset(ModelMap model, @Valid @ModelAttribute("category") LoaiSanPhamModel dto, BindingResult result) {
		model.addAttribute("menuLa", "menu");
		if (result.hasErrors()) {
			return "admin/addLoaiSanPham";
		}
		if (dto.isEdit()) {
			Optional<LoaiSanPham> opt = loaiSanPhamRepository.findById(dto.getMaLoaiSP());
			LoaiSanPham entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setEdit(true);
			model.addAttribute("category", dto);
			return "/admin/addLoaiSanPham";
		} else {
			LoaiSanPhamModel n = new LoaiSanPhamModel();
			model.addAttribute("category", n);
			return "/admin/addLoaiSanPham";
		}
	}

	boolean checkCategory(int MaLoaiSP, String tenLoaiSP) {
		List<LoaiSanPham> list = loaiSanPhamRepository.findAll();
		for (LoaiSanPham item : list) {
			if (item.getTenLoaiSP().equalsIgnoreCase(tenLoaiSP) && item.getMaLoaiSP() != MaLoaiSP) {
				return false;
			}
		}
		return true;
	}

	boolean checkCategory(String tenLoaiSP) {
		List<LoaiSanPham> list = loaiSanPhamRepository.findAll();
		for (LoaiSanPham item : list) {
			if (item.getTenLoaiSP().equalsIgnoreCase(tenLoaiSP) ) {
				return false;
			}
		}
		return true;
	}
	@PostMapping("/add")
	public ModelAndView addd(ModelMap model, @Valid @ModelAttribute("category") LoaiSanPhamModel dto,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("menuLa", "menu");

			return new ModelAndView("admin/addLoaiSanPham");
		}
		if (dto.isEdit()) {
			if (!checkCategory(dto.getMaLoaiSP(), dto.getTenLoaiSP())) {
				model.addAttribute("error", "Loại sản phẩm này đã tồn tại!");

				// set active front-end
				model.addAttribute("menuLa", "menu");
				return new ModelAndView("admin/addLoaiSanPham", model);
			}
		} else {
			if (!checkCategory(dto.getTenLoaiSP())) {
				model.addAttribute("error", "Loại sản phẩm này đã tồn tại!");

				// set active front-end
				model.addAttribute("menuLa", "menu");
				return new ModelAndView("admin/addLoaiSanPham", model);
			}
		}

		LoaiSanPham c = new LoaiSanPham();
		BeanUtils.copyProperties(dto, c);
		c.setDelete(false);
		loaiSanPhamRepository.save(c);
		if (dto.isEdit()) {
			model.addAttribute("message", "Sửa thành công!");
		} else {
			model.addAttribute("message", "Thêm thành công!");
		}
		model.addAttribute("menuLa", "menu");
		return new ModelAndView("forward:/admin/loaisanpham", model);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id, ModelMap model) {
		Optional<LoaiSanPham> opt = loaiSanPhamRepository.findById(id);
		model.addAttribute("menuLa", "menu");
		LoaiSanPhamModel dto = new LoaiSanPhamModel();
		if (opt.isPresent()) {
			LoaiSanPham entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setEdit(true);
			model.addAttribute("category", dto);

			return new ModelAndView("/admin/addLoaiSanPham", model);
		}

		model.addAttribute("error", "Không tồn tại thương hiệu này!");
		return new ModelAndView("forward:/admin/loaisanpham", model);
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id, ModelMap model) {
		Optional<LoaiSanPham> opt = loaiSanPhamRepository.findById(id);
		if (opt.isPresent()) {

			loaiSanPhamRepository.isDelete(opt.get().getMaLoaiSP());
			model.addAttribute("message", "Xoá thành công!");

		} else {
			model.addAttribute("error", "Loại sản phẩm này không tồn tại!");
		}

		model.addAttribute("menuLa", "menu");
		return new ModelAndView("forward:/admin/loaisanpham", model);
	}

	@GetMapping("/search")
	public String search(ModelMap model, @RequestParam(name = "tenLoaiSP", required = false) String tenLoaiSP,
			@RequestParam("size") Optional<Integer> size) {
		int pageSize = size.orElse(5);
		Pageable pageable = PageRequest.of(0, pageSize);
		Page<LoaiSanPham> list = loaiSanPhamRepository.findBytenLoaiSPContaining(tenLoaiSP, pageable);
		model.addAttribute("loaisanpham", list);
		model.addAttribute("tenLoaiSP", tenLoaiSP);

		model.addAttribute("menuLa", "menu");
		return "admin/loaisanpham";
	}

	@RequestMapping("/page")
	public String page(ModelMap model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam(name = "tenLoaiSP", required = false) String tenLoaiSP) {
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(5);
		if (tenLoaiSP.equalsIgnoreCase("null")) {
			tenLoaiSP = "";
		}
		Pageable pageable = PageRequest.of(currentPage, pageSize);
		Page<LoaiSanPham> list = loaiSanPhamRepository.findBytenLoaiSPContaining(tenLoaiSP, pageable);
		model.addAttribute("loaisanpham", list);
		model.addAttribute("tenLoaiSP", tenLoaiSP);

		model.addAttribute("menuLa", "menu");
		return "admin/loaisanpham";
	}

	@RequestMapping("")
	public String list(ModelMap model, @RequestParam("size") Optional<Integer> size) {
		int pageSize = size.orElse(5);
		Pageable pageable = PageRequest.of(0, pageSize);
		Page<LoaiSanPham> list = loaiSanPhamRepository.findAllisDeleteContainning(pageable);
		model.addAttribute("loaisanpham", list);

		model.addAttribute("menuLa", "menu");
		return "admin/loaisanpham";
	}

}
