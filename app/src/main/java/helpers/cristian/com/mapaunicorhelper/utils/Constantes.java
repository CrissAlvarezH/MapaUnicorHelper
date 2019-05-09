package helpers.cristian.com.mapaunicorhelper.utils;

public class Constantes {

    public interface URLs {
        String BASE_URL = "http://142.93.71.94:4400/"; // Produccion: http://142.93.71.94:4400/
        String ENVIAR_IMGS = "imagenes/insertar";
        String ENVIAR_BLOQUES = "bloques/insertar";
        String BLOQUES = "bloques";
        String BLOQUES_ID = "bloques/{id}";
        String BLOQUES_UPDATE_POS = "bloques/update-posicion";
    }

    public interface Acciones {
        String ACTUALIZAR_BLOQUES = "actualizarbloques";
        String ACTUALIZAR_IMGS = "actualizarimgs";
    }
}
