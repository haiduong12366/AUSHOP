package AUSHOP.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	@Autowired
	//private SanPhamService service;

	@PostMapping("/login_success_handler")
	public String loginSuccessHandler() {
		System.out.println("dang nhap thanh cong");
		return "index";
	}

	@PostMapping("/login_failure_handler")
	public String loginFailureHandler() {
		System.out.println("dang nhap khong duoc emzai");
		return "login";
	}

	/*
	 * @RequestMapping("/") public String viewHomePage(Model model) { List<SanPham>
	 * listProducts = service.listAll(); model.addAttribute("listProducts",
	 * listProducts); return "index"; }
	 * 
	 * @RequestMapping("/new") public String showNewProductForm(Model
	 * model, @ModelAttribute("product") SanPham product) {
	 * model.addAttribute("product", product);
	 * 
	 * return "new_product"; }
	 * 
	 * @RequestMapping(value = "/save", method = RequestMethod.POST) public String
	 * saveProduct(@ModelAttribute("product") SanPham product) {
	 * service.save(product);
	 * 
	 * return "redirect:/"; }
	 * 
	 * @RequestMapping("/edit/{id}") public ModelAndView
	 * showEditProductForm(@PathVariable(name = "id") Long id) { ModelAndView mav =
	 * new ModelAndView("edit_product");
	 * 
	 * SanPham product = service.get(id); mav.addObject("product", product);
	 * 
	 * return mav; }
	 * 
	 * @RequestMapping("/delete/{id}") public String
	 * deleteProduct(@PathVariable(name = "id") Long id) { service.delete(id);
	 * 
	 * return "redirect:/"; }
	 */
}