package com.example.RodriguezPetVet.test.pruebasUnitarias;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.RodriguezPetVet.model.Product;
import com.example.RodriguezPetVet.repository.ProductRepository;
import com.example.RodriguezPetVet.service.ReporteInventarioService;

public class ReporteInventarioServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReporteInventarioService reporteInventarioService;

    public ReporteInventarioServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerarReporteInventario() throws IOException {
        // Crear productos falsos
        Product producto1 = new Product();
        producto1.setId(1L);
        producto1.setName("helado de yogurt");
        producto1.setPrice(25.0);
        producto1.setQuantity(50);

        Product producto2 = new Product();
        producto2.setId(2L);
        producto2.setName("proplan");
        producto2.setPrice(129.0);
        producto2.setQuantity(15);

        when(productRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        // Llamar al método a probar
        ByteArrayInputStream reporte = reporteInventarioService.generarReporteInventario();

        // Verificar que el resultado no es nulo y es un archivo Excel válido
        assertNotNull(reporte);
        assertTrue(reporte.available() > 0);

        // Validar que efectivamente se puede abrir como archivo Excel
        assertDoesNotThrow(() -> WorkbookFactory.create(reporte));
    }
}
