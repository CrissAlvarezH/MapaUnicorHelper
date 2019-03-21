package helpers.cristian.com.mapaunicorhelper.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.basedatos.DBManager;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.modelos.Imagen;
import helpers.cristian.com.mapaunicorhelper.modelos.Salon;
import helpers.cristian.com.mapaunicorhelper.modelos.Zona;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ResServer;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ServicioWeb;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ServicioWebUtils;
import helpers.cristian.com.mapaunicorhelper.utils.Constantes;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.ESTADO;


public class EnviarInfoService extends IntentService {
    private final String TAG = "ServicioInfo";

    private DBManager dbManager;
    private ServicioWeb servicioWeb;
    private Gson gson;

    public EnviarInfoService() {
        super("EnviarInfoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        gson = new Gson();
        dbManager = new DBManager(getApplicationContext());
        servicioWeb = ServicioWebUtils.getServicioWeb(true);

        if (intent != null) {
            Log.v(TAG, "Inición servicio");

            enviarImgs();
            enviarBloques();

        }
    }

    private void enviarBloques() {

        ArrayList<Bloque> bloques = dbManager.getBoques(ESTADO+" = ?", new String[] { Bloque.Estados.NO_ENVIADO });

        Log.v(TAG, "Inicio envio de bloques: "+bloques.size() );

        for ( Bloque bloque : bloques ) {

            Call<ResServer> resServerCall = servicioWeb.enviarBloque(bloque);

            try {
                Response<ResServer> response = resServerCall.execute();

                if ( response.isSuccessful() ) {

                    ResServer resServer = response.body();

                    if ( resServer != null && resServer.isOkay() ) {

                        dbManager.marcarBloqueEnviado(
                                resServer.getBloque(),
                                resServer.getBloque().getSalones()
                        );

                        LocalBroadcastManager.getInstance(getApplicationContext())
                                .sendBroadcast(
                                        new Intent( Constantes.Acciones.ACTUALIZAR_BLOQUES )
                                );
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void enviarImgs() {
        // Se construye la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "chanelunicor")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Enviando imagenes")
                .setContentText("Procesado...");

        ArrayList<Imagen> imagenes = dbManager.getImagenesParaEnviar();

        Log.v(TAG, "Inicio envio de imagenes: " + imagenes.size());

        int cantidad = imagenes.size();
        int enviadas = 0;

        for ( Imagen imagen : imagenes ) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(@NonNull String message) {
                    Log.v("LogginWebHelper", message);
                }
            });

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            MultipartBody.Builder builderBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            builderBody.addFormDataPart("de", imagen.getDe());
            builderBody.addFormDataPart("fecha", imagen.getFecha());
            builderBody.addFormDataPart("id_relacion", imagen.getIdRelacion()+"");

            File imgFileOriginal = new File(imagen.getUrl());

            File fileImgComprimida;

            try {
                fileImgComprimida = new Compressor(getApplicationContext())
                        .compressToFile(imgFileOriginal);
            } catch (IOException e) {
                e.printStackTrace();

                fileImgComprimida = imgFileOriginal;
            }

            // Tomamos el tipo de archivo a enviar, en este caso imagen, y se toma la extencion para eso
            String extencion = MimeTypeMap.getFileExtensionFromUrl( fileImgComprimida.getPath() );
            String tipoFile = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extencion);

            RequestBody imgFileBody = RequestBody.create( MediaType.parse(tipoFile), fileImgComprimida);
            builderBody.addFormDataPart("imagen", "imagen", imgFileBody);

            RequestBody requestBody = builderBody.build();

            Request request = new Request.Builder()
                    .url(Constantes.URLs.BASE_URL + Constantes.URLs.ENVIAR_IMGS )
                    .post(requestBody)
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();

                if ( response.isSuccessful() && response.body() != null) {
                    String body = response.body().string();
                    Log.v(TAG, "Response IMAGENES " + body );

                    ResServer resServer = gson.fromJson(body, ResServer.class);

                    if ( resServer != null && resServer.isOkay() ) {
                        enviadas++;
                        dbManager.marcarImagenEnviada(imagen);

                        // actualizamos el progreso de la notificacion
                        builder.setProgress(cantidad, enviadas, false);
                        builder.setContentText("Enviando... (" + enviadas + "/" + cantidad + ")");
                        // ponemos en primer plano la notificacion y le asignamos un id
                        startForeground(123, builder.build());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(
                        new Intent( Constantes.Acciones.ACTUALIZAR_IMGS )
                );
    }

}
