package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import Datos.DataParserJ;
import Datos.ImageTask;
import Datos.JsonTask;
import Datos.Persona;
import Datos.Sitio;

public class PerfilPersonal extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_personal);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        TextView textView1= findViewById(R.id.textView);
        TextView textView2= findViewById(R.id.textView2);
        ImageView imageView= findViewById(R.id.imageView3);
        textView1.setText(MainActivity.persona.getNombre());
        textView2.setText(MainActivity.persona.getApellido());
        ImageTask imageTask = new ImageTask();
        Bitmap bitmap= null;
        try {
            bitmap=imageTask.execute(MainActivity.persona.getUrlImagen()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
    }

    public void configurar(View v)
    {
        Intent intent= new Intent(PerfilPersonal.this, Configuracion.class);
        startActivity(intent);
    }

    public void recomendar(View v){
        Intent intent= new Intent(PerfilPersonal.this, Recomendar.class);
        startActivity(intent);
    }

    public void logout(View v)
    {
        sharedPreferences.edit().putString("token","").apply();
        LoginManager.getInstance().logOut();
        finish();
    }

    public void seguidosClicked(View view) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(PerfilPersonal.this, Activity_Seguidos.class);
        startActivity(intent);

    }

    public void seguidoresClicked(View view) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(PerfilPersonal.this, Activity_Seguidores.class);
        startActivity(intent);

    }

    public void visitadosClicked(View view) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(PerfilPersonal.this, Activity_Visitados.class);
        startActivity(intent);
    }


}
