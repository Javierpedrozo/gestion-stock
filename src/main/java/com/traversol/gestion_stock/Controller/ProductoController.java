package com.traversol.gestion_stock.Controller;


import com.traversol.gestion_stock.model.Producto;
import com.traversol.gestion_stock.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());  // Envía la lista de productos al HTML
        return "lista-productos";  // Nombre del archivo HTML sin .html
    }
}