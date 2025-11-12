package com.traversol.gestion_stock.service;

import com.traversol.gestion_stock.model.Producto;
import com.traversol.gestion_stock.model.TipoTransaccion;
import com.traversol.gestion_stock.model.Transaccion;
import com.traversol.gestion_stock.model.Usuario;
import com.traversol.gestion_stock.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ProductoService productoService;



    public void registrarTransaccion(String sku, Integer cantidad, String motivo, TipoTransaccion tipo, Usuario usuario) {
        // Busca el producto por SKU
        Producto producto = productoService.findBySku(sku);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con SKU: " + sku);
        }

        // Valida stock para egreso
        if (tipo == TipoTransaccion.EGRESO && cantidad > producto.getStockActual()) {
            throw new IllegalArgumentException("Stock insuficiente para egreso");
        }

        // Actualiza stock
        if (tipo == TipoTransaccion.INGRESO) {
            producto.setStockActual(producto.getStockActual() + cantidad);
        } else if (tipo == TipoTransaccion.EGRESO) {
            producto.setStockActual(producto.getStockActual() - cantidad);
        }

        // Crea la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo(tipo);
        transaccion.setCantidad(cantidad);
        transaccion.setMotivo(motivo);
        transaccion.setProducto(producto);
        transaccion.setUsuario(usuario);

        // Guarda todo
        productoService.save(producto);
        transaccionRepository.save(transaccion);
    }
}