package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Datos.CustomListView;
import Datos.DataParserJ;
import Datos.JsonTask;
import Datos.Sitio;

public class Activity_Visitados_Otros extends AppCompatActivity {

    ArrayList<String> listaVisitados = new ArrayList<String>();
    ArrayList<String> listaIVisitados = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    ListView listView;
    String tokenKey = "";
    int value = 0;
    CustomListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__visitados__otros);
        sharedPreferences = this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        Intent intent = getIntent();
        String modo = intent.getStringExtra("Mode");
        value = intent.getIntExtra("Value", -1);
        tokenKey = sharedPreferences.getString("token", "");

        listView = findViewById(R.id.listaVisitadosOtros);

        String resultado;
        if(modo.equals("VISITED")){
            resultado = obtainJsonVisitados();
        }else{
            resultado = obtainJsonFavoritos();
        }

        listaVisitados = formatJsonName(resultado);
        listaIVisitados = formatJsonImage(resultado);
        tokenKey = formatJsonNewKey(resultado);

        customListView = new CustomListView(this, listaVisitados, listaIVisitados);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), InformacionActivity.class);
                intent.putExtra("valor", position);
                startActivity(intent);
            }

        });
    }

    public String obtainJsonVisitados() {
        String result = "";
        JsonTask jsonTask = new JsonTask();
        Log.d("ENTRA A", "ObtainJsonVisitados");
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/sitio/TYPE=VISITED/"+ MainActivity.personas.get(value).getId() +"/KEY=" + tokenKey + "/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String obtainJsonFavoritos() {
        String result = "";
        JsonTask jsonTask = new JsonTask();
        Log.d("ENTRA A", "obtainJsonFavoritos");
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/sitio/TYPE=FAVORITE/"+ MainActivity.personas.get(value).getId() +"/KEY=" + tokenKey + "/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String formatJsonNewKey(String resul) {
        Log.d("ENTRA A", "formatJsonNewKey");
        try {

            JSONObject jsonObject = new JSONObject(resul);

            String key = jsonObject.getString("token");
            sharedPreferences.edit().putString("token", key).apply();
            Log.i("jsonObject KEY FINAL", key);
            return key;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public ArrayList<String> formatJsonName(String resul) {
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("sitios"));
            ArrayList<String> temp = new ArrayList<String>();

            MainActivity.sitios.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("nombre")));
                MainActivity.sitios.add(new Sitio(Integer.parseInt(DataParserJ.deparsear(jsonSitio.getString("id"))),DataParserJ.deparsear(jsonSitio.getString("nombre")),
                        DataParserJ.deparsear(jsonSitio.getString("latitud")),DataParserJ.deparsear(jsonSitio.getString("longuitud")),
                        DataParserJ.deparsear(jsonSitio.getString("direccion")),DataParserJ.deparsear(jsonSitio.getString("descripcion")),
                        DataParserJ.deparsear(jsonSitio.getString("imagen"))));
            }

            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }


    public ArrayList<String> formatJsonImage(String resul) {
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("sitios"));
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < jsonArray.length(); i++) {
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