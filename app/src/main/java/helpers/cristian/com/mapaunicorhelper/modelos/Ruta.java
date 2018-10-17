package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import java.util.ArrayList;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.NOMBRE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_RUTAS;

public class Ruta implements BaseModelo{
    private int id;
    private String nombre;
    private ArrayList<Posicion> posiciones;

    public Ruta(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Ruta(String nombre) {
        this.nombre = nombre;
    }

    public Ruta() {
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(ID, id);
        values.put(NOMBRE, nombre);

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_RUTAS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Posicion> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(ArrayList<Posicion> posiciones) {
        this.posiciones = posiciones;
    }


}
