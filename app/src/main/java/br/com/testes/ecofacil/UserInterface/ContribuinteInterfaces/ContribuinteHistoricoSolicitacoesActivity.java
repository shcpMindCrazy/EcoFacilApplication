package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.testes.ecofacil.AdaptersList.RecyclerView.Chamado;
import br.com.testes.ecofacil.AdaptersList.RecyclerView.ChamadoAdapter;
import br.com.testes.ecofacil.AdaptersList.RecyclerView.ChamadoHistoricoAdapter;
import br.com.testes.ecofacil.AdaptersList.RecyclerView.RecyclerViewOnClickListener;
import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;
import br.com.testes.ecofacil.R;

public class ContribuinteHistoricoSolicitacoesActivity extends AppCompatActivity implements
        Transaction,
        RecyclerViewOnClickListener{

    //Componentes Visuais ==========================================================================;
    private RecyclerView rvHistoricoList;
    private Spinner spnHistoricoStatus;

    //Componente - Auxiliares Spinner;
    private List<String> opcoesHistoricoSpinner = new ArrayList<String>();
    //Componente - Auxiliares RecyclerView;
    private List<Chamado> mChamados = new ArrayList<Chamado>();
    private LinearLayoutManager llmVertical;
    private SwipeRefreshLayout srlRecyclerView;
    private ChamadoHistoricoAdapter cAdapter;

    //Variáveis globais =========================================================================== ;

    //Variáveis - Comuns;
    List<SolicitacaoMateriais> materiaisSolicitacaoList = new ArrayList<SolicitacaoMateriais>();
    boolean messageBoxPermission = false;
    int idSolicitacaoAtual = 0;
    int positionItemRecyclerView;

    //Variáveis - Volley;
    private String urlConnection;
    private String metodoNetworkConnection = "";
    private String messageResult = "";

    //Variaveis - Objetos;
    private Contribuinte contribuinteAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribuinte_historico_solicitacoes);

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.onCreate();");

        //Instanciamos o intent com as variáveis anteriores;
        Intent resultIntent = getIntent();
        //Buscamos os extras dentro do intent para este bundle;
        Bundle bundleNovo = resultIntent.getExtras();
        //Caso este bundle não estiver vazio;
        if (bundleNovo != null) {

            Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.onCreate() - Bundle OK");
            contribuinteAtual = (Contribuinte) bundleNovo.getSerializable("Usuario");
        }
        //Caso estiver vazio;
        else {

            Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.onCreate() - Bundle Vazio");
        }
        //Instanciamos o spinner e suas configurações;
        initializeSpinnerHistoricoStatus();
        //Instanciamos o RecyclerView e suas configurações;
        initializeRecyclerHistoricoList();
    }
    /*

       Métodos para configuração dos componentes;

     */
    //Método para iniciação do componente spnHistoricoStatus;
    public void initializeSpinnerHistoricoStatus() {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.initializeSpinnerHistoricoStatus();");
        //Instanciamos o objeto na variável;
        spnHistoricoStatus = (Spinner) findViewById(R.id.spnHistoricoStatusFiltro);
        //Instanciamos as opções disponíveis para selecionar no spinner;
        opcoesHistoricoSpinner.add("Tipo de solicitacao para pesquisar?");
        opcoesHistoricoSpinner.add("Aguardando atendimento...");
        opcoesHistoricoSpinner.add("Atendimento em curso...");
        opcoesHistoricoSpinner.add("Atendimento finalizado");
        //opcoesHistoricoSpinner.add("Solicitações de entrega em curso");
        //Criamos um ArrayAdapter para o spinner;
        ArrayAdapter<String> arrayAdaptercriterioPesquisa = new ArrayAdapter<String>(
                this, //Context;
                android.R.layout.simple_spinner_dropdown_item, //Style;
                opcoesHistoricoSpinner) {

            //Ao ativar a list...
            @Override
            public boolean isEnabled(int position){
                //É desativada a primeira opção, já que é apenas para visualização;
                if(position == 0)
                {
                    return false;
                }
                //As demais permanecem ativadas...
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                //A opção desativada é pintada de cinza;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                //E as demais de preto;
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        //Instanciamos o array adapter em um exclusivo para o spinner;
        ArrayAdapter<String> spinnerArrayAdapterPesquisaCriterio = arrayAdaptercriterioPesquisa;
        //Animação realizada ao clicar no botão;
        spinnerArrayAdapterPesquisaCriterio.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Atribuição do arrayAdapter para o apinner;
        spnHistoricoStatus.setAdapter(spinnerArrayAdapterPesquisaCriterio);
        //Criamos o evento de quando for alterado o item da Spinner;
        spnHistoricoStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.setOnItemSelectedListener(onItemSelected)");
                //Capturamos a frase selecionada na spinner;
                String criterioSelecionado = spnHistoricoStatus.getSelectedItem().toString();
                //Atribuímos o tipo de conexão a ser estabelecida no banco;
                metodoNetworkConnection = criterioSelecionado; //Dentre as opções: "Abertas", "Em andamento", "Finalizadas";
                //Atribuímos o caminho de conexão Volley;
                urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarHistoricoSolicitacoes.php";
                //E em seguida, iniciamos a conexão Volley;
                callVolleyRequest(urlConnection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.setOnItemSelectedListener(onNothingSelected)");
            }
        });

    }
    //Método para iniciação do componente rvHistoricoList
    public void initializeRecyclerHistoricoList() {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.initializeRecyclerHistoricoList();");
        //Instanciamos o objeto na variável;
        rvHistoricoList = (RecyclerView) findViewById(R.id.rvHistoricoRecycler);
        //Este list continuará a ter o mesmo tamanho, independente de seus resultados;
        rvHistoricoList.setHasFixedSize(true);
        //LinearLayoutManager para tratar os dados que forem enviados para a list;
        llmVertical = new LinearLayoutManager(this);
        //Setamos a orientação dos itens;
        llmVertical.setOrientation(LinearLayoutManager.VERTICAL);
        //Atribuímos para o RecyclerView;
        rvHistoricoList.setLayoutManager(llmVertical);
        //Instanciamos o nosso Adapter;
        cAdapter = new ChamadoHistoricoAdapter(this, mChamados);
        if( cAdapter.getItemCount() == 0 ) {

            //Setamos este objeto vazio com a informação para instruir o usuário a selecionar um item no spinner;
            Chamado chamado = new Chamado();
            chamado.setEnderecoContribuinteColeta("Selecione o tipo de solicitacao acima para exibirmos");
            rvHistoricoList.setEnabled(false);
            //Logo depois na list, que será preenchida de acordo com o que for selecionado no spinner;
            mChamados.add(chamado);
        }
        //Instanciamos o método de onClick na RecyclerView;
        cAdapter.setRecyclerViewOnClickListener(this);
        //Instanciamos o adapter dentro da nossa RecyclerView;
        rvHistoricoList.setAdapter(cAdapter);
    }
    //Método ao selecionar um item da recyclerView;
    @Override
    public void onClickItemListener(View view, int position) {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.onClickItemListener();");
        //Verificamos se a RecyclerView recebeu algum dado Volley para prosseguirmos;
        if (messageBoxPermission) {

            //Capturamos a posição da list (linha que se localiza o idSolicitação);
            positionItemRecyclerView = position;
            //Capturamos as informações dos materiais a serem oferecidos nessa solicitação informado
            idSolicitacaoAtual = mChamados.get(position).getIdSolicitacaoColeta();
            //Atribuímos a variável de conexão ao banco de dados;
            urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarMateriaisSolicitacao.php";
            //Atribuímos o tipo de conexão a ser realizada;
            metodoNetworkConnection = "Consultar materiais da solicitacao";
            //Iniciamos a conexão Volley;
            callVolleyRequest(urlConnection);
        }
        //Caso contrário, avisamos ao usuário a situação;
        else {

            Toast.makeText(this, "Para selecionar uma solicitação, primeiro busque a desejada a cima", Toast.LENGTH_SHORT).show();
        }
    }

    /*

        Métodos de rotina de execução;

     */
    //Método pós-volley, para atualizar os itens da list de acordo com o filtro;
    public void atualizarRecyclerViewEstado() {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.atualizarRecyclerViewEstado()");
        //Ativamos a list novamente;
        rvHistoricoList.setEnabled(true);
        //Instanciamos o adapter atual do RecyclerView;
        ChamadoHistoricoAdapter chamadoHistoricoAdapter = (ChamadoHistoricoAdapter) rvHistoricoList.getAdapter();
        //Passamos os novos parâmetros para o adapter de atualização;
        chamadoHistoricoAdapter.updateList(mChamados);
    }
    //Método pós-volley, para mostrar os dados da solicitação selecionada;
    public void mostrarMateriaisSolicitacao() {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.mostrarMateriaisSolicitacao()");
        //Transferimos ArrayList para um vetor de string
        String[] itensMessageMateriais = new String[materiaisSolicitacaoList.size()];
        for (int i = 0; i < materiaisSolicitacaoList.size(); i++) {

            //Capturamos o objeto atual para ser transferido como informação em extenso;
            String linha = "Carrinho Nº" + (i + 1) + "\n" +
                    //"Endereco: " + mChamados.get(positionItemRecyclerView).getEnderecoContribuinteColeta() + "\n" +
                    "Quantidade: " + materiaisSolicitacaoList.get(i).getQuantidadeSolicitacaoMaterial() + " | \n" +
                    "Medida: " + materiaisSolicitacaoList.get(i).getMedidaSolicitacaoMaterial() + " | \n" +
                    "Tipo: " + materiaisSolicitacaoList.get(i).getTipoSolicitacaoMaterial() + "\n" +
                    "Descrição: " + materiaisSolicitacaoList.get(i).getDescricaoSolicitacaoMaterial();
            Log.i("Script", "FragmentChamados.onClickItemListener( Linha: " + linha + " )");

            //Adicionamos este no vetor;
            itensMessageMateriais[i] = linha;
        }
        //Inicializamos o AlertDialog com os materiais;
        showAlertDialogMateriais(itensMessageMateriais);
    }
    //Método para construir e mostrar um AlertDialog para o usuário com os materiais;
    public void showAlertDialogMateriais(String[] itens) {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.showAlertDialogMateriais()");
        //Instanciamos este view contendo o ScrollView em outro layout.xml
        View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_materiais_info, null);
        //Criamos uma dialog para confirmação do usuário;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView); //Atribuímos o ScrollView;
        builder.setTitle("Materiais atrelados a este pedido:")
                //.setMessage("Endereco: " + mChamados.get(position).getEnderecoContribuinteColeta().toString())
                .setItems(itens, null);
        builder.create();
        builder.show();
    }
    /*

        Métodos para conexão Volley;

    */
    //Método de conexão Volley.
    public void callVolleyRequest(String url) {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.callVolleyRequest()");

        //Iniciamos uma conexão direta com o Volley - Inserir Jogador;
        NetworkConnection.getInstance(getApplicationContext()).execute(
                this,
                ContribuinteHistoricoSolicitacoesActivity.class.getName(),
                url);

    }
    //Método para desconectar o Volley;
    public void desconnectVolleyRequest() {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.desconnectVolleyRequest()");
        //Desconectamos através do name da conexão;
        NetworkConnection.getInstance(this).getRequestQueue().cancelAll( ContribuinteHistoricoSolicitacoesActivity.class.getName() );
    }
    //Método ativado antes da conexão volley;
    @Override
    public WrapObjToNetwork doBefore() {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doBefore()");

        //Verificamos qual tipo de conexão será requisitada;
        //Caso, verifiquemos apenas as solicitacoes dadas pelo contribuinte;
        if (metodoNetworkConnection.contains("Aguardando atendimento...") ||
                (metodoNetworkConnection.contains("Atendimento em curso...") ||
                        (metodoNetworkConnection.contains("Atendimento finalizado")))) {

            Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doBefore("+ metodoNetworkConnection +")");

            //Instanciamos o chamado para ser preenchidos com os parâmetros;
            //Parâmetro - id contribuinte atual a ser pesquisado;
            //Parâmetro - string de procura do estado da solicitacao;
            Chamado chamado = new Chamado(contribuinteAtual.getIdContribuinte(), metodoNetworkConnection);
            //Retornamos para a conexão Volley;
            return ( new WrapObjToNetwork(
                    chamado,
                    "java-web-jor",
                    urlConnection) );
        } else
        //Caso, necessário buscar os materiais da solicitação requisitada...
        if (metodoNetworkConnection.contains("Consultar materiais da solicitacao")) {

            //Instanciamos o objeto a ser usado para transmissão de parâmetros;
            SolicitacaoMateriais solicitacaoMateriais = new SolicitacaoMateriais();
            //Atribuimos o id respectivo da solicitação atual;
            solicitacaoMateriais.setFkSolicitacao(idSolicitacaoAtual);
            //Retornamos para a conexão Volley;
            return ( new WrapObjToNetwork(
                    solicitacaoMateriais,
                    "java-web-jor",
                    urlConnection) );
        }
        //Caso, estiver vazio ao requisitar esta conexão;
        else {

            Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doBefore(Vazio)");
            //Retornamos nulo, por não haver especificação da conexão;
            return null;
        }
    }
    //Método ativado após a conexão volley, trazendo seus resultados;
    @Override
    public void doAfter(JSONArray jsonArray) {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter()");

        //Verificamos se a conexão retornou algum dado...
        if ( jsonArray != null) {

            //Verificamos qual tipo de conexão será requisitada;
            //Caso, verifiquemos apenas as solicitacoes abertas pelo contribuinte;
            if (metodoNetworkConnection.contains("Aguardando atendimento...") ||
                    (metodoNetworkConnection.contains("Atendimento em curso...") ||
                            (metodoNetworkConnection.contains("Atendimento finalizado")))) {

                try {

                    Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter( " + jsonArray + " )");

                    //Atribuímos uma nova instanciação a list;
                    mChamados = new ArrayList<Chamado>();

                    //Verificamos o primeiro resultado;
                    String resultMateriais = jsonArray.getJSONObject(0).getString("estadoAtualColeta");

                    if (!resultMateriais.contains("Nenhuma solicitacao foi encontrada")) {

                        //Verificamos todos os materiais a serem exibidos ao usuário;
                        for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {

                            //Atribuimos o objeto para recebimento dos resultados;
                            Chamado chamadoResult = new Chamado();
                            //Resultado - idSolicitacaoColeta;
                            chamadoResult.setIdSolicitacaoColeta(jsonArray.getJSONObject(i).getInt("idSolicitacaoColeta"));
                            //Resultado - estadoAtualColeta;
                            chamadoResult.setEstadoAtualColeta(jsonArray.getJSONObject(i).getString("estadoAtualColeta"));
                            //Resultado - inicioSolicitacaoColeta;
                            chamadoResult.setInicioSolicitacaoColeta(jsonArray.getJSONObject(i).getString("inicioSolicitacaoColeta"));
                            //Resultado - finalSolicitacaoColeta;
                            if (jsonArray.getJSONObject(i).has("finalSolicitacaoColeta") && jsonArray.getJSONObject(i).isNull("finalSolicitacaoColeta")) {
                                chamadoResult.setFinalSolicitacaoColeta("__/__/___ | __:__");
                            } else {
                                chamadoResult.setFinalSolicitacaoColeta(jsonArray.getJSONObject(i).getString("finalSolicitacaoColeta"));
                            }
                            //Resultado - descricaoSolicitacaoColeta;
                            if (jsonArray.getJSONObject(i).has("descricaoSolicitacaoColeta") && jsonArray.getJSONObject(i).isNull("descricaoSolicitacaoColeta")) {
                                chamadoResult.setDescricaoMaterialColeta("Nenhuma anotação realizada");
                            } else {
                                chamadoResult.setDescricaoMaterialColeta(jsonArray.getJSONObject(i).getString("descricaoSolicitacaoColeta"));
                            }
                            //Resultado - idContribuinteColeta;
                            chamadoResult.setIdContribuinteColeta(jsonArray.getJSONObject(i).getInt("idContribuinteColeta"));
                            //Resultado - nomeContribuinteColeta;
                            chamadoResult.setNomeContribuinteColeta(jsonArray.getJSONObject(i).getString("nomeContribuinteColeta"));
                            //Resultado - idContribuinteEnderecoColeta;
                            if (jsonArray.getJSONObject(i).has("idContribuinteEnderecoColeta") && jsonArray.getJSONObject(i).isNull("idContribuinteEnderecoColeta")) {
                                chamadoResult.setIdContribuinteEnderecoColeta(0);
                            } else {
                                chamadoResult.setIdContribuinteEnderecoColeta(jsonArray.getJSONObject(i).getInt("idContribuinteEnderecoColeta"));
                            }
                            //Resultado - enderecoContribuinteColeta;
                            if (jsonArray.getJSONObject(i).has("enderecoContribuinteColeta") && jsonArray.getJSONObject(i).isNull("enderecoContribuinteColeta")) {
                                chamadoResult.setEnderecoContribuinteColeta("Foi utilizado a localidade atual do usuário (Detalhes)");
                            } else {
                                chamadoResult.setEnderecoContribuinteColeta(jsonArray.getJSONObject(i).getString("enderecoContribuinteColeta"));
                            }
                            //Adicionamos o item na list;
                            mChamados.add(chamadoResult);
                        }
                        //Realizamos a desconexão volley;
                        desconnectVolleyRequest();
                        //Acionamos o uso da messageBox dos materiais;
                        messageBoxPermission = true;
                        //Ao terminar de buscar os chamados, continuamos preenchendo os dados na RecyclerView;
                        atualizarRecyclerViewEstado();
                    }
                } catch (JSONException e) {

                    Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter( " + e + " )");
                    Toast.makeText(this, "Houve um erro na execução do aplicativo com a rede, contate o suporte", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            //Caso, verifiquemos os materiais da solicitação selecionada;
            else if  (metodoNetworkConnection.contains("Consultar materiais da solicitacao")) {

                Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter("+ metodoNetworkConnection +")");

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    //Atribuímos uma nova instanciação a list;
                    materiaisSolicitacaoList = new ArrayList<SolicitacaoMateriais>();

                    //Verificamos o primeiro resultado;
                    String resultMateriais = jsonArray.getJSONObject(0).getString("descricaoSolicitacaoMaterial");
                    if (!resultMateriais.contains("Nenhum material foi encontrado")) {

                        //Verificamos todos os materiais a serem exibidos ao usuário;
                        for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {

                            //Atribuimos o objeto para recebimento dos resultados;
                            SolicitacaoMateriais solicitacaoMateriaisResult = new SolicitacaoMateriais();
                            //Id do material;
                            solicitacaoMateriaisResult.setIdSolicitacaoMaterial(jsonArray.getJSONObject(i).getInt("idSolicitacaoMaterial"));
                            //Fk da solicitação atual;
                            solicitacaoMateriaisResult.setFkSolicitacao(jsonArray.getJSONObject(i).getInt("fkSolicitacao"));
                            //Quantidade a ser entregue/recebida;
                            solicitacaoMateriaisResult.setQuantidadeSolicitacaoMaterial(jsonArray.getJSONObject(i).getDouble("quantidadeSolicitacaoMaterial"));
                            //Tipo de medida a ser contabilizada;
                            solicitacaoMateriaisResult.setMedidaSolicitacaoMaterial(jsonArray.getJSONObject(i).getString("medidaSolicitacaoMaterial"));
                            //Tipo de material a ser entregue/recebido;
                            solicitacaoMateriaisResult.setTipoSolicitacaoMaterial(jsonArray.getJSONObject(i).getString("tipoSolicitacaoMaterial"));
                            //Descrição geral do pedido, produto ou detalhes do mesmo;
                            solicitacaoMateriaisResult.setDescricaoSolicitacaoMaterial(jsonArray.getJSONObject(i).getString("descricaoSolicitacaoMaterial"));
                            //Adicionamos o item na list;
                            materiaisSolicitacaoList.add(solicitacaoMateriaisResult);
                        }

                        //Realizamos a desconexão volley;
                        desconnectVolleyRequest();
                        //Através dessa list, iremos mostrar os materiais da solicitação selecionada;
                        mostrarMateriaisSolicitacao();
                    }

                } catch (JSONException e) {

                    Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter( " + e + " )");
                    Toast.makeText(this, "Houve um erro na execução do aplicativo com a rede, contate o suporte", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            //Caso, estiver vazio ao requisitar esta conexão;
            else {

                Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter(Vazio)");
                //Retornamos nulo, por não haver especificação da conexão;
            }
        }
    }
}
