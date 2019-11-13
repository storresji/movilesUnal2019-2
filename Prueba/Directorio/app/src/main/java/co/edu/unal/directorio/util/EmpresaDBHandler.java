package co.edu.unal.directorio.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.edu.unal.directorio.models.Clasificacion;

public class EmpresaDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "empresas.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLA_EMPRESAS = "empresas";
    public static final String COLUMN_ID = "empId";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_URL_PAGINA = "ulrPaginaWeb";
    public static final String COLUMNA_TELEFONO = "telefono";
    public static final String COLUMNA_CORREO = "correo";
    public static final String COLUMNA_PRODUCTOS = "productos";
    public static final String COLUMNA_SERVICIOS = "servicios";
    public static final String COLUMNA_CLASIFICACION = "clasificacion";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLA_EMPRESAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNA_NOMBRE + " TEXT, " +
                    COLUMNA_URL_PAGINA + " TEXT, " +
                    COLUMNA_TELEFONO + " TEXT, " +
                    COLUMNA_CORREO + " TEXT, " +
                    COLUMNA_PRODUCTOS + " TEXT, " +
                    COLUMNA_SERVICIOS + " TEXT, " +
                    COLUMNA_CLASIFICACION + " NUMERIC " +
                    ")";

    public EmpresaDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_EMPRESAS);
        db.execSQL(TABLE_CREATE);
    }
}