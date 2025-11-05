/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.example.RodriguezPetVet.repository;

import com.example.RodriguezPetVet.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Date;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {
@Query(value = "SELECT " +
               "YEAR(c.fecha_hora) AS anio, " + // ¡USAR fecha_hora!
               "MONTH(c.fecha_hora) AS mes, " + // ¡USAR fecha_hora!
               "COUNT(c.id) AS total " +
               "FROM Cita c " +
               "GROUP BY YEAR(c.fecha_hora), MONTH(c.fecha_hora) " + 
               "ORDER BY anio, mes",
       nativeQuery = true)
List<Object[]> countCitasByMonth();
    // Método personalizado para verificar si ya existe una cita en una fecha y hora específicas
    // Spring Data JPA generará automáticamente esta consulta
    Optional<Cita> findByFechaHora(Date fechaHora);
}
