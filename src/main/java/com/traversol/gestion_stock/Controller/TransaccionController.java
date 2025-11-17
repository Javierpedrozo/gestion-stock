package com.traversol.gestion_stock.Controller;

import com.traversol.gestion_stock.model.TipoTransaccion;
import com.traversol.gestion_stock.model.Transaccion;
import com.traversol.gestion_stock.model.Producto;
import com.traversol.gestion_stock.model.Usuario;
import com.traversol.gestion_stock.service.ProductoService;
import com.traversol.gestion_stock.service.TransaccionService;
import com.traversol.gestion_stock.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class TransaccionController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private UsuarioService usuarioService;

    // Nuevo: Agrega @GetMapping para mostrar el form (faltaba, para GET /ingreso)
    @GetMapping("/ingreso")
    public String mostrarFormularioIngreso() {
        return "form-ingreso";  // Nombre de tu vista
    }

    @PostMapping("/ingreso")
    public String registrarIngreso(@RequestParam String sku, @RequestParam int cantidad, @RequestParam String motivo, Model model) {
        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser positiva (SRS RF1 validación)");
            }

            // Obtén usuario actual (fix: usa UserDetails y cast a Usuario, ya que loadUserByUsername retorna UserDetails)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserDetails userDetails = usuarioService.loadUserByUsername(email);
            Usuario usuario = (Usuario) userDetails;  // Cast seguro si Usuario implements UserDetails

            // Busca producto
            Optional<Producto> optionalProducto = productoService.findBySku(sku);
            if (optionalProducto.isEmpty()) {
                model.addAttribute("error", "Producto no encontrado con SKU: " + sku);
                return "form-ingreso";
            }
            Producto producto = optionalProducto.get();

            // Crea y guarda transacción
            Transaccion transaccion = new Transaccion();
            transaccion.setTipo(TipoTransaccion.INGRESO);  // Asume Transaccion.Tipo si es enum anidado
            transaccion.setCantidad(cantidad);
            transaccion.setMotivo(motivo);
            transaccion.setProducto(producto);
            transaccion.setUsuario(usuario);
            transaccionService.save(transaccion);

            // Actualiza stock (incrementa)
            productoService.actualizarStock(sku, cantidad);

            return "redirect:/home";  // Redirige a dashboard (cambia si es /dashboard)
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", "Usuario no encontrado: " + e.getMessage());
            return "form-ingreso";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "form-ingreso";
        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return "form-ingreso";
        }
    }
}