package br.com.testes.ecofacil.AsyncTasks;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by samue on 17/03/2018.
 */

public interface AsyncResponse {

    void processFinish(LatLng output);
}
