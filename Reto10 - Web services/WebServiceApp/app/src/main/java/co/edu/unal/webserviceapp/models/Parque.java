package co.edu.unal.webserviceapp.models;

import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

public class Parque {

    private String codigo;
    private String tipo;
    private String nombre;
    private String localidad;
    private List<Dotacion> dotaciones;

    //private String equipamiento;
    //private String material;
    //private String cerramiento;

    public Parque(String codigo, String tipo, String nombre, String localidad) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.nombre = nombre;
        this.localidad = localidad;
        //this.dotaciones = dotaciones;

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public List<Dotacion> getDotaciones() {
        return dotaciones;
    }

    public void setDotaciones(List<Dotacion> dotaciones) {
        this.dotaciones = dotaciones;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, tipo, nombre, localidad, dotaciones);
    }

    @Override
    public boolean equals(Object other){
        if(other == null &&  !(other instanceof Parque))
            return false;
        else if(((Parque)other).getCodigo().equals(codigo))
            return true;

        return false;
    }

    public void addDotacion(Dotacion dotacion){
        dotaciones.add(dotacion);
    }
}
