package app.controller;


import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/socios")
public class SocioController {

    @RequestMapping("/gestion") 
    public String alamat(Model model) {
    	System.out.println("LLEGO SOC");
        return "socios/gestion";

    }
    @RequestMapping("estadosocio/{id}")
	public String generateImageQRCode(@PathVariable("id") String id,Model modelo) {
//		log.info("BookResourceImpl - generateImageQRCode");
		System.out.println("DATO:"+id); 
		modelo.addAttribute("codigosocio", id);	
		return "socios/estadosociodrive";
	}
    @RequestMapping("/empresas/{id}")
 	public String showCatalogoSocio(@PathVariable("id") String id,Model modelo) {
// 		log.info("BookResourceImpl - generateImageQRCode");
 		System.out.println("DATO:"+id); 
 		modelo.addAttribute("codigosocio", id);	
 		return "socios/catalogosociodrive";
 	}
}
