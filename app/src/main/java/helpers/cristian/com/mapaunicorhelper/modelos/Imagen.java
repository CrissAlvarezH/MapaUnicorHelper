package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.FECHA_TOMADA;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_POSICION;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGENES;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.URL;

public class Imagen implements BaseModelo {

    private int id;
    private String url;
    private String fecha;
    private Posicion posicion;

    public Imagen(String url, String fecha, Posicion posicion) {
        this.url = url;
        this.fecha = fecha;
        this.posicion = posicion;
    }

    public Imagen(int id, String url, String fecha, Posicion posicion) {
        this.id = id;
        this.url = url;
        this.fecha = fecha;
        this.posicion = posicion;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(URL, url);
        values.put(FECHA_TOMADA, fecha);
        values.put(ID_POSICION, posicion.getId());

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_IMAGENES;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
}
