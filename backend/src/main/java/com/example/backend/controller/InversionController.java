package com.example.backend.controller;

import com.example.backend.model.Inversion;
import com.example.backend.service.InversionService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inversiones")
@CrossOrigin(origins = "https://smart-ledger-ai.vercel.app/")
public class InversionController {

    private InversionService inversionService;

    public InversionController(InversionService inversionService) {
        this.inversionService = inversionService;
    }

    // Gemini integration
    @PostMapping("/analizar")
    public ResponseEntity<Inversion> analyzeInversion(@RequestBody String textoOriginal) {
        Inversion inversionAnalizada = inversionService.generateInversion(textoOriginal);
        System.out.println("Inversion generada: " + inversionAnalizada);
        return ResponseEntity.ok(inversionAnalizada);
    }

    // CRUD
    @GetMapping
    public List<Inversion> findAll() {
        return inversionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inversion> findById(@PathVariable Long id) {
        return inversionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inversion save(@RequestBody Inversion inversion) {
        return inversionService.save(inversion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inversion> update(@PathVariable Long id, @RequestBody Inversion inversion) {
        Inversion updatedInversion = inversionService.updateInversion(id, inversion);
        if (updatedInversion != null) {
            return ResponseEntity.ok(updatedInversion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inversionService.findById(id).isPresent()) {
            inversionService.delete(inversionService.findById(id).get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Filters
    @GetMapping("/categoria/{categoria}")
    public List<Inversion> findByCategoria(@PathVariable String categoria) {
        return inversionService.findByCategoria(categoria);
    }

    @GetMapping("/fecha-between")
    public List<Inversion> findByFechaBetween(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return inversionService.findByFechaBetween(startDate, endDate);
    }

    @GetMapping("/buscar")
    public List<Inversion> findByDescripcionContainingIgnoreCase(@RequestParam String texto) {
        return inversionService.findByDescripcionContainingIgnoreCase(texto);
    }

}
