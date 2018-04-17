package enigma.proyectofindoor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.LocaleDisplayNames;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Datos.CustomListView;
import Datos.DataParserJ;
import Datos.ImageTask;
import Datos.JsonTask;

public class Activity_CercanosR extends AppCompatActivity {

    private TextView mTextMessage;

    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<String> listaCercanos = new ArrayList<String>();
    ArrayList<String> listaICercanos = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    ListView listView;
    String tokenKey = "";
    CustomListView customListView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String temp;
            switch (item.getItemId()) {
                case R.id.home:
                    mTextMessage.setText(R.string.bottom_menu_home);
                    return true;
                case R.id.heart:
                    mTextMessage.setText(R.string.bottom_menu_heart);
                    temp = obtainJsonFavoritos();
                    listaCercanos = formatJsonName(temp);
                    listaICercanos = formatJsonImage(temp);
                    tokenKey = formatJsonNewKey(temp);
                    customListView = new CustomListView(Activity_CercanosR.this, listaCercanos, listaICercanos);
                    listView.setAdapter(customListView);
                    return true;
                case R.id.star:
                    mTextMessage.setText(R.string.bottom_menu_star);
                    temp = obtainJsonPopulares();
                    listaCercanos = formatJsonName(temp);
                    listaICercanos = formatJsonImage(temp);
                    tokenKey = formatJsonNewKey(temp);
                    customListView = new CustomListView(Activity_CercanosR.this, listaCercanos, listaICercanos);
                    listView.setAdapter(customListView);
                    return true;
                case R.id.user:
                    mTextMessage.setText(R.string.bottom_menu_user);
                    Intent intent = new Intent(Activity_CercanosR.this, Activity_ComentariosR.class);
                    intent.putExtra("token", tokenKey);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cercanos);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        Intent intent = getIntent();
        tokenKey = intent.getStringExtra("token");
        Log.d("TOKEN", tokenKey+"");
        listView = findViewById(R.id.listaCercanos);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("location changed", location.toString());
             /*   String temp = obtainJson(location);
                listaCercanos = formatJsonName(temp);
                listaICercanos = formatJsonImage(temp);*/
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT < 23){
            int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d("PERMISO",permissionCheck+"");
            if(permissionCheck==PackageManager.PERMISSION_DENIED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.d("SITE", location.getLatitude()+" "+location.getLongitude());
                    if(location != null){
                        String temp = obtainJsonCercanos(location);
                        listaCercanos = formatJsonName(temp);
                        listaICercanos = formatJsonImage(temp);
                        tokenKey = formatJsonNewKey(temp);
                    }
                }else{
                    Log.d("PERMISO","NO");
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.d("SITE", location.getLatitude()+" "+location.getLongitude());
                if(location != null){
                    String temp = obtainJsonCercanos(location);
                    listaCercanos = formatJsonName(temp);
                    listaICercanos = formatJsonImage(temp);
                    tokenKey = formatJsonNewKey(temp);
                }
            }
        }else{
            Log.d("ENTRA A", "ELSE");
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("SITE", location.getLatitude()+" "+location.getLongitude());
                if(location != null){
                    String temp = obtainJsonCercanos(location);
                    listaCercanos = formatJsonName(temp);
                    listaICercanos = formatJsonImage(temp);
                    tokenKey = formatJsonNewKey(temp);
                }
            }
        }

        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        customListView = new CustomListView(this, listaCercanos, listaICercanos);
        listView.setAdapter(customListView);
    }


    public String obtainJsonCercanos(Location location){
        String result = "";
        JsonTask jsonTask = new JsonTask();
        Log.d("ENTRA A", "ObtainJsonCercanos");
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/sitio/close/"+DataParserJ.parsear(getLat(location))+"/"+ DataParserJ.parsear(getLon(location))+"/KEY="+tokenKey+"/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String obtainJsonFavoritos( ){
        String result = "";
        Log.d("ENTRA A", "ObtainJsonFavoritos");
        JsonTask jsonTask = new JsonTask();
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/sitio/TYPE=FAVORITE/"+MainActivity.persona.getId()+"/KEY=" + tokenKey + "/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String obtainJsonPopulares(){
        String result = "";
        Log.d("ENTRA A", "ObtainJsonPopulares");
        JsonTask jsonTask = new JsonTask();
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/sitio/ranking.json/KEY=" + tokenKey + "/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getLat(Location location){
        String lat = "" + location.getLatitude();
        return lat;
    }

    public String getLon(Location location){
        String lon = "" + location.getLongitude();
        return lon;
    }

    public String formatJsonNewKey(String resul){
        Log.d("ENTRA A", "formatJsonNewKey");
        try {

            JSONObject jsonObject = new JSONObject(resul);

            String key = jsonObject.getString("token");
            sharedPreferences.edit().putString("token",key).apply();
            Log.i("jsonObject KEY FINAL", key);
            return key;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public ArrayList<String> formatJsonName(String resul){
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("sitios"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("nombre")));
            }

            Log.i("jsonObject AQUI NOMBRE", temp.toString());
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }

    public ArrayList<String> formatJsonImage(String resul){
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("sitios"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("imagen")));
            }
            Log.i("jsonObject AQUI IMAGEN", temp.toString());
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }
}
