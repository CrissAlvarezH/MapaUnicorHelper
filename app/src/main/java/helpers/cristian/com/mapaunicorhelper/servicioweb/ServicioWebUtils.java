package helpers.cristian.com.mapaunicorhelper.servicioweb;

import android.util.Log;

import helpers.cristian.com.mapaunicorhelper.utils.Constantes;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioWebUtils {
    public static ServicioWeb getServicioWeb(boolean conInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (conInterceptor) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.v("LogginWebHelper", message);
                }
            });

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.addInterceptor(interceptor).build();
        }

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( Constantes.URLs.BASE_URL )
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ServicioWeb.class);
    }


}
