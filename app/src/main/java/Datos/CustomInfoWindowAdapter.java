package Datos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.concurrent.ExecutionException;

import enigma.proyectofindoor.MainActivity;
import enigma.proyectofindoor.R;

/**
 * Created by ramir on 4/15/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;

    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.infowindow, null);
        ((TextView)v.findViewById(R.id.info_window_nombre)).setText(MainActivity.sitios.get(Integer.parseInt(m.getTag()+"")).getNombre());
        ((TextView)v.findViewById(R.id.info_window_placas)).setText(MainActivity.sitios.get(Integer.parseInt(m.getTag()+"")).getDireccion());

        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}
