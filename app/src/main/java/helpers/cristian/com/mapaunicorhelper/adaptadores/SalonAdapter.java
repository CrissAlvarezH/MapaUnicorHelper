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
    public interface ListenerSalones {
        void longClickSalon(Salon salon, int posicion);
    }

    private Context contexto;
    private ArrayList<Salon> salones;
    private ListenerSalones listener;

    public SalonAdapter(Context contexto, ArrayList<Salon> salones, ListenerSalones listener) {
        this.contexto = contexto;
        this.salones = salones;
        this.listener = listener;
    }

    public class SalonViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView txtTexto, txtPiso;
        private ImageView img;

        public SalonViewHolder(View itemView) {
            super(itemView);

            txtPiso = itemView.findViewById(R.id.item_txt_piso);
            txtTexto = itemView.findViewById(R.id.item_img_texto);
            img = itemView.findViewById(R.id.item_img);

            itemView.setOnLongClickListener(this);
        }

        public void setImg(String ruta){
            Glide.with(contexto)
                    .load(ruta)
                    .into(img);
        }

        @Override
        public boolean onLongClick(View v) {
            if(listener != null){
                listener.longClickSalon( salones.get(getAdapterPosition()), getAdapterPosition() );
                return true;
            }
            return false;
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

    public boolean hayUnSalonConEsteCod(String cod){
        for(Salon salon : salones) {
            if (salon.getCodigo().equalsIgnoreCase(cod))
                return true;

        }

        return false;
    }

    public ArrayList<Salon> getSalones() {
        return salones;
    }

    public void eliminarSalon(int posicion) {
        this.salones.remove(posicion);
        notifyItemRemoved(posicion);
    }
}
