package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Datos.*;

public class MainActivity extends AppCompatActivity {
    public static Persona persona= new Persona();
    public static ArrayList<Sitio> sitios= new ArrayList<>();
    public static ArrayList<Persona> personas= new ArrayList<>();
    SharedPreferences sharedPreferences;
    EditText editText1;
    EditText editText2;

    public void registrar(View v){
        Intent intent= new Intent(MainActivity.this, Registrar.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1= findViewById(R.id.editText2);
        editText2= findViewById(R.id.editText);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        /*sharedPreferences.edit().putString("auth_token","2qwHJDSFIOIDwad").apply();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token= sharedPreferences.getString("token", "");
        if(token.length()>0){
            // https://findoor.herokuapp.com/persona/token/token
            String url = "https://findoor.herokuapp.com/persona/token/"+token+"/";
            JsonTask downloadTask = new JsonTask();
            String resultado= null;
            try {
                resultado = downloadTask.execute(url).get();
            } catch (InterruptedException e) {
                resultado="";
            } catch (ExecutionException e) {
                resultado="";
            }
            try {
                JSONObject obj = new JSONObject(resultado);
                int id= Integer.parseInt(obj.getString("id"));
                String nom= DataParserJ.deparsear(obj.getString("nombre"));
                String ap= DataParserJ.deparsear(obj.getString("apellido"));
                String img= DataParserJ.deparsear(obj.getString("imagen"));
                String user= DataParserJ.deparsear(obj.getString("correo"));
                String pwd= DataParserJ.deparsear(obj.getString("contrasenna"));
                int isf= Integer.parseInt(DataParserJ.deparsear(obj.getString("isface")));
                String tok= obj.getString("token");
                persona= new Persona(id, nom,ap,user,pwd,img,tok,isf);
                Log.d("PERSONA", persona.toString());
                sharedPreferences.edit().putString("token",tok).apply();
                Intent intent = new Intent(MainActivity.this, Activity_CercanosR.class);
                intent.putExtra("token", tok);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void ingresar(View view) throws ExecutionException, InterruptedException {
        String user = editText1.getText().toString();
        String pwd = editText2.getText().toString();
        // https://findoor.herokuapp.com/persona/login/jajaKgmailPcom/123/
        String url = "https://findoor.herokuapp.com/persona/login/"+DataParserJ.parsear(user)+"/"+DataParserJ.parsear(pwd)+"/";
        JsonTask downloadTask = new JsonTask();
        String resultado=downloadTask.execute(url).get();
        try {
            JSONObject obj = new JSONObject(resultado);
            int id= Integer.parseInt(obj.getString("id"));
            String nom= DataParserJ.deparsear(obj.getString("nombre"));
            String ap= DataParserJ.deparsear(obj.getString("apellido"));
            String img= DataParserJ.deparsear(obj.getString("imagen"));
            int isf= Integer.parseInt(DataParserJ.deparsear(obj.getString("isface")));
            String tok= obj.getString("token");
            persona= new Persona(id, nom,ap,user,pwd,img,tok,isf);
            Log.d("PERSONA", persona.toString());
            sharedPreferences.edit().putString("token",tok).apply();
            Intent intent = new Intent(MainActivity.this, Activity_CercanosR.class);
            intent.putExtra("token", tok);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error! Datos de ingreso incorrectos",Toast.LENGTH_SHORT).show();
        }
    }



}
