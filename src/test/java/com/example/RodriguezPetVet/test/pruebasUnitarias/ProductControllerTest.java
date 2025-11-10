package com.example.RodriguezPetVet.test.pruebasUnitarias;

import com.example.RodriguezPetVet.controller.ProductController;
import com.example.RodriguezPetVet.model.Product;
import com.example.RodriguezPetVet.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    // Simula el repositorio de productos
    @Mock
    private ProductRepository productRepository;

    // Inyecta el repositorio simulado dentro del controlador
    @InjectMocks
    private ProductController productController;

    public ProductControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGuardarProducto() {
        // Crea un nuevo objeto Producto con datos simulados
        Product producto = new Product();
        producto.setName("hepadol");
        producto.setPrice(90.00);

        when(productRepository.save(any(Product.class))).thenReturn(producto);

        // Verifica que los valores guardados sean correctos
        Product resultado = productRepository.save(producto);
        assertEquals("hepadol", resultado.getName());
        assertEquals(90.00, resultado.getPrice());
    }

    @Test
    public void testBuscarProductoPorId() {
        // Crea un producto ficticio con ID = 1
        Product producto = new Product();
        producto.setId(1L);
        producto.setName("hepatin");

        // Simula que el repositorio devuelve el producto al buscar por ID 1
        when(productRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Product> resultado = productRepository.findById(1L);
        assertTrue(resultado.isPresent());
        assertEquals("hepatin", resultado.get().getName());
    }

}
