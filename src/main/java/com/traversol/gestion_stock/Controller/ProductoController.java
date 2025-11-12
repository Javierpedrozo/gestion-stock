package com.traversol.gestion_stock.Controller;

import com.traversol.gestion_stock.model.Producto;
import com.traversol.gestion_stock.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "lista-productos";
    }

    // Nuevo: Mostrar formulario para agregar producto (RF1)
    @GetMapping("/agregar-producto")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("producto", new Producto());
        return "agregar-producto";  // Vista Thymeleaf
    }

    // Nuevo: Procesar el formulario y guardar (con validaciones del SRS)
    @PostMapping("/agregar-producto")
    public String agregarProducto(@Valid @ModelAttribute Producto producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "agregar-producto";
        }
        if (productoService.existsBySku(producto.getSku())) {
            model.addAttribute("error", "SKU ya existe");
            return "agregar-producto";
        }
        try {
            productoService.save(producto);  // Inicializa stockActual = stockInicial
            return "redirect:/productos";  // Redirige a la lista después de guardar
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            return "agregar-producto";
        }
    }
}