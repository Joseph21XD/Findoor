package Datos;

import android.graphics.Bitmap;

/**
 * Created by ramir on 4/8/2018.
 */

public class Persona {
    String nombre= "";
    String apellido= "";
    String correo= "";
    String contrasenna= "";
    String urlImagen= "";
    Bitmap imagen;
    String token="";
    int isFacebook= 0;

    public Persona(String n, String ap, String cor, String pwd, String url, String tok, int isf){
        nombre= n;
        apellido= ap;
        correo= cor;
        contrasenna= pwd;
        urlImagen= url;
        token= tok;
        isFacebook= isf;
    }

    public Persona(){
        nombre="";
    }


}
