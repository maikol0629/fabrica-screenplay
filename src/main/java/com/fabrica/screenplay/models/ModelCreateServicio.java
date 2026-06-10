package com.fabrica.screenplay.models;

public class ModelCreateServicio {

    private String nombreServicio;
    private String descripcion;
    private double costo;
    private int duracion;

    public ModelCreateServicio(String nombreServicio, String descripcion, double costo, int duracion) {
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.costo = costo;
        this.duracion = duracion;
    }

    public String getNombreServicio() { return nombreServicio; }
    public String getDescripcion() { return descripcion; }
    public double getCosto() { return costo; }
    public int getDuracion() { return duracion; }
}
