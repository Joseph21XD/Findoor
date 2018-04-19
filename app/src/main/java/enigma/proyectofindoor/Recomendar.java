package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import Datos.DataParserJ;
import Datos.ImageTask;
import Datos.JsonTask;

public class Recomendar extends AppCompatActivity {
    EditText editText1 , editText2, editText3, editText4, editText5, editText6;
    TextView textView1, textView2;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendar);
        editText1= findViewById(R.id.editText);
        editText4= findViewById(R.id.editText4);
        editText5= findViewById(R.id.editText5);
        editText6= findViewById(R.id.editText6);
        textView1= findViewById(R.id.textView8);
        textView2= findViewById(R.id.textView9);
        imageView= findViewById(R.id.imageView7);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MapsRecomendarActivity.coordenadas!=null){
            String lat=MapsRecomendarActivity.coordenadas.latitude+"";
            String lon=MapsRecomendarActivity.coordenadas.longitude+"";
            textView1.setText(lat.substring(0,lat.indexOf(".")+6));
            textView2.setText(lon.substring(0,lon.indexOf(".")+6));
            MapsRecomendarActivity.coordenadas= null;
            editText4.setText(MapsRecomendarActivity.dir);
        }
    }

    public  void getCoordenadas(View view){
        Intent intent= new Intent(Recomendar.this, MapsRecomendarActivity.class);
        startActivity(intent);
    }

    public void ingresar(View view){
        if(editText1.length()>0 && editText4.length()>0 && editText5.length()>0){
            if(editText6.length()==0)
                editText6.setText("noAgregado");
            if(textView1.length()==0){
                textView1.setText("noAgregado");
                textView2.setText("noAgregado");
            }
            String nombre= DataParserJ.parsear(editText1.getText().toString());
            String lat= DataParserJ.parsear(textView1.getText().toString());
            String lon= DataParserJ.parsear(textView2.getText().toString());
            String dir= DataParserJ.parsear(editText4.getText().toString());
            String desc= DataParserJ.parsear(editText5.getText().toString());
            String image= DataParserJ.parsear(editText6.getText().toString());
            String token= sharedPreferences.getString("token", "");
            String url = "https://findoor.herokuapp.com/sitio/suggest/"+nombre+"/"+lat+"/"+lon+"/"+dir+"/"+desc+"/"+image+"/KEY="+token+"/";
            JsonTask downloadTask = new JsonTask();
            String resultado= "";
            try {
                resultado = downloadTask.execute(url).get();
            } catch (InterruptedException e) {
                resultado="";
            } catch (ExecutionException e) {
                resultado="";
            }
            if(resultado.equals("")){
                Toast.makeText(getApplicationContext(),"Error al recomendar sitio", Toast.LENGTH_SHORT).show();
            }
            else{
                sharedPreferences.edit().putString("token",resultado).apply();
                Toast.makeText(getApplicationContext(),"Éxito al recomendar el sitio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cargar(View view) throws ExecutionException, InterruptedException {
        String s= editText6.getText().toString();
        Bitmap bmp;
        if(s.length()>0){
            ImageTask imageTask= new ImageTask();
            bmp= imageTask.execute(s).get();
            imageView.setImageBitmap(bmp);
        }
    }
}
