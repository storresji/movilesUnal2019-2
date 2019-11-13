package co.edu.unal.directorio.models;

import java.util.ArrayList;
import java.util.List;

public class Empresa {

    private long empId;
    private String nombre;
    private String urlPaginaWeb;
    private String telefono;
    private String correo;
    private String productos;
    private String servicios;
    private Clasificacion clasificacion;

    public Empresa(long empId, String nombre, String urlPaginaWeb, String telefono, String correo, String productos, String servicios, Clasificacion clasificacion) {
        this.empId = empId;
        this.nombre = nombre;
        this.urlPaginaWeb = urlPaginaWeb;
        this.telefono = telefono;
        this.correo = correo;
        this.productos = productos;
        this.servicios = servicios;
        this.clasificacion = clasificacion;
    }

    public Empresa(long empId, String nombre, String urlPaginaWeb, String telefono, String correo, Clasificacion clasificacion){
        this.empId = empId;
        this.nombre = nombre;
        this.urlPaginaWeb = urlPaginaWeb;
        this.telefono = telefono;
        this.correo = correo;
        this.clasificacion = clasificacion;
    }

    public Empresa() {

    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlPaginaWeb() {
        return urlPaginaWeb;
    }

    public void setUrlPaginaWeb(String urlPaginaWeb) {
        this.urlPaginaWeb = urlPaginaWeb;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public String toString() {
        return "Empresa " + empId + " - " + nombre + '\n' +
                "urlPaginaWeb: '" + urlPaginaWeb + '\'' +
                ",\n telefono: '" + telefono + '\'' +
                ",\n correo: '" + correo + '\'' +
                ",\n productos: " + productos +
                ",\n servicios: " + servicios +
                ",\n clasificacion: " + clasificacion ;
    }
}
