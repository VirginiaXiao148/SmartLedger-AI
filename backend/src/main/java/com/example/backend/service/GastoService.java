package com.example.backend.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import com.example.backend.model.Gasto;
import com.example.backend.repository.GastoRepository;

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
        try {
            // Gemini API key
            String apiKey = dotenv.get("GEMINI_API_KEY");
            // Gemini API endpoint
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
            // Create HTTP client
            HttpClient client = HttpClient.newHttpClient();
            
            // Gemini prompt
            String prompt = "Analiza este gasto: '" + textoOriginal.replace("\"", "") + "'. " +
                    "Devuelve un JSON con: categoria (Alimentación, Transporte, Ocio, Casa, Otros), " +
                    "importe (número), descripcion (texto breve). " +
                    "Ejemplo: {\"categoria\": \"Ocio\", \"importe\": 10.5, \"descripcion\": \"Cine\"}";
            
            // Request body
            String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

            // Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Process response
            JsonNode jsonNode = objectMapper.readTree(response.body());
            String contentNode = jsonNode.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
            String content = contentNode.replace("```json", "").replace("```", "").trim();

            // Parse JSON
            JsonNode gastoNode = objectMapper.readTree(content);

            // Create Gasto object
            return Gasto.builder()
                    .textoOriginal(textoOriginal)
                    .categoria(gastoNode.get("categoria").asText())
                    .descripcion(gastoNode.get("descripcion").asText())
                    .importe(new BigDecimal(gastoNode.get("importe").asText()))
                    .fecha(LocalDate.now())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error conectando con Gemini: " + e.getMessage());
        }
    }

}
