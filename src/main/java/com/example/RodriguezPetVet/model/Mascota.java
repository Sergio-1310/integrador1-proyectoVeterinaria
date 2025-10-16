/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String especie;
    private String raza;
    private String sexo; // Macho, Hembra
    private String color;
    private int edad; // En años o meses, puedes ajustar el tipo
    private double peso; // En kg
    private boolean esterilizado;
    private String medicinaPreventiva; // Vacunas, desparasitaciones, etc.

    private String nombreDueno;
    private String telefonoDueno;
    private String direccionDueno;

    private String rutaFoto; // Para almacenar la ruta de la imagen de la mascota

    // Un historial médico puede tener múltiples atenciones
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("fechaAtencion DESC") // Ordenar las atenciones por fecha descendente
    private List<Atencion> atenciones = new ArrayList<>();

    // --- Constructores, Getters y Setters ---

    public Mascota() {
    }

    // Constructor para facilitar la creación (opcional)
    public Mascota(String nombre, String especie, String raza, String sexo, String color, int edad, double peso, boolean esterilizado, String medicinaPreventiva, String nombreDueno, String telefonoDueno, String direccionDueno) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.color = color;
        this.edad = edad;
        this.peso = peso;
        this.esterilizado = esterilizado;
        this.medicinaPreventiva = medicinaPreventiva;
        this.nombreDueno = nombreDueno;
        this.telefonoDueno = telefonoDueno;
        this.direccionDueno = direccionDueno;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public boolean isEsterilizado() { return esterilizado; }
    public void setEsterilizado(boolean esterilizado) { this.esterilizado = esterilizado; }
    public String getMedicinaPreventiva() { return medicinaPreventiva; }
    public void setMedicinaPreventiva(String medicinaPreventiva) { this.medicinaPreventiva = medicinaPreventiva; }
    public String getNombreDueno() { return nombreDueno; }
    public void setNombreDueno(String nombreDueno) { this.nombreDueno = nombreDueno; }
    public String getTelefonoDueno() { return telefonoDueno; }
    public void setTelefonoDueno(String telefonoDueno) { this.telefonoDueno = telefonoDueno; }
    public String getDireccionDueno() { return direccionDueno; }
    public void setDireccionDueno(String direccionDueno) { this.direccionDueno = direccionDueno; }
    public String getRutaFoto() { return rutaFoto; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }
    public List<Atencion> getAtenciones() { return atenciones; }
    public void setAtenciones(List<Atencion> atenciones) { this.atenciones = atenciones; }
    
    // Método de utilidad para añadir atención
    public void addAtencion(Atencion atencion) {
        if (atenciones == null) {
            atenciones = new ArrayList<>();
        }
        atenciones.add(atencion);
        atencion.setMascota(this);
    }
}
