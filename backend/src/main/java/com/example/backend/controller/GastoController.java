package com.example.backend.controller;

import com.example.backend.model.Gasto;
import com.example.backend.service.GastoService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gastos")
@CrossOrigin(origins = "http://localhost:3000")
public class GastoController {

    private GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    // Gemini integration
    @PostMapping("/analizar")
    public ResponseEntity<Gasto> analyzeGasto(@RequestBody String textoOriginal) {
        Gasto gastoAnalizado = gastoService.generateGasto(textoOriginal);
        return ResponseEntity.ok(gastoAnalizado);
    }

    // CRUD
    @GetMapping
    public List<Gasto> findAll() {
        return gastoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gasto> findById(@PathVariable Long id) {
        return gastoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Gasto save(@RequestBody Gasto gasto) {
        return gastoService.save(gasto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gasto> update(@PathVariable Long id, @RequestBody Gasto gasto) {
        Gasto updatedGasto = gastoService.updateGasto(id, gasto);
        if (updatedGasto != null) {
            return ResponseEntity.ok(updatedGasto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Verify if gasto exists
        if (gastoService.findById(id).isPresent()) {
            gastoService.delete(gastoService.findById(id).get());
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }

    // Filters
    @GetMapping("/categoria/{categoria}")
    public List<Gasto> findByCategoria(@PathVariable String categoria) {
        return gastoService.findByCategoria(categoria);
    }

    @GetMapping("/fecha-between")
    public List<Gasto> findByFechaBetween(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return gastoService.findByFechaBetween(startDate, endDate);
    }

    @GetMapping("/buscar")
    public List<Gasto> findByDescripcionContainingIgnoreCase(@PathVariable String descripcion) {
        return gastoService.findByDescripcionContainingIgnoreCase(descripcion);
    }

}
