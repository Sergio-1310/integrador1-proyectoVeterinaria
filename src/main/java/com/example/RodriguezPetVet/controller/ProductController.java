/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.controller;

import com.example.RodriguezPetVet.model.Product;
import com.example.RodriguezPetVet.repository.ProductRepository;
import com.example.RodriguezPetVet.service.ReporteVentaPdfService;
import com.example.RodriguezPetVet.service.ReporteInventarioService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReporteInventarioService reporteInventarioService;

    @Autowired
    private ReporteVentaPdfService reporteVentaPdfService;

    // mostrar el inventario general
    @GetMapping("/inventory")
    public String viewInventoryPage(Model model, @RequestParam(required = false) String keyword) {
        List<Product> products;

        // Manejar la BÚSQUEDA
        if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        return "inventory"; // Nombre del archivo Thymeleaf
    }

    // crear nuevo producto
    @GetMapping("/productos/new")
    public String showNewProductForm(Model model) {
        // Objeto vacío para el formulario de creación
        model.addAttribute("product", new Product());

        model.addAttribute("lowStockProducts", productRepository.findLowStockProducts());

        return "producto_editar";
    }

    // editar productos existentes
    @GetMapping("/productos/editar/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de producto no válido:" + id));

        model.addAttribute("product", product);

        model.addAttribute("lowStockProducts", productRepository.findLowStockProducts());

        return "producto_editar";
    }

    // Guardar cambios
    @PostMapping("/productos/save")
    public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes ra) {
        productRepository.save(product);

        String action = (product.getId() == null) ? "creado" : "actualizado";
        ra.addFlashAttribute("message", "Producto " + action + " exitosamente.");

        return "redirect:/inventory";
    }

    // eliminar productos
    @PostMapping("/inventory/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        productRepository.deleteById(id);
        ra.addFlashAttribute("message", "Producto eliminado exitosamente.");
        return "redirect:/inventory";
    }

    // mostrar vista de venta
    @GetMapping("/productos/vender/{id}")
    public String showVentaForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido para venta: " + id));
        model.addAttribute("product", product);
        return "venta"; // vista venta.html
    }

    // Procesar venta y generar PDF
    @PostMapping("/productos/procesar_venta")
    public ResponseEntity<InputStreamResource> processSale(
            @RequestParam Long productId,
            @RequestParam Integer amount) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + productId));

        if (product.getQuantity() < amount) {
            throw new IllegalArgumentException("Stock insuficiente para la venta.");
        }

        // Actualizar stock
        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);

        // Generar PDF directamente
        ByteArrayInputStream pdfStream = reporteVentaPdfService.generarReporteVenta(product, amount);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=comprobante_venta.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    // reporte en excel
    @GetMapping("/inventario/reporte")
    public ResponseEntity<InputStreamResource> generarReporteInventario() {
        try {
            // Generar el archivo Excel en memoria
            ByteArrayInputStream excelStream = reporteInventarioService.generarReporteInventario();

            // Guardar archivo físicamente en /static/uploads/reportes/
            Path reportDir = Paths.get("src/main/resources/static/uploads/reportes");
            if (!Files.exists(reportDir)) {
                FileUtils.forceMkdir(reportDir.toFile()); // Crea carpeta si no existe
            }

            String fileName = "reporte_inventario.xlsx";
            File outputFile = reportDir.resolve(fileName).toFile();

            // Guarda el archivo Excel en el disco
            FileUtils.copyInputStreamToFile(excelStream, outputFile);
            System.out.println("✅ Reporte guardado en: " + outputFile.getAbsolutePath());

            // volver a crear el stream para la descarga
            ByteArrayInputStream downloadStream = new ByteArrayInputStream(Files.readAllBytes(outputFile.toPath()));

            // Configurar cabeceras HTTP para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + fileName);

            // Devolver respuesta para descarga
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(downloadStream));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error al generar o guardar el reporte", e);
        }
    }
}
