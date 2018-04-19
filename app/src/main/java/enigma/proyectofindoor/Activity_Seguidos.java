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
import Datos.Persona;
import Datos.Sitio;

public class Activity_Seguidos extends AppCompatActivity {


    ArrayList<String> listaSeguidos = new ArrayList<String>();
    ArrayList<String> listaISeguidos = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    ListView listView;
    String tokenKey = "";
    CustomListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__seguidos);

        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        Intent intent = getIntent();
        tokenKey = sharedPreferences.getString("token", "");
        listView = findViewById(R.id.listaSeguidos);

        String resultado = obtainJsonSeguidos();
        listaSeguidos = formatJsonName(resultado);
        listaISeguidos = formatJsonImage(resultado);
        tokenKey = formatJsonNewKey(resultado);

        customListView = new CustomListView(this, listaSeguidos, listaISeguidos);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PerfilUsuario.class);
                intent.putExtra("valor", position);
                startActivity(intent);
            }

        });
    }

    public String obtainJsonSeguidos(){
        String result = "";
        JsonTask jsonTask = new JsonTask();
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/persona/seguidos/KEY="+tokenKey+"/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String formatJsonNewKey(String resul){
        try {

            JSONObject jsonObject = new JSONObject(resul);

            String key = jsonObject.getString("token");
            sharedPreferences.edit().putString("token",key).apply();
            return key;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public ArrayList<String> formatJsonName(String resul){
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("personas"));
            ArrayList<String> temp = new ArrayList<String>();

            MainActivity.personas.clear();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPersona = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonPersona.getString("nombre"))+ " " +DataParserJ.deparsear(jsonPersona.getString("apellido")));
                MainActivity.personas.add(new Persona(Integer.parseInt(DataParserJ.deparsear(jsonPersona.getString("id"))),DataParserJ.deparsear(jsonPersona.getString("nombre")),
                        DataParserJ.deparsear(jsonPersona.getString("apellido")),DataParserJ.deparsear(jsonPersona.getString("imagen"))));
            }

            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }


    public ArrayList<String> formatJsonImage(String resul){
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("personas"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPersona = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonPersona.getString("imagen")));
            }
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }
}
