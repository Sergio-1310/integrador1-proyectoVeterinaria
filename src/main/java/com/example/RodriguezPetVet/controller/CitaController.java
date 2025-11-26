/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.controller;

import com.example.RodriguezPetVet.model.Cita;
import com.example.RodriguezPetVet.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// Usamos @RequestMapping("/citas") para que todas las rutas internas 
// de este controlador comiencen con /citas
@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    /**
     * Muestra el formulario de citas y la lista de citas existentes.
     * Mapea a /citas (gracias al RequestMapping de la clase)
     */
    @GetMapping
    public String citas(Model model) {
        // 1. Añade un objeto Cita vacío para que el formulario se pueda llenar
        model.addAttribute("nuevaCita", new Cita());

        // 2. Añade todas las citas existentes a la vista para mostrarlas
        List<Cita> listaCitas = citaRepository.findAll();
        model.addAttribute("citas", listaCitas);

        return "citas";
    }

    /**
     * Procesa el formulario de registro de citas.
     * Mapea a /citas/registrar
     */
    @PostMapping("/registrar")
    public String registrarCita(Cita nuevaCita, Model model) {

        Date fechaHora = nuevaCita.getFechaHora();

        // VALIDACIÓN: Busca si ya existe una cita en esa fecha y hora
        Optional<Cita> citaExistente = citaRepository.findByFechaHora(fechaHora);

        if (citaExistente.isPresent()) {
            model.addAttribute("error", "❌ ¡Día y hora ocupados! Por favor, selecciona otro momento.");
            model.addAttribute("nuevaCita", nuevaCita);
            model.addAttribute("citas", citaRepository.findAll());
            return "citas";
        }

        // REGISTRO
        citaRepository.save(nuevaCita);

        model.addAttribute("mensaje", "✅ Cita registrada exitosamente para " + nuevaCita.getNombreCliente());

        // Redirige a /citas
        return "redirect:/citas";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editarCita(@PathVariable("id") Long id, Model model) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de cita inválido: " + id));
        model.addAttribute("nuevaCita", cita);
        model.addAttribute("citas", citaRepository.findAll());
        model.addAttribute("editMode", true); // 👈 para que el HTML sepa si es edición
        return "citas";
    }

    // Guardar los cambios de la edición
    @PostMapping("/actualizar")
    public String actualizarCita(Cita citaEditada, Model model) {

        citaRepository.save(citaEditada);
        return "redirect:/citas";
    }

    // Eliminar cita
    @GetMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable("id") Long id) {
        citaRepository.deleteById(id);
        return "redirect:/citas";
    }

}
