package com.example.examen3;

public class Ciudad {
    private int id;
    private String nombre;
    private double latitud;
    private double longitud;

    // Constructor, getters y setters
    public Ciudad() {}

    public Ciudad(String nombre, double latitud, double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    @Override
    public String toString() {
        return nombre + " (Lat: " + latitud + ", Lon: " + longitud + ")";
    }
}