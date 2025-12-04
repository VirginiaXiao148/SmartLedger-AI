package com.example.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import io.github.cdimascio.dotenv.Dotenv;
import main.java.com.example.backend.model.Gasto;
import main.java.com.example.backend.repository.GastoRepository;

@Service
public class GastoService {

    private GastoRepository gastoRepository;
    private final Dotenv dotenv;
    private final ObjectMapper objectMapper;

    public GastoService(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
        this.dotenv = Dotenv.load(); // Cargar variables de entorno
        this.objectMapper = new ObjectMapper(); // Para manejar JSON
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

    // Gemini integration
    public Gasto generateGasto(String textoOriginal) {
        Client client = new Client();

        String prompt = "Analiza este gasto: '" + textoOriginal + "'. " +
                    "Devuelve un JSON con: categoria (Alimentación, Transporte, Ocio, Casa, Otros), " +
                    "importe (número), descripcion (texto breve). " +
                    "Ejemplo: {\"categoria\": \"Ocio\", \"importe\": 10.5, \"descripcion\": \"Cine\"}";

        GenerateContentResponse response =
            client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                null);

        System.out.println(response.text());

        return objectMapper.readValue(response.text(), Gasto.class);
    }

}
