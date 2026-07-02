package com.ucv.lab12.model;

import java.util.Date;

public class Empleado {
    private String codEmple;
    private String nombres;
    private String apellidos;
    private String dniEmple;
    private double sueldoBasico;
    private Date fechaIngreso;

    // Constructor vacío
    public Empleado() {}

    // Constructor completo
    public Empleado(String codEmple, String nombres, String apellidos, String dniEmple, double sueldoBasico, Date fechaIngreso) {
        this.codEmple = codEmple;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dniEmple = dniEmple;
        this.sueldoBasico = sueldoBasico;
        this.fechaIngreso = fechaIngreso;
    }

    // GETTERS Y SETTERS CORREGIDOS EN CAMELCASE (Obligatorio para JavaFX)
    public String getCodEmple() {
        return codEmple;
    }
    public void setCodEmple(String codEmple) {
        this.codEmple = codEmple;
    }

    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDniEmple() {
        return dniEmple;
    }
    public void setDniEmple(String dniEmple) {
        this.dniEmple = dniEmple;
    }

    public double getSueldoBasico() {
        return sueldoBasico;
    }
    public void setSueldoBasico(double sueldoBasico) {
        this.sueldoBasico = sueldoBasico;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public String toString() {
        return this.apellidos + ", " + this.nombres;
    }
}