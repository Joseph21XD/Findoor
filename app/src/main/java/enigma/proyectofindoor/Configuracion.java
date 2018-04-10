package enigma.proyectofindoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

import Datos.DataParserJ;
import Datos.ImageManagerJ;
import Datos.ImageTask;
import Datos.JsonTask;

public class Configuracion extends AppCompatActivity {
    public int mapa= 1;
    EditText edt1, edt2, edt3;
    RadioButton radioButton2;
    RadioButton radioButton1;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    String valor="";
    final String validChars = "abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    public void pushRadioButton(View view){
        RadioButton radioButton= (RadioButton)view;
        if(radioButton.getTag().equals("1")){
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
            mapa= 1;
        }
        else{
            radioButton1.setChecked(false);
            radioButton2.setChecked(true);
            mapa= 2;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        imageView= findViewById(R.id.imageView3);
        sharedPreferences= this.getSharedPreferences("enigma.proyectofindoor", getApplicationContext().MODE_PRIVATE);
        edt1= findViewById(R.id.editText);
        edt2= findViewById(R.id.editText2);
        edt3= findViewById(R.id.editText4);
        edt1.setText(MainActivity.persona.getNombre());
        edt2.setText(MainActivity.persona.getApellido());
        edt3.setText(MainActivity.persona.getContrasenna());
        radioButton1= findViewById(R.id.radioButton);
        radioButton2= findViewById(R.id.radioButton2);
        String mapatoken= sharedPreferences.getString("mapa", "");
        if(!mapatoken.equals("") && !mapatoken.equals("1")){
            radioButton1.setChecked(false);
            radioButton2.setChecked(true);
            mapa= 2;
        }
        ImageTask imageTask= new ImageTask();
        Bitmap bmp= null;
        try {
            bmp = imageTask.execute(MainActivity.persona.getUrlImagen()).get();
            imageView.setImageBitmap(bmp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public void abrirGaleria(View v) {
        valor=MainActivity.persona.getUrlImagen().substring(47);
        if(valor.equals("logo.png"))
            valor= randomString(10);
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
                                MainActivity.persona.setImagen(imageStream);
                                MainActivity.persona.setUrlImagen("https://findoor.blob.core.windows.net/imagenes/"+valor);
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

    public void ingresar(View view){
        sharedPreferences.edit().putString("mapa",mapa+"").apply();
        if(edt1.length()>0 && edt2.length()>0 && edt3.length()>0){
            MainActivity.persona.setNombre(edt1.getText().toString());
            MainActivity.persona.setApellido(edt2.getText().toString());
            MainActivity.persona.setContrasenna(edt3.getText().toString());
            String token= sharedPreferences.getString("token", "");
            String url = "https://findoor.herokuapp.com/persona/update/"+MainActivity.persona.getId()+"/"+DataParserJ.parsear(MainActivity.persona.getNombre())
                    +"/"+DataParserJ.parsear(MainActivity.persona.getApellido())+"/" +MainActivity.persona.getIsFacebook()+
                    "/"+DataParserJ.parsear(MainActivity.persona.getCorreo())+"/"
                    +DataParserJ.parsear(MainActivity.persona.getContrasenna())+"/"+
                    DataParserJ.parsear(MainActivity.persona.getUrlImagen())+"/KEY="+token+"/";
            JsonTask downloadTask = new JsonTask();
            try {
                final String resultado=downloadTask.execute(url).get();
                if(!resultado.equals("")){
                    sharedPreferences.edit().putString("token",resultado).apply();
                    finish();}
                else{
                    Toast.makeText(getApplicationContext(),"Error a la hora de guardar los datos", Toast.LENGTH_SHORT);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }
}
