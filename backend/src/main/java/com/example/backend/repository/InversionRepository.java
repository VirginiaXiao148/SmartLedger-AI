package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Inversion;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InversionRepository extends JpaRepository<Inversion, Long> {

    List<Inversion> findByCategoria(String categoria);

    List<Inversion> findByFechaBetween(LocalDate startDate, LocalDate endDate);

    List<Inversion> findByDescripcionContainingIgnoreCase(String descripcion);
}
