package com.example.RodriguezPetVet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.RodriguezPetVet.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}