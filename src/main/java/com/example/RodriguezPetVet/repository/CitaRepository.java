/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.example.RodriguezPetVet.repository;

import com.example.RodriguezPetVet.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Método personalizado para verificar si ya existe una cita en una fecha y hora específicas
    // Spring Data JPA generará automáticamente esta consulta
    Optional<Cita> findByFechaHora(Date fechaHora);
}
