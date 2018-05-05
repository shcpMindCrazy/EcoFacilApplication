package br.com.testes.ecofacil.LocationAPI;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;

/**
 * Created by samue on 24/04/2018.
 */

public class LocationAPI implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    //Variáveis Globais;

    //Contexto da aplicação;
    Context context;
    Activity activity;

    //Instanciamos esta variável para perminssão da Requisição de Localicação
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //Variáveis de configuração do Location
    private GoogleApiClient mGoogleApiClient; //Instanciamos as configurações de API;
    private Location mLastLocation; //Variável com o contexto de localização;
    private LocationRequest mLocationRequest; //Variável com a requisição da localização;
    private LatLng coordenadasAtual; //Coordenadas atuais para contexto;

    //Construtor;
    public LocationAPI(Context context, Activity activity) {

        //Transferimos o contexto da aplicação a classe;
        this.context = context;
        this.activity = activity;

        //Conectamos a api;
        connectionAPI();
    }

    //Método de conexão de API - Synchronized permite que a execução deste seja em fila, organizada e sem atropelos;
    private synchronized void connectionAPI() {

        Log.i("Script", "LocationAPI.connectionAPI()");

        //Verificação se há necessidade de pedir autorização para uso do Location;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            checkLocationPermission();
        }
        //Configurações para a conexão da API LOCATION;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //Método para desconectar a API da aplicação, após o seu uso;
    public void desconnectAPI() {

        //Para desconectar a API;
        mGoogleApiClient.disconnect();
    }

    public LatLng getCoordenadasAtuais() {

        Log.i("Script", "LocationAPI.getCoordenadasAtuais()");

        coordenadasAtual = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        //Retornamos ao resultado a coordenada atual do usuário;
        return coordenadasAtual;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i("Script", "LocationAPI.onConnected()");

        //Instanciamos o Location Request para os parametros de requisição;
        mLocationRequest = new LocationRequest();
        //Determinamos o intervalo para cada requisição (1 seg = 1000 milisegundos);
        mLocationRequest.setInterval(1000);
        //Determinamos o intervalo acelerado para cada requisição (1 seg = 1000 milisegundos);
        mLocationRequest.setFastestInterval(1000);
        //Determinamos a prioridade que o aparelho irá intepretar esta tarefa;
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Verificamos se as permissões de acesso estão de acordo com a solicação atual
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Log.i("Script", "LocationAPI.onConnected("+ mLastLocation +")");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation == null) {

                Log.i("Script", "LocationAPI.onConnectednull("+ mLastLocation +")");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
            } else {

                Log.i("Script", "LocationAPI.onConnectedok("+ mLastLocation +")");
                onLocationChanged(mLastLocation);
            }

            //Ativamos a localização em tempo real (Ponto azul)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i("Script", "LocationAPI.onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i("Script", "LocationAPI.onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("Script", "LocationAPI.onLocationChanged()");

        //Recebe a localização que foi dada anteriormente;
        mLastLocation = location;
        Log.i("Script", "LocationAPI.onLocationChanged("+ mLastLocation +")");

        //E paramos as atualizações de localização, caso a API seja desligada;
        if (mGoogleApiClient != null) {

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.i("Script", "LocationAPI.onLocationChangedok("+ mLastLocation +")");
        }
    }

    //Solicitação da permissão do uso de GPS;
    public boolean checkLocationPermission() {

        //Registramos o evento no Android Monitor;
        Log.i("Script", "LocationAPI.checkLocationPermission()");

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;

        } else {

            return true;
        }
    }
}
