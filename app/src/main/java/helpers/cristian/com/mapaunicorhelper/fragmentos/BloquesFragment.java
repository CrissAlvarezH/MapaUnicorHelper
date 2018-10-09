package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import helpers.cristian.com.mapaunicorhelper.AddBloqueActivity;
import helpers.cristian.com.mapaunicorhelper.MainActivity;
import helpers.cristian.com.mapaunicorhelper.R;


public class BloquesFragment extends Fragment implements OnMapReadyCallback, MainActivity.ListenerPosiciones, View.OnClickListener {

    private final String TAG = "FragmentoBloques";

    private Marker miMarker;
    private GoogleMap mapa;

    private FloatingActionButton fabAddBloque;

    private MapView mapView;
    private ProgressBar progress;

    private LatLng posActual;

    public BloquesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_bloques, container, false);
        miMarker = null;
        posActual = null;

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
                            if(location != null)
                                establecerPosicion(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    });
        }


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
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();

        ( (MainActivity) getContext()).removerListenerPosicion(this);
    }
}
