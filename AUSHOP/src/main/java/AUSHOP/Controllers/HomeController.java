package AUSHOP.Controllers;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
	public String name() {
		return "index";
	}
}
