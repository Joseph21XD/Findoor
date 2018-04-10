package enigma.proyectofindoor;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsRecomendarActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {


    private Marker site;
    public static LatLng coordenadas;
    private GoogleMap mMap;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_recomendar);
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


        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"GPS desactivado", Toast.LENGTH_SHORT);
        }
        else{
            mMap.setMyLocationEnabled(true);
        }
    }

    /** Called when the user clicks a marker. */
    public boolean onMarkerClick(final Marker marker) {

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        coordenadas= latLng;
        mMap.clear();
        // create marker
        MarkerOptions marker = new MarkerOptions().position(latLng).title("Sitio a recomendar");
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.point));
        site = mMap.addMarker(marker);
        site.setTag(0);
        Log.d("LAT", coordenadas.latitude+"");
        Log.d("LON", coordenadas.longitude+"");

    }

    public void aceptarCoor(View view){
        finish();
    }
}
