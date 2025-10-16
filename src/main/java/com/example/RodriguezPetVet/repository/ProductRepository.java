/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.example.RodriguezPetVet.repository;

import com.example.RodriguezPetVet.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Método para la alerta de BAJO STOCK (stock < 10)
    @Query("SELECT p FROM Product p WHERE p.quantity < 10")
    List<Product> findLowStockProducts();
}

