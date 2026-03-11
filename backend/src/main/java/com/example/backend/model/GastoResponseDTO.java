package com.example.backend.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GastoResponseDTO {
    private Gasto gasto;
    private String mensajeAlerta;
}
