package com.example.backend.controller;

import com.example.backend.model.Presupuesto;
import com.example.backend.repository.PresupuestoRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/presupuesto")
@CrossOrigin(origins = "https://smart-ledger-ai.vercel.app/")
public class PresupuestoController {

    private PresupuestoRepository presupuestoRepository;

    public PresupuestoController(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    @GetMapping
    public Presupuesto getPresupuesto() {
        return presupuestoRepository.findFirstByOrderByIdAsc().orElse(new Presupuesto());
    }

    @PostMapping
    public Presupuesto savePresupuesto(@RequestBody Presupuesto presupuesto) {
        return presupuestoRepository.findFirstByOrderByIdAsc().map(p -> {
            p.setPresupuestoMensual(presupuesto.getPresupuestoMensual());
            return presupuestoRepository.save(p);
        }).orElse(presupuestoRepository.save(presupuesto));
    }
}
