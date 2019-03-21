package helpers.cristian.com.mapaunicorhelper.adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.modelos.Bloque;

public class BloquesAdapter extends RecyclerView.Adapter<BloquesAdapter.BloquesViewHolder> {

    private ArrayList<Bloque> bloques;

    public BloquesAdapter(ArrayList<Bloque> bloques) {
        this.bloques = bloques;
    }

    public class BloquesViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtCod, txtCantSalones;
        private final ImageView imgEstado;

        public BloquesViewHolder(View itemView) {
            super(itemView);

            txtCod = itemView.findViewById(R.id.item_cod_bloque);
            txtCantSalones = itemView.findViewById(R.id.item_cant_salones);
            imgEstado = itemView.findViewById(R.id.item_img_enviado);
        }
    }

    @NonNull
    @Override
    public BloquesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from( parent.getContext() )
                        .inflate(R.layout.item_bloque, parent, false);

        return new BloquesViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull BloquesViewHolder holder, int position) {
        Bloque bloque = bloques.get(position);

        holder.txtCod.setText( bloque.getCodigo() );
        holder.txtCantSalones.setText( bloque.getNumSalones() + " Salones" );

        int imgRecurso = R.drawable.ic_cloud_done;

        switch ( bloque.getEstado() ) {
            case Bloque.Estados.NO_ENVIADO:
                imgRecurso = R.drawable.ic_cloud_upload;
                break;
            case Bloque.Estados.ENVIADO:
                imgRecurso = R.drawable.ic_cloud_done;
                break;
        }

        holder.imgEstado.setImageResource(imgRecurso);
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
