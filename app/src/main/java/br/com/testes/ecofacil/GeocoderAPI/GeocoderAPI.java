package br.com.testes.ecofacil.GeocoderAPI;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by samue on 24/04/2018.
 */

public class GeocoderAPI {

    //Contexto da aplicação;
    Context context;

    //Variáveis globais;
    LatLng coordenadasAtual;

    //Composição do endereco;
    private String enderecoCompleto;
    private String enderecoRua, enderecoNumero, enderecoBairro, enderecoCidade, enderecoEstado, enderecoPais, enderecoCEP;
    //Variável que receberá o resultado do método da API;
    Address addressGeocoder;

    //Método Construtor;
    public GeocoderAPI(Context context, LatLng coordenadas) {

        //Atribuimos as configurações da aplicação;
        this.context = context;
        //Atribuímos as coordenadas a classe;
        this.coordenadasAtual = coordenadas;
    }

    //Método para utilização do geocoder;
    public void activateGeocoder() {

        //Retorna seus dados da localização atual (Incluindo pais);
        Geocoder gc = new Geocoder(context);

        //String que receberá o address resultante;
        List<Address> addressResult = null;

        //Iniciamos o processo de requisição, podendo ocorrer erros no processo, por isso do try;
        try {

            //Variável receberá o endereco de acordo com as coordenadas atuais, tendo um resultado como foco;
            addressResult = gc.getFromLocation(coordenadasAtual.latitude, coordenadasAtual.longitude, 1);

            //nomeEndereco + "," + numeroEndereco + " - " + bairroEndereco + ", " + cidadeEndereco + " - " + estadoEndereco + ", " + cepEndereco;

            //Pega o nome da rua, cidade, estado. - getThroughFare retorna o nome da rua ao invés da avenida;
            enderecoCompleto =
                    addressResult.get(0).getThoroughfare().toString() + ", " +
                            addressResult.get(0).getSubThoroughfare().toString() + " - " +
                            addressResult.get(0).getSubLocality().toString() + ", " +
                            addressResult.get(0).getLocality().toString() + " - " +
                            addressResult.get(0).getAdminArea().toString() + ", " +
                            addressResult.get(0).getCountryCode().toString() + ", " +
                            addressResult.get(0).getPostalCode().toString();

            //Strings informando partes do endereco;
            enderecoRua = addressResult.get(0).getThoroughfare();
            enderecoNumero = addressResult.get(0).getSubThoroughfare();
            enderecoBairro = addressResult.get(0).getSubLocality();
            enderecoCidade = addressResult.get(0).getLocality();
            enderecoEstado = addressResult.get(0).getAdminArea();
            enderecoPais = addressResult.get(0).getCountryCode();
            enderecoCEP = addressResult.get(0).getPostalCode();

            //Retorno da única linha de address a variável global;
            addressGeocoder = addressResult.get(0);

            //Usando o último endereço do adress, conseguimos a localização em latitude e longetude;
            //LatLng llAtual = new LatLng(addressResult.get(0).getLatitude(), addressResult.get(0).getLongitude());
            //Exibimos as informações coletadas;
            //Toast.makeText(context, "Rua: " + addressResult + "\n Latitude: " + llAtual.latitude + "\n Longetude: " + llAtual.longitude, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Método para recebimento do address
    public String retornoGeocoder(String escolha) {

        //Verificamos qual a String a ser retornada;
        if (escolha.equals("enderecoCompleto")) {

            return enderecoCompleto;
        } else
        if (escolha.equals("enderecoRua")) {

            return enderecoRua;
        }else
        if (escolha.equals("enderecoNumero")) {

            return enderecoNumero;
        }else
        if (escolha.equals("enderecoBairro")) {

            return enderecoBairro;
        }else
        if (escolha.equals("enderecoCidade")) {

            return enderecoCidade;
        }else
        if (escolha.equals("enderecoEstado")) {

            return enderecoEstado;
        }else
        if (escolha.equals("enderecoPais")) {

            return enderecoPais;
        }else
        if (escolha.equals("enderecoCEP")) {

            return enderecoCEP;
        }else {

            return "String de recurso informada não bate com as configurações";
        }
    }

    public Address retornoAddress() { return addressGeocoder; }

    public String retornoEnderecoCompleto() {

        Toast.makeText(context, "Endereco Atual: " + enderecoCompleto, Toast.LENGTH_LONG).show();
        return enderecoCompleto;
    }

    public String retornoEnderecoRua() {
        return enderecoRua;
    }

    public String retornoEnderecoNumero() {
        return enderecoNumero;
    }

    public String retornoEnderecoBairro() {
        return enderecoBairro;
    }

    public String retornoEnderecoCidade() {
        return enderecoCidade;
    }

    public String retornoEnderecoEstado() {
        return enderecoEstado;
    }

    public String retornoEnderecoPais() {
        return enderecoPais;
    }

    public String retornoEnderecoCEP() {
        return enderecoCEP;
    }
}
