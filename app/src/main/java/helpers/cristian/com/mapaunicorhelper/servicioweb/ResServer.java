package helpers.cristian.com.mapaunicorhelper.servicioweb;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.modelos.Salon;

public class ResServer {
    private boolean okay;
    private ArrayList<Salon> salones;
    private ArrayList<Bloque> bloques;
    private Bloque bloque;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<Bloque> getBloques() {
        return bloques;
    }

    public void setBloques(ArrayList<Bloque> bloques) {
        this.bloques = bloques;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }

    public boolean isOkay() {
        return okay;
    }

    public ArrayList<Salon> getSalones() {
        return salones;
    }

    public void setOkay(boolean okay) {
        this.okay = okay;
    }

    public void setSalones(ArrayList<Salon> salones) {
        this.salones = salones;
    }
}
