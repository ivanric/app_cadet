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
@RequestMapping("/catalogos")
public class CatalogosController {

    @RequestMapping("/gestion") 
    public String alamat(Model model) {
    	System.out.println("LLEGOOO cat");
        return "catalogos/gestion";

    }

} 
