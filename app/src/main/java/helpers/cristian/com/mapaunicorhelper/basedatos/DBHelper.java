package helpers.cristian.com.mapaunicorhelper.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "helpermapa.db", null, 1);
    }

    public static final String TABLA_POSICIONES = "posiciones";
    public static final String TABLA_IMAGENES = "imagenes";
    public static final String TABLA_ZONAS = "zonas";
    public static final String TABLA_BLOQUES = "bloques";
    public static final String TABLA_SALONES = "salones";
    public static final String TABLA_IMAGEN_BLOQUE = "imagen_bloque";
    public static final String TABLA_IMAGEN_SALON = "imagen_salon";
    public static final String TABLA_POSICION_BLOQUE = "posicion_bloque";
    public static final String TABLA_IMAGEN_POSICION = "imagen_posicion";

    public static final String ID = "id";
    public static final String ID_POSICION = "id_posicion";
    public static final String ID_ZONA = "id_zona";
    public static final String ID_BLOQUE = "id_bloque";
    public static final String ID_IMAGEN = "id_imagen";
    public static final String ID_SALON = "id_salon";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String URL = "url";
    public static final String FECHA_TOMADA = "fecha_tomada";
    public static final String NOMBRE = "nombre";
    public static final String CODIGO = "codigo";
    public static final String ORIENTACION = "orientacion";


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREAR_TABLA_POSICIONES = "CREATE TABLE "+TABLA_POSICIONES+" ( " +
                ID + " INTEGER AUTOINCREMENT PRIMARY KEY," +
                LATITUD + " DOUBLE NOT NULL, " +
                LONGITUD + " DOUBLE NOT NULL " +
                ");";

        String CREAR_TABLA_IMAGENES = "CREATE TABLE "+TABLA_IMAGENES+" ( " +
                ID + " INTEGER AUTOINCREMENT PRIMARY KEY," +
                URL + " TEXT NOT NULL, " +
                FECHA_TOMADA + " TEXT NOT NULL, " +
                ID_POSICION + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_POSICION+") REFERENCER "+TABLA_POSICIONES+"("+ID+") " +
                ");";

        String CREAR_TABLA_ZONA = "CREATE TABLE "+TABLA_ZONAS+" ( " +
                ID + " INTEGER AUTOINCREMENT PRIMARY KEY," +
                NOMBRE + " TEXT NOT NULL " +
                ");";

        String CREAR_TABLA_BLOQUE = "CREATE TABLE "+TABLA_BLOQUES+" ( " +
                ID + " INTEGER AUTOINCREMENT PRIMARY KEY," +
                NOMBRE + " TEXT NOT NULL, " +
                CODIGO + " TEXT NOT NULL, " +
                ID_ZONA + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_ZONA+") REFERENCER "+TABLA_POSICIONES+"("+ID+") " +
                ");";

        String CREAR_TABLA_SALONES = "CREATE TABLE "+TABLA_SALONES+" ( " +
                ID + " INTEGER AUTOINCREMENT PRIMARY KEY," +
                NOMBRE + " TEXT NOT NULL, " +
                CODIGO + " TEXT NOT NULL, " +
                ID_BLOQUE + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_BLOQUE+") REFERENCER "+TABLA_BLOQUES+"("+ID+") " +
                ");";

        String CREAR_TABLA_IMAGEN_BLOQUE = "CREATE TABLE "+TABLA_IMAGEN_BLOQUE+" ( " +
                ID_IMAGEN + " INTEGER NOT NULL, " +
                ID_BLOQUE + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_IMAGEN+") REFERENCER "+TABLA_IMAGENES+"("+ID+"), " +
                "FOREIGN KEY("+ID_BLOQUE+") REFERENCER "+TABLA_BLOQUES+"("+ID+") " +
                ");";

        String CREAR_TABLA_IMAGEN_SALON = "CREATE TABLE "+TABLA_IMAGEN_SALON+" ( " +
                ID_IMAGEN + " INTEGER NOT NULL, " +
                ID_SALON + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_IMAGEN+") REFERENCER "+TABLA_IMAGENES+"("+ID+"), " +
                "FOREIGN KEY("+ID_SALON+") REFERENCER "+TABLA_SALONES+"("+ID+") " +
                ");";

        String CREAR_TABLA_IMAGEN_POSICION = "CREATE TABLE "+TABLA_IMAGEN_POSICION+" ( " +
                ID_IMAGEN + " INTEGER NOT NULL, " +
                ID_POSICION + " INTEGER NOT NULL, " +
                ORIENTACION + " TEXT NOT NULL, " +
                "FOREIGN KEY("+ID_IMAGEN+") REFERENCER "+TABLA_IMAGENES+"("+ID+"), " +
                "FOREIGN KEY("+ID_POSICION+") REFERENCER "+TABLA_POSICIONES+"("+ID+") " +
                ");";

        String CREAR_TABLA_POSICION_BLOQUE = "CREATE TABLE "+TABLA_POSICION_BLOQUE+" ( " +
                ID_POSICION + " INTEGER NOT NULL, " +
                ID_BLOQUE + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_POSICION+") REFERENCER "+TABLA_POSICIONES+"("+ID+"), " +
                "FOREIGN KEY("+ID_BLOQUE+") REFERENCER "+TABLA_BLOQUES+"("+ID+") " +
                ");";

        // TODO tablas completas (creo), terminar de ejecutar los create table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
