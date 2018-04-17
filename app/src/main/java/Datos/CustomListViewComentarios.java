package Datos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import enigma.proyectofindoor.R;

public class CustomListViewComentarios extends ArrayAdapter<String> {

    private ArrayList<String> listaUsuarios;
    private ArrayList<String> listaIUsuarios;
    private ArrayList<String> listaComentarios;
    private Activity context;

    public CustomListViewComentarios(Activity context, ArrayList<String> listaUsuarios, ArrayList<String> listaIUsuarios, ArrayList<String> listaComentarios) {
        super(context, R.layout.listascomentarios, listaUsuarios);

        this.context = context;
        this.listaUsuarios = listaUsuarios;
        this.listaIUsuarios = listaIUsuarios;
        this.listaComentarios = listaComentarios;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        CustomListViewComentarios.ViewHolder viewHolder = null;
        if (view == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listascomentarios, null, true);
            viewHolder = new CustomListViewComentarios.ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (CustomListViewComentarios.ViewHolder) view.getTag();
        }
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask().execute(listaIUsuarios.get(position)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        viewHolder.imageView.setImageBitmap(bitmap);
        viewHolder.textView.setText(listaUsuarios.get(position));
        viewHolder.textView2.setText(listaComentarios.get(position));

        return view;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView textView2;

        ViewHolder(View view) {
            textView = view.findViewById(R.id.nombreUsuario);
            imageView = view.findViewById(R.id.imagenUsuario);
            textView2 = view.findViewById(R.id.textoComentario);
        }
    }
}