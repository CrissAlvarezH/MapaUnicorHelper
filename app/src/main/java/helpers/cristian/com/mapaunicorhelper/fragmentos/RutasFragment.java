package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import helpers.cristian.com.mapaunicorhelper.MainActivity;
import helpers.cristian.com.mapaunicorhelper.R;


public class RutasFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, MainActivity.ListenerPosiciones {

    private GoogleMap mapa;
    private FloatingActionButton fabAddRuta, fabAddPos;

    private Marker miMarker;
    private MapView mapView;
    private ProgressBar progress;

    public RutasFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_rutas, container, false);
        miMarker = null;

        progress = vista.findViewById(R.id.progress_rutas);
        fabAddRuta = vista.findViewById(R.id.fab_add_ruta);
        fabAddPos = vista.findViewById(R.id.fab_add_posicion_a_ruta);

        mapView = vista.findViewById(R.id.map_rutas);
        mapView.onCreate(savedInstanceState);

        fabAddPos.setOnClickListener(this);
        fabAddRuta.setOnClickListener(this);

        mapView.getMapAsync(this);

        ( (MainActivity) getContext()).agregarListenerPosicion(this);

        return vista;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_ruta:

                break;
            case R.id.fab_add_posicion_a_ruta:

                break;
        }
    }


    @Override
    public void cambioPosicion(LatLng posicion) {

        if( mapa != null) {
            if (miMarker != null) {
                miMarker.setPosition(posicion);
                return;
            }

            miMarker = mapa.addMarker(new MarkerOptions().title("Mi posición").position(posicion));

            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();

        ( (MainActivity) getContext()).removerListenerPosicion(this);
    }
}
