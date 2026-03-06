package com.example.backend.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "configuracion")
public class Configuracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double presupuestoMensual;
}
