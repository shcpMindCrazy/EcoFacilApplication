package br.com.testes.ecofacil.Network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.HashMap;

import br.com.testes.ecofacil.Domain.WrapObjToNetwork;

/**
 * Created by samue on 05/04/2018.
 */

public class NetworkConnection {

    private static NetworkConnection instance;
    private Context mContext;
    private RequestQueue mRequestQueue;


    public NetworkConnection(Context c){
        mContext = c;
        mRequestQueue = getRequestQueue();
    }


    public static NetworkConnection getInstance( Context c ){
        if( instance == null ){
            instance = new NetworkConnection( c.getApplicationContext() );
        }
        return( instance );
    }


    public RequestQueue getRequestQueue(){
        if( mRequestQueue == null ){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return(mRequestQueue);
    }


    public <T> void addRequestQueue( Request<T> request ){
        getRequestQueue().add(request);
    }


    public void execute( final Transaction transaction, final String tag, final String url ){

        Log.i("Script", "NetworkConnect.execute()");
        WrapObjToNetwork obj = transaction.doBefore();
        Gson gson = new Gson();

        if( obj == null ){

            Log.i("Script", "NetworkConnect.execute().obj(null)");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("jsonArray", gson.toJson(obj));

        Log.i("Script", "NetworkConnect.execute()" + params);

        CustomRequest request = new CustomRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.i("VolleyConnection", tag+" ---> "+response);
                        transaction.doAfter(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Script", "onErrorResponse(): "+error.getMessage());
                        transaction.doAfter(null);
                    }
                });

        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequestQueue(request);
    }
}
