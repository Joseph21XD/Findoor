package enigma.proyectofindoor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Datos.CustomInfoWindowAdapter;

public class MapsSitiosActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {


    private ArrayList<Marker> site= new ArrayList<>();
    public static LatLng coordenadas;
    public static String dir="";
    private GoogleMap mMap;
    private int value=0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_sitios);
        Intent intent= getIntent();
        value= intent.getIntExtra("valor",-1);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {
        String mapa_code= sharedPreferences.getString("mapa","");
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success= false;
            if(mapa_code.equals("") || mapa_code.equals("1")){
                success = map.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.retro));}
            else{
                success = map.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.dark));
            }

            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }

        mMap = map;
        String code= sharedPreferences.getString("mapa","");
        int icon=0;
        if(code.equals("") || code.equals("1"))
            icon=R.drawable.point;
        else
            icon=R.drawable.point2;
        for(int i=0; i<MainActivity.sitios.size(); i++) {
            LatLng latLng= new LatLng(Double.parseDouble(MainActivity.sitios.get(i).getLatitud()),Double.parseDouble(MainActivity.sitios.get(i).getLongitud()));
            MarkerOptions marker = new MarkerOptions().position(latLng).title(MainActivity.sitios.get(i).getNombre());
            marker.icon(BitmapDescriptorFactory.fromResource(icon));
            Marker mark= mMap.addMarker(marker);
            mark.setTag(i+"");
            site.add(mark);
        }
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getApplicationContext())));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"GPS desactivado", Toast.LENGTH_SHORT);
        }
        else{
            mMap.setMyLocationEnabled(true);
        }
        if(value==-1){
        LatLng latLng = new LatLng(10, -84);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 7);
        mMap.animateCamera(cameraUpdate);}
        else{
            LatLng latLng = new LatLng( Double.parseDouble(MainActivity.sitios.get(value).getLatitud()), Double.parseDouble(MainActivity.sitios.get(value).getLongitud()));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
            mMap.animateCamera(cameraUpdate);
        }
    }

    /** Called when the user clicks a marker. */
    public boolean onMarkerClick(final Marker marker) {
        int i= Integer.parseInt(marker.getTag()+"");
        Log.d("PRESIONÓ", MainActivity.sitios.get(i).getNombre());

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        /*
        coordenadas= latLng;
        mMap.clear();
        // create marker
        Geocoder geocoder;
        List<Address> addresses= new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address="";
        try {
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        }
        catch (Exception e){
            address = "océano";
        }
        dir=address;
        String mapa_code= sharedPreferences.getString("mapa","");
        int icon=0;
        if(mapa_code.equals("") || mapa_code.equals("1"))
            icon=R.drawable.point;
        else
            icon=R.drawable.point2;
        MarkerOptions marker = new MarkerOptions().position(latLng).title(address);
        marker.icon(BitmapDescriptorFactory.fromResource(icon));
        site = mMap.addMarker(marker);
        site.setTag(0);
        Log.d("LAT", coordenadas.latitude+"");
        Log.d("LON", coordenadas.longitude+"");*/


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int i= Integer.parseInt(marker.getTag()+"");
        Log.d("PRESIONÓ INFO", MainActivity.sitios.get(i).getNombre());
        Intent intent= new Intent(MapsSitiosActivity.this, InformacionActivity.class);
        intent.putExtra("valor", i);
        startActivity(intent);
    }
}
