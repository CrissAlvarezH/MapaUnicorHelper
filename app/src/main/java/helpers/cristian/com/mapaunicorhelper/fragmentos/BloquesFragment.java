package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.AddBloqueActivity;
import helpers.cristian.com.mapaunicorhelper.MainActivity;
import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.adaptadores.SalonAdapter;
import helpers.cristian.com.mapaunicorhelper.basedatos.DBManager;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.modelos.Salon;


public class BloquesFragment extends Fragment implements OnMapReadyCallback,
        MainActivity.ListenerPosiciones,
        View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener{

    private final String TAG = "FragmentoBloques";

    private Marker miMarker;
    private GoogleMap mapa;

    private FloatingActionButton fabAddBloque;

    private MapView mapView;
    private ProgressBar progress;

    private LatLng posActual;

    private DBManager dbManager;
    private ArrayList<Bloque> bloques = new ArrayList<>();


    public BloquesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        refreshBloques();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_bloques, container, false);
        miMarker = null;
        posActual = null;
        dbManager = new DBManager(getContext());

        progress = vista.findViewById(R.id.progress_bloques);
        fabAddBloque = vista.findViewById(R.id.fab_add_bloque);
        fabAddBloque.setOnClickListener(this);

        mapView = vista.findViewById(R.id.map_bloques);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        ((MainActivity) getContext()).agregarListenerPosicion(this);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            new FusedLocationProviderClient(getContext()).getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

                                establecerPosicion(pos);
                            }
                        }
                    });
        }

        return vista;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setMapToolbarEnabled(false);
        mapa.setOnInfoWindowClickListener(this);

        refreshBloques();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            new FusedLocationProviderClient(getContext()).getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

                                if (miMarker != null) {
                                    miMarker.setPosition(pos);
                                    return;
                                }

                                miMarker = mapa.addMarker(new MarkerOptions().title("Mi posición").position(pos));
                            }
                        }
                    });
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Integer idBloque = (Integer) marker.getTag();

        if( idBloque != null ) {
            startActivity(
                    new Intent(getContext(), AddBloqueActivity.class)
                            .putExtra("id_bloque", idBloque)
                            .putExtra("pos", marker.getPosition())
            );
        }
    }


    private void refreshBloques(){
        bloques = dbManager.getBoques(null, null);
        if( mapa != null ){
            mapa.clear();
            miMarker = null;// porque limpiamos el mapa

            // Colocamos los marcadores de las posiciones de los bloques
            for(Bloque bloque : bloques){
                MarkerOptions mkopt = new MarkerOptions()
                        .title( bloque.getCodigo() + " - " + bloque.getNombre() )
                        .position( new LatLng(
                                bloque.getPosicion().getLatitud(),
                                bloque.getPosicion().getLongitud()
                        ))
                        .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE) );

                Marker marker = mapa.addMarker( mkopt );
                marker.setTag( bloque.getId() );// Para identificar el marcador con el bloque
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_bloque:
                if(posActual != null){
                    startActivity(
                            new Intent(getContext(), AddBloqueActivity.class).putExtra("pos", posActual)
                    );
                }else{
                    Toast.makeText(getContext(), "Aún no se ha cargado la posición", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void cambioPosicion(LatLng posicion) {
        establecerPosicion(posicion);
    }

    private void establecerPosicion(LatLng pos){
        posActual = pos;

        if( mapa != null) {
            if (miMarker != null) {
                miMarker.setPosition(pos);
                return;
            }

            miMarker = mapa.addMarker(new MarkerOptions().title("Mi posición").position(pos));

            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            new FusedLocationProviderClient(getContext()).getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if( location != null )
                                establecerPosicion(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();

        ( (MainActivity) getContext()).removerListenerPosicion(this);
    }



}
