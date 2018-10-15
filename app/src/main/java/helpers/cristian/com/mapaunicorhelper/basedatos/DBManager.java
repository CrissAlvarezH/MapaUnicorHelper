package helpers.cristian.com.mapaunicorhelper.basedatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.modelos.BaseModelo;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.modelos.Imagen;
import helpers.cristian.com.mapaunicorhelper.modelos.Posicion;
import helpers.cristian.com.mapaunicorhelper.modelos.Salon;
import helpers.cristian.com.mapaunicorhelper.modelos.Zona;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.CODIGO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.FECHA_TOMADA;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_BLOQUE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_IMAGEN;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_POSICION;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_SALON;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_ZONA;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.LATITUD;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.LONGITUD;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.NOMBRE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.PISO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_BLOQUES;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGENES;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGEN_BLOQUE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGEN_SALON;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_POSICIONES;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_SALONES;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.URL;

public class DBManager {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DBManager(Context contexto) {
        this.dbHelper = new DBHelper(contexto);
    }

    public long insertarModelo(BaseModelo modelo){
        db = dbHelper.getWritableDatabase();

        return db.insert(modelo.getNombreTabla(), null, modelo.toContentValues());
    }

    public int actualizarModelo(BaseModelo modelo, String where, String[] args){
        db = dbHelper.getWritableDatabase();

        return db.update(modelo.getNombreTabla(), modelo.toContentValues(), where, args);
    }

    public void ejecutarSql(String sql, String[] args){
        db = dbHelper.getWritableDatabase();

        if( args != null)
            db.execSQL(sql, args);
        else
            db.execSQL(sql);
    }

    public ArrayList<Bloque> getBoques(String where, String[] args) {
        db = dbHelper.getReadableDatabase();

        Cursor cur = db.query(TABLA_BLOQUES, null, where, args, null, null, null);

        ArrayList<Bloque> bloques = new ArrayList<>();

        if( cur.moveToFirst() ){
            do{
                Bloque bloque = new Bloque();

                bloque.setId( cur.getInt( cur.getColumnIndex(ID) ) );
                bloque.setNombre( cur.getString( cur.getColumnIndex(NOMBRE) ) );
                bloque.setCodigo( cur.getString( cur.getColumnIndex(CODIGO) ) );
                bloque.setZona( new Zona( cur.getInt( cur.getColumnIndex(ID_ZONA) ), "" ) );

                // Agregamos la posicion del bloque
                Cursor cPos = db.rawQuery(
                        "SELECT * FROM "+TABLA_POSICIONES+" WHERE "+ID+" = ?",
                        new String[] { cur.getInt( cur.getColumnIndex(ID_POSICION) ) + "" }
                );

                if( cPos.moveToFirst() ) {
                    Posicion posicion = new Posicion(
                            cPos.getInt( cPos.getColumnIndex(ID) ),
                            cPos.getDouble( cPos.getColumnIndex(LATITUD) ),
                            cPos.getDouble( cPos.getColumnIndex(LONGITUD) )
                    );

                    bloque.setPosicion( posicion );
                }

                cPos.close();

                // Agregamos las imagenes
                Cursor cImg = db.rawQuery(
                        "SELECT "+TABLA_IMAGENES+".* FROM "+TABLA_IMAGENES+", "+TABLA_IMAGEN_BLOQUE+
                                " WHERE "+TABLA_IMAGENES+"."+ID+" = "+TABLA_IMAGEN_BLOQUE+"."+ID_IMAGEN+
                                    " AND "+TABLA_IMAGEN_BLOQUE+"."+ID_BLOQUE+" = ?",
                        new String[] { bloque.getId() + ""}
                );

                ArrayList<Imagen> imagenes = new ArrayList<>();

                if( cImg.moveToFirst() ) {
                    do{
                        Imagen imagen = new Imagen(
                                cImg.getInt( cImg.getColumnIndex(ID) ),
                                cImg.getString( cImg.getColumnIndex(URL) ),
                                cImg.getString( cImg.getColumnIndex(FECHA_TOMADA) ),
                                null
                        );

                        imagenes.add(imagen);
                    }while ( cImg.moveToNext() );
                }

                cImg.close();

                bloque.setImagenes(imagenes);

                // Agregamos los salones
                Cursor cSalon = db.rawQuery(
                        "SELECT * FROM "+TABLA_SALONES+" WHERE "+ID_BLOQUE+" = ?",
                        new String[] { bloque.getId() + "" }
                );

                ArrayList<Salon> salones = new ArrayList<>();

                if( cSalon.moveToFirst() ) {
                    do {

                        Salon salon = new Salon(
                                cSalon.getInt( cSalon.getColumnIndex(ID) ),
                                cSalon.getString( cSalon.getColumnIndex(NOMBRE) ),
                                cSalon.getString( cSalon.getColumnIndex(CODIGO) ),
                                bloque,
                                cSalon.getInt( cSalon.getColumnIndex(PISO) ),
                                null
                        );

                        // Agregamos la imagen del salon

                        Cursor cimgs = db.rawQuery(
                                "SELECT "+TABLA_IMAGENES+".* "+
                                        " FROM "+TABLA_IMAGENES+", "+TABLA_IMAGEN_SALON +
                                        " WHERE "+TABLA_IMAGENES+"."+ID+" = "+TABLA_IMAGEN_SALON+"."+ID_IMAGEN+
                                            " AND "+TABLA_IMAGEN_SALON+"."+ID_SALON+" = ?",
                                new String[] { salon.getId() + ""}
                        );

                        if( cimgs.moveToFirst() ) {
                            salon.setImagen( new Imagen(
                                    cimgs.getInt( cimgs.getColumnIndex(ID) ),
                                    cimgs.getString( cimgs.getColumnIndex(URL) ),
                                    cimgs.getString( cimgs.getColumnIndex(FECHA_TOMADA) ),
                                    null
                            ));
                        }

                        cimgs.close();

                        salones.add(salon);

                    } while ( cSalon.moveToNext() );

                }

                cSalon.close();

                bloque.setSalones(salones);

                bloques.add(bloque);

            } while ( cur.moveToNext() );
        }

        cur.close();

        return bloques;
    }
}
