package helpers.cristian.com.mapaunicorhelper.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.R;
import helpers.cristian.com.mapaunicorhelper.glide.GlideApp;
import helpers.cristian.com.mapaunicorhelper.modelos.Imagen;

public class ImagenAdapter extends RecyclerView.Adapter<ImagenAdapter.ImagenViewHolder>{
    public interface ListenerImagenes {
        void longClickImg(Imagen imagen, int posicion);
    }

    private Context contexto;
    private ArrayList<Imagen> imagenes;
    private ListenerImagenes listener;

    public ImagenAdapter(Context contexto, ArrayList<Imagen> imagenes, ListenerImagenes listener) {
        this.contexto = contexto;
        this.imagenes = imagenes;
        this.listener = listener;
    }

    public class ImagenViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView txtTexto;
        private ImageView img;

        public ImagenViewHolder(View itemView) {
            super(itemView);

            txtTexto = itemView.findViewById(R.id.item_img_texto);
            img = itemView.findViewById(R.id.item_img);

            itemView.setOnLongClickListener(this);
        }

        public void setTexto(String texto){
            if( texto.trim().isEmpty() ){
                txtTexto.setVisibility(View.GONE);
            }else{
                txtTexto.setText(texto);
                txtTexto.setVisibility(View.VISIBLE);
            }
        }

        public void setImg(String ruta){
            GlideApp.with(contexto)
                    .load(ruta)
                    .into(img);
        }

        @Override
        public boolean onLongClick(View v) {
            if(listener != null){
                listener.longClickImg( imagenes.get(getAdapterPosition()), getAdapterPosition() );
                return true;
            }
            return false;
        }
    }

    @NonNull
    @Override
    public ImagenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from( parent.getContext() )
                        .inflate(R.layout.item_imagen, parent, false);

        return new ImagenViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenViewHolder holder, int position) {
        Imagen imagen = imagenes.get(position);

        holder.setImg( imagen.getUrl() );
        holder.setTexto( imagen.getFecha() );
    }

    @Override
    public int getItemCount() {
        return this.imagenes.size();
    }

    public void agregarImagen(Imagen imagen){
        imagenes.add(0, imagen);
        notifyItemInserted(0);
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public void eliminarImagen(int posicion){
        this.imagenes.remove(posicion);
        notifyItemRemoved(posicion);
    }

    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
        notifyDataSetChanged();
    }
}
