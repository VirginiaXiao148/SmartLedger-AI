package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String categoria;
    
    @NotNull(message = "El importe no puede ser nulo")
    @Positive(message = "El importe debe ser positivo")
    @Column(precision = 10, scale = 2) // 10 dígitos en total, 2 decimales
    private BigDecimal importe;
    
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @Builder.Default
    private LocalDate fecha = LocalDate.now();

    @Column(name = "texto_original")
    private String textoOriginal;
}
