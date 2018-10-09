package helpers.cristian.com.mapaunicorhelper.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class GaleriaUtils {

    private Context contexto;
    private Uri imgUri;

    public GaleriaUtils(Context context){
        this.contexto = context;
    }

    public Intent getIntentGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction( Intent.ACTION_GET_CONTENT );

        return Intent.createChooser(intent, "Selecciona una imagen");
    }

    public String getRuta(){
        return FileUtils.getPath(contexto, this.imgUri);
    }

    public void setImgUri(Uri imgUri){
        this.imgUri = imgUri;
    }
}
