/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @Temporal(TemporalType.DATE)
    private Date fechaAtencion;

    private String diagnostico;
    private String tratamiento;
    
    // Constantes fisiológicas
    private String temperatura;
    private String pulso;
    private String llenadoCapilar;
    private String fr; // Frecuencia respiratoria

    private String pruebasRealizadas; // Opcional: para registrar pruebas de laboratorio, etc.

    // --- Constructores, Getters y Setters ---

    public Atencion() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public Date getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(Date fechaAtencion) { this.fechaAtencion = fechaAtencion; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public String getTemperatura() { return temperatura; }
    public void setTemperatura(String temperatura) { this.temperatura = temperatura; }
    public String getPulso() { return pulso; }
    public void setPulso(String pulso) { this.pulso = pulso; }
    public String getLlenadoCapilar() { return llenadoCapilar; }
    public void setLlenadoCapilar(String llenadoCapilar) { this.llenadoCapilar = llenadoCapilar; }
    public String getFr() { return fr; }
    public void setFr(String fr) { this.fr = fr; }
    public String getPruebasRealizadas() { return pruebasRealizadas; }
    public void setPruebasRealizadas(String pruebasRealizadas) { this.pruebasRealizadas = pruebasRealizadas; }
}