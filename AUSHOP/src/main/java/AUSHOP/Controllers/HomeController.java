package AUSHOP.Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import AUSHOP.entity.KhachHang;
import AUSHOP.entity.LoaiSanPham;
import AUSHOP.entity.SanPham;
import AUSHOP.entity.UserRole;
import AUSHOP.repository.KhachHangRepository;
import AUSHOP.repository.LoaiSanPhamRepository;
import AUSHOP.repository.SanPhamRepository;
import AUSHOP.repository.UserRoleRepository;
import AUSHOP.services.ShoppingCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class HomeController {


	@Autowired
	SanPhamRepository productRepository;

	@Autowired
	LoaiSanPhamRepository categoryRepository;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	KhachHangRepository customerRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@RequestMapping(value = {"/home",""})
	public ModelAndView home(ModelMap model, Principal principal,@RequestParam(value="error",required = false) String error) {
		boolean isLogin = false;
		if (principal != null) {
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if(principal!=null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(c.get().getMaKhachHang()));
			if(uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}
		if (error != null) {
			model.addAttribute("error", "Giỏ hàng chưa có sản phẩm");
		}
		List<SanPham> list8Last = productRepository.get8Last();
		List<SanPham> listtop4 = productRepository.gettop4();
//		List<SanPham> listnext4 = productRepository.getNext4(1);
		List<Object[]> listbest = productRepository.getTopProducts(PageRequest.of(0, 4));
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		model.addAttribute("products", list8Last);
		model.addAttribute("products_top4", listtop4);
		model.addAttribute("products_banchay", listbest);
		model.addAttribute("slide", true);
		return new ModelAndView("/site/index-home", model);
	}

	@GetMapping(value = {"/load"})
	protected void load(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		String amount = request.getParameter("exits");
		int iamount = Integer.parseInt(amount);
		List<SanPham> list = productRepository.getNext4(iamount);
		PrintWriter out = response.getWriter();

		for (SanPham item : list) {
			out.println("<div class=\"product col-lg-3 col-md-6 mb-4\">\r\n"
					+ "					<div class=\"card h-100\">\r\n"
					+ "						<a href=\"/shop/item/"+item.getMaSP()+"\"><img class=\"card-img-top\"\r\n"
					+ "								src=\"/getproduct/"+item.getHinhAnh()+"\" alt=\"...\" /></a>\r\n"
					+ "						<div class=\"card-body\">\r\n"
					+ "							<h4 class=\"card-title text-center\">\r\n"
					+ "								<a href=\"/shop/item/"+item.getMaSP()+"\" >"+item.getTenSP()+"</a>\r\n"
					+ "							</h4>\r\n"
					+ "							<p class=\"text-center\">\r\n"
					+ "								<small>(còn "+item.getSlTonKho()+" sản phẩm)</small>\r\n"
					+ "							</p>\r\n"
					+ "							<p class=\"card-text text-center\">Bàn phím cơ chơi game. Sự lựa\r\n"
					+ "								chọn tuyệt vời.</p>\r\n"
					+ "						</div>\r\n"
					+ "						<div class=\"card-footer text-center\">\r\n"
					+ "   							 <button onclick=\"addCart(\"+item.getMaSP()+\")\" class=\"btn btn-default btn-sm\" style=\"cursor: pointer; text-decoration: none; color: gray;\"> <span style=\"margin: auto;\">Thêm\r\n"
					+ "							vào giỏ hàng <i class=\"fa fa-shopping-cart\"></i></span></button>\r\n"
					+ "  						</div>\r\n"
					+ "					</div>\r\n"
					+ "				</div>");
		}
	}

	@RequestMapping(value = {"/load"}, method=RequestMethod.POST)
	public void loadPost(HttpServletRequest request, HttpServletResponse response)throws IOException {

		response.setContentType("text/html;charset=UTF-8");
		String amount = request.getParameter("exits");
		int iamount = Integer.parseInt(amount);
		List<SanPham> list = productRepository.getNext4(iamount);
		PrintWriter out = response.getWriter();

		for (SanPham item : list) {
			out.println("<div class=\"product col-lg-3 col-md-6 mb-4\">\n"
					+ "						<div class=\"card h-100\">\n"
					+ "							<a th:href=\"@{'/shop/item/'"+item.getMaSP()+"}\"><img class=\"card-img-top\"\n"
					+ "									th:src=\"@{'/getproduct/'"+item.getHinhAnh()+"}\" alt=\"...\" /></a>\n"
					+ "							<div class=\"card-body\">\n"
					+ "								<h4 class=\"card-title text-center\">\n"
					+ "									<a th:href=\"@{'/shop/item/'"+item.getMaSP()+"}\" th:text=\""+item.getTenSP()+"\"></a>\n"
					+ "								</h4>\n"
					+ "								<p class=\"text-center\">\n"
					+ "									<small>(còn [["+item.getSlTonKho()+"]] sản phẩm)</small>\n"
					+ "								</p>\n"
					+ "								<p class=\"card-text text-center\">Bàn phím cơ chơi game. Sự lựa\n"
					+ "									chọn tuyệt vời.</p>\n"
					+ "							</div>\n"
					+ "							<a class=\"card-footer text-center\" th:href=\"@{'/addCart/'"+item.getMaSP()+"}\"\n"
					+ "								style=\"cursor: pointer; text-decoration: none; color: gray;\"> <span\n"
					+ "									style=\"margin: auto;\">Thêm vào giỏ hàng <i class=\"fa fa-shopping-cart\"></i></span>\n"
					+ "							</a>\n"
					+ "						</div>\n"
					+ "					</div>");
		}
	}
	@RequestMapping(value = {"/shop"})
	public ModelAndView shop(ModelMap model, Principal principal) {
		boolean isLogin = false;
		if (principal != null) {
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if(principal!=null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(c.get().getMaKhachHang()));
			if(uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		Page<SanPham> listP = productRepository.findAll(PageRequest.of(0, 6));

		int totalPage = listP.getTotalPages();
		if (totalPage > 0) {
			int start = 1;
			int end = Math.min(2, totalPage);
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		List<LoaiSanPham> listC = categoryRepository.findAll();
		model.addAttribute("categories", listC);
		model.addAttribute("products", listP);
		model.addAttribute("slide", true);
		return new ModelAndView("/site/index", model);
	}
	
	@RequestMapping("/shop/item/{id}")
	public ModelAndView item(ModelMap model, @PathVariable("id") int id,
							 Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(c.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		Optional<SanPham> p = productRepository.findById(id);
		String cateName = productRepository.getTenLoaiSP(id);
		String supName = productRepository.getTenNhaCC(id);

		// random item product
//		Long quantity = productRepository.count();
//		int index = (int) (Math.random() * (quantity / 6));
//		if (index > quantity - 6) {
//			index -= 6;
//		}
		//Page<SanPham> productSuggest = productRepository.findAll(PageRequest.of(index, 6));
		Page<SanPham> productSameCate = productRepository.ListSanPhamCungLoai(id, PageRequest.of(0, 6));
		Page<SanPham> productSameSup = productRepository.ListSanPhamCungNhaCC(id, PageRequest.of(0, 6));
		if (p.isPresent()) {
			model.addAttribute("product", p.get());
			model.addAttribute("cateName", cateName);
			model.addAttribute("supName", supName);
			model.addAttribute("productSameCate", productSameCate);
			model.addAttribute("productSameSup", productSameSup);
			List<LoaiSanPham> listC = categoryRepository.findAll();
			model.addAttribute("categories", listC);
			model.addAttribute("totalCartItems", shoppingCartService.getCount());
			return new ModelAndView("/site/item", model);
		} else {
			model.addAttribute("message", "Sản phẩm đã bị xoá !");
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return new ModelAndView("forward:/shop", model);
	}
	@RequestMapping("/shop/brand/{id}")
	public ModelAndView brand(ModelMap model, @PathVariable("id") int categoryId, Principal principal) {
		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(c.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		Page<SanPham> listP = productRepository.findAllProductByCategoryId(categoryId, PageRequest.of(0, 6));

		System.out.println(listP.getTotalElements());
		int totalPage = listP.getTotalPages();
		if (totalPage > 0) {
			int start = 1;
			int end = Math.min(2, totalPage);
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		List<LoaiSanPham> listC = categoryRepository.findAll();
		model.addAttribute("categories", listC);
		model.addAttribute("brand", categoryId);
		model.addAttribute("products", listP);
		model.addAttribute("slide", true);
		return new ModelAndView("/site/index", model);
	}

	@RequestMapping("/shop/page")
	public ModelAndView page(ModelMap model, @RequestParam("page") Optional<Integer> page,
							 @RequestParam("name") String name, @RequestParam("filter") Optional<Integer> filter,
							 @RequestParam("brand") int brand, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(c.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		int currentPage = page.orElse(0);
		int filterPage = filter.orElse(0);

		Pageable pageable = PageRequest.of(currentPage, 6);

		if (brand != -1) {
			if (filterPage == 0) {
				pageable = PageRequest.of(currentPage, 6);
			} else if (filterPage == 1) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.DESC, "ngaynhaphang"));
			} else if (filterPage == 2) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.ASC, "ngaynhaphang"));
			} else if (filterPage == 3) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.DESC, "don_gia"));
			} else if (filterPage == 4) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.ASC, "don_gia"));
			}
		} else {
			if (filterPage == 0) {
				pageable = PageRequest.of(currentPage, 6);
			} else if (filterPage == 1) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.DESC, "ngaynhaphang"));
			} else if (filterPage == 2) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.ASC, "ngaynhaphang"));
			} else if (filterPage == 3) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.DESC, "don_gia"));
			} else if (filterPage == 4) {
				pageable = PageRequest.of(currentPage, 6, Sort.by(Sort.Direction.ASC, "don_gia"));
			}
		}

		Page<SanPham> listP = null;
		if (brand == -1) {
			listP = productRepository.findBytenSPContaining(name, pageable);
		} else {
			listP = productRepository.findAllProductByCategoryId(brand, pageable);
		}

		int totalPage = listP.getTotalPages();
		if (totalPage > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPage);
			if (totalPage > 5) {
				if (end == totalPage) {
					start = end - 5;
				} else if (start == 1) {
					end = start + 5;
				}
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		List<LoaiSanPham> listC = categoryRepository.findAll();
		model.addAttribute("categories", listC);
		model.addAttribute("brand", brand);
		model.addAttribute("filter", filterPage);
		model.addAttribute("name", name);
		model.addAttribute("products", listP);
		model.addAttribute("slide", true);
		return new ModelAndView("/site/index", model);
	}

	@RequestMapping("/shop/search")
	public ModelAndView search(ModelMap model, @RequestParam("name") String name,
							   @RequestParam("filter") Optional<Integer> filter, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(Integer.valueOf(c.get().getMaKhachHang()));
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		int filterPage = filter.orElse(0);
		Pageable pageable = PageRequest.of(0, 6);

		if (filterPage == 0) {
			pageable = PageRequest.of(0, 6);
		} else if (filterPage == 1) {
			pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "ngaynhaphang"));
		} else if (filterPage == 2) {
			pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "ngaynhaphang"));
		} else if (filterPage == 3) {
			pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "don_gia"));
		} else if (filterPage == 4) {
			pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "don_gia"));
		}
		Page<SanPham> listP = productRepository.findBytenSPContaining(name, pageable);

		int totalPage = listP.getTotalPages();
		if (totalPage > 0) {
			int start = 1;
			int end = Math.min(2, totalPage);
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		model.addAttribute("name", name);
		List<LoaiSanPham> listC = categoryRepository.findAll();
		model.addAttribute("categories", listC);
		model.addAttribute("filter", filterPage);
		model.addAttribute("products", listP);
		model.addAttribute("slide", true);
		return new ModelAndView("/site/index", model);

	}
}
