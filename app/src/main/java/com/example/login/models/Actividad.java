package com.example.login.models;

public class Actividad {
    private String id;
    private String nombre;
    private String descripcion;
    private String fechaIncial;
    private String fechaFinal;

    public Actividad(){

    }

    public Actividad(String id, String nombre, String descripcion, String fechaIncial, String fechaFinal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaIncial = fechaIncial;
        this.fechaFinal = fechaFinal;
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

    public String getFechaIncial() {
        return fechaIncial;
    }

    public void setFechaIncial(String fechaIncial) {
        this.fechaIncial = fechaIncial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
