package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.MainActivity;
import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.basedatos.DBManager;
import helpers.cristian.com.mapaunicorhelper.modelos.Posicion;
import helpers.cristian.com.mapaunicorhelper.modelos.Ruta;


public class RutasFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, MainActivity.ListenerPosiciones {

    private GoogleMap mapa;
    private FloatingActionButton fabAddRuta, fabAddPos;

    private Marker miMarker;
    private MapView mapView;
    private Spinner spnRutas;
    private ProgressBar progress;

    private ArrayList<Ruta> rutas;
    private Posicion posicionActual;

    private DBManager dbManager;

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
        dbManager = new DBManager(getContext());

        progress = vista.findViewById(R.id.progress_rutas);
        fabAddRuta = vista.findViewById(R.id.fab_add_ruta);
        fabAddPos = vista.findViewById(R.id.fab_add_posicion_a_ruta);
        spnRutas = vista.findViewById(R.id.spn_rutas);

        mapView = vista.findViewById(R.id.map_rutas);
        mapView.onCreate(savedInstanceState);

        fabAddPos.setOnClickListener(this);
        fabAddRuta.setOnClickListener(this);

        mapView.getMapAsync(this);

        ( (MainActivity) getContext()).agregarListenerPosicion(this);

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

        spnRutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pintarRutas( rutas.get(i).getId() );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        refreshSpinner();


        return vista;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setMapToolbarEnabled(false);

        if( spnRutas.getCount() > 0 ) {
            // Esto solo funciona si los id de las rutas son 1, 2, 3, etc...
            pintarRutas( rutas.get( spnRutas.getSelectedItemPosition() + 1 ).getId() );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_ruta:
                Dialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("¿Seguro que quiere agregar una ruta?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dbManager.agregarRuta();
                                refreshSpinner();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .create();

                dialog.show();

                break;
            case R.id.fab_add_posicion_a_ruta:

                Dialog dialogPos = new AlertDialog.Builder(getContext())
                        .setMessage("¿Seguro que quiere agregar una posición?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if( posicionActual != null ) {
                                    Posicion pos = new Posicion(posicionActual.getLatitud(), posicionActual.getLongitud());

                                    int idPos = (int) dbManager.insertarModelo(pos);

                                    pos.setId(idPos);

                                    dbManager.agregarPosToRuta(pos, rutas.get( spnRutas.getSelectedItemPosition() ).getId());

                                    pintarRutas( rutas.get( spnRutas.getSelectedItemPosition() ).getId() );

                                } else {
                                    Toast.makeText(getContext(), "Posición nula", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("NO", null)
                        .create();

                dialogPos.show();

                break;
        }
    }


    @Override
    public void cambioPosicion(LatLng posicion) {
        establecerPosicion(posicion);
    }

    private void establecerPosicion(LatLng pos){
        posicionActual = new Posicion(pos.latitude, pos.longitude);

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

    private void refreshSpinner() {
        rutas = dbManager.getRutas(null, null);

        String[] rutasArray = new String[rutas.size()];

        int i = 0;
        for(Ruta ruta : rutas){
            rutasArray[i] = ruta.getNombre();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, rutasArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnRutas.setAdapter(adapter);

    }

    private void pintarRutas(int idRutaSeleccionada) {
        if( idRutaSeleccionada != -1 ) {// No hay rutas
            mapa.clear();// limpiamos las polilineas anteriores

            ArrayList<Ruta> rutas = dbManager.getRutas(null, null);

            for (Ruta ruta : rutas) {
                if( ruta.getPosiciones().size() > 0 ) {
                    // Creamos una polilinea por cada ruta
                    PolylineOptions plOpt = new PolylineOptions();

                    for (Posicion posicion : ruta.getPosiciones()) {
                        plOpt.add(new LatLng(posicion.getLatitud(), posicion.getLongitud()));
                    }

                    // Si la polilinea es de la ruta seleccionada establacemos un color mas llamativo
                    if (ruta.getId() == idRutaSeleccionada) {
                        plOpt.color(Color.parseColor("#730c43"));

                        // Ponemos un marcador indicando el inicio de la ruta
                        Posicion posInicio = ruta.getPosiciones().get(0);
                        LatLng latLngInicio = new LatLng( posInicio.getLatitud(), posInicio.getLongitud() );
                        mapa.addMarker(
                                new MarkerOptions().title("Inicio")
                                        .position( latLngInicio )
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_inicio_recorrido))
                        );

                    } else {
                        plOpt.color(Color.parseColor("#3c000000"));
                    }

                    mapa.addPolyline(plOpt);// Agregamos la polilinea al mapa

                }else {
                    // Si es la ruta seleccionada y no tiene posiciones
                    if( ruta.getId() == idRutaSeleccionada )
                        Toast.makeText(getContext(), "La ruta "+idRutaSeleccionada+" no tiene posiciones", Toast.LENGTH_SHORT).show();
                }
            }

            if( miMarker != null && posicionActual != null ) {
                // Volvemos a colocar el marcador que se quitó con el clean
                miMarker = mapa.addMarker(new MarkerOptions().title("Mi posición")
                        .position(new LatLng(posicionActual.getLatitud(), posicionActual.getLongitud())));

            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();

        ( (MainActivity) getContext()).removerListenerPosicion(this);
    }
}
