package com.example.backend.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "configuracion")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double presupuestoMensual;
}
