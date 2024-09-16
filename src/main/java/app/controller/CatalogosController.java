package app.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/catalogos")
public class CatalogosController {

    @RequestMapping("/gestion") 
    public String alamat(Model model) {
    	System.out.println("LLEGOOO cat");
        return "catalogos/gestion_drive";

    }

} 
