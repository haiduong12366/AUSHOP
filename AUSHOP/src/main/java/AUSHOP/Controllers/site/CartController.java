package AUSHOP.Controllers.site;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import AUSHOP.Model.CartItem;
import AUSHOP.entity.KhachHang;
import AUSHOP.entity.SanPham;
import AUSHOP.entity.UserRole;
import AUSHOP.repository.KhachHangRepository;
import AUSHOP.repository.NhaCungCapRepository;
import AUSHOP.repository.SanPhamRepository;
import AUSHOP.repository.UserRoleRepository;
import AUSHOP.services.ShoppingCartService;

@Controller
public class CartController {
	@Autowired
	SanPhamRepository productRepository;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	NhaCungCapRepository categoryRepository;

	@Autowired
	KhachHangRepository customerRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@RequestMapping("/addCart/{id}")
	public ModelAndView addCart(ModelMap model, @PathVariable("id") Integer id, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(c.get().getMaKhachHang());
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		Optional<SanPham> p = productRepository.findById(id);
		int count = shoppingCartService.getMountById(p.get().getMaSP());
		if (count < p.get().getSlTonKho()) {

			if (p.isPresent()) {
				CartItem item = new CartItem();
				item.setProductId(p.get().getMaSP());
				item.setName(p.get().getTenSP());
				item.setDateAdd(new Date());
				item.setPrice(p.get().getDonGia() - p.get().getDonGia() * p.get().getDiscount() / 100);
				item.setQuantity(1);
				shoppingCartService.add(item);
//			model.addAttribute("message", "Đã thêm vào giỏ hàng!");
			} else {
				model.addAttribute("message", "Sản phẩm này không tồn tại!");
			}

		} else {
			model.addAttribute("message", "Quá số lượng!");
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return new ModelAndView("forward:/shop", model);
	}

	@RequestMapping("/cart/update")
	public ModelAndView updateCart(@RequestParam("id") Integer id, @RequestParam("quantity") int quantity,
			ModelMap model, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(c.get().getMaKhachHang());
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}
		Optional<SanPham> p = productRepository.findById(id);
		if (quantity < p.get().getSlTonKho())
			shoppingCartService.update(id, quantity);
		else
			model.addAttribute("message", "Hết hàng!");
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return new ModelAndView("forward:/shop", model);
	}

	@RequestMapping("/cart/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Integer id, ModelMap model, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(c.get().getMaKhachHang());
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		shoppingCartService.remove(id);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return new ModelAndView("forward:/cart", model);
	}

	@RequestMapping("/cart")
	public ModelAndView cart(ModelMap model, Principal principal) {

		boolean isLogin = false;
		if (principal != null) {
			System.out.println(principal.getName());
			isLogin = true;
		}
		model.addAttribute("isLogin", isLogin);

		if (principal != null) {
			Optional<KhachHang> c = customerRepository.findByEmail(principal.getName());
			Optional<UserRole> uRole = userRoleRepository.findByMaKhachHang(c.get().getMaKhachHang());
			if (uRole.get().getRoleId().getTen().equals("ROLE_ADMIN")) {
				return new ModelAndView("forward:/admin/customers", model);
			}
		}

		Collection<CartItem> cart = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cart);

		double amount = shoppingCartService.getAmount();
		model.addAttribute("amount", amount);

		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return new ModelAndView("/site/cart");
	}

}
