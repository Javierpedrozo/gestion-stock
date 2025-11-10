package com.traversol.gestion_stock.service;

import com.traversol.gestion_stock.model.Producto;
import com.traversol.gestion_stock.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repository;

    public List<Producto> findAll() {
        return repository.findAll();
    }

    public Producto save(Producto producto) {
        producto.setStockActual(producto.getStockInicial());
        return repository.save(producto);
    }

    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }
}