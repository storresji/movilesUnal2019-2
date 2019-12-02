package co.edu.unal.webserviceapp.models;

public class Dotacion {

    private String nombre;
    private String tipoEquipamiento;
    private String material;
    private String cerramiento;
    private String area; //metros

    public Dotacion(String nombre, String tipoEquipamiento, String material, String cerramiento, String area) {
        this.nombre = nombre;
        this.tipoEquipamiento = tipoEquipamiento;
        this.material = material;
        this.cerramiento = cerramiento;
        this.area = area;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoEquipamiento() {
        return tipoEquipamiento;
    }

    public void setTipoEquipamiento(String tipoEquipamiento) {
        this.tipoEquipamiento = tipoEquipamiento;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCerramiento() {
        return cerramiento;
    }

    public void setCerramiento(String cerramiento) {
        this.cerramiento = cerramiento;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
