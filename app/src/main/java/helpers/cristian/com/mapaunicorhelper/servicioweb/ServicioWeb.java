package helpers.cristian.com.mapaunicorhelper.servicioweb;

import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.utils.Constantes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServicioWeb {

    @POST(Constantes.URLs.ENVIAR_BLOQUES)
    Call<ResServer> enviarBloque (@Body Bloque bloque);

    @GET(Constantes.URLs.BLOQUES)
    Call<ResServer> pedirTodosLosBloques();

    @FormUrlEncoded
    @PUT(Constantes.URLs.BLOQUES_UPDATE_POS)
    Call<ResServer> actualizarPosDeBloque(@Field("latitud") String latitud,
                                          @Field("longitud") String longitud,
                                          @Field("idBloque") int idBloque);
}
