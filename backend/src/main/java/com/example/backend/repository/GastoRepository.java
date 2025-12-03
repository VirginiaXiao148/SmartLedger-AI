package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.Gasto;
import java.util.List;
import java.time.LocalDate;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByCategoria(String categoria);

    List<Gasto> findByFechaBetween(LocalDate startDate, LocalDate endDate);

    List<Gasto> findByDescripcionContainingIgnoreCase(String descripcion);
}
