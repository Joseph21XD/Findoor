package Datos;


import java.io.InputStream;

/**
 * Created by ramir on 4/8/2018.
 */

public class Persona {
    private int id= 0;
    private String nombre= "";
    private String apellido= "";
    private String correo= "";
    private String contrasenna= "";
    private String urlImagen= "";
    private InputStream imagen;
    private String token="";
    private int isFacebook= 0;

    public Persona(int i,String n, String ap, String cor, String pwd, String url, String tok, int isf){
        id= i;
        nombre= n;
        apellido= ap;
        correo= cor;
        contrasenna= pwd;
        urlImagen= url;
        token= tok;
        isFacebook= isf;
    }

    public Persona(int i,String n, String ap, String url){
        id= i;
        nombre= n;
        apellido= ap;
        urlImagen= url;
    }

    public Persona(){
        nombre="";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public InputStream getImagen() {
        return imagen;
    }

    public void setImagen(InputStream imagen) {
        this.imagen = imagen;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIsFacebook() {
        return isFacebook;
    }

    public void setIsFacebook(int isFacebook) {
        this.isFacebook = isFacebook;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasenna='" + contrasenna + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                ", token='" + token + '\'' +
                ", isFacebook=" + isFacebook +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
