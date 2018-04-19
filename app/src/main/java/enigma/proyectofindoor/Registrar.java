package enigma.proyectofindoor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import Datos.DataParserJ;
import Datos.ImageManagerJ;
import Datos.JsonTask;
import Datos.Persona;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Registrar extends AppCompatActivity {
    public static final String MIXPANEL_TOKEN = "3c4b7583313688d65dfcacdff72ba77c";
    SharedPreferences sharedPreferences;
    EditText editText1, editText2, editText3, editText4;
    public static Persona persona;
    final String validChars = "abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    String valor="";
    MixpanelAPI mixpanel;
    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mixpanel = MixpanelAPI.getInstance(Registrar.this, MIXPANEL_TOKEN);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        setContentView(R.layout.activity_registrar);
        editText1 = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        persona = new Persona();
        valor= randomString(10);
    }

    public void ingresar(View view) throws ExecutionException, InterruptedException, JSONException {
        String correo = editText3.getText().toString();
        if (correo.contains("@") && correo.contains(".com") && (correo.contains("hotmail") || correo.contains("gmail")) && correo.length() > 10) {
            String nombre = editText1.getText().toString();
            String apellido = editText2.getText().toString();
            String contrasenna = editText4.getText().toString();
            if (contrasenna.length() >= 8) {
                if (nombre.length() > 0 && apellido.length() > 0) {
                    nombre = DataParserJ.parsear(nombre);
                    apellido = DataParserJ.parsear(apellido);
                    correo = DataParserJ.parsear(correo);
                    contrasenna = DataParserJ.parsear(contrasenna);
                    persona.setApellido(apellido);
                    persona.setNombre(nombre);
                    persona.setCorreo(correo);
                    persona.setContrasenna(contrasenna);
                    if (persona.getUrlImagen().length() == 0) {
                        persona.setUrlImagen("https://findoor.blob.core.windows.net/imagenes/logo.png");
                    }
                    String url = "https://findoor.herokuapp.com/persona/add/"+persona.getNombre()+"/"+persona.getApellido()+"/"
                            +persona.getIsFacebook()+"/"+persona.getCorreo()+"/"+persona.getContrasenna()+"/"+DataParserJ.parsear(persona.getUrlImagen())+"/";
                    JsonTask downloadTask = new JsonTask();
                    final String resultado=downloadTask.execute(url).get();
                    if(resultado.length()==0){
                        Toast.makeText(getApplicationContext(), "Correo no permitido, cambie de correo", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        JSONObject props = new JSONObject();
                        props.put("Name", DataParserJ.deparsear(persona.getNombre())+"");
                        props.put("Last-Name", DataParserJ.deparsear(persona.getApellido())+"");
                        mixpanel.track("Sign in Findoor", props);
                        new AlertDialog.Builder(this).setIcon(R.drawable.logo_lleno)
                                .setTitle("Ya estás logueado")
                                .setMessage("Quiere empezar la experiencia Findoor?")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        sharedPreferences.edit().putString("token",resultado).apply();
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error! Dato no ingresado", Toast.LENGTH_SHORT).show();
                    editText1.setText("");
                    editText2.setText("");
                }

            } else {
                Toast.makeText(getApplicationContext(), "la contraseña debe tener más de 8 caracteres", Toast.LENGTH_SHORT).show();
                editText4.setText("");
            }
        } else {
            Toast.makeText(getApplicationContext(), "correo incorrecto", Toast.LENGTH_SHORT).show();
            editText3.setText("");
        }
    }

    public void abrirGaleria(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImage;
        switch (requestCode) {
            case 1:
                if (resultCode == Registrar.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    String selectedPath = selectedImage.getPath();
                    if (requestCode == 1) {

                        if (selectedPath != null) {
                            InputStream imageStream = null;
                            InputStream imageStream2 = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        selectedImage);
                                imageStream2 = getContentResolver().openInputStream(
                                        selectedImage);
                                DownloadTask downloadTask= new DownloadTask();
                                String st= downloadTask.execute(getBytes(imageStream)).get();
                                Bitmap bmp= BitmapFactory.decodeStream(imageStream2);
                                ImageView imageView= findViewById(R.id.imageView3);
                                imageView.setImageBitmap(bmp);
                                persona.setImagen(imageStream);
                                persona.setUrlImagen("https://findoor.blob.core.windows.net/imagenes/"+valor);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
        callbackManager.onActivityResult(requestCode,resultCode,imageReturnedIntent);

    }

    public class DownloadTask extends AsyncTask<byte[], Void, String> {

        @Override
        protected String doInBackground(byte[]... urls) {
            try {
//http://files.softicons.com/download/culture-icons/sharingan-icons-1.5-by-harenome-razanajato/png/256x256/kakashi.png
                ImageManagerJ.UploadImage2(urls[0], valor);
                return "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

    public String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( validChars.charAt( rnd.nextInt(validChars.length()) ) );
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

}
