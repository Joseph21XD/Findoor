package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import Datos.DataParserJ;
import Datos.ImageTask;
import Datos.JsonTask;

public class InformacionActivity extends AppCompatActivity {
    public static final String MIXPANEL_TOKEN = "3c4b7583313688d65dfcacdff72ba77c";
    TextView textView1, textView2;
    RatingBar ratingBar;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    int favorito= 0;
    int visitado=0;
    int value=0;
    MixpanelAPI mixpanel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        mixpanel = MixpanelAPI.getInstance(InformacionActivity.this, MIXPANEL_TOKEN);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        ratingBar= findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if(fromUser) {
                    int x= (int)(rating*2);
                    String tok= sharedPreferences.getString("token", "");
                    String url = "http://findoor.herokuapp.com/sitio/ranking/add/"+MainActivity.sitios.get(value).getId()+"/"+x+"/KEY="+tok+"/";
                    JsonTask jsonTask= new JsonTask();
                    try {
                        String resultado = jsonTask.execute(url).get();
                        sharedPreferences.edit().putString("token",resultado).apply();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= getIntent();
        int valor= intent.getIntExtra("valor",-1);
        textView1= findViewById(R.id.textView19);
        textView2= findViewById(R.id.textView20);
        imageView= findViewById(R.id.imageView5);
        if(valor!=-1){
            value=valor;
            textView1.setText(MainActivity.sitios.get(valor).getNombre());
            textView2.setText(MainActivity.sitios.get(valor).getDireccion()+"\n"+MainActivity.sitios.get(valor).getDescripcion());
            ImageTask imageTask= new ImageTask();
            try {
                Bitmap bmp= imageTask.execute(MainActivity.sitios.get(valor).getImagen()).get();
                imageView.setImageBitmap(bmp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            String url="http://findoor.herokuapp.com/sitio/get/TYPE=FAVORITE/"+MainActivity.sitios.get(valor).getId()+"/KEY="+sharedPreferences.getString("token", "")+"/";
            JsonTask jsonTask= new JsonTask();
            try {
                String resultado = jsonTask.execute(url).get();
                JSONObject obj = new JSONObject(resultado);
                String respuesta = obj.getString("respuesta");
                String token = obj.getString("token");
                sharedPreferences.edit().putString("token",token).apply();
                if(respuesta.equals("true")){
                    ImageView imageView= findViewById(R.id.imageView6);
                    imageView.setImageResource(R.drawable.logo_lleno);
                    favorito= 1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            url="http://findoor.herokuapp.com/sitio/get/TYPE=VISITED/"+MainActivity.sitios.get(valor).getId()+"/KEY="+sharedPreferences.getString("token", "")+"/";
            JsonTask jsonTask2= new JsonTask();
            try {
                String resultado = jsonTask2.execute(url).get();
                JSONObject obj = new JSONObject(resultado);
                String respuesta = obj.getString("respuesta");
                String token = obj.getString("token");
                sharedPreferences.edit().putString("token",token).apply();
                if(respuesta.equals("true")){
                    ImageView imageView= findViewById(R.id.imageView7);
                    imageView.setImageResource(R.drawable.visitado);
                    visitado= 1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tok= sharedPreferences.getString("token", "");
            url="http://findoor.herokuapp.com/sitio/ranking/get/"+MainActivity.sitios.get(valor).getId()+"/KEY="+tok+"/";
            JsonTask jsonTask1= new JsonTask();
            try {
                String resultado = jsonTask1.execute(url).get();
                JSONObject obj = new JSONObject(resultado);
                int respuesta = Integer.parseInt(obj.getString("respuesta"));
                String token = obj.getString("token");
                sharedPreferences.edit().putString("token",token).apply();
                if(respuesta!=0){
                RatingBar ratingBar= findViewById(R.id.ratingBar);
                float f= (float) respuesta/2;
                ratingBar.setRating(f);}
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void pushTortuga(View v)  {
        ImageView imageView= (ImageView)v;
        if(favorito==0){
            String token= sharedPreferences.getString("token","");
            String url="https://findoor.herokuapp.com/sitio/add/TYPE=FAVORITE/"+MainActivity.sitios.get(value).getId()+"/KEY="+token+"/";
            JsonTask jsonTask= new JsonTask();
            try {
                String resultado=jsonTask.execute(url).get();
                sharedPreferences.edit().putString("token",resultado).apply();
                imageView.setImageResource(R.drawable.logo_lleno);
                favorito=1;
                Toast.makeText(getApplicationContext(),MainActivity.sitios.get(value).getNombre()+" es favorito",Toast.LENGTH_SHORT).show();
                JSONObject props = new JSONObject();
                props.put("Name", MainActivity.persona.getNombre());
                props.put("Last-Name", MainActivity.persona.getApellido());
                mixpanel.track("Add Favorite", props);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        else{
            String token= sharedPreferences.getString("token","");
            String url="https://findoor.herokuapp.com/sitio/delete/TYPE=FAVORITE/"+MainActivity.sitios.get(value).getId()+"/KEY="+token+"/";
            JsonTask jsonTask= new JsonTask();
            try {
                String resultado=jsonTask.execute(url).get();
                sharedPreferences.edit().putString("token",resultado).apply();
                imageView.setImageResource(R.drawable.logo_vacio);
                favorito=0;
                Toast.makeText(getApplicationContext(),MainActivity.sitios.get(value).getNombre()+" ya no es favorito",Toast.LENGTH_SHORT).show();
                JSONObject props = new JSONObject();
                props.put("Name", MainActivity.persona.getNombre());
                props.put("Last-Name", MainActivity.persona.getApellido());
                mixpanel.track("delete Favorite", props);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e){
            e.printStackTrace();
            }
        }
    }

    public void pushHouse(View v)  {
        ImageView imageView= (ImageView)v;
        if(visitado==0){
            String token= sharedPreferences.getString("token","");
            String url="https://findoor.herokuapp.com/sitio/add/TYPE=VISITED/"+MainActivity.sitios.get(value).getId()+"/KEY="+token+"/";
            JsonTask jsonTask= new JsonTask();
            try {
                String resultado=jsonTask.execute(url).get();
                sharedPreferences.edit().putString("token",resultado).apply();
                imageView.setImageResource(R.drawable.visitado);
                visitado=1;
                Toast.makeText(getApplicationContext(),MainActivity.sitios.get(value).getNombre()+" fue visitado",Toast.LENGTH_SHORT).show();
                JSONObject props = new JSONObject();
                props.put("Name", MainActivity.persona.getNombre());
                props.put("Last-Name", MainActivity.persona.getApellido());
                mixpanel.track("add Visited", props);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        else{
            String token= sharedPreferences.getString("token","");
            String url="https://findoor.herokuapp.com/sitio/delete/TYPE=VISITED/"+MainActivity.sitios.get(value).getId()+"/KEY="+token+"/";
            JsonTask jsonTask= new JsonTask();
            try {
                String resultado=jsonTask.execute(url).get();
                sharedPreferences.edit().putString("token",resultado).apply();
                imageView.setImageResource(R.drawable.visitar);
                visitado=0;
                Toast.makeText(getApplicationContext(),MainActivity.sitios.get(value).getNombre()+" no fue visitado",Toast.LENGTH_SHORT).show();
                JSONObject props = new JSONObject();
                props.put("Name", MainActivity.persona.getNombre());
                props.put("Last-Name", MainActivity.persona.getApellido());
                mixpanel.track("delete Visited", props);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void mapear(View v){
        Intent intent = new Intent(InformacionActivity.this, MapsSitiosActivity.class);
        intent.putExtra("valor", value);
        startActivity(intent);
    }

    public void pushComentar(View view){
        Intent intent = new Intent(InformacionActivity.this, Activity_ComentariosR.class);
        intent.putExtra("IDlugar", value);
        intent.putExtra("token", sharedPreferences.getString("token", ""));
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
