package helpers.cristian.com.mapaunicorhelper.modelos;

import android.content.ContentValues;

public interface BaseModelo {
    ContentValues toContentValues();
    String getNombreTabla();
}
