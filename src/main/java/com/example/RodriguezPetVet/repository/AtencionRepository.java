/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.example.RodriguezPetVet.repository;

import com.example.RodriguezPetVet.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {
    // Buscar atenciones por ID de mascota
    List<Atencion> findByMascotaIdOrderByFechaAtencionDesc(Long mascotaId);
}