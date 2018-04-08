package Datos;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ramir on 4/8/2018.
 */

public class JsonTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        try {
            String result = "";
            URL url= new URL(urls[0]);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream inputStream= httpURLConnection.getInputStream();
            int data= inputStream.read();
            while(data!=-1){
                result+=(char) data;
                data= inputStream.read();
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
