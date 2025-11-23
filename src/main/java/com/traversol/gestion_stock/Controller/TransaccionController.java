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

    // Desperdicio (RF3)
    @GetMapping("/desperdicio")
    public String mostrarFormularioDesperdicio() {
        return "form-desperdicio";
    }

    @PostMapping("/desperdicio")
    public String registrarDesperdicio(@RequestParam String sku, @RequestParam int cantidad, @RequestParam String motivo, Model model) {
        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser positiva");
            }
            if (motivo == null || motivo.trim().isEmpty()) {
                throw new IllegalArgumentException("Motivo requerido (SRS RF3)");
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserDetails userDetails = usuarioService.loadUserByUsername(email);
            Usuario usuario = (Usuario) userDetails;

            Optional<Producto> optionalProducto = productoService.findBySku(sku);
            if (optionalProducto.isEmpty()) {
                model.addAttribute("error", "Producto no encontrado con SKU: " + sku);
                return "form-desperdicio";
            }
            Producto producto = optionalProducto.get();

            if (cantidad > producto.getStockActual()) {
                throw new IllegalArgumentException("Cantidad excede stock disponible (SRS RF3 validación)");
            }

            Transaccion transaccion = new Transaccion();
            transaccion.setTipo(TipoTransaccion.DESPERDICIO);
            transaccion.setCantidad(cantidad);
            transaccion.setMotivo(motivo);
            transaccion.setProducto(producto);
            transaccion.setUsuario(usuario);
            transaccionService.save(transaccion);

            productoService.actualizarStock(sku, -cantidad);

            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "form-desperdicio";
        }
    }

    @GetMapping("/ingreso")
    public String mostrarFormularioIngreso() {
        return "form-ingreso";
    }

    @PostMapping("/ingreso")
    public String registrarIngreso(@RequestParam String sku, @RequestParam int cantidad, @RequestParam String motivo, Model model) {
        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser positiva");
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserDetails userDetails = usuarioService.loadUserByUsername(email);
            Usuario usuario = (Usuario) userDetails;

            Optional<Producto> optionalProducto = productoService.findBySku(sku);
            if (optionalProducto.isEmpty()) {
                model.addAttribute("error", "Producto no encontrado con SKU: " + sku);
                return "form-ingreso";
            }
            Producto producto = optionalProducto.get();

            Transaccion transaccion = new Transaccion();
            transaccion.setTipo(TipoTransaccion.INGRESO);
            transaccion.setCantidad(cantidad);
            transaccion.setMotivo(motivo);
            transaccion.setProducto(producto);
            transaccion.setUsuario(usuario);
            transaccionService.save(transaccion);

            productoService.actualizarStock(sku, cantidad);

            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "form-ingreso";
        }
    }

    @GetMapping("/egreso")
    public String mostrarFormularioEgreso() {
        return "form-egreso";
    }

    @PostMapping("/egreso")
    public String registrarEgreso(@RequestParam String sku, @RequestParam int cantidad, @RequestParam String motivo, Model model) {
        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser positiva");
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserDetails userDetails = usuarioService.loadUserByUsername(email);
            Usuario usuario = (Usuario) userDetails;

            Optional<Producto> optionalProducto = productoService.findBySku(sku);
            if (optionalProducto.isEmpty()) {
                model.addAttribute("error", "Producto no encontrado con SKU: " + sku);
                return "form-egreso";
            }
            Producto producto = optionalProducto.get();

            if (cantidad > producto.getStockActual()) {
                throw new IllegalArgumentException("Cantidad excede stock disponible (SRS RF2 validación)");
            }

            Transaccion transaccion = new Transaccion();
            transaccion.setTipo(TipoTransaccion.EGRESO);
            transaccion.setCantidad(cantidad);
            transaccion.setMotivo(motivo);
            transaccion.setProducto(producto);
            transaccion.setUsuario(usuario);
            transaccionService.save(transaccion);

            productoService.actualizarStock(sku, -cantidad);

            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "form-egreso";
        }
    }
}