package com.example.RodriguezPetVet.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.RodriguezPetVet.model.Rol;
import com.example.RodriguezPetVet.model.Usuario;
import com.example.RodriguezPetVet.repository.RolRepository;
import com.example.RodriguezPetVet.repository.UsuarioRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Si la base de datos está vacía, crea los roles y usuarios base
        if (usuarioRepository.count() == 0) {

            // Rol ADMIN
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ROLE_ADMIN");
            rolRepository.save(rolAdmin);

            // Rol EMPLEADO
            Rol rolEmpleado = new Rol();
            rolEmpleado.setNombre("ROLE_EMPLEADO");
            rolRepository.save(rolEmpleado);

            // Usuario ADMIN
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRoles(Collections.singleton(rolAdmin));
            usuarioRepository.save(admin);

            // Usuario EMPLEADO
            Usuario empleado = new Usuario();
            empleado.setUsername("empleado");
            empleado.setPassword(passwordEncoder.encode("empleado123"));
            empleado.setEnabled(true);
            empleado.setRoles(Collections.singleton(rolEmpleado));
            usuarioRepository.save(empleado);
        }
    }
}