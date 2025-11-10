package com.traversol.gestion_stock.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "unidad_medida", nullable = false)
    private String unidadMedida;

    @Column(name = "stock_actual")
    private Integer stockActual = 0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 0;

    @Column(name = "stock_inicial")
    private Integer stockInicial = 0;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion = Instant.now();
}