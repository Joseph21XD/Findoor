package enigma.proyectofindoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void registrar(View view){
        Intent intent= new Intent(MainActivity.this,Registrar.class);
        startActivity(intent);
    }

    public void information(View view){
        Intent intent= new Intent(MainActivity.this,InformacionActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
