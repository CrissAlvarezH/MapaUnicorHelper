package helpers.cristian.com.mapaunicorhelper.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import helpers.cristian.com.mapaunicorhelper.R;


public class BloquesFragment extends Fragment {

    public BloquesFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_bloques, container, false);

        return vista;
    }


}
