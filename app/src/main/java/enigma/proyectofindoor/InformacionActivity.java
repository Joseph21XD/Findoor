package enigma.proyectofindoor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import Datos.ImageTask;

public class InformacionActivity extends AppCompatActivity {
    TextView textView1, textView2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
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
            textView1.setText(MainActivity.sitios.get(valor).getNombre());
            textView2.setText(MainActivity.sitios.get(valor).getDireccion()+" "+MainActivity.sitios.get(valor).getDireccion());
            ImageTask imageTask= new ImageTask();
            try {
                Bitmap bmp= imageTask.execute(MainActivity.sitios.get(valor).getImagen()).get();
                imageView.setImageBitmap(bmp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}
