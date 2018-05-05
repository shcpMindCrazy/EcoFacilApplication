package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.testes.ecofacil.AsyncTasks.AsyncResponse;
import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.AvaliacaoContribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.Reciclador;
import br.com.testes.ecofacil.ObjetoTransferencia.RecicladorEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.Solicitacao;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;
import br.com.testes.ecofacil.R;

public class ContribuinteAcompanharActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        AsyncResponse,
        Transaction{

    //Componentes AsyncTask
    //Volley - Requisitos para conexão;
    String metodoNetworkConnection = "";
    String urlConnection = "";
    String messageResult = "";
    boolean solicitacaoStatus = false;
    int quantMateriaisIndex = 0;

    //Componentes do Google Maps;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99; //Instanciamos esta variável para perminssão da Requisição de Localicação
    private SupportMapFragment supMap; //Instanciamos o supportMapFragment para versões anteriores do Android;
    private GoogleApiClient mGoogleApiClient; //Instanciamos as configurações de API;
    private GoogleMap mGoogleMap; //Instanciamos o mapa de Google API;
    private MapView mapView;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private int recicladorAvaliado;
    private String descricaoAvaliacao;
    private double estrelasAvaliacao;
    private List<SolicitacaoMateriais> mSolicitacaoMateriaisList;
    boolean verificacaoDestinoInicial = true;
    boolean avaliacaoPopUp = true;

    private List<LatLng> listLocalizacao = new ArrayList<LatLng>(); //Instanciamos esta list que irá carregar as informações dos pontos iniciais e finais;
    private Polyline polyline; //Instanciamos este Polyline para calculo da rota e desenho da mesma;
    private long distance; //Instanciamos esta variavel para calculo de distancia do ponto inicial e final;

    ArrayList<String> enderecosCadastrados = new ArrayList();
    ArrayList<LatLng> coordenadasCadastradas = new ArrayList();

    LatLng currLatLng;
    Marker currLocationMarker;
    LatLng destLatLng;
    Marker destLocationMarker;
    String destinoLougradouro;

    //Componentes Visuais;
    ProgressBar mProgressBar;

    LatLng latlngInformado;
    Marker destinationMarker;
    LatLng latlngSolicitacao;
    Marker solicitacaoMarker;

    ArrayList<String> enderecos = new ArrayList<String>();
    ArrayList<LatLng> coordenadas = new ArrayList<LatLng>();

    //Variaveis globais;
    private String servicoSolicitado;
    private String action;
    private Contribuinte contribuinteAtual;
    private String enderecoAtual;
    private Reciclador recicladorAtual;

    private Solicitacao solicitacaoAtual = new Solicitacao();
    private SolicitacaoMateriais solicitacaoMaterial;
    private List<SolicitacaoMateriais> solicitacaoMateriaisList;

    List<RecicladorEndereco> recicladorEnderecos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doador_acompanhar);

        //Logcat
        Log.i("Script", "ContribuinteAcompanharActivity.onCreate()");

        /*
            INFORMAÇÕES TRANSMITIDAS PELA ACTIVITY DE SOLICITAÇÃO;
         */

        Intent intentResult = getIntent();
        Bundle bundleResult = intentResult.getBundleExtra("Bundle");

        if (bundleResult != null) {

            contribuinteAtual = (Contribuinte) bundleResult.getSerializable("Usuario");
            solicitacaoAtual = (Solicitacao) bundleResult.getSerializable("Solicitacao");
            enderecoAtual = bundleResult.getString("Endereco");
            solicitacaoMateriaisList = (ArrayList<SolicitacaoMateriais>) bundleResult.getSerializable("Materiais");
            servicoSolicitado = bundleResult.getString("Serviço");

            //Log.i("Script", "ContribuinteAcompanharActivity.onCreate( " +
            //        "\n Action: " + action +
            //        "\n doador atual: " + contribuinteAtual.getNomeContribuinte() +
            //        "\n endereco atual: " + enderecoAtual + ")");
        }

        /*

            PERMISSÃO DE VERSÃO PARA LOCALIZAÇÃO;
         */

        //Verificação de permissão de acesso para versões de android atualizadas;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            //Registramos o evento no Android Monitor;
            Log.i("Script", "ContribuinteAcompanharActivity.KITKAT>");
            //Iniciamos um procedimento para solicitar a permissão de localização do android;
            checkLocationPermission();
        }

        //Instanciamos o componente do progress bar;
        //mProgressBar = (ProgressBar) findViewById(R.id);

        //Atribuímos o método a ser executado pelo Volley;
        metodoNetworkConnection = "Consultar recicladores";
        //Atribuímos o link de conexão a ser executado;
        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarRecicladorEnderecos.php";
        //Iniciamos a conexão volley;
        callVolleyRequest(urlConnection);
        //Rotina atrasada para manipulação das informações volley;
        try {

            Thread.sleep(2000);
            //Instanciamos o viewer do mapa
            mapView = (MapView) findViewById(R.id.mapView);
            //Iniciamos o viewer;
            mapView.onCreate(savedInstanceState);
            //Inicia a renderização do mapa usando os parametros do OnMapReady
            mapView.getMapAsync(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void consultarHoraDataAtual() throws ParseException {

        Log.i("Script", "ContribuinteSolicitacaoActivity.consultarHoraDataAtual()");
        //Processo para criação e requisição da data e hora atual;
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss"); //Formato a ser tratado;
        Date date = new Date(); //Date util pelos dados atuais do sistema, já que o construtor está vazio;
        String dataFormatada = dateFormat.format(date); //String de teste;
        solicitacaoAtual.setFinalSolicitacao(dateFormat.format(date)); //String de teste;

        Log.i("Script", "ContribuinteSolicitacaoActivity.consultarHoraDataAtual("+ dataFormatada + ")");
        Log.i("Script", "ContribuinteSolicitacaoActivity.consultarHoraDataAtual("+ solicitacaoAtual.getInicioSolicitacao() + ")");
    }

    //Método para carregar o AlertDialog para avaliação do contribuinte;
    public void mostrarAvaliacaoReciclador() {

        //Verificamos se já foi notificado por esta tela;
        if (avaliacaoPopUp) {

            //Desativamos a proxima abertura nessa mesma tela;
            avaliacaoPopUp = false;

            //Instanciamos este view contendo o ScrollView em outro layout.xml
            View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_avaliacao_contribuinte, null);

            //Instanciamos os componentes do AlertDialog da avaliação;
            final TextView tvAvaliacaoContribuinteTitulo = (TextView) dialogView.findViewById(R.id.tvAvaliacaoContribuinteTitulo);
            final ImageView ivAvaliacaoContribuinteIcone = (ImageView) dialogView.findViewById(R.id.ivAvaliacaoContribuinteIcone);
            final TextView tvAvaliacaoContribuinteTitulo2 = (TextView) dialogView.findViewById(R.id.tvAvaliacaoContribuinteTitulo2);
            final RatingBar rbAvaliacaoContribuinteEstrelas = (RatingBar) dialogView.findViewById(R.id.rbAvaliacaoContribuinteEstrelas);
            final TextView tvAvaliacaoContribuinteTitulo3 = (TextView) dialogView.findViewById(R.id.tvAvaliacaoContribuinteTitulo3);
            final EditText etAvaliacaoContribuinteDescricao = (EditText) dialogView.findViewById(R.id.etAvaliacaoContribuinteDescricao);

            //Criamos uma dialog para confirmação do usuário;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false); //Impede que a Dialog seja cancelada com miss click.
            builder.setView(dialogView); //Atribuímos o ScrollView;
            builder.setTitle("Por último... não se esqueça:")
                    .setPositiveButton("Avaliar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Log.i("Script", "RecicladorSolicitacaoActivity.mostrarAvaliacaoContribuinte(onClick - Positive)");

                            //Verificamos se ao menos as estrelas foram dadas ao usuário;
                            if (rbAvaliacaoContribuinteEstrelas.getRating() > 0) {

                                //Verificamos se o texto de descrição foi dado;
                                if (etAvaliacaoContribuinteDescricao.getText().toString().trim().equals("")) {

                                    //Atribuímos o texto para ser aplicado ao banco de dados;
                                    etAvaliacaoContribuinteDescricao.setText("Sem descrições");
                                }

                                //Capturamos as informações dadas na AlertDialog;
                                recicladorAvaliado = contribuinteAtual.getIdContribuinte();                    //Id do contribuinte;
                                descricaoAvaliacao = etAvaliacaoContribuinteDescricao.getText().toString();     //Descrição do reciclador;
                                estrelasAvaliacao = rbAvaliacaoContribuinteEstrelas.getRating();              //Estrelas dadas ao contribuinte;

                                Log.i("Script", "RecicladorSolicitacaoActivity.mostrarAvaliacaoContribuinte(onClick - " +
                                        "Contribuinte: " + recicladorAvaliado + "| Descricao: " + descricaoAvaliacao + "| Estrelas: " + estrelasAvaliacao + ")");

                                //Atribuímos o método a ser rexecutado na conexão Volley;
                                metodoNetworkConnection = "Solicitacao - Finalizado";
                                //Atribuímos o link do servidor Volley;
                                urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/AlterarSolicitacaoFinalizada.php";
                                //Iniciamos a conexão Volley;
                                callVolleyRequest(urlConnection);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.i("Script", "RecicladorSolicitacaoActivity.mostrarAvaliacaoContribuinte( callVolleyRequest )");

                                        //Atribuímos o método a ser rexecutado na conexão Volley;
                                        metodoNetworkConnection = "Solicitacao - Avaliar";
                                        //Atribuímos o link do servidor Volley;
                                        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/InserirAvaliacaoReciclador.php";
                                        //Iniciamos a conexão Volley;
                                        callVolleyRequest(urlConnection);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                Toast.makeText(getApplicationContext(), "Serviço prestado com sucesso, obrigado por escolher a Ecofácil", Toast.LENGTH_SHORT).show();

                                                //E por último, voltamos a tela de Reciclador;
                                                Intent intentContribuinte = new Intent(ContribuinteAcompanharActivity.this, ContribuinteActivity.class);
                                                intentContribuinte.putExtra("Contribuinte", contribuinteAtual);
                                                startActivity(intentContribuinte);
                                            }
                                        }, 2000);

                                    }
                                }, 2000);
                            } else {

                                Toast.makeText(getApplicationContext(), "Para avaliação, é necessário a aplicação de ao menos uma estela.", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();

                                avaliacaoPopUp = true;
                                mostrarAvaliacaoReciclador();
                            }
                        }
                    })
                    .setNegativeButton("Não avaliar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Log.i("Script", "RecicladorSolicitacaoActivity.mostrarAvaliacaoContribuinte(onClick - Negative)");

                            //Voltamos a tela de Reciclador após a recusa da avaliação;
                            Intent intentContribuinte = new Intent(ContribuinteAcompanharActivity.this, ContribuinteActivity.class);
                            intentContribuinte.putExtra("Usuario", contribuinteAtual);
                            startActivity(intentContribuinte);
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    class TransformarCoordenadas extends AsyncTask<String, String, LatLng> {

        @Override
        protected LatLng doInBackground(String... params) {

            try {

                String link = params[0].replaceAll(" ", "+");
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

                latlngInformado = resultCoordenadas;

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
        protected void onPostExecute(LatLng msg) {

            Log.i("Script", "MapsFragment.onPostExecute(Finish)");

        }

    }

    public void clickButtonCancelarSolicitacao(View view) {

        //Criamos uma rotina de caixa de alerta
        new AlertDialog.Builder(this)
                //Atribuímos um titulo;
                .setTitle("Deseja mesmo cancelar a solicitação?")
                //Atribuímos uma mensagem abaixo;
                .setMessage("Isso fará com que o Coletor tenha de interromper seu trajeto. Tem certeza?")
                //Atribuímos uma opção negativa;
                .setNegativeButton("Não", null)
                //Atribuímos uma opção positiva;
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    //Método quando a ação for positiva;
                    public void onClick(DialogInterface arg0, int arg1) {

                        Toast.makeText(getApplicationContext(), "Solicitação cancelada pelo usuário", Toast.LENGTH_LONG);

                        //Ativamos o comportamento default do back key;
                        Intent intentLogin = new Intent(ContribuinteAcompanharActivity.this, ContribuinteActivity.class);
                        startActivity(intentLogin);
                    }
                    //Iniciamos toda a rotina e mostramos na tela;
                }).create().show();
    }

    public void iniciarAvaliacaoServico(View view) {

        if (servicoSolicitado.contains("Coletor")) {

            //Logcat
            Log.i("Script", "ContribuinteAcompanharActivity.onCreate.Coletor");
            iniciarAvaliacaoColetor();

        } else if (servicoSolicitado.contains("Ponto")) {

            //Logcat
            Log.i("Script", "ContribuinteAcompanharActivity.onCreate.Ponto");
            iniciarAvaliacaoPontoRecliclador();

        }
    }

    public void iniciarAvaliacaoColetor() {

        Log.i("Script", "ContribuinteAcompanharActivity.avaliarPontoReciclador()");

        final Dialog rankDialog = new Dialog(this);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText("Avalie o Coletor");

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Script", ratingBar.getRating() + "");

                Toast.makeText(getApplicationContext(), "O coletor foi avaliado com " + ratingBar.getRating() + " estrelas", Toast.LENGTH_LONG);
                //RatingBar ratebar = (RatingBar) findViewById(R.id); // The RatingBar in the ListView.
                //ratebar.setRating(ratingBar.getRating()); // Change the rating.
                rankDialog.dismiss();
            }
        });
        rankDialog.show();

    }

    public void iniciarAvaliacaoPontoRecliclador() {

        Log.i("Script", "ContribuinteAcompanharActivity.avaliarPontoReciclador()");

        final Dialog rankDialog = new Dialog(this);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText("Avalie o Ponto Reciclador:");

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Script", ratingBar.getRating() + "");

                Toast.makeText(getApplicationContext(), "O Ponto Reciclador foi avaliado com " + ratingBar.getRating() + " estrelas", Toast.LENGTH_LONG);
                //RatingBar ratebar = (RatingBar) findViewById(R.id); // The RatingBar in the ListView.
                //ratebar.setRating(ratingBar.getRating()); // Change the rating.
                rankDialog.dismiss();
            }
        });
        rankDialog.show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i("Script", "ContribuinteAcompanharActivity.onMapReady()");

        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (ContextCompat.checkSelfPermission(this,
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


    //Resultado do AsyncTaskEndereco;
    @Override
    public void processFinish(LatLng output) {

        Log.i("Script", "ContribuinteAcompanharActivity.processFinish( " + output + " )");
        coordenadas.add(output);

        if (coordenadas.size() > 2) {

            Log.i("Script", "ContribuinteAcompanharActivity.processFinish( Finalizado )");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i("Script", "ContribuinteAcompanharActivity.onConnected()");

        //Instanciamos o Location Request para os parametros de requisição;
        mLocationRequest = new LocationRequest();
        //Determinamos o intervalo para cada requisição (1 seg = 1000 milisegundos);
        mLocationRequest.setInterval(1000);
        //Determinamos o intervalo acelerado para cada requisição (1 seg = 1000 milisegundos);
        mLocationRequest.setFastestInterval(1000);
        //Determinamos a prioridade que o aparelho irá intepretar esta tarefa;
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Verificamos se as permissões de acesso estão de acordo com a solicação atual
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null) {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
            } else {

                onLocationChanged(mLastLocation);
            }

            //Ativamos a localização em tempo real (Ponto azul)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i("Script", "ContribuinteAcompanharActivity.onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i("Script", "ContribuinteAcompanharActivity.onConnectionFailed()");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("Script", "ContribuinteAcompanharActivity.onLocationChanged()");

        //Recebe a localização que foi dada anteriormente;
        mLastLocation = location;
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }

        //Colocamos o marcador onde o GPS apontou inicialmente
        currLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        Log.i("Fudeu", "Coordenada: "+ currLatLng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currLatLng);
        markerOptions.title("Você");
        markerOptions.snippet("Solicitando um serviço...");
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.));
        currLocationMarker = mGoogleMap.addMarker(markerOptions);

        //Android monitor annotation;
        Log.i("Script.MapsAPI", "MapsFragment.onLocationChanged('Marcador')");

        //E paramos as atualizações de localização, caso a API seja desligada;
        if (mGoogleApiClient != null) {

            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            //Android monitor annotation;
            //Log.i("Script.MapsAPI", "MapsFragment.onLocationChanged('Location API') " + listLocalizacao.get(0));

            try {

                if (servicoSolicitado.contains("Ponto")) {

                    if (mLastLocation != null && currLatLng != null && verificacaoDestinoInicial) {

                        //É comparado as coordenadas em metros e depois é a escolhida a mais proxima;
                        buscarPontoProximo();
                        verificacaoDestinoInicial = false;
                    }

                    if (mLastLocation != null && listLocalizacao.isEmpty() == false) {

                        //Android monitor annotation;
                        Log.i("Script.MapsAPI", "MapsFragment.onLocationChanged('Desenha rota atualizada')");

                        getRouteByGMAV2(currLatLng, destLatLng);
                        //getDistance();
                    }
                }
                else if (servicoSolicitado.contains("Coletor")) {

                    if (mLastLocation != null && currLatLng != null && verificacaoDestinoInicial) {

                        //É comparado as coordenadas em metros e depois é a escolhida a mais proxima;
                        solicitarColetor();
                        verificacaoDestinoInicial = false;
                    }

                    if (mLastLocation != null && listLocalizacao.isEmpty() == false) {

                        //Android monitor annotation;
                        Log.i("Script.MapsAPI", "MapsFragment.onLocationChanged('Desenha rota atualizada')");

                        getRouteByGMAV2(latlngSolicitacao, currLatLng);
                        //getDistance();
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {

        Log.i("Script", "ContribuinteAcompanharActivity.onLocationChanged()");
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {

        Log.i("Script", "ContribuinteAcompanharActivity.onLocationChanged()");
        super.onPause();
        mGoogleApiClient.disconnect();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {

        //Android monitor annotation;
        Log.i("Script", "ContribuinteAcompanharActivity.onDestroy()");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {

        //Android monitor annotation;
        Log.i("Script", "ContribuinteAcompanharActivity.onLowMemory()");
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void buscarPontoProximo() {

        Log.i("Script", "MapsFragment.buscarPontoProximo()");

        //Tratamos os enderecos para coordenadas;
        for (int i = 0; i < recicladorEnderecos.size(); i++) {

            Log.i("Script", "MapsFragment.buscarPontoProximo(Coordenadas)");

            //Tranformamos este endereco em coordenadas;
            new TransformarCoordenadas().execute(recicladorEnderecos.get(i).toString());

            try {
                //Pausamos a atividade principal, para receber as converções;
                Thread.sleep(2000);

                //Adicionamos os respectivos enderecos para coordenadas;
                coordenadasCadastradas.add(latlngInformado);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

        //Iniciamos o método onde será selecionado o id correspondente ao ponto mais próximo encontrado;
        int idCoordenada = distanceCompare(coordenadasCadastradas);

        //Coordenadas correspondentes ao local de destino mais proximo;
        destLatLng = coordenadasCadastradas.get(idCoordenada);

        //Instanciar um marcador do local de destino;
        destLocationMarker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .title("Ponto reciclador")
                        .position(destLatLng)
                        .snippet("Sua entrega será aqui")
        );

        //LatLngBounds cameraMov = new LatLngBounds(
           //     new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
         //       new LatLng(destLatLng.latitude, destLatLng.longitude));
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraMov.getCenter(), 1));

        LatLng cameraMov = new LatLng(destLatLng.latitude, destLatLng.longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(cameraMov)      // Sets the center of the map to Mountain View
                .zoom(12)                   // Sets the zoom
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //Se a localização atual já estiver sido requisitada...
        if (mLastLocation != null) {

            //Traçamos a list de localização para o drawroute();
            listLocalizacao = new ArrayList<>();
            listLocalizacao.add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            listLocalizacao.add(destLatLng);
        }
    }

    public void solicitarColetor() {

        Log.i("Script", "MapsFragment.buscarPontoProximo()");

        //Tranformamos este endereco em coordenadas;
        new TransformarCoordenadas().execute(enderecoAtual);

        try {
            //Pausamos a atividade principal, para receber as converções;
            Thread.sleep(2000);

            //Adicionamos os respectivos enderecos para coordenadas;
            latlngSolicitacao = latlngInformado;
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        //Coordenadas correspondentes ao local de destino mais proximo;
        //Instanciar um marcador do local de destino;
        solicitacaoMarker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .title("Pedido solicitado")
                        .position(latlngSolicitacao)
                        .snippet("O coletor virá buscar os recicláveis nesta localização")
        );

        LatLng cameraMov = new LatLng(latlngSolicitacao.latitude, latlngSolicitacao.longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(cameraMov)      // Sets the center of the map to Mountain View
                .zoom(12)                   // Sets the zoom
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //Solicitação do Coletor mais próximo;


        //Se a localização atual já estiver sido requisitada...
        if (mLastLocation != null) {

            //Traçamos a list de localização para o drawroute();
            listLocalizacao = new ArrayList<>();
            listLocalizacao.add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())); //Coletor mais proximo
            listLocalizacao.add(latlngSolicitacao);


        }
    }

    public int distanceCompare(ArrayList<LatLng> coordenadas) {

        //A maior distância possível em uma viagem;
        double distancia = 3000000;
        //Id correspondente ao index da coordenada mais proxima;
        int idCoordenada = 0;

        //Verificamos todos as coordenadas;
        for (int i = 0, tam = coordenadas.size(); i < tam; i++) {

            //Verificamos a distância do localização atual para o destino atual;
            double distanceToMarker = distance(currLatLng, coordenadas.get(i));

            //Se este for menor que o informado anteriormente;
            if (distancia < distanceToMarker) {

                //Informamos a distância menor nova;
                distancia = distanceToMarker;
                //E o id correspondente do vetor;
                idCoordenada = i;
            }
        }

        //Retornamos o id correspondente ao ponto mais proximo;
        return idCoordenada;
    }

    /*
   ************************************************************************************************
   *************  Métodos para configuração de rota, calculo de curvas e distância ****************
   ************************************************************************************************
   */
    //Clicar e pintar a rota - Para retornar distancia e o local;
    public void drawRoute() {

        //Instanciamos as configurações de geolocalização
        PolylineOptions po;

        //Caso a linha esteja vazia, sem nenhum local definido
        if (polyline == null) {

            //Inicializamos as configurações de geolocalização;
            po = new PolylineOptions();

            //Adicionamos as localizações de acordo com as criadas
            for (int i = 0, tam = listLocalizacao.size(); i < tam; i++) {

                po.add(listLocalizacao.get(i));
            }

            //Determinamos uma cor para o trajeto;
            po.color(Color.BLUE);
            //Atribuimos estas configurações ao mapa;
            polyline = mGoogleMap.addPolyline(po);
        }

        //Se ja tivermos valores atribuidos...
        else {

            //Iremos apenas concatenar os valores
            polyline.setPoints(listLocalizacao);

        }
    }

    //Retornamos a distancia dada pelo click atual;
    public void getDistance() {

        double distanceInterna = 0;

        //for (int i = 0, tam = coordenadasCadastradas.size(); i < tam; i++) {

        //if (i < tam - 1) {

        distanceInterna = distance(currLatLng, destLatLng);

        if (distanceInterna < 21) {

            Toast.makeText(this, "Distancia: " + distanceInterna + " metros - Você chegou", Toast.LENGTH_LONG).show();

            mGoogleApiClient.disconnect();

            mostrarAvaliacaoReciclador();
        }


        Toast.makeText(this, "Distancia: " + distanceInterna + " metros", Toast.LENGTH_LONG).show();
    }

    //Método matemático para o getDistance() para calcular os metros de um ponto para o outro;
    public static double distance(LatLng StartP, LatLng EndP) {

        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;

        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
    }

    public void getLocation(View view) {

        //Retorna seus dados da localização atual (Incluindo pais);
        Geocoder gc = new Geocoder(this);

        //Sempre retornando a última localização disponivel;
        List<Address> addressList = null;

        try {
            addressList = gc.getFromLocation(listLocalizacao.get(listLocalizacao.size() - 1).latitude,
                    listLocalizacao.get(listLocalizacao.size() - 1).longitude,
                    1);

            //Pega o nome da rua, cidade, estado. - getThroughFare retorna o nome da rua ao invés da avenida;
            String address = addressList.get(0).getThoroughfare() + "\n";
            address += "Cidade: " + addressList.get(0).getLocality() + "\n";
            address += "Estado: " + addressList.get(0).getAdminArea() + "\n";
            address += "Pais: " + addressList.get(0).getCountryName();

            //Usando o último endereço do adress, conseguimos a localização em latitude e longetude;
            LatLng llAtual = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());

            //Exibimos as informações coletadas;
            Toast.makeText(this, "Rua: " + address + "\n Latitude: " + llAtual.latitude + "\n Longetude: " + llAtual.longitude, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRouteByGMAV2(LatLng originCoordenadas, LatLng destinationCoordenadas) throws UnsupportedEncodingException {

        //Relátorio de execução deste evento - Android Monitor;
        Log.i("Script", "MapsFragment.getRouteByGMAV2()");

        //Capturamos a nossa localização atual;
        String originLatitude = String.valueOf(originCoordenadas.latitude);
        String originLongetude = String.valueOf(originCoordenadas.longitude);
        //Capturamos a localização do hospital mais próximo;
        String destinationLatitude = String.valueOf(destinationCoordenadas.latitude);
        String destinationLongetude = String.valueOf(destinationCoordenadas.longitude);
        //Coletamos as informações e as codificamos em formato URL para que o Google Maps compreenda;
        String origin = URLEncoder.encode(originLatitude + "," + originLongetude, "UTF-8");
        String destination = URLEncoder.encode(destinationLatitude + "," + destinationLongetude, "UTF-8");
        //Iniciamos a representação no Google Map através desse método;
        getRoute(origin, destination);
        //getRouteCoordenadas(new LatLng(-23.535682, -46.325543), new LatLng(-23.523248, -46.303227));
    }

    //Conexão WEB - Por Latitude e Longetude;
    public void getRouteCoordenadas(final LatLng origin, final LatLng destination) {

        new Thread() {

            public void run() {

                //Url do Google maps para realizar a requisição;
                String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" +
                        origin.latitude + "," + origin.longitude + "&destination=" +
                        destination.latitude + "," + destination.longitude + "&sensor=false";

                //Abre os parâmetros e objetos com suas configurações;
                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");
                request = new HttpGet(url);

                /* Trecho para a coleta de configurações de COOKIES - Erro de HTTP context not found */
                // Create a local instance of cookie store
                CookieStore cookieStore = new BasicCookieStore();
                // Create local HTTP context
                HttpContext localContext = new BasicHttpContext();
                // Bind custom cookie store to the local context
                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

                try {

                    //Abrimos a conexão por este lado;
                    response = client.execute(request, localContext);
                    //Transformamos a resposta (Response) em uma String;
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                listLocalizacao = buildJSONRoute(answer);
                                //drawRoute();
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (client != null) {
                        try {
                            if (client instanceof AndroidHttpClient) {
                                ((AndroidHttpClient) client).close();
                            }
                        } catch (Exception e) {
                            // Ignore
                        }
                    }
                }
            }
        }.start();
    }

    //Conexão WEB - Por nome das Ruas;
    public void getRoute(final String origin, final String destination) {

        new Thread() {

            public void run() {

                //Url do Google maps para realizar a requisição;
                String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" +
                        origin + "&destination=" +
                        destination + "&sensor=false";

                //Abre os parâmetros e objetos com suas configurações;
                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");
                request = new HttpGet(url);

                /* Trecho para a coleta de configurações de COOKIES - Erro de HTTP context not found */
                // Create a local instance of cookie store
                CookieStore cookieStore = new BasicCookieStore();
                // Create local HTTP context
                HttpContext localContext = new BasicHttpContext();
                // Bind custom cookie store to the local context
                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

                try {

                    //Abrimos a conexão por este lado;
                    response = client.execute(request, localContext);
                    //Transformamos a resposta (Response) em uma String;
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                listLocalizacao = buildJSONRoute(answer);
                                drawRoute();
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (client != null) {
                        try {
                            if (client instanceof AndroidHttpClient) {
                                ((AndroidHttpClient) client).close();
                            }
                        } catch (Exception e) {
                            // Ignore
                        }
                    }
                }
            }
        }.start();
    }

    //Parser JSON
    public List<LatLng> buildJSONRoute(String json) throws JSONException {

        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        //O legs é um cara que retorna o inicio e o final do destino. Seja endereço extenso, como também suas coordenadas
        //O steps é os pontos até se obter a rota
        distance = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        List<LatLng> lines = new ArrayList<LatLng>();

        for (int i = 0; i < steps.length(); i++) {
            Log.i("Script", "STEP: LAT: " + steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat") + " LNG: " + steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));

            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for (LatLng p : decodePolyline(polyline)) {

                lines.add(p);
            }

            Log.i("Script", "STEP: LAT: " + steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat") + " LNG: " + steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
        }

        getDistance();

        return (lines);
    }

    //Decode Polyline - Seriam os diversos pontos de "pausa" entre origem e destino;
    private List<LatLng> decodePolyline(String encoded) {

        //Este são a lista de pontos da origem até o destino
        List<LatLng> listPoints = new ArrayList<LatLng>();

        //Estes recebem os dados para trabalhar os pontos durante a rota;
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {

            int b, shift = 0, result = 0;

            do {

                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {

                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));

            Log.i("Script", "POL : LAT: " + p.latitude + " | LNG:" + p.longitude);

            listPoints.add(p);
        }

        return listPoints;
    }

    //Método de conexão de API - Synchronized permite que a execução deste seja em fila, organizada e sem atroplelos;
    private synchronized void connectionAPI() {

        Log.i("LOG", "ContribuinteAcompanharActivity.connectionAPI()");

        //Configurações para a conexão da API LOCATION;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void exibirProgress(boolean exibir) {

        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    /*

        MÉTODOS DO TRANSACTION

    */

    //Método de conexão Volley.
    public void callVolleyRequest(String url) {

        Log.i("Script", "FragmentChamados.callVolleyRequest()");

        //Iniciamos uma conexão direta com o Volley - Inserir Jogador;
        NetworkConnection.getInstance(getApplicationContext()).execute(
                this,
                ContribuinteAcompanharActivity.class.getName(),
                url);

    }

    //Método para desconectar o Volley;
    public void desconnectVolleyRequest() {

        Log.i("Script", "FragmentSolicitacaoMateriais.desconnectVolleyRequest()");
        //Desconectamos através do name da conexão;
        NetworkConnection.getInstance(this).getRequestQueue().cancelAll( ContribuinteAcompanharActivity.class.getName() );
    }

    //Método iniciado antes da execução do networkConnection
    @Override
    public WrapObjToNetwork doBefore() {

        Log.i("Script", "ContribuinteAcompanharActivity.doBefore()");
        //Verificamos qual o tipo de conexão a ser estabelecida;
        //Caso seja para alterar a solicitação em andamento;
        if (metodoNetworkConnection.contains("Consultar recicladores")) {

            Log.i("Script", "ContribuinteAcompanharActivity.doBefore(Consultar recicladores)");

            //Instanciamos o objeto a ser utilizado nessa consulta;
            RecicladorEndereco recicladorEndereco = new RecicladorEndereco();

            //Retornamos para a conexão os parâmetros para o Volley;
            return (new WrapObjToNetwork(
                    recicladorEndereco,
                    "java-web-jor",
                    urlConnection));
        }
        //Caso seja para encerramento deste serviço;
        else
        if (metodoNetworkConnection.contains("Solicitacao - Finalizado")) {

            Log.i("Script", "RecicladorSolicitacaoActivity.doBefore(Finalizado)");

            //Instanciamos o objeto a ser utilizado nessa consulta;
            solicitacaoAtual = new Solicitacao();
            //Atribuímos o id da solicitacao através do fkSolicitacao dos materiais;
            solicitacaoAtual.setIdSolicitacao(solicitacaoMateriaisList.get(0).getFkSolicitacao());
            //Atribuímos o status de atendimento encerrado;
            solicitacaoAtual.setEstadoAtualSolicitacao("Atendimento finalizado");
            //Pegamos a data e hora atual do encerramento desta solicitação;
            try {
                consultarHoraDataAtual();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Retornamos para a conexão os parâmetros para o Volley;
            return (new WrapObjToNetwork(
                    solicitacaoAtual,
                    "java-web-jor",
                    urlConnection));

        }
        //Caso seja para encerramento deste serviço;
        else if (metodoNetworkConnection.contains("Solicitacao - Avaliar")) {

            Log.i("Script", "RecicladorSolicitacaoActivity.doBefore(Avaliar)");

            //Instanciamos o objeto a ser utilizado nessa consulta;
            AvaliacaoContribuinte avaliacaoContribuinte = new AvaliacaoContribuinte();
            //Atribuímos o id da solicitacao atual;
            avaliacaoContribuinte.setFkSolicitacao(solicitacaoAtual.getIdSolicitacao());
            //Atribuímos o id do contribuinte atual;
            avaliacaoContribuinte.setFkContribuinte(0);
            //Atribuimos a quantidade de estrelas informados no AlertDialog;
            avaliacaoContribuinte.setQuantStarsAvaliacaoContribuinte(estrelasAvaliacao);
            //Atribuímos a descricao dessa avaliacao dada no AlertDialog;
            avaliacaoContribuinte.setDescricaoAvaliacaoContribuinte(descricaoAvaliacao);

            //Retornamos para a conexão os parâmetros para o Volley;
            return (new WrapObjToNetwork(
                    avaliacaoContribuinte,
                    "java-web-jor",
                    urlConnection));
        } else {

            Log.i("Script", "RecicladorSolicitacaoActivity.doBefore(Nenhum)");
            return null;
        }
    }

    @Override
    public void doAfter(JSONArray jsonArray) {

        Log.i("Script", "ContribuinteAcompanharActivity.doAfter()");

        //Verificamos se a conexão retornou algum dado...
        if ( jsonArray != null) {

            Log.i("Script", "ContribuinteAcompanharActivity.doAfter( "+ jsonArray +" )");

            //Verificamos qual o tipo de conexão a ser estabelecida;
            //Caso seja para alterar a solicitação em andamento;
            if (metodoNetworkConnection.contains("Consultar recicladores")) {

                Log.i("Script", "ContribuinteAcompanharActivity.doAfter(Finalizado)");

                //Atribuímos a list que receberá todos os enderecos de recicladores cadastrados;
                recicladorEnderecos = new ArrayList<RecicladorEndereco>();

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {

                        //Objeto a receber o endereco;
                        RecicladorEndereco recicladorEndereco = new RecicladorEndereco();

                        //Instanciamos os valores do número da linha atual a ser tratada;
                        recicladorEndereco.setIdRecicladorEndereco(jsonArray.getJSONObject(i).getInt("idRecicladorEndereco"));
                        recicladorEndereco.setFkReciclador(jsonArray.getJSONObject(i).getInt("fkReciclador"));
                        recicladorEndereco.setNomeEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_lougradouro"));
                        recicladorEndereco.setNumeroEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_numero"));
                        recicladorEndereco.setBairroEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_bairro"));
                        recicladorEndereco.setCidadeEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_cidade"));
                        recicladorEndereco.setEstadoEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_estado"));
                        recicladorEndereco.setPaisEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_pais"));
                        recicladorEndereco.setCepEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_cep"));
                        recicladorEndereco.setcomplementoEnderecoReciclador(jsonArray.getJSONObject(i).getString("rce_complemento"));

                        //Adicionamos o valor na lista de enderecos;
                        recicladorEnderecos.add(recicladorEndereco);
                    }
                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            else
                //Caso seja para alterar a solicitação em andamento;
            if (metodoNetworkConnection.contains("Solicitacao - Finalizado")) {

                Log.i("Script", "RecicladorSolicitacaoActivity.doAfter(Finalizado)");

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    //Transferimos o resultado vindo do banco;
                    messageResult = jsonArray.getJSONObject(0).getString("resultQuery");
                    solicitacaoAtual.setIdSolicitacao(jsonArray.getJSONObject(0).getInt("idSolicitacao"));

                } catch (JSONException e) {

                    messageResult = "Erro";
                    e.printStackTrace();
                }
            }
            //Caso seja para encerramento deste serviço;
            else if (metodoNetworkConnection.contains("Solicitacao - Avaliar")) {

                Log.i("Script", "ContribuinteAcompanharActivity.doAfter(Avaliar)");

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    //Transferimos o resultado vindo do banco;
                    messageResult = jsonArray.getJSONObject(0).getString("resultQuery");
                    solicitacaoAtual.setIdSolicitacao(jsonArray.getJSONObject(0).getInt("idSolicitacao"));

                } catch (JSONException e) {

                    messageResult = "Erro";
                    e.printStackTrace();
                }
            }
            else {

                Log.i("Script", "ContribuinteAcompanharActivity.doAfter(Nenhum)");
            }

        } else {

            Log.i("Script", "ContribuinteAcompanharActivity.doAfter(Volley Problem)");
        }
    }

    /*
    *
    * MÉTODOS DE VALIDAÇÃO =========================================================================
    *
    * */

    //Solicitação da permissão do uso de GPS;
    public boolean checkLocationPermission() {

        //Registramos o evento no Android Monitor;
        Log.i("Script", "ContribuinteAcompanharActivity.checkLocationPermission()");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;

        } else {

            return true;
        }
    }
}
