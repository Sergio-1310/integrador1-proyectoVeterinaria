/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.service;

import com.example.RodriguezPetVet.model.Mascota;
import com.example.RodriguezPetVet.repository.MascotaRepository;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID; // Para generar nombres de archivo únicos

@Service
public class MascotaService {

    private final Path rootLocation = Paths.get("src/main/resources/static/uploads"); // Carpeta base de imágenes

    @Autowired
    private MascotaRepository mascotaRepository;

    public MascotaService() {
        try {
            // Asegura que el directorio de uploads exista
            File uploadDir = rootLocation.toFile();
            if (!uploadDir.exists()) {
                FileUtils.forceMkdir(uploadDir); // Crea el directorio usando Apache Commons IO
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio de carga de imágenes", e);
        }
    }

    public String guardarImagen(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // Generar nombre único
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Ruta destino
        File destino = rootLocation.resolve(uniqueFileName).toFile();

        // Guardar archivo con Apache Commons IO
        FileUtils.copyInputStreamToFile(file.getInputStream(), destino);

        return "/uploads/" + uniqueFileName;
    }

    public void eliminarImagen(String rutaFoto) throws IOException {
        if (rutaFoto != null && !rutaFoto.isEmpty() && !rutaFoto.equals("/uploads/default.png")) {
            File fileToDelete = rootLocation.resolve(rutaFoto.replace("/uploads/", "")).toFile();

            // Eliminar archivo con Apache Commons IO
            FileUtils.forceDelete(fileToDelete);
        }
    }
}

