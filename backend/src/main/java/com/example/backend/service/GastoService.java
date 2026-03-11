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
import com.example.backend.model.Gasto;
import com.example.backend.repository.GastoRepository;
import com.example.backend.repository.PresupuestoRepository;

import com.example.backend.model.Presupuesto;

@Service
public class GastoService {

    private GastoRepository gastoRepository;
    private PresupuestoRepository presupuestoRepository;
    private final Dotenv dotenv;
    private final ObjectMapper objectMapper;

    public GastoService(GastoRepository gastoRepository, PresupuestoRepository presupuestoRepository) {
        this.gastoRepository = gastoRepository;
        this.presupuestoRepository = presupuestoRepository;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load(); // Cargar variables de entorno si se encuentran en
                                                                   // .env
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

    // Comprobar que el presupuesto no se exceda
    public String comprobarEstadoPresupuesto() {
        // 1. Obtener el límite guardado
        BigDecimal limite = presupuestoRepository.findFirstByOrderByIdAsc()
                .map(p -> BigDecimal.valueOf(p.getPresupuestoMensual()))
                .orElse(BigDecimal.ZERO);

        if (limite.compareTo(BigDecimal.ZERO) == 0)
            return "Presupuesto no definido";

        // 2. Calcular gastos del mes actual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<Gasto> gastosMes = gastoRepository.findByFechaBetween(inicioMes, finMes);
        BigDecimal totalGastado = gastosMes.stream()
                .map(Gasto::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Lógica de alertas
        if (totalGastado.compareTo(limite) >= 0) {
            return "¡ALERTA! Has superado tu presupuesto mensual.";
        } else if (totalGastado.compareTo(limite.multiply(new BigDecimal("0.8"))) >= 0) {
            return "Cuidado: Has gastado más del 80% de tu presupuesto.";
        }

        return "Presupuesto bajo control.";
    }

    // Gemini integration
    public Gasto generateGasto(String textoOriginal) {

        String prompt = "Analiza este gasto: '" + textoOriginal + "'. " +
                "Devuelve un JSON con: categoria (Alimentación, Transporte, Ocio, Casa, Otros), " +
                "importe (número), descripcion (texto breve). " +
                "Ejemplo: {\"categoria\": \"Ocio\", \"importe\": 10.5, \"descripcion\": \"Cine\"}";

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
            System.out.println("Respuesta Gemini: " + responseText);

            // Limpiar la respuesta para extraer el JSON
            String jsonLimpio = responseText.replace("```json", "").replace("```", "").trim();
            JsonNode gastoNode = objectMapper.readTree(jsonLimpio);

            Gasto nuevoGasto = Gasto.builder()
                    .textoOriginal(textoOriginal)
                    .categoria(gastoNode.path("categoria").asText("Otros"))
                    .descripcion(gastoNode.path("descripcion").asText("Sin descripción"))
                    .importe(new BigDecimal(gastoNode.path("importe").asText("0.0")))
                    .fecha(LocalDate.now())
                    .build();
            return gastoRepository.save(nuevoGasto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
