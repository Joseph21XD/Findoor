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

public class Activity_Seguidores extends AppCompatActivity {

    ArrayList<String> listaSeguidores = new ArrayList<String>();
    ArrayList<String> listaISeguidores = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    ListView listView;
    String tokenKey = "";
    CustomListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__seguidores);

        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        Intent intent = getIntent();
        tokenKey = sharedPreferences.getString("token", "");
        /*
        tokenKey = intent.getStringExtra("token");
        Log.d("TOKEN", tokenKey+"");*/
        listView = findViewById(R.id.listaSeguidores);

        String resultado = obtainJsonSeguidores();
        listaSeguidores = formatJsonName(resultado);
        listaISeguidores = formatJsonImage(resultado);
        tokenKey = formatJsonNewKey(resultado);

        customListView = new CustomListView(this, listaSeguidores, listaISeguidores);
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

    public String obtainJsonSeguidores(){
        String result = "";
        JsonTask jsonTask = new JsonTask();
        Log.d("ENTRA A", "ObtainJsonSeguidores");
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/persona/seguidores/KEY="+tokenKey+"/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
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

            JSONArray jsonArray = new JSONArray(jsonObject.getString("personas"));
            ArrayList<String> temp = new ArrayList<String>();

            MainActivity.personas.clear();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPersona = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonPersona.getString("nombre"))+ " " +DataParserJ.deparsear(jsonPersona.getString("apellido")));
                MainActivity.personas.add(new Persona(Integer.parseInt(DataParserJ.deparsear(jsonPersona.getString("id"))),DataParserJ.deparsear(jsonPersona.getString("nombre")),
                        DataParserJ.deparsear(jsonPersona.getString("apellido")),DataParserJ.deparsear(jsonPersona.getString("imagen"))));
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

            JSONArray jsonArray = new JSONArray(jsonObject.getString("personas"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPersona = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonPersona.getString("imagen")));
            }
            Log.i("jsonObject AQUI IMAGEN", temp.toString());
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }
}
