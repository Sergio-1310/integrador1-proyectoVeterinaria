package com.example.RodriguezPetVet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.RodriguezPetVet.model.Product;
import com.example.RodriguezPetVet.repository.ProductRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductRepository productRepository;

    // Obtener todos los productos
    @GetMapping
    public List<Product> getAllProductos() {
        // Devuelve una lista con todos los productos en la base de datos
        return productRepository.findAll();
    }

    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductoById(@PathVariable Long id) {
        // Busca el producto por ID
        Optional<Product> producto = productRepository.findById(id);

        // Si existe, devuelve el producto, si no, un código 404
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo producto
    @PostMapping
    public Product createProducto(@RequestBody Product product) {
        // Guarda un nuevo producto en la base de datos
        return productRepository.save(product);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProducto(@PathVariable Long id, @RequestBody Product detallesProducto) {
        // Busca el producto existente
        Optional<Product> productoExistente = productRepository.findById(id);

        if (productoExistente.isPresent()) {
            Product producto = productoExistente.get();
            producto.setName(detallesProducto.getName());
            producto.setPrice(detallesProducto.getPrice());
            producto.setQuantity(detallesProducto.getQuantity());

            // Guarda los cambios
            Product actualizado = productRepository.save(producto);
            return ResponseEntity.ok(actualizado);
        } else {
            // Si no existe el producto, devuelve 404
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        // Verifica si el producto existe
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            // Devuelve código 204 si se eliminó correctamente
            return ResponseEntity.noContent().build();
        } else {
            // Si no existe, devuelve 404
            return ResponseEntity.notFound().build();
        }
    }
}