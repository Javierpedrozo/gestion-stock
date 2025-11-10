package com.traversol.gestion_stock.Controller;

import com.traversol.gestion_stock.model.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("usuario", usuario);
        return "home";
    }
}