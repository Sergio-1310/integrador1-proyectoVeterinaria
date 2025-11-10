package com.example.RodriguezPetVet.test.pruebasUnitarias;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.mock.web.MockMultipartFile;

import com.example.RodriguezPetVet.service.MascotaService;

public class MascotaServiceTest {
    private MascotaService mascotaService;

    @BeforeEach
    public void setUp() {
        mascotaService = new MascotaService();
    }

    @Test
    public void testGuardarImagen() throws IOException {
        // Crear un archivo de imagen simulado (no real)
        byte[] contenido = "imagen falsa".getBytes();
        MockMultipartFile archivo = new MockMultipartFile("file", "foto.png", "image/png", contenido);

        // Simulamos el método FileUtils.copyInputStreamToFile para no crear archivos
        // reales
        try (MockedStatic<FileUtils> fileUtilsMock = org.mockito.Mockito.mockStatic(FileUtils.class)) {
            fileUtilsMock
                    .when(() -> FileUtils.copyInputStreamToFile(org.mockito.ArgumentMatchers.any(),
                            org.mockito.ArgumentMatchers.any(File.class)))
                    .thenAnswer(invocation -> null); // no hace nada

            // Llamamos al método real
            String rutaGuardada = mascotaService.guardarImagen(archivo);

            // Validamos resultados
            assertNotNull(rutaGuardada);
            assertTrue(rutaGuardada.startsWith("/uploads/"));
            assertTrue(rutaGuardada.endsWith(".png"));
        }
    }
}
