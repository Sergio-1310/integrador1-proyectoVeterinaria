package com.example.RodriguezPetVet.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import com.example.RodriguezPetVet.repository.CitaRepository;
import com.example.RodriguezPetVet.repository.MascotaRepository;
import com.example.RodriguezPetVet.repository.ProductRepository;

import java.util.List; // Necesaria para el tipo List de las consultas

@Controller
public class DashboardController {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final ProductRepository productRepository;

    public DashboardController(CitaRepository citaRepository,
                               MascotaRepository mascotaRepository,
                               ProductRepository productRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 1. Verificar si el usuario tiene rol ADMIN (Lógica de Seguridad)
        boolean esAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!esAdmin) {
            return "redirect:/acceso_denegado";
        }

        // 2. Obtener datos para las tarjetas resumen
        long totalCitas = citaRepository.count();
        long totalMascotas = mascotaRepository.count();
        var lowStockProducts = productRepository.findLowStockProducts();
        long totalStock = productRepository.findAll()
                .stream()
                .mapToLong(p -> p.getQuantity())
                .sum();

        // 3. Obtener datos para los gráficos por mes (¡NUEVO!)
        List<Object[]> citasPorMesData = citaRepository.countCitasByMonth();
        List<Object[]> mascotasPorSexoData = mascotaRepository.countMascotasBySexo();
        
        // 4. Enviar datos a Thymeleaf (Model)
        
        // Datos de tarjetas
        model.addAttribute("numAppointments", totalCitas);
        model.addAttribute("numPetsAttended", totalMascotas);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("totalStock", totalStock);

        // Datos para gráficos (¡NUEVO!)
        model.addAttribute("citasPorMesData", citasPorMesData);
        model.addAttribute("mascotasPorSexoData", mascotasPorSexoData);

        return "dashboard"; // dashboard.html
    }
}