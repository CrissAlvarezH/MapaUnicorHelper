package helpers.cristian.com.mapaunicorhelper;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.fragmentos.BloquesFragment;
import helpers.cristian.com.mapaunicorhelper.fragmentos.RutasFragment;
import helpers.cristian.com.mapaunicorhelper.fragmentos.InfoFragment;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.utils.Constantes;

public class MainActivity extends FragmentActivity {
    private final String TAG = "ActividadMain";
    private int codPermisos = 4324;

    private PagerAdaptador pagerAdaptador;

    private ViewPager viewPager;
    private TabLayout tabs;

    private FusedLocationProviderClient clientePosiciones;
    private LinearLayout layoutPosNoDisp;

    private ArrayList<ListenerPosiciones> listenerPosiciones = new ArrayList<>();
    private PosicionesCallback posicionesCallbak;
    private LocationRequest locationRequest;
    private MiReceiver miReceiver;

    public interface ListenerPosiciones {
        void cambioPosicion(LatLng posicion);
    }

    public void agregarListenerPosicion(ListenerPosiciones listener){
        this.listenerPosiciones.add(listener);
    }

    public void removerListenerPosicion(ListenerPosiciones listener){
        this.listenerPosiciones.remove(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( Constantes.Acciones.ACTUALIZAR_BLOQUES );
        intentFilter.addAction( Constantes.Acciones.ACTUALIZAR_IMGS );

        miReceiver = new MiReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(miReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutPosNoDisp = findViewById(R.id.layout_pos_no_disp);
        viewPager = findViewById(R.id.pager);
        tabs = findViewById(R.id.tabs);

        agregarFragmentosToTabs(viewPager, tabs);

        clientePosiciones = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(0);
        locationRequest.setInterval(0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Debes conseder los permisos", Toast.LENGTH_SHORT).show();
            validarPermisos(true);
        }else{
            posicionesCallbak = new PosicionesCallback();
            clientePosiciones.requestLocationUpdates(locationRequest, posicionesCallbak, null);
        }
    }

    // =============================================================================================
    // [ INICIO ] CONFIGUTACION DE LOS TABS CON LOS FRAGMENTS

    private void agregarFragmentosToTabs(ViewPager viewPager, TabLayout tabs){
        pagerAdaptador = new PagerAdaptador(getSupportFragmentManager());

        pagerAdaptador.agregarFragment(new BloquesFragment(), "BLOQUES");
        pagerAdaptador.agregarFragment(new RutasFragment(), "RUTAS");
        pagerAdaptador.agregarFragment(new InfoFragment(), "INFO");

        viewPager.setAdapter(pagerAdaptador);
        tabs.setupWithViewPager(viewPager);
    }

    public class PagerAdaptador extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentos;
        private ArrayList<String> titulos;

        public PagerAdaptador(FragmentManager fm) {
            super(fm);
            fragmentos = new ArrayList<>();
            titulos = new ArrayList<>();
        }

        public void agregarFragment(Fragment fragment, String titulo){
            this.fragmentos.add(fragment);
            this.titulos.add(titulo);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);

            fragmentos.set(position, fragment);

            return fragment;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titulos.get(position);
        }
    }

    // [ FIN ] CONFIGUTACION DE LOS TABS CON LOS FRAGMENTS
    // =============================================================================================

    public class PosicionesCallback extends LocationCallback {

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);

            if( locationAvailability.isLocationAvailable() ){
                layoutPosNoDisp.setVisibility(View.GONE);
            }else{
                layoutPosNoDisp.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if( layoutPosNoDisp.getVisibility() == View.GONE )
                layoutPosNoDisp.setVisibility(View.GONE);

            for(ListenerPosiciones listener : listenerPosiciones){

                if(listener != null) {
                    listener.cambioPosicion(
                            new LatLng(
                                    locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude()
                            )
                    );
                }
            }
        }
    }

    public boolean validarPermisos(boolean pedirlos){
        boolean todosConsedidos = true;
        ArrayList<String> permisosFaltantes = new ArrayList<>();


        boolean permisoFineLocation = ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);

        boolean permisoCoarseLocation = ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);


        if(!permisoFineLocation){
            todosConsedidos = false;
            permisosFaltantes.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(!permisoCoarseLocation){
            todosConsedidos = false;
            permisosFaltantes.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }


        if(!todosConsedidos && pedirlos){
            String [] permisosArray = new String[permisosFaltantes.size()];
            permisosArray = permisosFaltantes.toArray(permisosArray);

            ActivityCompat.requestPermissions(this, permisosArray, codPermisos);
        }

        return todosConsedidos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == codPermisos) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                validarPermisos(true);


            } else {
                posicionesCallbak = new PosicionesCallback();
                clientePosiciones.requestLocationUpdates(locationRequest, posicionesCallbak, null);
            }

        }
    }

    private class MiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ( intent != null && intent.getAction() != null ) {
                switch ( intent.getAction() ) {
                    case Constantes
                            .Acciones.ACTUALIZAR_BLOQUES:

                        try {

                            InfoFragment infoFragment = (InfoFragment) pagerAdaptador.getItem(2);

                            infoFragment.refreshBloques();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constantes.Acciones.ACTUALIZAR_IMGS:

                        try {

                            InfoFragment infoFragment = (InfoFragment) pagerAdaptador.getItem(2);

                            infoFragment.refreshImgs();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if ( miReceiver != null ) LocalBroadcastManager.getInstance(this).unregisterReceiver(miReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( clientePosiciones != null && posicionesCallbak != null)
            clientePosiciones.removeLocationUpdates(posicionesCallbak);
    }
}
