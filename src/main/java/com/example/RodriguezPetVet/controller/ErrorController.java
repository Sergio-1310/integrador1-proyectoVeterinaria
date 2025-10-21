package com.example.RodriguezPetVet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/acceso_denegado")
    public String accesoDenegado() {
        return "acceso_denegado"; // carga acceso_denegado.html
    }
}