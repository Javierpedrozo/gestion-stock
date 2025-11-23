package com.traversol.gestion_stock.service;

import com.traversol.gestion_stock.model.*;
import com.traversol.gestion_stock.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository repository;

    @Autowired
    private ProductoService productoService;

    public Transaccion save(Transaccion transaccion) {
        return repository.save(transaccion);
    }

    public List<DesperdicioSemanal> getDesperdicioSemanal() {
        // Calcula fechas para la última semana (SRS: reportes en periodo determinado)
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioSemana = ahora.minus(7, ChronoUnit.DAYS)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finSemana = ahora.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // Llamamos al repositorio CON argumentos
        List<Object[]> dataList = repository.getDesperdicioSemanal(inicioSemana, finSemana);

        // Mapea a DTO (maneja nulls si no hay datos)
        return dataList.stream()
                .map(data -> {
                    String producto = (String) data[0];
                    Integer cantidad = (Integer) data[1];
                    Long total = (data[2] != null) ? ((Number) data[2]).longValue() : 0L;
                    return new DesperdicioSemanal(producto, cantidad, total);
                })
                .collect(Collectors.toList());
    }

    // Método para registrar ingreso (tu código, sin cambios)
    public void registrarIngreso(String sku, int cantidad, String motivo, Usuario usuario) {
        Optional<Producto> optionalProducto = productoService.findBySku(sku);
        if (optionalProducto.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado con SKU: " + sku);
        }
        Producto producto = optionalProducto.get();

        // Crea transacción con enum calificado
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo(TipoTransaccion.INGRESO); // Asume TipoTransaccion.INGRESO si es el enum
        transaccion.setCantidad(cantidad);
        transaccion.setMotivo(motivo);
        transaccion.setProducto(producto);
        transaccion.setUsuario(usuario);
        save(transaccion);

        // Actualiza stock (incrementa, SRS RF1 tiempo real)
        productoService.actualizarStock(sku, cantidad);
    }
}