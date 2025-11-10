package com.traversol.gestion_stock.repository;

import com.traversol.gestion_stock.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    boolean existsBySku(String sku);
}