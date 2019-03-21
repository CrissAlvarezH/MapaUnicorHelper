package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

import java.util.ArrayList;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.CODIGO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ESTADO;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_POSICION;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ID_ZONA;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.NOMBRE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_BLOQUES;

public class Bloque implements BaseModelo {

    private int id;
    private int idServer;
    private int idZona;
    private int idPosicion;
    private String nombre;
    private String codigo;
    private Zona zona;
    private Posicion posicion;
    private int numSalones;
    private ArrayList<Imagen> imagenes;
    private ArrayList<Salon> salones;
    private String estado;

    public Bloque(String nombre, String codigo, Zona zona, Posicion posicion, String estado) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.zona = zona;
        this.posicion = posicion;
        this.estado = estado;
    }

    public Bloque(int id, String nombre, String codigo, Zona zona, Posicion posicion, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.zona = zona;
        this.posicion = posicion;
        this.estado = estado;
    }

    public Bloque(){

    }

    public interface Estados {
        String NO_ENVIADO = "no_enviado";
        String ENVIADO = "enviado";
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(NOMBRE, nombre);
        values.put(CODIGO, codigo);
        values.put(ID_ZONA, zona.getId());
        values.put(ID_POSICION, posicion.getId());
        values.put(ESTADO, estado);

        return values;
    }

    @Override
    public String getNombreTabla() {
        return TABLA_BLOQUES;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public int getIdPosicion() {
        return idPosicion;
    }

    public void setIdPosicion(int idPosicion) {
        this.idPosicion = idPosicion;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public int getNumSalones() {
        return numSalones;
    }

    public void setNumSalones(int numSalones) {
        this.numSalones = numSalones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public ArrayList<Salon> getSalones() {
        return salones;
    }

    public void setSalones(ArrayList<Salon> salones) {
        this.salones = salones;
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

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }


}
