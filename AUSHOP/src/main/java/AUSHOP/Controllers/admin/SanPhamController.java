package AUSHOP.Controllers.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

import AUSHOP.Model.LoaiSanPhamModel;
import AUSHOP.Model.SanPhamModel;
import AUSHOP.entity.ChiTietDonHang;
import AUSHOP.entity.LoaiSanPham;
import AUSHOP.entity.NhaCungCap;
import AUSHOP.entity.SanPham;
import AUSHOP.repository.ChiTietDonHangRepository;
import AUSHOP.repository.DonHangRepository;
import AUSHOP.repository.LoaiSanPhamRepository;
import AUSHOP.repository.NhaCungCapRepository;
import AUSHOP.repository.SanPhamRepository;

@Controller
@RequestMapping("/admin/products")
public class SanPhamController {

	@Autowired
	SanPhamRepository sanPhamRepository;

	@Autowired
	NhaCungCapRepository nhaCungCapRepository;

	@Autowired
	LoaiSanPhamRepository loaiSanPhamRepository;

	@Autowired
	ChiTietDonHangRepository chiTietDonHangRepository;

	@RequestMapping("")
	public ModelAndView list(ModelMap model) {
		Pageable pageable = PageRequest.of(0, 5);
		Page<SanPham> list = sanPhamRepository.findWithIsDelete(pageable);

		model.addAttribute("products", list);
		List<NhaCungCap> listC = nhaCungCapRepository.findAll();
		model.addAttribute("categories", listC);

		List<LoaiSanPham> listl = loaiSanPhamRepository.findAll();
		model.addAttribute("loaisanpham", listl);
		// set active front-end
		model.addAttribute("menuP", "menu");
		return new ModelAndView("/admin/product", model);
	}

	@GetMapping("/add")
	public ModelAndView add(ModelMap model) {
		model.addAttribute("product", new SanPhamModel());
		model.addAttribute("photo", "logo.jpg");
		List<NhaCungCap> categories = nhaCungCapRepository.findAll();
		model.addAttribute("categories", categories);

		List<NhaCungCap> listC = nhaCungCapRepository.findAll();
		model.addAttribute("categories", listC);

		List<LoaiSanPham> listl = loaiSanPhamRepository.findAll();
		model.addAttribute("loaisanpham", listl);
		// set active front-end
		model.addAttribute("menuP", "menu");
		return new ModelAndView("/admin/addProduct", model);
	}

	@PostMapping("/reset")
	public ModelAndView reset(ModelMap model, @Valid @ModelAttribute("products") SanPhamModel dto,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("redirect:/admin/products/add", model);
		}
		dto.setEdit(true);
		if (dto.isEdit()) {
			return new ModelAndView("redirect:edit/" + dto.getMaSP(), model);
		} else {
			return new ModelAndView("redirect:/admin/products/add", model);
		}
	}

	@PostMapping("/add")
	public ModelAndView addd(ModelMap model, @Valid @ModelAttribute("product") SanPhamModel dto, BindingResult result,
			@RequestParam("photo") MultipartFile photo, @RequestParam("hinhAnh") String img) throws IOException {
		List<NhaCungCap> categories = nhaCungCapRepository.findAll();
		model.addAttribute("categories", categories);
		if (result.hasErrors()) {
			if (dto.isEdit()) {
				model.addAttribute("photo", img);
				dto.setHinhAnh(img);
			} else {
				model.addAttribute("photo", "logo.jpg");
			}
			List<LoaiSanPham> listl = loaiSanPhamRepository.findAll();
			model.addAttribute("loaisanpham", listl);

			List<NhaCungCap> listC = nhaCungCapRepository.findAll();
			model.addAttribute("categories", listC);
			// set active front-end
			model.addAttribute("menuP", "menu");
			return new ModelAndView("/admin/addProduct", model);
		}
		SanPham p = new SanPham();
		BeanUtils.copyProperties(dto, p);
		NhaCungCap nn = nhaCungCapRepository.findById(dto.getMaNhaCC()).get();
		LoaiSanPham ll = loaiSanPhamRepository.findById(dto.getMaLoaiSP()).get();
		p.setMaNhaCC(nn);
		p.setMaLoaiSP(ll);
		p.setNgaynhaphang(new Date());
		p.setDelete(false);
		if (photo.getOriginalFilename().equals("")) {
			if (!img.equals("")) {
				p.setHinhAnh(img);
				dto.setHinhAnh(img);

			} else {
				p.setHinhAnh("logo.jpg");
				dto.setHinhAnh("logo.jpg");
			}
		} else {
			p.setHinhAnh(photo.getOriginalFilename());
			dto.setHinhAnh(photo.getOriginalFilename());
			upload(photo, "uploads/products/", p.getHinhAnh());
		}

		if (dto.isEdit()) {
			sanPhamRepository.update(dto.getDiscount(), dto.getDonGia(), dto.getHinhAnh(), dto.getMoTa(),
					dto.getSlTonKho(), dto.getTenSP(), dto.isTinhTrang(), dto.getMaLoaiSP(), dto.getMaNhaCC(),
					dto.getMaSP());
			model.addAttribute("message", "Sửa thành công!");
		} else {
			sanPhamRepository.save(p);
			model.addAttribute("message", "Thêm thành công!");
		}

		return new ModelAndView("forward:/admin/products", model);
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id, ModelMap model) {
		Optional<SanPham> p = sanPhamRepository.findById(id);
		if (p.isPresent()) {

			sanPhamRepository.isDelete(id);
			model.addAttribute("message", "Xoá thành công!");

		} else {
			model.addAttribute("error", "Sản phẩm không tồn tại!");
		}

		return new ModelAndView("forward:/admin/products", model);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable("id") Integer id, ModelMap model) {
		Optional<SanPham> p = sanPhamRepository.findById(id);
		SanPhamModel dto = new SanPhamModel();
		if (p.isPresent()) {
			List<NhaCungCap> categories = nhaCungCapRepository.findAll();
			model.addAttribute("categories", categories);

			BeanUtils.copyProperties(p.get(), dto);
			dto.setEdit(true);
			dto.setMaNhaCC(p.get().getMaNhaCC().getMaNhaCC());
			dto.setMaLoaiSP(p.get().getMaLoaiSP().getMaLoaiSP());

			model.addAttribute("product", dto);

			model.addAttribute("photo", dto.getHinhAnh());

			List<LoaiSanPham> listl = loaiSanPhamRepository.findAll();
			model.addAttribute("loaisanpham", listl);

			List<NhaCungCap> listC = nhaCungCapRepository.findAll();
			model.addAttribute("categories", listC);
			// set active front-end
			model.addAttribute("menuP", "menu");
			return new ModelAndView("/admin/addProduct", model);
		}

		model.addAttribute("error", "Sản phẩm này không tồn tại!");

		return new ModelAndView("forward:/admin/products", model);
	}

	@RequestMapping("/search")
	public ModelAndView search(ModelMap model, @RequestParam("tenSP") String tenSP,
			@RequestParam("size") Optional<Integer> size, @RequestParam("filter") Optional<Integer> filter) {
		int pageSize = size.orElse(5);
		int filterPage = filter.orElse(0);
		Pageable pageable = PageRequest.of(0, pageSize);

		if (filterPage == 0) {
			pageable = PageRequest.of(0, pageSize);
		} else if (filterPage == 1) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "ngaynhaphang"));
		} else if (filterPage == 2) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, "ngaynhaphang"));
		} else if (filterPage == 3) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, "don_gia"));
		} else if (filterPage == 4) {
			pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "don_gia"));
		}

		Page<SanPham> list = sanPhamRepository.findBytenSPContaining(tenSP, pageable);

		model.addAttribute("tenSP", tenSP);
		model.addAttribute("filter", filterPage);
		model.addAttribute("products", list);
		List<NhaCungCap> listC = nhaCungCapRepository.findAll();
		model.addAttribute("categories", listC);

		List<LoaiSanPham> listl = loaiSanPhamRepository.findAll();
		model.addAttribute("loaisanpham", listl);
		// set active front-end
		model.addAttribute("menuP", "menu");
		return new ModelAndView("/admin/product", model);
	}

	@RequestMapping("/page")
	public ModelAndView page(ModelMap model, @RequestParam("page") Optional<Integer> page,
			@RequestParam(value = "tenSP", required = false) String tenSP, @RequestParam("size") Optional<Integer> size,
			@RequestParam("filter") Optional<Integer> filter, @RequestParam("brand") Optional<Long> brandPage,
			@RequestParam("type") Optional<Long> typePage) {

		int filterPage = filter.orElse(0);
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(5);
		if (tenSP.equalsIgnoreCase("null")) {
			tenSP = "";
		}

		Long ma_nhacc = brandPage.orElse(0L);
		Long ma_loaisp = typePage.orElse(0L);

		Pageable pageable = PageRequest.of(currentPage, pageSize);

		if (filterPage == 0) {
			pageable = PageRequest.of(currentPage, pageSize);
		} else if (filterPage == 1) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "ngaynhaphang"));
		} else if (filterPage == 2) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "ngaynhaphang"));
		} else if (filterPage == 3) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "don_gia"));
		} else if (filterPage == 4) {
			pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "don_gia"));
		}

		Page<SanPham> list = null;

		if (ma_nhacc == 0) {
			if (ma_loaisp == 0) {
				list = sanPhamRepository.findBytenSPContaining(tenSP, pageable);
			} else {
				list = sanPhamRepository.findByTenSPAndMaLoaiSPContaining(tenSP, ma_loaisp, pageable);
			}

		} else {
			if (ma_loaisp == 0) {
				list = sanPhamRepository.findSanPhamByTenSPAndMaNhaCCContaining(tenSP, ma_nhacc, pageable);
			} else {
				list = sanPhamRepository.findSanPhamByTenSPAndMaNhaCCAndMaLoaiSPContaining(tenSP, ma_nhacc, ma_loaisp,
						pageable);
			}

		}
		model.addAttribute("type", ma_loaisp);
		model.addAttribute("brand", ma_nhacc);
		model.addAttribute("products", list);
		model.addAttribute("tenSP", tenSP);
		model.addAttribute("filter", filterPage);
		List<NhaCungCap> listC = nhaCungCapRepository.findAll();
		model.addAttribute("categories", listC);

		List<LoaiSanPham> listl = loaiSanPhamRepository.findAll();
		model.addAttribute("loaisanpham", listl);
		// set active front-end
		model.addAttribute("menuP", "menu");
		return new ModelAndView("/admin/product", model);
	}

//upload(photo, "uploads/products/", p.getHinhAnh());
	// save file
	public void upload(MultipartFile file, String dir, String name) throws IOException {
		Path path = Paths.get(dir);
		if (!Files.exists(path))
			Files.createDirectories(path);
		InputStream inputStream = file.getInputStream();
		Files.copy(inputStream, path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
	}

	// kiem tra ten san pham
	public Boolean check(String name) {
		List<SanPham> list = sanPhamRepository.findAll();
		for (SanPham item : list) {
			if (item.getTenSP().equalsIgnoreCase(name))
				return false;
		}

		return true;
	}

}
