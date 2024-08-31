package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping({"/institucion/"})
@Controller 
public class InstitucionController {
	@RequestMapping({"gestion"})
	public String gestion(){
		return "institucion/gestion"; 
	}
}
