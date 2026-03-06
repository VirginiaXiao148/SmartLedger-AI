package com.example.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import io.github.cdimascio.dotenv.Dotenv;
import com.example.backend.model.Inversion;
import com.example.backend.repository.InversionRepository;

@Service
public class InversionService {

    private InversionRepository inversionRepository;
    private final Dotenv dotenv;
    private final ObjectMapper objectMapper;

    public InversionService(InversionRepository inversionRepository) {
        this.inversionRepository = inversionRepository;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.objectMapper = new ObjectMapper();
    }

    // CRUD
    public List<Inversion> findAll() {
        return inversionRepository.findAll();
    }

    public Inversion save(Inversion inversion) {
        return inversionRepository.save(inversion);
    }

    public Optional<Inversion> findById(Long id) {
        return inversionRepository.findById(id);
    }

    public void delete(Inversion inversion) {
        inversionRepository.delete(inversion);
    }

    public List<Inversion> findByCategoria(String categoria) {
        return inversionRepository.findByCategoria(categoria);
    }

    public List<Inversion> findByFechaBetween(LocalDate startDate, LocalDate endDate) {
        return inversionRepository.findByFechaBetween(startDate, endDate);
    }

    public List<Inversion> findByDescripcionContainingIgnoreCase(String descripcion) {
        return inversionRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }

    public Inversion updateInversion(Long id, Inversion inversion) {
        Inversion existingInversion = inversionRepository.findById(id).orElse(null);
        if (existingInversion != null) {
            existingInversion.setCategoria(inversion.getCategoria());
            existingInversion.setImporte(inversion.getImporte());
            existingInversion.setDescripcion(inversion.getDescripcion());
            existingInversion.setFecha(inversion.getFecha());
            existingInversion.setTextoOriginal(inversion.getTextoOriginal());
            return inversionRepository.save(existingInversion);
        }
        return null;
    }

    // Gemini integration
    public Inversion generateInversion(String textoOriginal) {

        String prompt = "Analiza esta inversión: '" + textoOriginal + "'. " +
                "Devuelve un JSON con: categoria (Acciones, Criptomonedas, Bienes Raíces, Bonos, Otros), " +
                "importe (número), descripcion (texto breve). " +
                "Ejemplo: {\"categoria\": \"Acciones\", \"importe\": 500, \"descripcion\": \"Compra de Apple\"}";

        try {
            String apiKey = System.getenv("GEMINI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                apiKey = dotenv.get("GEMINI_API_KEY");
            }
            if (apiKey == null || apiKey.isEmpty()) {
                throw new RuntimeException("Falta la API KEY en .env");
            }
            Client client = Client.builder()
                    .apiKey(apiKey)
                    .build();
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null);

            String responseText = response.text();
            System.out.println("Respuesta Gemini (Inversiones): " + responseText);

            // Limpiar la respuesta para extraer el JSON
            String jsonLimpio = responseText.replace("```json", "").replace("```", "").trim();
            JsonNode inversionNode = objectMapper.readTree(jsonLimpio);

            Inversion nuevaInversion = Inversion.builder()
                    .textoOriginal(textoOriginal)
                    .categoria(inversionNode.path("categoria").asText("Otros"))
                    .descripcion(inversionNode.path("descripcion").asText("Sin descripción"))
                    .importe(new BigDecimal(inversionNode.path("importe").asText("0.0")))
                    .fecha(LocalDate.now())
                    .build();
            return inversionRepository.save(nuevaInversion);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
