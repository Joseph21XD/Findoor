package enigma.proyectofindoor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import Datos.*;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity extends AppCompatActivity {
    public static Persona persona= new Persona();
    public static ArrayList<Sitio> sitios= new ArrayList<>();
    public static ArrayList<Persona> personas= new ArrayList<>();
    public static boolean isfacebook=false;
    public static String email= "";
    SharedPreferences sharedPreferences;
    EditText editText1;
    EditText editText2;
    AccessToken accessToken;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ayuda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent= new Intent(MainActivity.this, AcercaDe.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void registrar(View v){
        Intent intent= new Intent(MainActivity.this, Registrar.class);
        startActivity(intent);
    }

    public void refresh(){
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1= findViewById(R.id.editText2);
        editText2= findViewById(R.id.editText);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        if(AccessToken.getCurrentAccessToken()!=null){
            accessToken= AccessToken.getCurrentAccessToken();
            Profile profile= Profile.getCurrentProfile();
            isfacebook=true;
            try {
                logFacebook(profile.getId(),profile.getFirstName(),profile.getLastName(),profile.getProfilePictureUri(100,100).toString());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{}
            callbackManager= CallbackManager.Factory.create();
            loginButton=(LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email", "user_birthday", "user_friends"));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    refresh();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(),"Ingreso Cancelado",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(),"Error de Ingreso",Toast.LENGTH_SHORT).show();
                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String token= sharedPreferences.getString("token", "");
        if(token.length()>0){
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
            sharedPreferences.edit().putString("token",tok).apply();
            Intent intent = new Intent(MainActivity.this, Activity_CercanosR.class);
            intent.putExtra("token", tok);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error! Datos de ingreso incorrectos",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    public void logFacebook(String id, String nombre, String apellido, String imagen) throws ExecutionException, InterruptedException{
        persona.setApellido(apellido);
        persona.setNombre(nombre);
        persona.setCorreo(id);
        persona.setUrlImagen(imagen);
        persona.setIsFacebook(1);
        nombre = DataParserJ.parsear(nombre);
        apellido = DataParserJ.parsear(apellido);
        String url = "https://findoor.herokuapp.com/persona/facebook/"+nombre+"/"+apellido+"/"
                +1+"/"+id+"/facebook/"+DataParserJ.parsear(persona.getUrlImagen())+"/";
        JsonTask downloadTask = new JsonTask();
        final String resultado=downloadTask.execute(url).get();
        if(resultado.length()==0){
            Toast.makeText(getApplicationContext(), "Error a la hora de ingresar", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                JSONObject obj = new JSONObject(resultado);
                int idetify= Integer.parseInt(obj.getString("id"));
                String nom= DataParserJ.deparsear(obj.getString("nombre"));
                String ap= DataParserJ.deparsear(obj.getString("apellido"));
                String img= DataParserJ.deparsear(obj.getString("imagen"));
                String correo= obj.getString("correo");
                int isf= Integer.parseInt(DataParserJ.deparsear(obj.getString("isface")));
                String tok= obj.getString("token");
                persona= new Persona(idetify, nom,ap,correo,"facebook",img,tok,isf);
                sharedPreferences.edit().putString("token",tok).apply();
                Intent intent = new Intent(MainActivity.this, Activity_CercanosR.class);
                intent.putExtra("token", tok);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error! Datos de ingreso incorrectos",Toast.LENGTH_SHORT).show();
            }

        }

    }



}
