package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import java.util.ArrayList;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.LATITUD;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.LONGITUD;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGEN_POSICION;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_POSICIONES;

public class Posicion implements BaseModelo{
    private int id;
    private double latitud;
    private double longitud;
    private int numero;// Numero en la ruta (Solo para posiciones que pertenezcan a una ruta)
    private ArrayList<Imagen> imagenes;// para las posiciones que hacen parte de rutas (algunas tienen imagenes)

    public Posicion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Posicion(int id, double latitud, double longitud) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(LATITUD, latitud);
        values.put(LONGITUD, longitud);

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_POSICIONES;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }


}
