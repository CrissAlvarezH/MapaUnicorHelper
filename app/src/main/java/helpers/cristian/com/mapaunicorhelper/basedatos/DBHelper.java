package helpers.cristian.com.mapaunicorhelper.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "helpermapa.db", null, 1);
    }

    public static final String TABLA_RUTAS = "rutas";
    public static final String TABLA_RUTA_POSICION = "ruta_posicion";
    public static final String TABLA_INTERCEPCION_RUTAS = "intercepciones_rutas";
    public static final String TABLA_POSICIONES = "posiciones";
    public static final String TABLA_IMAGENES = "imagenes";
    public static final String TABLA_ZONAS = "zonas";
    public static final String TABLA_BLOQUES = "bloques";
    public static final String TABLA_SALONES = "salones";
    public static final String TABLA_IMAGEN_BLOQUE = "imagen_bloque";
    public static final String TABLA_IMAGEN_SALON = "imagen_salon";
    public static final String TABLA_IMAGEN_POSICION = "imagen_posicion";

    public static final String ID = "id";
    public static final String ID_RUTA = "id_ruta";
    public static final String ID_RUTA_1 = "id_ruta_1";
    public static final String ID_RUTA_2 = "id_ruta_2";
    public static final String ID_POSICION = "id_posicion";
    public static final String ID_ZONA = "id_zona";
    public static final String ID_BLOQUE = "id_bloque";
    public static final String ID_IMAGEN = "id_imagen";
    public static final String ID_SALON = "id_salon";
    public static final String ID_SERVER = "id_server";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String URL = "url";
    public static final String FECHA_TOMADA = "fecha_tomada";
    public static final String NOMBRE = "nombre";
    public static final String CODIGO = "codigo";
    public static final String PISO = "piso";
    public static final String NUMERO = "numero";
    public static final String ESTADO = "estado";


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREAR_TABLA_RUTAS = "CREATE TABLE "+TABLA_RUTAS +" ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOMBRE + " TEXT NOT NULL " +
                ");";

        String CREAR_TABLA_POSICIONES = "CREATE TABLE "+TABLA_POSICIONES+" ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LATITUD + " DOUBLE NOT NULL, " +
                LONGITUD + " DOUBLE NOT NULL " +
                ");";

        String CREAR_TABLA_RUTA_POSICION = "CREATE TABLE "+TABLA_RUTA_POSICION+" ( " +
                NUMERO + " INTEGER NOT NULL, " +// Para llevar un orden y saber cual es el inicio y cual posicion sigue
                ID_POSICION + " INTEGER NOT NULL, " +
                ID_RUTA + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_POSICION+") REFERENCES "+TABLA_POSICIONES+"("+ID+"), " +
                "FOREIGN KEY("+ID_RUTA+") REFERENCES "+TABLA_RUTAS+"("+ID+") " +
                ");";

        // Cuando hay un cruce de caminos
        String CREAR_TABLA_INTERCEPCION_RUTAS = "CREATE TABLE "+TABLA_INTERCEPCION_RUTAS+" ( " +
                ID_RUTA_1 + " INTEGER NOT NULL, " +
                ID_RUTA_2 + " INTEGER NOT NULL, " +
                ID_POSICION + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_RUTA_1+") REFERENCES "+TABLA_RUTAS+"("+ID+"), " +
                "FOREIGN KEY("+ID_RUTA_2+") REFERENCES "+TABLA_RUTAS+"("+ID+"), " +
                "FOREIGN KEY("+ID_POSICION+") REFERENCES "+TABLA_POSICIONES+"("+ID+") " +
                ");";

        String CREAR_TABLA_IMAGENES = "CREATE TABLE "+TABLA_IMAGENES+" ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                URL + " TEXT NOT NULL, " +
                FECHA_TOMADA + " TEXT NOT NULL, " +
                ESTADO + " TEXT NOT NULL " +// Campo solo para la app, para saber si fue enviada al servidor
                ");";

        String CREAR_TABLA_ZONA = "CREATE TABLE "+TABLA_ZONAS+" ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOMBRE + " TEXT NOT NULL " +
                ");";

        String CREAR_TABLA_BLOQUE = "CREATE TABLE "+TABLA_BLOQUES+" ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID_SERVER + " INTEGER," +
                NOMBRE + " TEXT NOT NULL, " +
                CODIGO + " TEXT NOT NULL, " +
                ID_ZONA + " INTEGER NOT NULL, " +
                ID_POSICION + " INTEGER NOT NULL, " +
                ESTADO + " TEXT NOT NULL, " +
                "FOREIGN KEY("+ID_ZONA+") REFERENCES "+TABLA_ZONAS+"("+ID+"), " +
                "FOREIGN KEY("+ID_POSICION+") REFERENCES "+TABLA_POSICIONES+"("+ID+") " +
                ");";

        String CREAR_TABLA_SALONES = "CREATE TABLE "+TABLA_SALONES+" ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID_SERVER + " INTEGER," +
                NOMBRE + " TEXT NOT NULL, " +
                CODIGO + " TEXT NOT NULL, " +
                PISO + " INTEGER NOT NULL, " +
                ID_BLOQUE + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_BLOQUE+") REFERENCES "+TABLA_BLOQUES+"("+ID+") " +
                ");";

        String CREAR_TABLA_IMAGEN_BLOQUE = "CREATE TABLE "+TABLA_IMAGEN_BLOQUE+" ( " +
                ID_IMAGEN + " INTEGER NOT NULL, " +
                ID_BLOQUE + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_IMAGEN+") REFERENCES "+TABLA_IMAGENES+"("+ID+"), " +
                "FOREIGN KEY("+ID_BLOQUE+") REFERENCES "+TABLA_BLOQUES+"("+ID+") " +
                ");";

        String CREAR_TABLA_IMAGEN_SALON = "CREATE TABLE "+TABLA_IMAGEN_SALON+" ( " +
                ID_IMAGEN + " INTEGER NOT NULL, " +
                ID_SALON + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_IMAGEN+") REFERENCES "+TABLA_IMAGENES+"("+ID+"), " +
                "FOREIGN KEY("+ID_SALON+") REFERENCES "+TABLA_SALONES+"("+ID+") " +
                ");";

        String CREAR_TABLA_IMAGEN_POSICION = "CREATE TABLE "+TABLA_IMAGEN_POSICION+" ( " +
                ID_IMAGEN + " INTEGER NOT NULL, " +
                ID_POSICION + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ID_IMAGEN+") REFERENCES "+TABLA_IMAGENES+"("+ID+"), " +
                "FOREIGN KEY("+ID_POSICION+") REFERENCES "+TABLA_POSICIONES+"("+ID+") " +
                ");";


        db.execSQL(CREAR_TABLA_RUTAS);
        db.execSQL(CREAR_TABLA_POSICIONES);
        db.execSQL(CREAR_TABLA_RUTA_POSICION);
        db.execSQL(CREAR_TABLA_INTERCEPCION_RUTAS);
        db.execSQL(CREAR_TABLA_IMAGENES);
        db.execSQL(CREAR_TABLA_ZONA);
        db.execSQL(CREAR_TABLA_BLOQUE);
        db.execSQL(CREAR_TABLA_SALONES);
        db.execSQL(CREAR_TABLA_IMAGEN_BLOQUE);
        db.execSQL(CREAR_TABLA_IMAGEN_SALON);
        db.execSQL(CREAR_TABLA_IMAGEN_POSICION);

        String INSERTAR_ZONAS = "INSERT INTO "+TABLA_ZONAS+" VALUES (1, 'Zona A'),(2, 'Zona B'),(3, 'Zona C'), (4, 'Zona D')";
        db.execSQL(INSERTAR_ZONAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
