package com.traversol.gestion_stock.service;

import com.traversol.gestion_stock.model.TipoTransaccion;
import com.traversol.gestion_stock.model.Transaccion;
import com.traversol.gestion_stock.model.Producto;
import com.traversol.gestion_stock.model.Usuario;
import com.traversol.gestion_stock.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransaccionService {
    @Autowired
    private TransaccionRepository repository;

    @Autowired
    private ProductoService productoService;

    public Transaccion save(Transaccion transaccion) {
        return repository.save(transaccion);
    }

    // Método para registrar ingreso (línea 34: usa Transaccion.Tipo.INGRESO)
    public void registrarIngreso(String sku, int cantidad, String motivo, Usuario usuario) {
        Optional<Producto> optionalProducto = productoService.findBySku(sku);
        if (optionalProducto.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado con SKU: " + sku);
        }
        Producto producto = optionalProducto.get();

        // Crea transacción con enum calificado
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo(TipoTransaccion.INGRESO);  // Fix: Transaccion.Tipo en lugar de Tipo
        transaccion.setCantidad(cantidad);
        transaccion.setMotivo(motivo);
        transaccion.setProducto(producto);
        transaccion.setUsuario(usuario);
        save(transaccion);

        // Actualiza stock (incrementa, SRS RF1 tiempo real)
        productoService.actualizarStock(sku, cantidad);
    }
}