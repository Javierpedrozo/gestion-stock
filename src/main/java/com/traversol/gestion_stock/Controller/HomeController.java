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
        if (usuario == null) {
            return "redirect:/login";  // Redirige si no autenticado
        }
        model.addAttribute("usuario", usuario);
        // Opcional: Lógica por rol (usa Usuario.Rol para el enum anidado)
        if (usuario.getRol() == Usuario.Rol.GERENTE) {
            // Ejemplo: Agrega alertas o datos extras para Gerente
            // model.addAttribute("alertas", alertaService.getAlertasStockBajo()); // Si implementas RF4
        }
        return "home";
    }
}