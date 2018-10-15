package helpers.cristian.com.mapaunicorhelper;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import helpers.cristian.com.mapaunicorhelper.adaptadores.ImagenAdapter;
import helpers.cristian.com.mapaunicorhelper.adaptadores.SalonAdapter;
import helpers.cristian.com.mapaunicorhelper.basedatos.DBManager;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.modelos.Imagen;
import helpers.cristian.com.mapaunicorhelper.modelos.Posicion;
import helpers.cristian.com.mapaunicorhelper.modelos.Salon;
import helpers.cristian.com.mapaunicorhelper.modelos.Zona;
import helpers.cristian.com.mapaunicorhelper.utils.CamaraUtils;
import helpers.cristian.com.mapaunicorhelper.utils.GaleriaUtils;

import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGEN_BLOQUE;
import static helpers.cristian.com.mapaunicorhelper.basedatos.DBHelper.TABLA_IMAGEN_SALON;

public class AddBloqueActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback{

    private static final int INTENT_CAMARA_BLOQUE = 432;
    private static final int INTENT_GALERIA_BLOQUE = 623;
    private static final int INTENT_CAMARA_SALON = 712;
    private static final int INTENT_GALERIA_SALON = 129;

    private MapView mapView;
    private EditText edtNombre, edtCodigo;
    private Spinner spnZona;
    private TextView txtNoHayImgs, txtNoHaySalones;
    private RecyclerView recyclerImgs, recyclerSalones;
    private Button btnAddImg, btnAddSalon, btnGuardar;

    private GoogleMap mapa;
    private LatLng posBloque;

    private CamaraUtils camaraUtils;
    private GaleriaUtils galeriaUtils;

    private ImagenAdapter imgAdapter;
    private Posicion posicionBloque;

    private Imagen imagenSalon;
    private ImageView imgSalon;
    private Button btnCambiarImgSalon;
    private EditText edtCodSalon, edtPisoSalon, edtNombreSalon;
    private SalonAdapter salonAdapter;

    private DBManager dbManager;

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bloque);
        enlazarXML();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        camaraUtils = new CamaraUtils(this);
        galeriaUtils = new GaleriaUtils(this);

        posBloque = (LatLng) getIntent().getParcelableExtra("pos");

        posicionBloque = new Posicion(posBloque.latitude, posBloque.longitude);

        imgAdapter = new ImagenAdapter(this, new ArrayList<Imagen>());
        recyclerImgs.setAdapter(imgAdapter);
        recyclerImgs.setVisibility(View.GONE);

        salonAdapter = new SalonAdapter(this, new ArrayList<Salon>());
        recyclerSalones.setAdapter(salonAdapter);
        recyclerSalones.setVisibility(View.GONE);

        dbManager = new DBManager(this);
    }

    private void enlazarXML(){
        Toolbar toolbar = findViewById(R.id.toolbar_add_bloque);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar bloque");
        }

        mapView =  findViewById(R.id.map_add_bloque);
        edtNombre = findViewById(R.id.edt_nombre_add_bloque);
        edtCodigo = findViewById(R.id.edt_cod_add_bloque);
        spnZona = findViewById(R.id.spn_zona_add_bloque);
        txtNoHayImgs = findViewById(R.id.txt_no_hay_imagenes);
        txtNoHaySalones = findViewById(R.id.txt_no_hay_salones);
        recyclerImgs = findViewById(R.id.recycler_imgs_add_bloque);
        btnAddImg = findViewById(R.id.btn_add_img_bloque);
        recyclerSalones = findViewById(R.id.recycler_salones_add_bloque);
        btnAddSalon = findViewById(R.id.btn_add_salon_bloque);
        btnGuardar = findViewById(R.id.btn_guardar_bloque);

        RecyclerView.LayoutManager lmImgs = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerImgs.setLayoutManager(lmImgs);

        RecyclerView.LayoutManager lmSalones = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerSalones.setLayoutManager(lmSalones);

        btnAddImg.setOnClickListener(this);
        btnAddSalon.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AlertDialog.Builder builderDialog = new AlertDialog.Builder(this)
                        .setMessage("¿Seguro que quieres salir?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AddBloqueActivity.this.finish();;
                            }
                        })
                        .setNegativeButton("NO", null);

                builderDialog.create().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this)
                .setMessage("¿Seguro que quieres salir?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddBloqueActivity.this.finish();;
                    }
                })
                .setNegativeButton("NO", null);

        builderDialog.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_img_bloque:
                if(pedirPermisosCamara()){
                    AlertDialog.Builder builderDialog = new AlertDialog.Builder(this)
                            .setMessage("¿De donde desea tomar la imagen?")
                            .setPositiveButton("CAMARA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        Intent intentCamara = camaraUtils.crearIntentCamera();

                                        startActivityForResult(intentCamara, INTENT_CAMARA_BLOQUE);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(AddBloqueActivity.this, "Error al tomar imagen", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("GALERIA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intentGaleria = galeriaUtils.getIntentGaleria();

                                    startActivityForResult(intentGaleria, INTENT_GALERIA_BLOQUE);

                                }
                            });

                    builderDialog.create().show();
                }

                break;
            case R.id.btn_add_salon_bloque:

                final Dialog dialogAddSalon = new Dialog(this);
                dialogAddSalon.setContentView(R.layout.dialog_add_salon);

                imgSalon = dialogAddSalon.findViewById(R.id.add_img_salon);
                btnCambiarImgSalon = dialogAddSalon.findViewById(R.id.btn_add_img_salon);
                Button btnCancelar = dialogAddSalon.findViewById(R.id.btn_cancelar_add_salon);
                Button btnGuardarSalon = dialogAddSalon.findViewById(R.id.btn_guardar_add_salon);
                edtCodSalon = dialogAddSalon.findViewById(R.id.edt_add_cod_salon);
                edtPisoSalon = dialogAddSalon.findViewById(R.id.edt_add_piso_salon);
                edtNombreSalon = dialogAddSalon.findViewById(R.id.edt_add_nombre_salon);

                btnCambiarImgSalon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builderDialog = new AlertDialog.Builder(AddBloqueActivity.this)
                                .setMessage("¿De donde desea tomar la imagen?")
                                .setPositiveButton("CAMARA", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            Intent intentCamara = camaraUtils.crearIntentCamera();

                                            startActivityForResult(intentCamara, INTENT_CAMARA_SALON);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Toast.makeText(AddBloqueActivity.this, "Error al tomar imagen", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                                .setNegativeButton("GALERIA", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intentGaleria = galeriaUtils.getIntentGaleria();

                                        startActivityForResult(intentGaleria, INTENT_GALERIA_SALON);

                                    }
                                });

                        builderDialog.create().show();
                    }
                });

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( !AddBloqueActivity.this.isFinishing() )
                            dialogAddSalon.dismiss();
                    }
                });

                btnGuardarSalon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validarCamposSalon()){
                            String cod = edtCodSalon.getText().toString().trim();
                            String nombre = edtNombreSalon.getText().toString().trim();
                            int piso = Integer.parseInt( edtPisoSalon.getText().toString().trim() );

                            Salon salon = new Salon(
                                    nombre,
                                    cod,
                                    null,// el bloque aún no se ha insertado
                                    piso,
                                    imagenSalon
                            );

                            recyclerSalones.setVisibility(View.VISIBLE);
                            txtNoHaySalones.setVisibility(View.GONE);

                            salonAdapter.agregarSalon(salon);
                            recyclerSalones.smoothScrollToPosition(0);

                            imagenSalon = null;// para el proximo salon

                            if( !AddBloqueActivity.this.isFinishing() )
                                dialogAddSalon.dismiss();
                        }
                    }
                });

                dialogAddSalon.show();

                break;
            case R.id.btn_guardar_bloque:

                if( validarCamposBloque() ){
                    guardar();
                }

                break;
        }
    }

    private void guardar(){
        btnGuardar.setEnabled(false);// Para que no de doble click

        // Insertamos la posicion del bloque
        long idPos = dbManager.insertarModelo(posicionBloque);

        posicionBloque.setId( (int) idPos );

        Bloque bloque = new Bloque(
                edtNombre.getText().toString().trim(),
                edtCodigo.getText().toString().trim(),
                new Zona( spnZona.getSelectedItemPosition(), "" ), // solo importa el id
                posicionBloque
        );

        // Insertamos el bloque
        long bloqueId = dbManager.insertarModelo(bloque);
        bloque.setId( (int) bloqueId );

        ArrayList<Salon> salones = salonAdapter.getSalones();

        // Insertamos los salones del bloque
        for( Salon salon : salones ) {
            salon.setBloque(bloque);

            long idSalon = dbManager.insertarModelo(salon);

            // Insertamos y relacionamos la imagen con el salon
            long idImg = dbManager.insertarModelo(salon.getImagen());

            dbManager.ejecutarSql(
                    "INSERT INTO "+TABLA_IMAGEN_SALON+" VALUES (?, ?)",
                    new String[] {idImg+"", idSalon+""}
            );
        }

        ArrayList<Imagen> imagenesBloque = imgAdapter.getImagenes();

        // Insertamos y relacionamos las imagenes con el bloque
        for( Imagen imagen : imagenesBloque ) {
            long idImg = dbManager.insertarModelo(imagen);

            dbManager.ejecutarSql(
                    "INSERT INTO "+TABLA_IMAGEN_BLOQUE+" VALUES (?, ?)",
                    new String[] {idImg+"", bloque.getId() + ""}
            );
        }

        Toast.makeText(this, "Bloque guardado", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setMapToolbarEnabled(false);
        mapa.getUiSettings().setScrollGesturesEnabled(false);

        mapa.moveCamera( CameraUpdateFactory.newLatLngZoom(posBloque, 16) );
        mapa.addMarker( new MarkerOptions().position(posBloque) );
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String rutaImg = null;
        String ahora = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case INTENT_CAMARA_BLOQUE:
                    recyclerImgs.setVisibility(View.VISIBLE);
                    txtNoHayImgs.setVisibility(View.GONE);

                    rutaImg = camaraUtils.getRuta();

                    imgAdapter.agregarImagen(new Imagen(
                            rutaImg,
                            ahora,
                            posicionBloque
                    ));

                    recyclerImgs.smoothScrollToPosition(0);

                    break;
                case INTENT_GALERIA_BLOQUE:
                    recyclerImgs.setVisibility(View.VISIBLE);
                    txtNoHayImgs.setVisibility(View.GONE);

                    galeriaUtils.setImgUri( data.getData() );

                    rutaImg = galeriaUtils.getRuta();

                    imgAdapter.agregarImagen(new Imagen(
                            rutaImg,
                            ahora,
                            posicionBloque
                    ));

                    recyclerImgs.smoothScrollToPosition(0);

                    break;
                case INTENT_CAMARA_SALON:
                    rutaImg = camaraUtils.getRuta();

                    Glide.with(this)
                            .load(rutaImg)
                            .into(imgSalon);

                    imagenSalon = new Imagen(
                            rutaImg,
                            ahora,
                            null// Esta imagen solo está asociada al salón
                    );

                    break;
                case INTENT_GALERIA_SALON:

                    galeriaUtils.setImgUri( data.getData() );

                    rutaImg = galeriaUtils.getRuta();

                    Glide.with(this)
                            .load(rutaImg)
                            .into(imgSalon);

                    imagenSalon = new Imagen(
                            rutaImg,
                            ahora,
                            null// Esta imagen solo está asociada al salón
                    );

                    break;
            }

        }

    }

    private boolean validarCamposBloque(){
        if( edtCodigo.getText().toString().trim().isEmpty() ){
            edtCodigo.setError("Campo obligatorio");
            edtCodigo.requestFocus();
            return false;
        }

        if( spnZona.getSelectedItemPosition() == 0 ){
            Toast.makeText(this, "Elija la zona", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( imgAdapter.getItemCount() == 0 ){
            Toast.makeText(this, "Debe tomar al menos una imagen para el bloque", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( salonAdapter.getItemCount() == 0 ){
            Toast.makeText(this, "Debe agregar los salones", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validarCamposSalon(){
        if( imagenSalon == null ){
            Toast.makeText(this, "Debe tomar la imagen del salon", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( edtCodSalon.getText().toString().trim().isEmpty() ){
            edtCodSalon.setError("Campo obligatorio");
            edtCodSalon.requestFocus();
            return false;
        }

        if( edtPisoSalon.getText().toString().trim().isEmpty() ){
            edtPisoSalon.setError("Campo obligatorio");
            edtPisoSalon.requestFocus();
            return false;
        }

        if( salonAdapter.hayUnSalonConEsteCod( edtCodSalon.getText().toString().trim() ) ) {
            Toast.makeText(this, "Ya existe un salon con ese codigo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
