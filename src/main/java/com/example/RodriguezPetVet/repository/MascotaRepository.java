/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.example.RodriguezPetVet.repository;

import com.example.RodriguezPetVet.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    // Buscar mascotas por nombre (para el buscador)
    List<Mascota> findByNombreContainingIgnoreCase(String nombre);
    
    // Opcional: encontrar por nombre exacto para evitar duplicados
    Optional<Mascota> findByNombre(String nombre);
}
