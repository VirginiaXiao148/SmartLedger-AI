package com.example.backend.service;

import com.example.backend.model.Gasto;
import com.example.backend.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    public GastoService(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

    // CRUD
    public List<Gasto> findAll() {
        return gastoRepository.findAll();
    }

    public Gasto save(Gasto gasto) {
        return gastoRepository.save(gasto);
    }

    public Optional<Gasto> findById(Long id) {
        return gastoRepository.findById(id);
    }

    public void delete(Gasto gasto) {
        gastoRepository.delete(gasto);
    }

    public List<Gasto> findByCategoria(String categoria) {
        return gastoRepository.findByCategoria(categoria);
    }

    public List<Gasto> findByFechaBetween(LocalDate startDate, LocalDate endDate) {
        return gastoRepository.findByFechaBetween(startDate, endDate);
    }

    public List<Gasto> findByDescripcionContainingIgnoreCase(String descripcion) {
        return gastoRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }

    public Gasto updateGasto(Long id, Gasto gasto) {
        Gasto existingGasto = gastoRepository.findById(id).orElse(null);
        if (existingGasto != null) {
            existingGasto.setCategoria(gasto.getCategoria());
            existingGasto.setImporte(gasto.getImporte());
            existingGasto.setDescripcion(gasto.getDescripcion());
            existingGasto.setFecha(gasto.getFecha());
            existingGasto.setTextoOriginal(gasto.getTextoOriginal());
            return gastoRepository.save(existingGasto);
        }
        return null;
    }

    // Gemini
    public Gasto generateGasto(String textoOriginal) {
        return textoOriginal;
    }

}
