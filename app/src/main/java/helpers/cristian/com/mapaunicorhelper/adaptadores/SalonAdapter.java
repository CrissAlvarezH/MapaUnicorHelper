package helpers.cristian.com.mapaunicorhelper.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.modelos.Salon;

public class SalonAdapter extends RecyclerView.Adapter<SalonAdapter.SalonViewHolder>{
    private Context contexto;
    private ArrayList<Salon> salones;

    public SalonAdapter(Context contexto, ArrayList<Salon> salones) {
        this.contexto = contexto;
        this.salones = salones;
    }

    public class SalonViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTexto, txtPiso;
        private ImageView img;

        public SalonViewHolder(View itemView) {
            super(itemView);

            txtPiso = itemView.findViewById(R.id.item_txt_piso);
            txtTexto = itemView.findViewById(R.id.item_img_texto);
            img = itemView.findViewById(R.id.item_img);
        }

        public void setImg(String ruta){
            Glide.with(contexto)
                    .load(ruta)
                    .into(img);
        }
    }

    @NonNull
    @Override
    public SalonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from( parent.getContext() )
                        .inflate(R.layout.item_imagen, parent, false);
        return new SalonViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SalonViewHolder holder, int position) {
        Salon salon = salones.get(position);

        holder.setImg( salon.getImagen().getUrl() );
        holder.txtPiso.setText( salon.getPiso() + "" );
        holder.txtTexto.setText( salon.getCodigo() );
    }

    @Override
    public int getItemCount() {
        return salones.size();
    }

    public void agregarSalon(Salon salon){
        salones.add(0, salon);
        notifyItemInserted(0);
    }
}
