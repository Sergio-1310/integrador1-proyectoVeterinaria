/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.controller;

import com.example.RodriguezPetVet.model.Mascota;
import com.example.RodriguezPetVet.model.Atencion;
import com.example.RodriguezPetVet.repository.MascotaRepository;
import com.example.RodriguezPetVet.repository.AtencionRepository;
import com.example.RodriguezPetVet.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/historial_clinico")
public class HistorialClinicoController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private AtencionRepository atencionRepository;
    
    @Autowired
    private MascotaService mascotaService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // --- 1. MOSTRAR TODAS LAS MASCOTAS Y FORMULARIO DE REGISTRO ---
    @GetMapping
    public String historialClinico(Model model, @RequestParam(value = "search", required = false) String search) {
        List<Mascota> mascotas;
        if (search != null && !search.isEmpty()) {
            mascotas = mascotaRepository.findByNombreContainingIgnoreCase(search);
        } else {
            mascotas = mascotaRepository.findAll();
        }
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("nuevaMascota", new Mascota()); // Para el modal de registro
        model.addAttribute("nuevaAtencion", new Atencion()); // Para el modal de nueva atención
        model.addAttribute("editarAtencion", new Atencion()); // Para el modal de edición de atención
        return "historial_clinico";
    }

    // --- 2. REGISTRAR NUEVA MASCOTA (CON FOTO) ---
    @PostMapping("/registrar_mascota")
    public String registrarMascota(
            @ModelAttribute Mascota nuevaMascota, 
            @RequestParam("fotoMascota") MultipartFile fotoMascota,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Guardar la imagen si existe
            if (!fotoMascota.isEmpty()) {
                String rutaFoto = mascotaService.guardarImagen(fotoMascota);
                nuevaMascota.setRutaFoto(rutaFoto);
            } else {
                nuevaMascota.setRutaFoto("/images/default_pet.png"); // O una imagen por defecto
            }
            
            mascotaRepository.save(nuevaMascota);
            redirectAttributes.addFlashAttribute("mensaje", "Mascota " + nuevaMascota.getNombre() + " registrada con éxito.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la imagen de la mascota: " + e.getMessage());
        }
        return "redirect:/historial_clinico";
    }

    // --- 3. MOSTRAR DETALLES DE UNA MASCOTA Y SUS ATENCIONES ---
    @GetMapping("/detalles/{id}")
    public String verDetallesMascota(@PathVariable Long id, Model model) {
        Optional<Mascota> mascotaOptional = mascotaRepository.findById(id);
        if (mascotaOptional.isPresent()) {
            Mascota mascota = mascotaOptional.get();
            model.addAttribute("mascota", mascota);
            model.addAttribute("atenciones", mascota.getAtenciones()); // Ya vienen ordenadas por @OrderBy
            model.addAttribute("nuevaAtencion", new Atencion()); // Para el modal de nueva atención
            model.addAttribute("editarAtencion", new Atencion()); // Para el modal de edición de atención
            return "historial_clinico_detalles"; // Nueva plantilla para detalles
        }
        return "redirect:/historial_clinico"; // Si no encuentra la mascota, vuelve a la lista
    }

    // --- 4. REGISTRAR NUEVA ATENCIÓN PARA UNA MASCOTA ESPECÍFICA ---
    @PostMapping("/registrar_atencion/{mascotaId}")
    public String registrarAtencion(
            @PathVariable Long mascotaId,
            @ModelAttribute Atencion nuevaAtencion,
            @RequestParam("fechaAtencionInput") String fechaAtencionStr,
            RedirectAttributes redirectAttributes) {
        
        Optional<Mascota> mascotaOptional = mascotaRepository.findById(mascotaId);
        if (mascotaOptional.isPresent()) {
            try {
                Date fechaAtencion = dateFormat.parse(fechaAtencionStr);
                nuevaAtencion.setFechaAtencion(fechaAtencion);
                nuevaAtencion.setMascota(mascotaOptional.get());
                atencionRepository.save(nuevaAtencion);
                redirectAttributes.addFlashAttribute("mensaje", "Atención registrada con éxito.");
            } catch (ParseException e) {
                redirectAttributes.addFlashAttribute("error", "Error en el formato de fecha: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Mascota no encontrada para registrar atención.");
        }
        return "redirect:/historial_clinico"; // O "redirect:/historial_clinico/detalles/" + mascotaId;
    }

    // --- 5. EDITAR ATENCIÓN EXISTENTE ---
    @PostMapping("/editar_atencion/{atencionId}")
    public String editarAtencion(
            @PathVariable Long atencionId,
            @ModelAttribute Atencion atencionActualizada,
            @RequestParam("fechaAtencionInput") String fechaAtencionStr,
            RedirectAttributes redirectAttributes) {
        
        Optional<Atencion> atencionOptional = atencionRepository.findById(atencionId);
        if (atencionOptional.isPresent()) {
            Atencion atencion = atencionOptional.get();
            try {
                Date fechaAtencion = dateFormat.parse(fechaAtencionStr);
                atencion.setFechaAtencion(fechaAtencion);
                atencion.setDiagnostico(atencionActualizada.getDiagnostico());
                atencion.setTratamiento(atencionActualizada.getTratamiento());
                // Actualizar constantes fisiológicas
                atencion.setTemperatura(atencionActualizada.getTemperatura());
                atencion.setPulso(atencionActualizada.getPulso());
                atencion.setLlenadoCapilar(atencionActualizada.getLlenadoCapilar());
                atencion.setFr(atencionActualizada.getFr());
                atencion.setPruebasRealizadas(atencionActualizada.getPruebasRealizadas());

                atencionRepository.save(atencion);
                redirectAttributes.addFlashAttribute("mensaje", "Atención actualizada con éxito.");
                return "redirect:/historial_clinico"; // O a los detalles: "redirect:/historial_clinico/detalles/" + atencion.getMascota().getId();
            } catch (ParseException e) {
                redirectAttributes.addFlashAttribute("error", "Error en el formato de fecha: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Atención no encontrada para editar.");
        }
        return "redirect:/historial_clinico";
    }

    // --- 6. ELIMINAR ATENCIÓN ---
    @PostMapping("/eliminar_atencion/{atencionId}")
    public String eliminarAtencion(@PathVariable Long atencionId, RedirectAttributes redirectAttributes) {
        Optional<Atencion> atencionOptional = atencionRepository.findById(atencionId);
        if (atencionOptional.isPresent()) {
            Long mascotaId = atencionOptional.get().getMascota().getId();
            atencionRepository.deleteById(atencionId);
            redirectAttributes.addFlashAttribute("mensaje", "Atención eliminada con éxito.");
            return "redirect:/historial_clinico"; // O a los detalles: "redirect:/historial_clinico/detalles/" + mascotaId;
        }
        redirectAttributes.addFlashAttribute("error", "Atención no encontrada.");
        return "redirect:/historial_clinico";
    }
    
    // --- 7. ELIMINAR MASCOTA ---
    @PostMapping("/eliminar_mascota/{mascotaId}")
    public String eliminarMascota(@PathVariable Long mascotaId, RedirectAttributes redirectAttributes) {
        Optional<Mascota> mascotaOptional = mascotaRepository.findById(mascotaId);
        if (mascotaOptional.isPresent()) {
            Mascota mascota = mascotaOptional.get();
            try {
                mascotaService.eliminarImagen(mascota.getRutaFoto()); // Elimina la imagen del sistema de archivos
                mascotaRepository.deleteById(mascotaId);
                redirectAttributes.addFlashAttribute("mensaje", "Mascota " + mascota.getNombre() + " y su historial eliminados con éxito.");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Error al eliminar la imagen de la mascota: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Mascota no encontrada para eliminar.");
        }
        return "redirect:/historial_clinico";
    }
}
