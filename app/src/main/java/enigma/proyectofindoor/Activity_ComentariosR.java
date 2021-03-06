package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Datos.CustomListView;
import Datos.CustomListViewComentarios;
import Datos.DataParserJ;
import Datos.JsonTask;

public class Activity_ComentariosR extends AppCompatActivity {
    public static final String MIXPANEL_TOKEN = "3c4b7583313688d65dfcacdff72ba77c";
    ArrayList<String> listaUsuarios = new ArrayList<String>();
    ArrayList<String> listaIUsuarios = new ArrayList<String>();
    ArrayList<String> listaComentarios = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    ListView listView;
    String tokenKey = "";
    int idlugar;
    CustomListViewComentarios customListView;
    MixpanelAPI mixpanel;


    public void comentarClicked(View view){
        EditText editText = (EditText) findViewById(R.id.comentario);
        String comentario = editText.getText().toString();
        JsonTask jsonTask = new JsonTask();
        try {
            comentario = DataParserJ.parsear(comentario);
            String respuesta = jsonTask.execute("http://findoor.herokuapp.com/sitio/comment/"+ idlugar +"/"+ comentario +"/KEY="+ tokenKey + "/").get();
            tokenKey = respuesta;
            JSONObject props = new JSONObject();
            props.put("Name", MainActivity.persona.getNombre());
            props.put("Last-Name", MainActivity.persona.getApellido());
            mixpanel.track("add comment", props);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        String temp = obtainJsonUsuariosComentarios(idlugar);
        listaUsuarios = formatJsonName(temp);
        listaIUsuarios = formatJsonImage(temp);
        listaComentarios = formatJsonComment(temp);
        tokenKey = formatJsonNewKey(temp);

        listView = (ListView) findViewById(R.id.listaComentarios);
        customListView = new CustomListViewComentarios(this, listaUsuarios, listaIUsuarios, listaComentarios);
        listView.setAdapter(customListView);

        editText.setText("");

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__comentarios_r);
        mixpanel = MixpanelAPI.getInstance(Activity_ComentariosR.this, MIXPANEL_TOKEN);
        sharedPreferences = this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        Intent intent = getIntent();
        tokenKey = intent.getStringExtra("token");
        int pos = intent.getIntExtra("IDlugar", -1);
        idlugar =  MainActivity.sitios.get(pos).getId();
        String temp = obtainJsonUsuariosComentarios(idlugar);
        listaUsuarios = formatJsonName(temp);
        listaIUsuarios = formatJsonImage(temp);
        listaComentarios = formatJsonComment(temp);
        tokenKey = formatJsonNewKey(temp);

        listView = (ListView) findViewById(R.id.listaComentarios);
        customListView = new CustomListViewComentarios(this, listaUsuarios, listaIUsuarios, listaComentarios);
        listView.setAdapter(customListView);
    }


    public String obtainJsonUsuariosComentarios(int id){
        String result = "";
        JsonTask jsonTask = new JsonTask();
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/sitio/comment/"+ id +".json/KEY="+tokenKey+"/").get();
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

            JSONArray jsonArray = new JSONArray(jsonObject.getString("comentarios"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("nombre")) + " " + DataParserJ.deparsear(jsonSitio.getString("apellido")));
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

            JSONArray jsonArray = new JSONArray(jsonObject.getString("comentarios"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("imagen")));
            }
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }

    public ArrayList<String> formatJsonComment(String resul){
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("comentarios"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("comentario")));
            }

            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }
}
