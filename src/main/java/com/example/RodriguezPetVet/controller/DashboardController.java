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

        // Verificar si el usuario tiene rol ADMIN
        boolean esAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!esAdmin) {
            return "redirect:/acceso_denegado";
        }

        // Total de citas
        long totalCitas = citaRepository.count();

        // Total de mascotas
        long totalMascotas = mascotaRepository.count();

        // Productos con bajo stock
        var lowStockProducts = productRepository.findLowStockProducts();

        // Total de stock disponible
        long totalStock = productRepository.findAll()
                                           .stream()
                                           .mapToLong(p -> p.getQuantity())
                                           .sum();

        // Enviar datos a Thymeleaf
        model.addAttribute("numAppointments", totalCitas);
        model.addAttribute("numPetsAttended", totalMascotas);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("totalStock", totalStock);

        return "dashboard"; // dashboard.html
    }
}