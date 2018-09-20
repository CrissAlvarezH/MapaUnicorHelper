package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.CODIGO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_BLOQUE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.NOMBRE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_SALONES;

public class Salon implements BaseModelo {

    private int id;
    private String nombre;
    private String codigo;
    private Bloque bloque;

    public Salon(String nombre, String codigo, Bloque bloque) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.bloque = bloque;
    }

    public Salon(int id, String nombre, String codigo, Bloque bloque) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.bloque = bloque;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(NOMBRE, nombre);
        values.put(CODIGO, codigo);
        values.put(ID_BLOQUE, bloque.getId());

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_SALONES;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }
}
