package helpers.cristian.com.mapaunicorhelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.modelos.Imagen;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ResServer;
import helpers.cristian.com.mapaunicorhelper.utils.CamaraUtils;
import helpers.cristian.com.mapaunicorhelper.utils.Constantes;
import helpers.cristian.com.mapaunicorhelper.utils.GaleriaUtils;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class SetImgBloqueActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int INTENT_CAMARA = 423;
    private static final int INTENT_GALERIA = 152;

    private ImageView img2, img1, img3;
    private Button btnAddImg1, btnAddImg2, btnAddImg3, btnEnviar;
    private ProgressBar progress;

    private CamaraUtils camaraUtils;
    private GaleriaUtils galeriaUtils;

    private String ruta1, ruta2, ruta3;

    private int imagenTomada = 1;
    private int numBloque;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_img_bloque);
        gson = new Gson();

        camaraUtils = new CamaraUtils(this);
        galeriaUtils = new GaleriaUtils(this);

        numBloque = getIntent().getIntExtra("num_bloque", -1);

        if ( numBloque == -1 ) finish();

        Toolbar toolbar = findViewById(R.id.toolbar_set_img_bloque);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bloque "+numBloque);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img1 = findViewById(R.id.img_bloque_1);
        img2 = findViewById(R.id.img_bloque_2);
        img3 = findViewById(R.id.img_bloque_3);
        btnAddImg1 = findViewById(R.id.btn_add_img_bloque_1);
        btnAddImg2 = findViewById(R.id.btn_add_img_bloque_2);
        btnAddImg3 = findViewById(R.id.btn_add_img_bloque_3);
        btnEnviar = findViewById(R.id.btn_enviar_imgs_bloque);
        progress = findViewById(R.id.progress_set_imgs_bloque);

        btnAddImg1.setOnClickListener(this);
        btnAddImg2.setOnClickListener(this);
        btnAddImg3.setOnClickListener(this);
        btnEnviar.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.btn_add_img_bloque_1:

                preguntarDeDondeTomarImagen(1);

                break;
            case R.id.btn_add_img_bloque_2:

                preguntarDeDondeTomarImagen(2);

                break;
            case R.id.btn_add_img_bloque_3:

                preguntarDeDondeTomarImagen(3);

                break;
            case R.id.btn_enviar_imgs_bloque:

                enviarImagenes();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String rutaImg = null;

        if(resultCode == RESULT_OK){
            switch (requestCode){

                case INTENT_CAMARA:

                    rutaImg = camaraUtils.getRuta();

                    break;
                case INTENT_GALERIA:

                    galeriaUtils.setImgUri( data.getData() );

                    rutaImg = galeriaUtils.getRuta();

                    break;
            }

            switch (imagenTomada) {
                case 1:

                    ruta1 = rutaImg;

                    Glide.with(this)
                            .load(rutaImg)
                            .into(img1);

                    break;
                case 2:

                    ruta2 = rutaImg;

                    Glide.with(this)
                            .load(rutaImg)
                            .into(img2);

                    break;
                case 3:

                    ruta3 = rutaImg;

                    Glide.with(this)
                            .load(rutaImg)
                            .into(img3);

                    break;
            }

        }
    }

    private void enviarImagenes() {

        if ( ruta1 == null || ruta2 == null || ruta3 == null ) {
            Toast.makeText(this, "Tiene que tomar las tres imagenes", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.setVisibility(View.VISIBLE);
        btnEnviar.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {

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

                builderBody.addFormDataPart("numBloque", numBloque+"");
                builderBody.addFormDataPart("numImagenes", 3+"");
                builderBody.addFormDataPart("tipo", "bloque");

                // Agregamos las tres imagenes en un ciclo
                for ( int numImg = 1; numImg <= 3; numImg++ ) {
                    String ruta = ruta1;

                    // Agregamos una imagen diferente cada ciclo
                    if ( numImg == 1 ) ruta = ruta1;
                    if ( numImg == 2 ) ruta = ruta2;
                    if ( numImg == 3 ) ruta = ruta3;

                    File imgFileOriginal = new File(ruta);

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
                    builderBody.addFormDataPart("imagen"+numImg, "imagen"+numImg, imgFileBody);

                }


                RequestBody requestBody = builderBody.build();

                Request request = new Request.Builder()
                        .url(Constantes.URLs.BASE_URL + Constantes.URLs.ENVIAR_IMGS )
                        .post(requestBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    if ( response.isSuccessful() && response.body() != null) {
                        String body = response.body().string();

                        ResServer resServer = gson.fromJson(body, ResServer.class);

                        if ( resServer != null && resServer.isOkay() ) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setVisibility(View.GONE);
                                    btnEnviar.setVisibility(View.VISIBLE);

                                    Toast.makeText(SetImgBloqueActivity.this, "Imagenes enviadas correctamente", Toast.LENGTH_SHORT).show();
                                    SetImgBloqueActivity.this.finish();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setVisibility(View.GONE);
                                    btnEnviar.setVisibility(View.VISIBLE);

                                    Toast.makeText(SetImgBloqueActivity.this, "No se pudo guardar las imagenes", Toast.LENGTH_SHORT).show();
                                    SetImgBloqueActivity.this.finish();
                                }
                            });
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.setVisibility(View.GONE);
                            btnEnviar.setVisibility(View.VISIBLE);

                            Toast.makeText(SetImgBloqueActivity.this, "Error al enviar las imagenes", Toast.LENGTH_SHORT).show();
                            SetImgBloqueActivity.this.finish();
                        }
                    });
                }

            }
        }).start();

    }


    private void preguntarDeDondeTomarImagen(final int numImg) {
        if(pedirPermisosCamara()){
            AlertDialog.Builder builderDialog = new AlertDialog.Builder(this)
                    .setMessage("¿De donde desea tomar la imagen?")
                    .setPositiveButton("CAMARA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                Intent intentCamara = camaraUtils.crearIntentCamera();

                                imagenTomada = numImg;
                                startActivityForResult(intentCamara, INTENT_CAMARA);

                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(SetImgBloqueActivity.this, "Error al tomar imagen", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton("GALERIA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intentGaleria = galeriaUtils.getIntentGaleria();

                            imagenTomada = numImg;

                            startActivityForResult(intentGaleria, INTENT_GALERIA);

                        }
                    });

            builderDialog.create().show();
        }

    }

    private boolean pedirPermisosCamara(){
        boolean todosConsedidos = true;
        ArrayList<String> permisosFaltantes = new ArrayList<>();

        boolean permisoCamera = ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED);

        boolean permisoEscrituraSD = ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);

        boolean permisoLecturaSD = ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);


        if(!permisoCamera){
            todosConsedidos = false;
            permisosFaltantes.add(android.Manifest.permission.CAMERA);
        }

        if(!permisoEscrituraSD){
            todosConsedidos = false;
            permisosFaltantes.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permisoLecturaSD){
            todosConsedidos = false;
            permisosFaltantes.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(!todosConsedidos) {
            String[] permisos = new String[permisosFaltantes.size()];
            permisos = permisosFaltantes.toArray(permisos);

            ActivityCompat.requestPermissions(this, permisos, 5234);
        }

        return todosConsedidos;
    }
}
