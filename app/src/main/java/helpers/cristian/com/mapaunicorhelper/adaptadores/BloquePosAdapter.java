package helpers.cristian.com.mapaunicorhelper.adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;

public class BloquePosAdapter extends RecyclerView.Adapter<BloquePosAdapter.BloquePosViewHolder> {

    public interface BloquePosListener {
        void clickBloque( Bloque bloque, int posicion);
    }

    private ArrayList<Bloque> bloques;
    private BloquePosListener listener;

    public BloquePosAdapter(ArrayList<Bloque> bloques, BloquePosListener listener) {
        this.bloques = bloques;
        this.listener = listener;
    }

    public class BloquePosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtNombre, txtCodigo, txtZona;

        public BloquePosViewHolder(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.item_nombre);
            txtCodigo = itemView.findViewById(R.id.item_codigo);
            txtZona = itemView.findViewById(R.id.item_zona);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ( listener != null ) listener.clickBloque( bloques.get( getAdapterPosition() ), getAdapterPosition() );
        }
    }

    @NonNull
    @Override
    public BloquePosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from( viewGroup.getContext() )
                        .inflate(R.layout.item_bloque_pos, viewGroup, false);

        return new BloquePosViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull BloquePosViewHolder holder, int pos) {
        Bloque bloque = bloques.get(pos);

        holder.txtNombre.setText( bloque.getNombre() );
        holder.txtCodigo.setText( bloque.getCodigo() );

        String zona = "A";
        int color = R.color.verde;

        switch ( bloque.getIdZona() ) {
            case 1:
                zona = "A";
                color = R.color.verde;
                break;
            case 2:
                zona = "B";
                color = R.color.azul;
                break;
            case 3:
                zona = "C";
                color = R.color.morado;
                break;
            case 4:
                zona = "D";
                color = R.color.rojo;
                break;
            case 5:
                zona = "E";
                color = R.color.naranja;
                break;
        }

        holder.txtZona.setText("Zona "+zona);
        holder.txtZona.setBackgroundResource(color);
    }

    @Override
    public int getItemCount() {
        return bloques.size();
    }

    public void setBloques(ArrayList<Bloque> bloques) {
        this.bloques = bloques;
        notifyDataSetChanged();
    }
}
