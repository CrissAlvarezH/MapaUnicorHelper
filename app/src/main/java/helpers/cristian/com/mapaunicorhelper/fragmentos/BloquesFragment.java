package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.SetImgBloqueActivity;
import helpers.cristian.com.mapaunicorhelper.SetUbicacionActivity;
import helpers.cristian.com.mapaunicorhelper.adaptadores.BloquePosAdapter;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ResServer;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ServicioWeb;
import helpers.cristian.com.mapaunicorhelper.servicioweb.ServicioWebUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BloquesFragment extends Fragment implements BloquePosAdapter.BloquePosListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int COD_SET_UBICACION = 234;

    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerBloques;
    private BloquePosAdapter bloquesAdapter;
    private ServicioWeb servicioWeb;

    public BloquesFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_bloques, container, false);

        refresh = vista.findViewById(R.id.swipe_refresh_bloques);
        recyclerBloques = vista.findViewById(R.id.recycler_bloques);

        RecyclerView.LayoutManager lmBloques = new LinearLayoutManager(getContext());
        recyclerBloques.setLayoutManager(lmBloques);

        bloquesAdapter = new BloquePosAdapter(new ArrayList<Bloque>() , this);
        recyclerBloques.setAdapter(bloquesAdapter);

        servicioWeb = ServicioWebUtils.getServicioWeb(true);

        refresh.setOnRefreshListener(this);

        refreshBloques();

        return vista;
    }

    @Override
    public void clickBloque(final Bloque bloque, int posicion) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Bloque "+bloque.getCodigo())
                .setItems(R.array.opciones_bloque, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:

                                Intent intent = new Intent(getContext(), SetUbicacionActivity.class);
                                intent.putExtra("id_bloque", bloque.getId());

                                startActivityForResult(intent, COD_SET_UBICACION);

                                break;
                            case 1:

                                Intent intentImgs = new Intent(getContext(), SetImgBloqueActivity.class);
                                intentImgs.putExtra("num_bloque", bloque.getId());

                                startActivity(intentImgs);

                                break;
                        }

                    }
                });
        Dialog dialog = builder.create();

        if ( !getActivity().isFinishing() )
            dialog.show();


    }

    private void refreshBloques() {
        refresh.setRefreshing(true);

        Call<ResServer> resServerCall = servicioWeb.pedirTodosLosBloques();

        resServerCall.enqueue(new Callback<ResServer>() {
            @Override
            public void onResponse(Call<ResServer> call, Response<ResServer> response) {

                if ( response.isSuccessful() ) {
                    ResServer resServer = response.body();

                    if ( resServer != null && resServer.getBloques() != null ) {
                        bloquesAdapter.setBloques( resServer.getBloques() );
                    }
                }

                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResServer> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Error al pedir bloques", Toast.LENGTH_SHORT).show();
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshBloques();
    }
}
