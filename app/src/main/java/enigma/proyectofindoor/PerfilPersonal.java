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
        finish();
    }

    public void agregado(View view) throws ExecutionException, InterruptedException {
        String token= sharedPreferences.getString("token","");
        String url="https://findoor.herokuapp.com/sitio.json/KEY="+token+"/";
        JsonTask jsonTask= new JsonTask();
        String resultado= jsonTask.execute(url).get();
        Log.d("resultado",resultado);
        try {
            JSONObject obj = new JSONObject(resultado);
            JSONArray array = obj.getJSONArray("sitios");
            for(int i = 0 ; i < array.length() ; i++){
                array.getJSONObject(i).getString("nombre");
                int id=Integer.parseInt(array.getJSONObject(i).getString("id"));
                String nom= DataParserJ.deparsear(array.getJSONObject(i).getString("nombre"));
                String lat=DataParserJ.deparsear(array.getJSONObject(i).getString("latitud"));
                String lon=DataParserJ.deparsear(array.getJSONObject(i).getString("longuitud"));
                String dir=DataParserJ.deparsear(array.getJSONObject(i).getString("direccion"));
                String desc=DataParserJ.deparsear(array.getJSONObject(i).getString("descripcion"));
                String img=DataParserJ.deparsear(array.getJSONObject(i).getString("imagen"));
                MainActivity.sitios.add(new Sitio(id,nom,lat,lon,dir,desc,img));
            }
            Log.d("SITIO", MainActivity.sitios.get(0).toString());
            String tok= obj.getString("token");
            sharedPreferences.edit().putString("token",tok).apply();
            Intent intent = new Intent(PerfilPersonal.this, InformacionActivity.class);
            intent.putExtra("valor", 0);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error! Datos de ingreso incorrectos",Toast.LENGTH_SHORT).show();
        }




    }

    public void agregado2(View view) throws ExecutionException, InterruptedException {
        String token= sharedPreferences.getString("token","");
        String url="https://findoor.herokuapp.com/persona.json/KEY="+token+"/";
        JsonTask jsonTask= new JsonTask();
        String resultado= jsonTask.execute(url).get();
        Log.d("resultado",resultado);
        try {
            JSONObject obj = new JSONObject(resultado);
            JSONArray array = obj.getJSONArray("personas");
            for(int i = 0 ; i < array.length() ; i++){
                array.getJSONObject(i).getString("nombre");
                int id=Integer.parseInt(array.getJSONObject(i).getString("id"));
                String nom= DataParserJ.deparsear(array.getJSONObject(i).getString("nombre"));
                String ap=DataParserJ.deparsear(array.getJSONObject(i).getString("apellido"));
                String img=DataParserJ.deparsear(array.getJSONObject(i).getString("imagen"));
                MainActivity.personas.add(new Persona(id,nom,ap,img));
            }
            Log.d("PERSONA", MainActivity.personas.get(0).toString());
            String tok= obj.getString("token");
            sharedPreferences.edit().putString("token",tok).apply();
            Intent intent = new Intent(PerfilPersonal.this, PerfilUsuario.class);
            intent.putExtra("valor", 0);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error! Datos de ingreso incorrectos",Toast.LENGTH_SHORT).show();
        }




    }

    public void agregado3(View view) throws ExecutionException, InterruptedException {
        String token= sharedPreferences.getString("token","");
        String url="https://findoor.herokuapp.com/sitio.json/KEY="+token+"/";
        JsonTask jsonTask= new JsonTask();
        String resultado= jsonTask.execute(url).get();
        Log.d("resultado",resultado);
        try {
            JSONObject obj = new JSONObject(resultado);
            JSONArray array = obj.getJSONArray("sitios");
            for(int i = 0 ; i < array.length() ; i++){
                array.getJSONObject(i).getString("nombre");
                int id=Integer.parseInt(array.getJSONObject(i).getString("id"));
                String nom= DataParserJ.deparsear(array.getJSONObject(i).getString("nombre"));
                String lat=DataParserJ.deparsear(array.getJSONObject(i).getString("latitud"));
                String lon=DataParserJ.deparsear(array.getJSONObject(i).getString("longuitud"));
                String dir=DataParserJ.deparsear(array.getJSONObject(i).getString("direccion"));
                String desc=DataParserJ.deparsear(array.getJSONObject(i).getString("descripcion"));
                String img=DataParserJ.deparsear(array.getJSONObject(i).getString("imagen"));
                MainActivity.sitios.add(new Sitio(id,nom,lat,lon,dir,desc,img));
            }
            Log.d("SITIO", MainActivity.sitios.get(0).toString());
            String tok= obj.getString("token");
            sharedPreferences.edit().putString("token",tok).apply();
            Intent intent = new Intent(PerfilPersonal.this, MapsSitiosActivity.class);
            intent.putExtra("valor", 0);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error! Datos de ingreso incorrectos",Toast.LENGTH_SHORT).show();
        }




    }


}
