package volley;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.JsonArray;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jkydevelop.solidareus.Main;

/**
 * Created by Juan Carlos on 23/03/2017.
 */

public class ComunicationSetProject extends AsyncTask<String, Void, String> {

    /** Es necesario que a los constructores se les pase el contexto de la actividad desde la que lo llamamos para saber de
     donde venimos y asi poder gestionar la recogida de datos **/

    JsonArray jsonArray = null;
    Main main = null;

    public ComunicationSetProject(JsonArray jsonArray, Main main){
        this.jsonArray = jsonArray;
        this.main = main;
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... params) {
        String result = "";
        final String basicAuth = "Basic " + Base64.encodeToString(EndPoints.AUTH_USER_PASS.getBytes(), Base64.NO_WRAP);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setHeader("Authorization", basicAuth);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                if (jsonArray!=null){
                    nameValuePairs.add(new BasicNameValuePair("user", jsonArray.toString()));
                    //System.out.println("JSONARRAY"+jsonArray.toString());
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //execute http post
                HttpResponse response = httpclient.execute(httppost);

                // Intento de recuperar la respuesta
                result = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {

            } catch (IOException e) {
        }
        return result;
    }
        @Override
    protected void onPostExecute(String result) {
        //Log.d("UPLIMAGE", "result: " + result);

        main.loadProjects();
    }

}


