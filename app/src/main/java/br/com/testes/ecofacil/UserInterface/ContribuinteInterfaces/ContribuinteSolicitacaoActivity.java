package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.GeocoderAPI.GeocoderAPI;
import br.com.testes.ecofacil.LocationAPI.LocationAPI;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.ContribuinteEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.Reciclador;
import br.com.testes.ecofacil.ObjetoTransferencia.RecicladorEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.Solicitacao;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;
import br.com.testes.ecofacil.R;
import br.com.testes.ecofacil.UserInterface.RecicladorInterfaces.RecicladorSolicitacaoActivity;

public class ContribuinteSolicitacaoActivity extends AppCompatActivity implements
        FragmentSolicitacaoMateriais.FragmentMateriaisListener,
        FragmentSolicitacaoEndereco.FragmentEnderecoListener,
        FragmentSolicitacaoEnderecoNovo.FragmentEnderecoNovoListener,
        Transaction {

    //Variaveis Globais;
    String enderecoEscolhido;
    String servicoSolicitado;
    String action;
    //Recicladores;
    private Reciclador recicladorAtual = new Reciclador();
    private List<Reciclador> recicladoresList = new ArrayList<Reciclador>();
    private RecicladorEndereco recicladorEndereco = new RecicladorEndereco();
    private List<RecicladorEndereco> recicladorEnderecoList = new ArrayList<RecicladorEndereco>();
    private List<LatLng> coordenadasRecicladorEnderecoList = new ArrayList<LatLng>();
    //Latlng
    private LatLng coordenadasAtual;
    //Volley - Requisitos para consulta;
    Contribuinte contribuinteAtual;
    public ContribuinteEndereco contribuinteEnderecoAtual;
    List<ContribuinteEndereco> contribuinteEnderecoList = new ArrayList<>();
    List<LatLng> coordenadasContribuinteEnderecoList = new ArrayList<>();
    LatLng latlngInformado;
    SolicitacaoMateriais solicitacaoMaterialAtual;
    List<SolicitacaoMateriais> solicitacaoMateriaisList;
    List<SolicitacaoMateriais> solicitacaoMateriaisListResult;
    Solicitacao solicitacaoAtual;
    String inicioSolicitacaoAtual;
    String estadoSolicitacaoAtual;
    //Volley - Requisitos para conexão;
    String metodoNetworkConnection = "";
    String urlConnection = "";
    String messageResult = "";
    boolean solicitacaoStatus = false;
    int quantMateriaisIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doador_solicitar);

        //Atribuímos as variáveis anteriores neste contexto;
        Intent intentResultContribuinte = getIntent();
        requestResourceBundle(intentResultContribuinte);

        //Inicializamos o fragment respectivo de acordo com a resposta do Bundle;
        initializeFragmentLayout();
    }
    /*

       Métodos para configuração dos componentes;

    */
    //Método para receber as variáveis da activity/fragment anterior;
    public void requestResourceBundle(Intent intentResult) {

        Log.i("Script", "ContribuinteActivity.requestResourceBundle("+ intentResult +");");
        Bundle bundleResultAccess = intentResult.getExtras();
        if (bundleResultAccess != null) {

            //Logcat
            Log.i("Script", "ContribuinteSolicitacaoActivity.onCreate( "+ bundleResultAccess +" - success )");

            //Atribuímos a variável de usuário desta activity;
            contribuinteAtual = (Contribuinte) bundleResultAccess.getSerializable("Usuario");
            servicoSolicitado = bundleResultAccess.getString("Servico");
            action = bundleResultAccess.getString("Action");

            Log.i("Script", "ContribuinteSolicitacaoActivity.onCreate( Usuário: "+ contribuinteAtual.getIdContribuinte() + " - Email: " + contribuinteAtual.getEmailContribuinte() +")");

        } else {

            //Logcat
            Log.i("Script", "ContribuinteActivity.onCreate( "+ bundleResultAccess +" - failed )");
        }
    }
    //Método para inicializar o fragment do bundle respectivo;
    public void initializeFragmentLayout() {

        // Pega o FragmentManager
        FragmentManager fm = getSupportFragmentManager();

        if (action.contains("materiais")) {

            Log.i("Script", "ContribuinteSolicitacaoActivity.initializeFragmentLayout( "+ action +")");

            //Instanciamos o mapsFragment com o Bundle das variáveis passadas;
            FragmentSolicitacaoMateriais materiaisFrag = new FragmentSolicitacaoMateriais();

            Bundle bundleEnderecos = new Bundle();
            bundleEnderecos.putString("Action", action);
            bundleEnderecos.putSerializable("Usuario", contribuinteAtual);
            materiaisFrag.setArguments(bundleEnderecos);

            //Abre uma transação e adiciona
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentSolicitacao, materiaisFrag); //Carregando os arguments;
            ft.commit();
        }
        else if (action.contains("endereco")) {

            Log.i("Script", "ContribuinteSolicitacaoActivity.initializeFragmentLayout( "+ action +")");

            //Instanciamos o mapsFragment com o Bundle das variáveis passadas;
            FragmentSolicitacaoEndereco enderecoFragment = new FragmentSolicitacaoEndereco();

            Bundle bundleEnderecos = new Bundle();
            bundleEnderecos.putString("Action", "materiais/confirmar");
            bundleEnderecos.putSerializable("Usuario", contribuinteAtual);
            enderecoFragment.setArguments(bundleEnderecos);

            //Abre uma transação e adiciona
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentSolicitacao, enderecoFragment); //Carregando os arguments;
            ft.commit();
        }

    }
    /*

        Métodos de rotina de execução;

    */
    public void recolherCoordenadasLatLng() {

        //Verificamos os enderecos cadastrados dos recicladores;
        for (int i = 0; i < recicladorEnderecoList.size(); i++) {

            Log.i("Script", "ContribuinteSolicitacaoActivity.recolherCoordenadasLatLng( "+ recicladorEnderecoList.get(i).toString() +")");

            final int ifinal = i;

            //Transformamos o endereco obtido
            new TransformarCoordenadas().execute(recicladorEnderecoList.get(i).toString());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //Adicionamos os respectivos enderecos para coordenadas;
                    coordenadasRecicladorEnderecoList.add(latlngInformado);

                    if (ifinal == (recicladorEnderecoList.size() - 1)) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                encontrarRecicladorMaisProximo();
                            }
                        }, 3000);

                    }
                }
            }, 3000);
        }
    }

    public void encontrarRecicladorMaisProximo() {

        //Tranferimos a LatLng atual para o método;
        LatLng coordenadas = coordenadasAtual;
        //Atribuímos a variável do ponto mais proximo;
        LatLng coordenadasReciclador = new LatLng(0,0);
        //Distância entre o ponto atual e o destino comparado;
        double distancia = 0;
        //Posição correspondente das Latlng mais proxima;
        int i_enderecoEscolhido = 0;

        for (int i = 0; i < coordenadasRecicladorEnderecoList.size(); i++) {

            double distanciaAtual = getDistance(coordenadas, coordenadasRecicladorEnderecoList.get(i));

            if (distanciaAtual > distancia) {

                distancia = distanciaAtual;
                i_enderecoEscolhido = i;
            }
        }

        //Após verificar e resultar o ponto mais proximo, atribuímos estes as variáveis atuais;
        recicladorAtual.setIdReciclador(recicladorEnderecoList.get(i_enderecoEscolhido).getFkReciclador());

        Log.i("Script", "ContribuinteSolicitacaoActivity.encontrarRecicladorMaisProximo("+ recicladorAtual.getIdReciclador() +")");
    }

    public void consultarHoraDataAtual() throws ParseException {

        Log.i("Script", "ContribuinteSolicitacaoActivity.consultarHoraDataAtual()");
        //Processo para criação e requisição da data e hora atual;
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss"); //Formato a ser tratado;
        Date date = new Date(); //Date util pelos dados atuais do sistema, já que o construtor está vazio;
        String dataFormatada = dateFormat.format(date); //String de teste;
        solicitacaoAtual.setInicioSolicitacao(dateFormat.format(date)); //String de teste;

        Log.i("Script", "ContribuinteSolicitacaoActivity.consultarHoraDataAtual("+ dataFormatada + ")");
        Log.i("Script", "ContribuinteSolicitacaoActivity.consultarHoraDataAtual("+ solicitacaoAtual.getInicioSolicitacao() + ")");
    }
    /*

        Métodos para Coordenadas;

    */
    //Método para transformar lougradouro para coordenadas;
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

                coordenadasRecicladorEnderecoList.add(resultCoordenadas);
                resultCoordenadas = latlngInformado;

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
    //Retornamos a distancia dada pelo click atual;
    public Double getDistance(LatLng atual, LatLng destino) {

        double distanceInterna = 0;

        distanceInterna = distance(atual, destino);

        return distanceInterna;
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
    /*

        Métodos para transferência/acesso a activity;

    */
    //Método para alteração do fragment SolicitacaoMateriais aplicado no frameLayout;
    //FragmentMateriais -> ROTINA
    @Override
    public void onTranferirIntentMateriais(String action) {

        Log.i("Script", "DadosSolicitarActivity.onTranferirIntentMateriais");

        Bundle args = new Bundle();
        args.putString("Action", action);
        args.putSerializable("Usuario", contribuinteAtual);
        args.putString("Endereco", enderecoEscolhido);
        args.putSerializable("Materiais", (Serializable) FragmentSolicitacaoMateriais.materiaisDoados);

        solicitacaoMateriaisList = FragmentSolicitacaoMateriais.materiaisDoados;

        /*args.putDouble("Material", FragmentSolicitacaoMateriais.quantidadeMaterialEscolhida);
        args.putString("TipoMaterial", FragmentSolicitacaoMateriais.tipoMaterialEscolhido);
        args.putString("TipoPesoMaterial", FragmentSolicitacaoMateriais.tipoPesoEscolhido);*/

        if (action.contains("endereco")) {

            FragmentSolicitacaoEndereco segundoFragment = new FragmentSolicitacaoEndereco();
            segundoFragment.setArguments(args);

            android.support.v4.app.FragmentTransaction transacao = getSupportFragmentManager().beginTransaction();
            transacao.replace(R.id.fragmentSolicitacao, segundoFragment);
            transacao.commit();
        }
        else if (action.contains("solicitacao")) {

            Log.i("Script", "DadosSolicitarActivity.onTranferirIntentMateriais("+ action +")");
            //Primeiro, capturamos os dados necessários para aplicação da solicitação;
            List<SolicitacaoMateriais> solicitacaoMateriaisList = FragmentSolicitacaoMateriais.materiaisDoados;

            //Caso esteja inserido o "PONTO", Será buscado a localização do ponto mais proximo;
            if (servicoSolicitado.contains("Ponto")) {

                //contribuinteAtual = FragmentSolicitacaoMateriais.contribuinteAtual;

                //Iniciamos a conexão com a api da google;
                LocationAPI locationAPI = new LocationAPI(getApplicationContext(), ContribuinteSolicitacaoActivity.this);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais(Pós Requisição)");

                        //Requisição do endereco atual em formato de string;
                        String enderecoAtual;
                        //Requisição das Coordenadas atuais (LATLNG)
                        LatLng coordenadas = locationAPI.getCoordenadasAtuais();
                        //Instanciamos as configurações do Geocoder em seguida, usando as coordenadas obtidas;
                        GeocoderAPI geocoderAPI = new GeocoderAPI(getApplicationContext(), coordenadas);
                        //O geocoder é ativado, fazendo a transformação da LatLng para Address;
                        geocoderAPI.activateGeocoder();
                        //Instanciamos o resultado para a string de endereco;
                        enderecoAtual = geocoderAPI.retornoEnderecoCompleto();
                        //Passamos o endereco para a variável a ser transferida ao objeto;
                        enderecoEscolhido = enderecoAtual;
                        coordenadasAtual = coordenadas;

                        Log.i("Script", "DadosSolicitarActivity.onTranferirIntentMateriais("+ enderecoAtual +")");

                        //Pesquisamos dentre os recicladores mais proximos;
                        metodoNetworkConnection = "Buscar reciclador mais proximo";
                        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarRecicladorEnderecos";
                        //Criamos a conexão volley;
                        callVolleyRequest(urlConnection);
                        //Rotina pós-resultado da conexão;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //Verificamos os enderecos cadastrados dos recicladores;
                                for (int i = 0; i < recicladorEnderecoList.size(); i++) {

                                    Log.i("Script", "ContribuinteSolicitacaoActivity.recolherCoordenadasLatLng( "+ recicladorEnderecoList.get(i).toString() +")");

                                    final int ifinal = i;

                                    //Transformamos o endereco obtido
                                    new TransformarCoordenadas().execute(recicladorEnderecoList.get(i).toString());

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (ifinal == (recicladorEnderecoList.size() - 1)) {

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        //Tranferimos a LatLng atual para o método;
                                                        LatLng coordenadas = coordenadasAtual;
                                                        //Atribuímos a variável do ponto mais proximo;
                                                        LatLng coordenadasReciclador = new LatLng(0,0);
                                                        //Distância entre o ponto atual e o destino comparado;
                                                        double distancia = 0;
                                                        //Posição correspondente das Latlng mais proxima;
                                                        int i_enderecoEscolhido = 0;

                                                        for (int i = 0; i < coordenadasRecicladorEnderecoList.size(); i++) {

                                                            double distanciaAtual = getDistance(coordenadas, coordenadasRecicladorEnderecoList.get(i));

                                                            if (distanciaAtual > distancia) {

                                                                distancia = distanciaAtual;
                                                                i_enderecoEscolhido = i;
                                                            }
                                                        }

                                                        //Após verificar e resultar o ponto mais proximo, atribuímos estes as variáveis atuais;
                                                        recicladorAtual.setIdReciclador(recicladorEnderecoList.get(i_enderecoEscolhido).getFkReciclador());

                                                        //Atribuimos a url de conexão para criação da solicitação;
                                                        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/InserirSolicitacaoEntrega.php";
                                                        metodoNetworkConnection = "Criar Solicitação de Entrega";

                                                        //Criamos a conexão volley;
                                                        callVolleyRequest(urlConnection);

                                                        //Rotina pós-resultado da conexão;
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( postDelayed );");

                                                                if (messageResult.contains("sucesso")) {

                                                                    Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( Sucesso );");
                                                                    //Informamos que esta variável possui
                                                                    solicitacaoStatus = true;

                                                                    //Desconetamos a atual conexão volley;
                                                                    desconnectVolleyRequest();

                                                                    if (solicitacaoStatus) {

                                                                        Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( Materiais );");

                                                                        //Estabelecemos o novo método a ser executado no Volley;
                                                                        metodoNetworkConnection = "Interligar materiais a solicitação";

                                                                        //É necessário ter a informação de quantos materiais serão adicionados;
                                                                        int quantMateriais = solicitacaoMateriaisList.size();
                                                                        quantMateriaisIndex = 0;

                                                                        //Atribuimos a url de conexão para interligar os materiais da solicitação;
                                                                        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/InserirSolicitacaoMateriais.php";

                                                                        for (int i = 0; i < quantMateriais; i++) {

                                                                            int finalI = i;
                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {

                                                                                    //Criamos a conexão volley;
                                                                                    callVolleyRequest(urlConnection);

                                                                                    if (finalI == (quantMateriais - 1)) {

                                                                                        Toast.makeText(getApplicationContext(), messageResult, Toast.LENGTH_SHORT).show();

                                                                                        //Desconetamos a atual conexão volley;
                                                                                        desconnectVolleyRequest();

                                                                                        Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( Usuário: "+ contribuinteAtual.getIdContribuinte() + " - Email: " + contribuinteAtual.getEmailContribuinte() +")");

                                                                                        Contribuinte contribuinte = contribuinteAtual;

                                                                                        Intent mapsActivity = new Intent(ContribuinteSolicitacaoActivity.this, ContribuinteAcompanharActivity.class);
                                                                                        Bundle bundle = new Bundle();
                                                                                        bundle.putSerializable("Usuario", contribuinte);
                                                                                        bundle.putSerializable("Solicitacao", solicitacaoAtual);
                                                                                        bundle.putSerializable("Materiais", (Serializable) solicitacaoMateriaisList);
                                                                                        bundle.putSerializable("Reciclador", recicladorAtual);
                                                                                        bundle.putSerializable("RecicladorEndereco", recicladorAtual);
                                                                                        bundle.putString("Endereco", enderecoEscolhido);
                                                                                        bundle.putString("Serviço", "Ponto");
                                                                                        mapsActivity.putExtra("Bundle", bundle);
                                                                                        startActivity(mapsActivity);
                                                                                    }
                                                                                }
                                                                            }, 3000);

                                                                        }
                                                                    }
                                                                }
                                                                else
                                                                if (messageResult.contains("Erro")) {

                                                                    Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( Erro );");
                                                                    //Informamos que esta variável possui
                                                                    solicitacaoStatus = false;

                                                                }
                                                                //Se um dos campos não estiverem preenchidos, retornaremos uma mensagem;
                                                                else {

                                                                    //Verificação LOG;
                                                                    Log.i("Script", "ContribuinteSolicitacaoActivity.clickButtonIniciarSessao('Parametros Vazios');");

                                                                    //Informamos ao usuário a situação atual;
                                                                    Toast.makeText(getApplicationContext(), messageResult, Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        }, 3000);
                                                    }
                                                }, 3000);

                                            }
                                        }
                                    }, 3000);
                                }
                            }
                        }, 1000);
                    }
                }, 3000);
            }else
            //Caso tenha sido "solicitacao", será acionado a criação de uma a ser atendida;
            if (servicoSolicitado.contains("Solicitacao")) {

                //Atribuimos a url de conexão para criação da solicitação;
                urlConnection = "http://192.168.43.57:8080/EcoFacil_Server/InserirSolicitacaoInicial.php";
                metodoNetworkConnection = "Criar Solicitacão";
                //Criamos a conexão volley;
                callVolleyRequest(urlConnection);
                //Rotina pós-resultado da conexão;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( postDelayed );");

                        if (messageResult.contains("sucesso")) {

                            Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( Sucesso );");
                            //Informamos que esta variável possui
                            solicitacaoStatus = true;

                            //Desconetamos a atual conexão volley;
                            desconnectVolleyRequest();

                            if (solicitacaoStatus) {

                                Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( Materiais );");

                                //Estabelecemos o novo método a ser executado no Volley;
                                metodoNetworkConnection = "Interligar materiais a solicitação";

                                //É necessário ter a informação de quantos materiais serão adicionados;
                                int quantMateriais = solicitacaoMateriaisList.size();
                                quantMateriaisIndex = 0;

                                //Atribuimos a url de conexão para interligar os materiais da solicitação;
                                urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/InserirSolicitacaoMateriais.php";

                                for (int i = 0; i < quantMateriais; i++) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //Criamos a conexão volley;
                                            callVolleyRequest(urlConnection);
                                        }
                                    }, 3000);

                                    Toast.makeText(getApplicationContext(), messageResult, Toast.LENGTH_SHORT).show();

                                    //Desconetamos a atual conexão volley;
                                    desconnectVolleyRequest();

                                }

                                Intent contribuinteActivity = new Intent(ContribuinteSolicitacaoActivity.this, ContribuinteActivity.class);
                                startActivity(contribuinteActivity);
                            }
                        }
                        else
                        if (messageResult.contains("Erro")) {

                            Log.i("Script", "ContribuinteSolicitacaoActivity.onTranferirIntentMateriais( Erro );");
                            //Informamos que esta variável possui
                            solicitacaoStatus = false;

                        }
                        //Se um dos campos não estiverem preenchidos, retornaremos uma mensagem;
                        else {

                            //Verificação LOG;
                            Log.i("Script", "ContribuinteSolicitacaoActivity.clickButtonIniciarSessao('Parametros Vazios');");

                            //Informamos ao usuário a situação atual;
                            Toast.makeText(getApplicationContext(), messageResult, Toast.LENGTH_LONG).show();
                        }
                    }
                }, 3000);
            }
        }
        else {

            action = "solicitacao";

            /*Intent intentSolicitacao = new Intent(this, ContribuinteAcompanharActivity.class);
            intentSolicitacao.putExtra("Servico", servicoSolicitado);
            intentSolicitacao.putExtra("Action", action);
            intentSolicitacao.putExtra("Usuario", contribuinteAtual);
            intentSolicitacao.putExtra("Endereco", enderecoEscolhido);
            intentSolicitacao.putExtra("Material", FragmentSolicitacaoMateriais.quantidadeMaterialEscolhida);
            intentSolicitacao.putExtra("TipoMaterial", FragmentSolicitacaoMateriais.tipoMaterialEscolhido);
            intentSolicitacao.putExtra("TipoPesoMaterial", FragmentSolicitacaoMateriais.tipoPesoEscolhido);
            startActivity(intentSolicitacao);*/
        }


    }
    //Método para alteração do fragment Endereco aplicado no frameLayout;
    //FragmentEndereco -> FragmentMateriais;
    @Override
    public void onTranferirIntentEndereco() {

        Bundle args = new Bundle();
        args.putString("Action", FragmentSolicitacaoEndereco.actionFragment);
        args.putString("EnderecoExtenso", FragmentSolicitacaoEndereco.enderecoEscolhido);
        args.putSerializable("Usuario", contribuinteAtual);
        args.putSerializable("Endereco", (Serializable) FragmentSolicitacaoEndereco.contribuinteEnderecoAtual);
        args.putSerializable("Materiais", (Serializable) FragmentSolicitacaoMateriais.materiaisDoados);

        //Tranferimos o enderecoEscolhido para global;
        enderecoEscolhido = FragmentSolicitacaoEndereco.enderecoEscolhido;
        contribuinteEnderecoAtual = FragmentSolicitacaoEndereco.contribuinteEnderecoAtual;

        FragmentSolicitacaoMateriais segundoFragment = new FragmentSolicitacaoMateriais();
        segundoFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction transacao = getSupportFragmentManager().beginTransaction();
        transacao.replace(R.id.fragmentSolicitacao, segundoFragment);
        transacao.commit();
    }
    //Método para alteração do fragment Endereco novo aplicado no frameLayout;
    //FragmentEnderecoNovo -> FragmentEndereco;
    @Override
    public void onTransferirIntentEnderecoNovo() {

        Bundle args = new Bundle();
        args.putString("Action", FragmentSolicitacaoEndereco.actionFragment);
        args.putSerializable("Usuario", contribuinteAtual);

        FragmentSolicitacaoEnderecoNovo segundoFragment = new FragmentSolicitacaoEnderecoNovo();
        segundoFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction transacao = getSupportFragmentManager().beginTransaction();
        transacao.replace(R.id.fragmentSolicitacao, segundoFragment);
        transacao.commit();

    }
    //Método para alteração do fragment Endereco, após cadastrar um novo, aplicado no frameLayout;
    //FragmentEnderecoNovo onBackPressed;
    @Override
    public void onTransferirIntentEnderecoNovoReturn() {

        Bundle args = new Bundle();
        args.putString("Action", FragmentSolicitacaoEnderecoNovo.actionFragment);
        args.putSerializable("Usuario", contribuinteAtual);

        FragmentSolicitacaoEndereco segundoFragment = new FragmentSolicitacaoEndereco();
        segundoFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction transacao = getSupportFragmentManager().beginTransaction();
        transacao.replace(R.id.fragmentSolicitacao, segundoFragment);
        transacao.commit();

    }
    /*

        Métodos para conexão Volley;

    */
    //Método de conexão Volley.
    public void callVolleyRequest(String url) {

        Log.i("Script", "FragmentSolicitacaoMateriais.callVolleyRequest()");

        //Iniciamos uma conexão direta com o Volley - Inserir Jogador;
        NetworkConnection.getInstance(getApplicationContext()).execute(
                this,
                ContribuinteSolicitacaoActivity.class.getName(),
                url);

    }
    //Método para desconectar o Volley;
    public void desconnectVolleyRequest() {

        Log.i("Script", "FragmentSolicitacaoMateriais.desconnectVolleyRequest()");
        //Desconectamos através do name da conexão;
        NetworkConnection.getInstance(this).getRequestQueue().cancelAll( ContribuinteSolicitacaoActivity.class.getName() );
    }
    //Antes de se iniciar a conexão volley;
    @Override
    public WrapObjToNetwork doBefore() {

        Log.i("Script", "ContribuinteSolicitacaoActivity.onBefore(Solicitacao & Materiais)");

        //Preparação para cadastro da solicitacao;
        if (metodoNetworkConnection.equals("Criar Solicitacão")) {

            Log.i("Script", "ContribuinteSolicitacaoActivity.onBefore(Solicitacao)");

            solicitacaoAtual = new Solicitacao();

            //é necessário a data e hora atual em conjunto com o id do contribuinte para iniciar esta solicitação;
            //id Contribuinte - Fk;
            //Capturamos o id do contribuinte atual;
            int idContribuinte = contribuinteAtual.getIdContribuinte();
            //Atribuímos ele ao fkContribuinte da solicitação atual;
            solicitacaoAtual.setFkContribuinte(idContribuinte);
            //id ContribuinteEndereco - Fk
            //Capturamos o id do contribuinte atual;
            int idContribuinteEndereco = contribuinteAtual.getIdContribuinte();
            solicitacaoAtual.setFkContribuinteEndereco(idContribuinteEndereco);
            //Atribuímos ele ao fkContribuinte da solicitação atual;
            solicitacaoAtual.setFkContribuinte(idContribuinte);
            //Data e Hora atual;
            try {
                consultarHoraDataAtual();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Iniciamos a conexão volley;
            return ( new WrapObjToNetwork(
                    solicitacaoAtual,
                    "java-web-jor",
                    urlConnection) );

        } else
        //Preparação para cadastro da solicitação por meio de entrega propria
        if (metodoNetworkConnection.contains("Criar Solicitação de Entrega")) {

            Log.i("Script", "ContribuinteSolicitacaoActivity.onBefore(Criar Solicitação de Entrega)");

            //Instanciamos o objeto que carregara os parâmetros para a query;
            solicitacaoAtual = new Solicitacao();
            //Instanciamos o fkContribuinte na solicitação;
            solicitacaoAtual.setFkContribuinte(contribuinteAtual.getIdContribuinte());
            //Instanciamos o fkReciclador da solicitação do endereco;
            solicitacaoAtual.setFkReciclador(recicladorAtual.getIdReciclador());
            //Instanciamos a data e hora atual do atendimento;
            try {
                consultarHoraDataAtual();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Endereco de partida para anotação no sistema;
            solicitacaoAtual.setDescricaoSolicitacao(enderecoEscolhido);

            //Iniciamos a conexão volley;
            return ( new WrapObjToNetwork(
                    solicitacaoAtual,
                    "java-web-jor",
                    urlConnection) );
        }
        else
        //Preparação para cadastro dos materiais interligados a solicitacao;
        if (metodoNetworkConnection.equals("Interligar materiais a solicitação")) {

            solicitacaoMaterialAtual = solicitacaoMateriaisList.get(quantMateriaisIndex);
            quantMateriaisIndex =+ 1;
            //Capturamos o id do contribuinte atual;
            solicitacaoMaterialAtual.setFkSolicitacao(solicitacaoAtual.getIdSolicitacao());

            Log.i("Script", "ContribuinteSolicitacaoActivity.onBefore(" + solicitacaoAtual.getIdSolicitacao() + ")");

            //Iniciamos a conexão volley;
            return ( new WrapObjToNetwork(
                    solicitacaoMaterialAtual,
                    "java-web-jor",
                    urlConnection) );

        }
        //Caso tenhamos que procurar um ponto reciclador mais proximo;
        if (metodoNetworkConnection.contains("Buscar reciclador mais proximo")) {

            //Iniciamos a conexão volley;
            return ( new WrapObjToNetwork(
                    contribuinteAtual,
                    "java-web-jor",
                    urlConnection) );
        }
        //Caso contrário, houve algum erro interno do sistema;
        else {

            Log.i("Script", "FragmentSolicitacaoMateriais.doBefore(Enderecos - Erro)");

            return null;
        }
    }
    //Depois do resultado da conexão ao servidor;
    @Override
    public void doAfter(JSONArray jsonArray) {

        Log.i("Script", "FragmentSolicitacaoMateriais.doAfter()");

        //Verificamos se a conexão retornou algum dado...
        if ( jsonArray != null) {

            Log.i("Script", "FragmentSolicitacaoMateriais.doAfter( "+ jsonArray +" )");

            //Preparação para cadastro da solicitacao;
            if (metodoNetworkConnection.equals("Criar Solicitacão") || metodoNetworkConnection.contains("Criar Solicitação de Entrega")) {

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    Log.i("Script", "ContribuinteSolicitacaoActivity.doAfter( "+ jsonArray +" )");

                    //solicitacaoAtual = new Solicitacao();

                    //Transferimos o resultado vindo do banco;
                    messageResult = jsonArray.getJSONObject(0).getString("resultQuery");
                    solicitacaoAtual.setIdSolicitacao(jsonArray.getJSONObject(0).getInt("idSolicitacao"));

                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            else
                //Preparação para cadastro dos materiais interligados a solicitacao;
            if (metodoNetworkConnection.equals("Interligar materiais a solicitação")) {

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    Log.i("Script", "ContribuinteSolicitacaoActivity.doAfter( "+ jsonArray +" )");

                    solicitacaoMaterialAtual = new SolicitacaoMateriais();

                    //Transferimos o resultado vindo do banco;
                    messageResult = jsonArray.getJSONObject(0).getString("resultQuery");
                    solicitacaoMaterialAtual.setIdSolicitacaoMaterial(jsonArray.getJSONObject(0).getInt("idSolicitacaoMaterial"));

                    //solicitacaoMateriaisListResult.add(solicitacaoMaterialAtual);
                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            } else
            //Caso seja para alterar a solicitação em andamento;
            if (metodoNetworkConnection.contains("Buscar reciclador mais proximo")) {

                Log.i("Script", "ContribuinteAcompanharActivity.doAfter(Finalizado)");

                //Atribuímos a list que receberá todos os enderecos de recicladores cadastrados;
                recicladorEnderecoList = new ArrayList<RecicladorEndereco>();

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
                        recicladorEnderecoList.add(recicladorEndereco);
                    }
                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }
        else {

            Log.i("Script", "Algum problema foi detectado no Volley");
        }
    }

}