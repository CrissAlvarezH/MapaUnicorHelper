package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.adaptadores.BloquesAdapter;
import helpers.cristian.com.mapaunicorhelper.adaptadores.ImagenAdapter;
import helpers.cristian.com.mapaunicorhelper.basedatos.DBManager;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;
import helpers.cristian.com.mapaunicorhelper.modelos.Imagen;
import helpers.cristian.com.mapaunicorhelper.servicios.EnviarInfoService;


public class InfoFragment extends Fragment implements View.OnClickListener {

    private TextView txtImgs, txtBloques;
    private RecyclerView recyclerImgs;
    private RecyclerView recyclerBloque;
    private ImagenAdapter imgsAdapter;
    private BloquesAdapter bloquesAdapter;
    private DBManager dbManager;
    private FloatingActionButton btnEnviarInfo;

    public InfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_info, container, false);
        dbManager = new DBManager(getContext());

        txtImgs = vista.findViewById(R.id.txt_imgs);
        txtBloques = vista.findViewById(R.id.txt_bloques);
        recyclerImgs = vista.findViewById(R.id.recycler_imgs);
        recyclerBloque = vista.findViewById(R.id.recycler_bloques);
        btnEnviarInfo = vista.findViewById(R.id.btn_enviar_info);

        RecyclerView.LayoutManager lmImgs = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerImgs.setLayoutManager(lmImgs);

        recyclerBloque.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager lmBloques = new LinearLayoutManager(getContext());
        recyclerBloque.setLayoutManager(lmBloques);

        imgsAdapter = new ImagenAdapter(getContext(), new ArrayList<Imagen>(), null);
        recyclerImgs.setAdapter(imgsAdapter);

        bloquesAdapter = new BloquesAdapter(new ArrayList<Bloque>());
        recyclerBloque.setAdapter(bloquesAdapter);

        btnEnviarInfo.setOnClickListener(this);

        refreshImgs();
        refreshBloques();
        return vista;
    }

    public void refreshImgs() {
        ArrayList<Imagen> imagenes = dbManager.getImagenesNoEnviadas();

        imgsAdapter.setImagenes(imagenes);
    }

    public void refreshBloques() {
        ArrayList<Bloque> bloques = dbManager.getBloqueInfo();

        bloquesAdapter.setBloques(bloques);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.btn_enviar_info:

                getContext().startService( new Intent(getContext(), EnviarInfoService.class) );

                break;
        }
    }
}
