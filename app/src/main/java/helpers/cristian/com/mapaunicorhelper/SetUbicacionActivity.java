package helpers.cristian.com.mapaunicorhelper;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import helpers.cristian.com.mapaunicorhelper.modelos.Posicion;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ResServer;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ServicioWeb;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ServicioWebUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, LocationListener {

    public static final String EXTRA_LONGITUDE = "longitud";
    public static final String EXTRA_LATITUDE = "latitiud";
    public static final String EXTRA_DIRECCION = "direccion";

    private static final String TAG = "ActividadMapa";
    private MapFragment mapFrament;
    private RelativeLayout layoutCargando;
    private FloatingActionButton btnSetUbicacion, btnFijarUbicacion, btnCancelar;
    private GoogleMap mapa;
    private ImageView imgMarcadorEstatico;
    private LinearLayout layoutIrMiUbicacion;
    private Button btnIrMiUbicacion;
    private LocationManager locationManager;
    private String provider;
    private Marker marker;
    private String direccion;
    private int idBloque;
    private ProgressBar progressSetPos;
    private ServicioWeb servicioWeb;

    @Override
    protected void onResume() {
        super.onResume();

        establecerListenerPosicion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ubicacion);

        idBloque = getIntent().getExtras().getInt("id_bloque");

        mapFrament = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_mapa_setubicacion);
        layoutCargando = findViewById(R.id.relative_cargando_ubicacion);
        btnSetUbicacion = findViewById(R.id.fab_guardar_ubicacion);
        btnFijarUbicacion = findViewById(R.id.fab_fijar_marcador);
        btnCancelar = findViewById(R.id.fab_cancelar);
        progressSetPos = findViewById(R.id.progress_set_pos);
        imgMarcadorEstatico = findViewById(R.id.img_marcador_estatico);
        layoutIrMiUbicacion = findViewById(R.id.layout_ir_mi_ubicacion);
        btnIrMiUbicacion = findViewById(R.id.btn_ir_mi_ubicacion);

        mapFrament.getMapAsync(this);
        btnCancelar.setOnClickListener(this);
        btnFijarUbicacion.setOnClickListener(this);
        btnSetUbicacion.setOnClickListener(this);
        btnIrMiUbicacion.setOnClickListener(this);

        servicioWeb = ServicioWebUtils.getServicioWeb(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        // Inicialmente ubicamos en bogota
        LatLng latLngBogota = new LatLng(4.0081794, -75.2522645);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngBogota, 5));

        // Si es posible, se pone la imagen del marcador en la posicion actual del dispositivo
        if (locationManager != null && provider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location posActual = locationManager.getLastKnownLocation(provider);

                if(posActual != null){
                    googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( posActual.getLatitude(), posActual.getLongitude() ), 15.5f ) );
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_guardar_ubicacion:
                if(imgMarcadorEstatico.getVisibility() == View.GONE && marker != null){

                    pedirActualizarPosicionBloque(
                            marker.getPosition().latitude + "",
                            marker.getPosition().longitude + "",
                            idBloque
                    );
                }else{
                    Toast.makeText(this, "Debe fijar una posicion primero, con el boton azul.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fab_fijar_marcador:
                if (mapa != null) {
                    if (imgMarcadorEstatico.getVisibility() == View.VISIBLE) {
                        btnFijarUbicacion.setImageResource(R.drawable.ic_location_off_blanco);

                        imgMarcadorEstatico.setVisibility(View.GONE);

                        marker = mapa.addMarker(new MarkerOptions().position(mapa.getCameraPosition().target));

                        marker.showInfoWindow();
                    } else {// si esta oculta la imagenn es porque ya se agregó un marcador
                        btnFijarUbicacion.setImageResource(R.drawable.ic_location_blanco);

                        mapa.clear();
                        imgMarcadorEstatico.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.fab_cancelar:
                finish();
                break;
            case R.id.btn_ir_mi_ubicacion:
                if (estaGpsActivado()) {
                    if (locationManager != null && provider != null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Falta conceder los permisos de posicionamiento", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Location location = locationManager.getLastKnownLocation(provider);

                        if(mapa != null){
                            if(location != null) {
                                mapa.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            }else{
                                Toast.makeText(this, "Cargando posicion, vuelva a intentar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else{
                    mandarActivarGps();
                }
                break;
        }
    }


    private void mandarActivarGps(){
        /* Construimos un dialogo que le diga que debe activar la ubicacion del dispositivo
               y le damos la opcion de mandarlo a configuraciones */
        AlertDialog.Builder builderAialogoAjusteHora = new AlertDialog.Builder(this);
        builderAialogoAjusteHora.setMessage("Debe activar el GPS")
                .setCancelable(false)
                .setPositiveButton("Ir a ajustes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Mandamos las configuraciones de posicionamiento
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Volver", null);
        // creamos el dialogo
        AlertDialog dialogo = builderAialogoAjusteHora.create();
        dialogo.show();// mostramos el dialogo
    }

    private boolean estaGpsActivado(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locationManager != null){
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return false;
    }

    private void establecerListenerPosicion(){
        /* OBTENEMOS LA LOCALIZACION */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            finish();
            return;

        }

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        if(locationManager != null){
            Criteria criteria = new Criteria();
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            provider = locationManager.getBestProvider(criteria,false);

            if(!locationManager.isProviderEnabled(provider)){
                Toast.makeText(this, "El GPS esta apagado", Toast.LENGTH_SHORT).show();

            }else{
                // Establecemos las actualizaciones cada 2 minutos y 1 metro
                locationManager.requestLocationUpdates(provider, 0, 0, this);
            }


        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void pedirActualizarPosicionBloque(String latitud, String longitud, int idBloque) {
        progressSetPos.setVisibility(View.VISIBLE);
        btnSetUbicacion.setVisibility(View.GONE);

        Call<ResServer> resServerCall = servicioWeb.actualizarPosDeBloque(
                latitud, longitud, idBloque
        );

        resServerCall.enqueue(new Callback<ResServer>() {
            @Override
            public void onResponse(Call<ResServer> call, Response<ResServer> response) {

                if ( response.isSuccessful() ) {
                    if ( response.body().isOkay() ) {
                        Toast.makeText(SetUbicacionActivity.this, "Posición guardada", Toast.LENGTH_SHORT).show();
                        SetUbicacionActivity.this.finish();
                    } else {
                        Toast.makeText(SetUbicacionActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                    }
                }

                progressSetPos.setVisibility(View.GONE);
                btnSetUbicacion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResServer> call, Throwable t) {
                progressSetPos.setVisibility(View.GONE);
                btnSetUbicacion.setVisibility(View.VISIBLE);

                Toast.makeText(SetUbicacionActivity.this, "Error al actualizar la posicion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
