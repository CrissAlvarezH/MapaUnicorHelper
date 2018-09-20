package helpers.cristian.com.mapaunicorhelper.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import helpers.cristian.com.mapaunicorhelper.modelos.BaseModelo;

public class DBManager {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DBManager(Context contexto) {
        this.dbHelper = new DBHelper(contexto);
    }

    public long insertarModelo(BaseModelo modelo){
        db = dbHelper.getWritableDatabase();

        return db.insert(modelo.getNombreTabla(), null, modelo.toContentValues());
    }

    public int actualizarModelo(BaseModelo modelo, String where, String[] args){
        db = dbHelper.getWritableDatabase();

        return db.update(modelo.getNombreTabla(), modelo.toContentValues(), where, args);
    }

    private void ejecutarSql(String sql, String[] args){
        db = dbHelper.getWritableDatabase();

        if( args != null)
            db.execSQL(sql, args);
        else
            db.execSQL(sql);
    }
}
