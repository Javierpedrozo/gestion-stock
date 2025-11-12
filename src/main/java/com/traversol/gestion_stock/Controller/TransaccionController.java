package com.traversol.gestion_stock.Controller;

import com.traversol.gestion_stock.model.TipoTransaccion;
import com.traversol.gestion_stock.model.Usuario;
import com.traversol.gestion_stock.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/ingreso")
    public String formIngreso() {
        return "form-ingreso";
    }

    @PostMapping("/ingreso")
    public String procesarIngreso(
            @RequestParam("sku") String sku,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam(value = "motivo", required = false) String motivo,
            @AuthenticationPrincipal Usuario usuario,
            Model model) {
        try {
            transaccionService.registrarTransaccion(sku, cantidad, motivo, TipoTransaccion.INGRESO, usuario);
            model.addAttribute("mensaje", "Ingreso registrado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "form-ingreso"; // Vuelve al form si error
        }
        return "redirect:/home"; // Redirige al dashboard
    }

    @GetMapping("/egreso")
    public String formEgreso() {
        return "form-egreso";
    }

    @PostMapping("/egreso")

    public String procesarEgreso(
            @RequestParam("sku") String sku,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam(value = "motivo", required = false) String motivo,
            @AuthenticationPrincipal Usuario usuario,
            Model model) {
        try {
            transaccionService.registrarTransaccion(sku, cantidad, motivo, TipoTransaccion.EGRESO, usuario);
            model.addAttribute("mensaje", "Egreso registrado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "form-egreso"; // Vuelve al form si error
        }
        return "redirect:/home"; // Redirige al dashboard
    }
}