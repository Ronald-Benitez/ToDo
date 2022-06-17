package com.example.login.models;

import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private String id;
    private String nombre;
    private String descripcion;
    private List<Actividad> pendientes;
    private List<Actividad> inProgress;
    private List<Actividad> finalizadas;

    public Proyecto() {
    }

    public Proyecto(String id, String nombre, String descripcion, List<Actividad> pendientes, List<Actividad> inProgress, List<Actividad> finalizadas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pendientes = pendientes;
        this.inProgress = inProgress;
        this.finalizadas = finalizadas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Actividad> getPendientes() {
        return pendientes;
    }

    public void setPendientes(List<Actividad> pendientes) {
        this.pendientes = pendientes;
    }

    public List<Actividad> getInProgress() {
        return inProgress;
    }

    public void setInProgress(List<Actividad> inProgress) {
        this.inProgress = inProgress;
    }

    public List<Actividad> getFinalizadas() {
        return finalizadas;
    }

    public void setFinalizadas(List<Actividad> finalizadas) {
        this.finalizadas = finalizadas;
    }
}
