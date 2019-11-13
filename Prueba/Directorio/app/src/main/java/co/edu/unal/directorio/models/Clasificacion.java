package co.edu.unal.directorio.models;

public enum Clasificacion {

    UNKNOWN(0,"Desconocido"),
    CONSULTORIA  (1, "CONSULTORIA"),  //calls constructor with value 3
    DESARROLLO_MEDIDA(2, "DESARROLLO A MEDIDA"),  //calls constructor with value 2
    FABRICA_SOFTWARE   (3, "FABRICA DE SOFTWARE")   //calls constructor with value 1
    ; // semicolon needed when fields / methods follow

    private final int codigoClas;
    private String descripcion;

    private Clasificacion(int codigoClas, String descripcion) {
        this.codigoClas = codigoClas;
        this.descripcion = descripcion;
    }

    private Clasificacion(int codigoClas) {
        this.codigoClas = codigoClas;
    }

    public static Clasificacion getById(int id) {

        for(Clasificacion e : values()) {
            if(e.codigoClas == id) return e;
        }
        return UNKNOWN;
    }

    public String getDescripcion(int codigoClas){
        return Clasificacion.values()[codigoClas].descripcion;
    }

    public String getDescripcionText(){
        return descripcion;
    }

    public int getId(){
        return this.codigoClas;
    }
}

