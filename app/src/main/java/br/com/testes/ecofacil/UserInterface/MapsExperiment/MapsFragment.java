package br.com.testes.ecofacil.UserInterface.MapsExperiment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.testes.ecofacil.R;

/**
 * Created by samue on 02/03/2018.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    //Componentes do Google Maps;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99; //Instanciamos esta variável para perminssão da Requisição de Localicação
    private GoogleApiClient mGoogleApiClient; //Instanciamos as configurações de API;
    private GoogleMap mGoogleMap; //Instanciamos o mapa de Google API;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {

        Log.i("Script", "MapsFragment.onCreateView()");

        View viewMaps = inflater.inflate(R.layout.fragment_maps, container, false);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            checkLocationPermission();
        }

        return viewMaps;
    }

    /*

        LISTENERS - OnMapReadyCallback

     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i("Script", "MapsFragment.onMapReady()");

        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        //Initialize Google Play Services - Verificamos se esta aplicação possui a permissão necessária
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    //Esta declarada no XML - Manifest
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Iniciamos a API da Google
                connectionAPI();

                //E a nossa localização atual é fornecida;
                mGoogleMap.setMyLocationEnabled(true);

            }
        } else {

            //Iniciamos a API da Google
            connectionAPI();

            //E a nossa localização atual é fornecida;
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    /*

        LISTENERS - GoogleApiClient

     */

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i("Script.GoogleApiClient", "MapsFragment.onConnected()");
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i("Script.GoogleApiClient", "MapsFragment.onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i("Script.GoogleApiClient", "MapsFragment.onConnectionFailed()");
    }

    /*

        LISTENERS - LocationListener

     */

    @Override
    public void onLocationChanged(Location location) {

        Log.i("Script.LocationListener", "MapsFragment.onLocationChanged()");
    }

    /*

        LISTENERS - onCreateView

     */

    @Override
    public void onResume() {

        //Android monitor annotation;
        Log.i("Script.MapsAPI", "MapsFragment.onResume()");
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {

        //Android monitor annotation;
        Log.i("Script.MapsAPI", "MapsFragment.onPause()");
        super.onPause();
        mGoogleApiClient.disconnect();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {

        //Android monitor annotation;
        Log.i("Script.MapsAPI", "MapsFragment.onDestroy()");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {

        //Android monitor annotation;
        Log.i("Script.MapsAPI", "MapsFragment.onLowMemory()");
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /*

        MÉTODOS DE CONEXÃO DE API

     */

    //Método de conexão de API - Synchronized permite que a execução deste seja em fila, organizada e sem atroplelos;
    private synchronized void connectionAPI() {

        Log.i("LOG", "EmergencyActivity.connectionAPI()");

        //Configurações para a conexão da API LOCATION;
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /*

        MÉTODOS DE PERMISSÕES DE EXECUÇÃO;

     */

    //Solicitação da permissão do uso de GPS;
    public boolean checkLocationPermission() {

        //Registramos o evento no Android Monitor;
        Log.i("Script.Permission", "MapsFragment.checkLocationPermission()");

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;

        } else {

            return true;
        }
    }
}
