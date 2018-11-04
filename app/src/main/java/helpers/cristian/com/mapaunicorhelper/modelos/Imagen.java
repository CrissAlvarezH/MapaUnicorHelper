package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ESTADO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.FECHA_TOMADA;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_POSICION;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGENES;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.URL;

public class Imagen implements BaseModelo {

    private int id;
    private String url;
    private String fecha;
    private String estado;
    private Posicion posicion;

    public Imagen(String url, String fecha, Posicion posicion, String estado) {
        this.url = url;
        this.fecha = fecha;
        this.posicion = posicion;
        this.estado = estado;
    }

    public Imagen(int id, String url, String fecha, Posicion posicion, String estado) {
        this.id = id;
        this.url = url;
        this.fecha = fecha;
        this.posicion = posicion;
        this.estado = estado;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(URL, url);
        values.put(FECHA_TOMADA, fecha);
        values.put(ESTADO, estado);
        if( posicion != null)
            values.put(ID_POSICION, posicion.getId());

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_IMAGENES;
    }

    public interface Estados {
        String NO_ENVIADA = "no_enviada";
        String ENVIADA = "enviada";
        String RUTA_NULA = "ruta_null";
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
