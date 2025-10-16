/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.service;

import com.example.RodriguezPetVet.model.Mascota;
import com.example.RodriguezPetVet.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID; // Para generar nombres de archivo únicos

@Service
public class MascotaService {

    private final Path rootLocation = Paths.get("src/main/resources/static/uploads"); // Carpeta para guardar las imágenes

    @Autowired
    private MascotaRepository mascotaRepository;

    public MascotaService() {
        try {
            // Asegura que el directorio de uploads exista
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio de carga de imágenes", e);
        }
    }

    public String guardarImagen(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null; // O lanzar una excepción si la imagen es obligatoria
        }
        
        // Generar un nombre único para el archivo para evitar sobrescribir
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        
        Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFileName))
                                                 .normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);
        
        return "/uploads/" + uniqueFileName; // Retorna la ruta relativa para usar en HTML
    }
    
    public void eliminarImagen(String rutaFoto) throws IOException {
        if (rutaFoto != null && !rutaFoto.isEmpty() && !rutaFoto.equals("/uploads/default.png")) { // Evitar borrar una imagen por defecto
            Path filePath = this.rootLocation.resolve(rutaFoto.replace("/uploads/", "")).normalize().toAbsolutePath();
            Files.deleteIfExists(filePath);
        }
    }
}
