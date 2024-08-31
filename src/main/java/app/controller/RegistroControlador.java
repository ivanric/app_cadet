package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import app.service.UsuarioServicio;

@Controller
public class RegistroControlador {
	
//	@Autowired
//	private UsuarioServicio usuarioServicio;
	
	@GetMapping("/login")
	public String iniciarSesion(){
		return "login";
	}
	@GetMapping("/") 
	public String verPaginaInicio(Model model){
//		model.addAttribute("usuarios",usuarioServicio.listarUsuarios());
		return "inicio";
	}
}
