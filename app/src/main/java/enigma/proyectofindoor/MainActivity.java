package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences sharedPreferences;
    EditText editText1;
    EditText editText2;
    String nombre="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1= findViewById(R.id.editText2);
        editText2= findViewById(R.id.editText);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        String token= sharedPreferences.getString("token", "");
        if(token.length()>0){
            // https://findoor.herokuapp.com/persona/token/token
            String url = "https://findoor.herokuapp.com/persona/token/"+token+"/";
            JsonTask downloadTask = new JsonTask();
            String resultado= "";
            try {
                resultado = downloadTask.execute(url).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                JSONObject obj = new JSONObject(resultado);
                String temp= obj.getString("nombre");
                Log.d("TOKEN", temp);
                Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
                nombre= temp;
            } catch (Exception e) {
                nombre = "";
            }

        }
        /*sharedPreferences.edit().putString("auth_token","2qwHJDSFIOIDwad").apply();*/
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
            String temp= DataParserJ.deparsear(obj.getString("nombre"));
            Log.d("TOKEN", temp);
            nombre= temp;
            Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            nombre = "";
        }
    }
}
