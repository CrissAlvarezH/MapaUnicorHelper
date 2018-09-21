package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import helpers.cristian.com.mapaunicorhelper.R;


public class RutasFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mapa;

    public RutasFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_rutas, container, false);

        // TODO Terminar de ajustar el icono del FAB y referenciar el XML

        MapFragment mapFragment = (MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map_rutas);

        mapFragment.getMapAsync(this);

        return vista;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
    }
}
