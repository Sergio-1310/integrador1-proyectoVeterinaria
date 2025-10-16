/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.controller;

import com.example.RodriguezPetVet.model.Product;
import com.example.RodriguezPetVet.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    

   

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

    
    @GetMapping("/main/new")
    public String showNewProductForm(Model model) {
        // Objeto vacío para el formulario de creación
        model.addAttribute("product", new Product()); 
        
        
        model.addAttribute("lowStockProducts", productRepository.findLowStockProducts());
        
        return "main"; 
    }

    
    @GetMapping("/main/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de producto no válido:" + id));
        
        model.addAttribute("product", product);
        
       
        model.addAttribute("lowStockProducts", productRepository.findLowStockProducts());
        
        return "main"; 
    }

    
    @PostMapping("/main/save")
    public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes ra) {
        productRepository.save(product);
        
        String action = (product.getId() == null) ? "creado" : "actualizado";
        ra.addFlashAttribute("message", "Producto " + action + " exitosamente.");
        
       
        return "redirect:/inventory"; 
    }
    
    
    @PostMapping("/inventory/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        productRepository.deleteById(id);
        ra.addFlashAttribute("message", "Producto eliminado exitosamente.");
        return "redirect:/inventory";
    }

    
    @PostMapping("/main/sell")
    public String processSale(@RequestParam Long productId, @RequestParam Integer amount, RedirectAttributes ra) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ID de producto no válido para la venta:" + productId));

        if (product.getQuantity() >= amount) {
            product.setQuantity(product.getQuantity() - amount);
            productRepository.save(product);
            ra.addFlashAttribute("message", "Venta de " + amount + " unidades de " + product.getName() + " procesada.");
        } else {
            ra.addFlashAttribute("message", "Error: Stock insuficiente para la venta.");
        }
        
        
        return "redirect:/main/" + productId;
    }
}
