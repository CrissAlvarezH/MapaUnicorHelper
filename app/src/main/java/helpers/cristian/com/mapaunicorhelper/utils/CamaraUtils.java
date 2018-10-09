package helpers.cristian.com.mapaunicorhelper.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CamaraUtils {
    private String ruta;
    private Context contexto;

    public CamaraUtils(Context contexto){
        this.contexto = contexto;
    }

    public Intent crearIntentCamera() throws IOException {
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

        File imgFile = crearImagenFile();

        if(imgFile != null){
            Uri uri;

            if( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ){
                uri = Uri.fromFile(imgFile);
            }else{
                uri = FileProvider.getUriForFile(contexto,
                        "helpers.cristian.com.mapaunicorhelper.provider",
                        imgFile);
            }

            intent.putExtra("output", uri);
        }

        return intent;
    }

    private File crearImagenFile() throws IOException {
        String fechaActual = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                                                    .format( new Date());

        File dirGuardado = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
        dirGuardado.mkdirs();

        File imagen = File.createTempFile(fechaActual, ".jpg", dirGuardado);

        this.ruta = imagen.getAbsolutePath();

        return imagen;
    }

    public String getRuta(){
        return this.ruta;
    }
}
