package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import java.io.Serializable;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.CODIGO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_BLOQUE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.NOMBRE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.PISO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_SALONES;

public class Salon implements BaseModelo, Serializable {

    private int id;
    private int idServer;
    private int piso;
    private String nombre;
    private String codigo;
    private transient Bloque bloque;
    private Imagen imagen;

    public Salon(String nombre, String codigo, Bloque bloque, int piso, Imagen imagen) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.bloque = bloque;
        this.piso = piso;
        this.imagen = imagen;
    }

    public Salon(int id, String nombre, String codigo, Bloque bloque, int piso, Imagen imagen) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.bloque = bloque;
        this.piso = piso;
        this.imagen = imagen;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(NOMBRE, nombre);
        values.put(CODIGO, codigo);
        values.put(PISO, piso);
        values.put(ID_BLOQUE, bloque.getId());

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_SALONES;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
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

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }
}
