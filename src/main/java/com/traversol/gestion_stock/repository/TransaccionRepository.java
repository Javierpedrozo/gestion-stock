package com.traversol.gestion_stock.repository;

import com.traversol.gestion_stock.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    @Query("SELECT t FROM Transaccion t WHERE t.fecha >= :fechaInicio AND t.fecha < :fechaFin")
    List<Transaccion> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);


    @Query("SELECT p.nombre, SUM(t.cantidad), COUNT(t) FROM Transaccion t JOIN t.producto p " +
            "WHERE t.tipo = 'DESPERDICIO' AND t.fecha BETWEEN :inicio AND :fin GROUP BY p.nombre")
    List<Object[]> getDesperdicioSemanal(LocalDateTime inicio, LocalDateTime fin);
}