package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.NOMBRE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_ZONAS;

public class Zona implements BaseModelo {

    private int id;
    private String nombre;

    public Zona(String nombre) {
        this.nombre = nombre;
    }

    public Zona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(NOMBRE, nombre);

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_ZONAS;
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
}
