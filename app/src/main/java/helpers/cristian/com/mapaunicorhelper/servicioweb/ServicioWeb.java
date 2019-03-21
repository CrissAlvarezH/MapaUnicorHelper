package helpers.cristian.com.mapaunicorhelper.servicioweb;

import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.utils.Constantes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServicioWeb {

    @POST(Constantes.URLs.ENVIAR_BLOQUES)
    Call<ResServer> enviarBloque (@Body Bloque bloque);

}
