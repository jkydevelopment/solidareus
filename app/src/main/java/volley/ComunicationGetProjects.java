package volley;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import jkydevelop.solidareus.Main;

/**
 * Created by Juan Carlos on 23/03/2017.
 */

public class ComunicationGetProjects extends AsyncTask<String, Void, String> {

    Main main = null;
    JsonArray jsonArray = null;
    boolean validUser = false;
    boolean checkIfExistsAlerts = false;

    public ComunicationGetProjects(Main main){
        this.main = main;
    }


    @Override
    protected String doInBackground(String... params) {
        String cadenaJson="";
        final String basicAuth = "Basic " + Base64.encodeToString(EndPoints.AUTH_USER_PASS.getBytes(), Base64.NO_WRAP);
        try{
            URL url=new URL(params[0]);
            URLConnection con=url.openConnection();
            con.setRequestProperty ("Authorization", basicAuth);
            //recuperacion de la respuesta JSON
            String s;
            InputStream is=con.getInputStream();
            //utilizamos UTF-8 para que interprete
            //correctamente las ñ y acentos
            BufferedReader bf=new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while((s=bf.readLine())!=null){
                cadenaJson+=s;
            }
            //Log.d("MISDATOS", ""+cadenaJson.toString());
            if (cadenaJson.contains("error") || cadenaJson.equals("") || cadenaJson.equals("[]"))
                return "error";
            if (cadenaJson.contains("deleted"))
                return "deleted";
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        return cadenaJson;
    }

    @Override
    protected void onPostExecute(String result) {
        //Log.d("NUEVOINSERTADO", ""+result);
        if(result.equals("error")){
            main.updateProjects(null);
            return;
        }
        String[] datosParticipante=null;
        try{
            //creamos un array JSON a partir de la cadena recibida
            JSONArray jarray=new JSONArray(result);
            //Log.d("GETALERT", ""+jarray.toString());
            if (jarray!=null)
                validUser = true;
            main.updateProjects(jarray);
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
    }


}


