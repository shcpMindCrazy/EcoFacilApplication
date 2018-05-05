package br.com.testes.ecofacil.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by samue on 17/03/2018.
 */

public class ATtransformarEndereco extends AsyncTask<String, String, LatLng> {

    public AsyncResponse delegate = null;

    private Context context;
    private ProgressDialog progressDialog;

    public ATtransformarEndereco(Context context){

        this.context = context;
    }

    @Override
    protected LatLng doInBackground(String... strings) {

        try {

            String link = strings[0].replaceAll(" ", "+");
            Log.i("Script", "ATtransformarEndereco.doInBackground( " + link + " )");

            String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + link + "&key=AIzaSyCsK62_RJlA5TGXxEJ-rO1pvY7M4W7p0hQ";

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            InputStream in = httpclient.execute(request).getEntity()
                    .getContent();

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            br = new BufferedReader(new InputStreamReader(in));

            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();

            }

            String resultStream = sb.toString();

            Log.i("Script", "Mensagem: " + resultStream);

            JSONObject msgJSON = new JSONObject(resultStream);
            JSONArray result = msgJSON.getJSONArray("results");
            Log.i("Script", "JSON: " + result);

            JSONObject resultJSON = result.getJSONObject(0).getJSONObject("geometry");
            Log.i("Script", "JSON2: " + resultJSON);
            JSONObject resultGeometry = resultJSON.getJSONObject("location");
            Log.i("Script", "JSON3: " + resultGeometry);
            double resultLongetude = resultGeometry.getDouble("lng");
            double resultLatitude = resultGeometry.getDouble("lat");

            LatLng resultCoordenadas = new LatLng(resultLatitude, resultLongetude);

            Log.i("Script", "Result coordenadas: " + resultCoordenadas);

            return resultCoordenadas;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPreExecute() {

        //progressDialog = new ProgressDialog(context);
        //progressDialog.setMessage("Carregando...");
        //progressDialog.setCancelable(false);
        //progressDialog.show();
    }


    @Override
    protected void onProgressUpdate(String... values) {

        super.onProgressUpdate(values);
        //progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(LatLng msg) {

        Log.i("Script", "ATtransformarEndereco.onPostExecute()");

        delegate.processFinish(msg);

        //if (progressDialog != null && progressDialog.isShowing())
        //    progressDialog.dismiss();

        //evaluate result
    }


}
