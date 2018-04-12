package Datos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import enigma.proyectofindoor.R;


public class CustomListView extends ArrayAdapter<String> {

    private ArrayList<String> listaSitiosCercanos;
    private ArrayList<String> listaISitiosCercanos;
    private Activity context;

    public CustomListView(Activity context, ArrayList<String> listaSitiosCercanos, ArrayList<String> listaISitiosCercanos){
        super(context, R.layout.listas, listaSitiosCercanos);

        this.context = context;
        this.listaSitiosCercanos = listaSitiosCercanos;
        this.listaISitiosCercanos = listaISitiosCercanos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listas,null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask().execute(listaISitiosCercanos.get(position)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        viewHolder.imageView.setImageBitmap(bitmap);
        viewHolder.textView.setText(listaSitiosCercanos.get(position));

        return view;
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;

        ViewHolder(View view){
            textView = view.findViewById(R.id.nombreLugar);
            imageView = view.findViewById(R.id.imagenLugar);
        }
    }
}
