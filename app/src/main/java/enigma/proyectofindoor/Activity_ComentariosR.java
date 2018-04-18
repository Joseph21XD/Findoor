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

    ArrayList<String> listaUsuarios = new ArrayList<String>();
    ArrayList<String> listaIUsuarios = new ArrayList<String>();
    ArrayList<String> listaComentarios = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    ListView listView;
    String tokenKey = "";
    int value=0;
    CustomListViewComentarios customListView;


    public void comentarClicked(View view){
        if(value!=-1){
        EditText editText = (EditText) findViewById(R.id.comentario);
        String comentario = editText.getText().toString();
        JsonTask jsonTask = new JsonTask();
        try {
            comentario = DataParserJ.parsear(comentario);
            String respuesta = jsonTask.execute("http://findoor.herokuapp.com/sitio/comment/"+MainActivity.sitios.get(value).getId()+"/"+ comentario +"/KEY="+ tokenKey + "/").get();
            tokenKey = respuesta;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String temp = obtainJsonUsuariosComentarios();
        listaUsuarios = formatJsonName(temp);
        listaIUsuarios = formatJsonImage(temp);
        listaComentarios = formatJsonComment(temp);
        tokenKey = formatJsonNewKey(temp);

        listView = (ListView) findViewById(R.id.listaComentarios);
        customListView = new CustomListViewComentarios(this, listaUsuarios, listaIUsuarios, listaComentarios);
        listView.setAdapter(customListView);}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__comentarios_r);

        sharedPreferences = this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        Intent intent = getIntent();
        tokenKey = intent.getStringExtra("token");
        value= intent.getIntExtra("valor",-1);
        if(value!=-1){
        String temp = obtainJsonUsuariosComentarios();
        listaUsuarios = formatJsonName(temp);
        listaIUsuarios = formatJsonImage(temp);
        listaComentarios = formatJsonComment(temp);
        tokenKey = formatJsonNewKey(temp);

        listView = (ListView) findViewById(R.id.listaComentarios);
        customListView = new CustomListViewComentarios(this, listaUsuarios, listaIUsuarios, listaComentarios);
        listView.setAdapter(customListView);}
    }


    public String obtainJsonUsuariosComentarios(){
        String result = "";
        JsonTask jsonTask = new JsonTask();
        Log.e("ENTRA A", "obtainJsonUsuariosComentarios");
        try {
            result = jsonTask.execute("http://findoor.herokuapp.com/sitio/comment/"+MainActivity.sitios.get(value).getId()+".json/KEY="+tokenKey+"/").get();
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

            JSONArray jsonArray = new JSONArray(jsonObject.getString("comentarios"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("nombre")) + " " + DataParserJ.deparsear(jsonSitio.getString("apellido")));
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

            JSONArray jsonArray = new JSONArray(jsonObject.getString("comentarios"));
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

    public ArrayList<String> formatJsonComment(String resul){
        try {

            JSONObject jsonObject = new JSONObject(resul);

            JSONArray jsonArray = new JSONArray(jsonObject.getString("comentarios"));
            ArrayList<String> temp = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonSitio = new JSONObject(jsonArray.getString(i));
                temp.add(DataParserJ.deparsear(jsonSitio.getString("comentario")));
            }

            Log.i("jsonObject COMENTARIO", temp.toString());
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }
}
