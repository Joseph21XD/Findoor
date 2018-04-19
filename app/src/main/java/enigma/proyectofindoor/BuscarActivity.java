package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Datos.CustomListView;
import Datos.DataParserJ;
import Datos.JsonTask;
import Datos.Persona;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BuscarActivity extends AppCompatActivity {

    ArrayList<String> listaCercanos = new ArrayList<String>();
    ArrayList<String> listaICercanos = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    ListView listView;
    CustomListView customListView;
    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        listView= findViewById(R.id.listaBuscar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item= menu.findItem(R.id.menusearch);
        SearchView searchView= (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listaICercanos.clear();
                listaCercanos.clear();
                MainActivity.personas.clear();
                token=sharedPreferences.getString("token","");
                s= DataParserJ.parsear(s);
                String url= "http://findoor.herokuapp.com/persona/search/"+s+"/KEY="+token+"/";
                JsonTask jsonTask= new JsonTask();
                try {
                    String resultado= jsonTask.execute(url).get();
                    JSONObject obj = new JSONObject(resultado);
                    JSONArray array = obj.getJSONArray("personas");
                    for(int i = 0 ; i < array.length() ; i++){
                        array.getJSONObject(i).getString("nombre");
                        int id=Integer.parseInt(array.getJSONObject(i).getString("id"));
                        String nom= DataParserJ.deparsear(array.getJSONObject(i).getString("nombre"));
                        String ap=DataParserJ.deparsear(array.getJSONObject(i).getString("apellido"));
                        String img=DataParserJ.deparsear(array.getJSONObject(i).getString("imagen"));
                        listaCercanos.add(nom);
                        listaICercanos.add(img);
                        MainActivity.personas.add(new Persona(id,nom,ap,img));
                    }
                    token= obj.getString("token");
                    sharedPreferences.edit().putString("token",token).apply();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                customListView = new CustomListView(BuscarActivity.this, listaCercanos, listaICercanos);
                listView.setAdapter(customListView);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), PerfilUsuario.class);
                        intent.putExtra("valor", position);
                        startActivity(intent);
                    }

                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
