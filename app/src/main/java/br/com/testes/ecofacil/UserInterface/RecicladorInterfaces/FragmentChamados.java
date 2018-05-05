package br.com.testes.ecofacil.UserInterface.RecicladorInterfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.testes.ecofacil.AdaptersList.RecyclerView.RecyclerViewOnClickListener;
import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.Reciclador;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;
import br.com.testes.ecofacil.R;

/**
 * Created by samue on 22/03/2018.
 */

public class FragmentChamados extends Fragment implements RecyclerViewOnClickListener, Transaction {

    private List<String> criteriosPesquisa = new ArrayList<String>();
    List<Chamado> mChamados = new ArrayList<Chamado>();

    private Spinner spnCriterioPesquisa;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager llmVertical;
    private ChamadoAdapter cAdapter;
    private FloatingActionButton fabAtualizarList;
    private SwipeRefreshLayout srlRecyclerView;

    FragmentChamadosListener mCallback;

    public static List<SolicitacaoMateriais> mSolicitacaoMateriaisList = new ArrayList<SolicitacaoMateriais>();
    public static Reciclador recicladorAtual;
    public static Contribuinte contribuinteSolicitado;
    public static int idContribuinteSolicitado;
    public static String destinoRetirada;

    //Volley - Requisitos para conexão;
    String metodoNetworkConnection = "";
    String urlConnection = "";
    String messageResult = "";
    boolean solicitacaoStatus = false;
    int quantMateriaisIndex = 0;

    int idSolicitacaoAtual;

    //Vetor de resultado da consulta de chamados;
    List<Chamado> chamadoResultList = new ArrayList<Chamado>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chamados, container, false);

        Bundle bundleNovo = getArguments();

        if (bundleNovo != null) {

            recicladorAtual = (Reciclador) bundleNovo.getSerializable("Reciclador");
        }

        //Instanciamos os componentes e tambem seus listeners;
        initializeListeners(view);

        //Instancia os spinners para o usuário e seus respectivos valores;
        initializeSpinners(view);

        //Instancia o recycler view para visualização dos chamados;
        initializeRecyclerView(view);

        //Log.i("Script", "Chamado: " + mChamados.get(4).getPessoaInformacoes().getNomeContribuinte());

        return view;
    }

    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_materiais_info, null);

        //final TextView etMateriaisInfoTitulo = (TextView) dialogView.findViewById(R.id.etMateriaisInfoTitulo);
        //final TextView etMateriaisInfoEndereco = (TextView) dialogView.findViewById(R.id.etMateriaisInfoEndereco);
        //final TextView etMateriaisInfoQuantidade = (TextView) dialogView.findViewById(R.id.etMateriaisInfoQuantidade);
        //final TextView etMateriaisInfoMedida = (TextView) dialogView.findViewById(R.id.etMateriaisInfoMedida);
        //final TextView etMateriaisInfoTipo = (TextView) dialogView.findViewById(R.id.etMateriaisInfoTipo);

        builder.setView(dialogView);
        builder.setTitle("Informações sobre essa solicitação: ");

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Log.i("Script", "FragmentChamados.showFeedbackDialog(" + etMateriaisInfoEndereco.getText().toString() + ")");
            }
        });

        builder.show();

    }

    @Override
    public void onClickItemListener(View view, int position) {

        Log.i("Script", "FragmentChamados.onClickItemListener("+mChamados.get(position).getIdSolicitacaoColeta()+")");
        //Capturamos as informações dos materiais a serem oferecidos nessa solicitação informado
        idSolicitacaoAtual = mChamados.get(position).getIdSolicitacaoColeta();
        //1º Atribuímos a variável de conexão ao banco de dados;
        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarMateriaisSolicitacao.php";
        //2º Atribuímos o tipo de conexão a ser realizada;
        metodoNetworkConnection = "Consultar materiais da solicitacao";
        //3º Iniciamos a conexão Volley;
        callVolleyRequest(urlConnection);
        //4º Rotina com delay para execução do Volley;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.i("Script", "FragmentChamados.onClickItemListener( postDelayed )");

                //5º Transferimos ArrayList para um vetor de string
                String[] itensMessageMateriais = new String[mSolicitacaoMateriaisList.size()];
                for (int i = 0; i < mSolicitacaoMateriaisList.size(); i++) {

                    //Capturamos o objeto atual para ser transferido como informação em extenso;
                    String linha = "Nº - " + i + "\n" +
                            "Endereco: " + mChamados.get(position).getEnderecoContribuinteColeta() + "\n" +
                            "Materiais/Descrição: " + "\n" +
                            "Quantidade: " + mSolicitacaoMateriaisList.get(i).getQuantidadeSolicitacaoMaterial() + " | " +
                            "Medida: " + mSolicitacaoMateriaisList.get(i).getMedidaSolicitacaoMaterial() + " | " +
                            "Tipo: " + mSolicitacaoMateriaisList.get(i).getTipoSolicitacaoMaterial() + "\n" +
                            "Descrição: " + mSolicitacaoMateriaisList.get(i).getDescricaoSolicitacaoMaterial();

                    Log.i("Script", "FragmentChamados.onClickItemListener( Linha: "+ linha +" )");

                    //Adicionamos este no vetor;
                    itensMessageMateriais[i] = linha;
                }
                //Instanciamos este view contendo o ScrollView em outro layout.xml
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_materiais_info, null);
                //Criamos uma dialog para confirmação do usuário;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView); //Atribuímos o ScrollView;
                builder.setTitle("Informações sobre essa solicitação:")
                        //.setMessage("Endereco: " + mChamados.get(position).getEnderecoContribuinteColeta().toString())
                        .setItems(itensMessageMateriais, null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            //Iniciamos a navegação com os dados do chamado e subsequentes;
                            //Primeiro, coletamos os dados da pessoa;
                            idContribuinteSolicitado = mChamados.get(position).getIdContribuinteColeta();
                            //Segundo, o endereco informado para este serviço;
                            destinoRetirada = mChamados.get(position).getEnderecoContribuinteColeta();
                            //Terceiro, atribuímos os materiais da solicitação;


                            mCallback.onTranferirIntentChamados();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Informamos ao usuário que o pedido foi cancelado;
                                Toast.makeText(getActivity(), "Serviço de retirada cancelado", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create();
                builder.show();
            }
        }, 2000);
    }

    public void initializeRecyclerView(View view){

        //Acessando o RecyclerView;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        //Este list continuará a ter o mesmo tamanho, independente de seus resultados;
        mRecyclerView.setHasFixedSize(true);

        //LinearLayoutManager para tratar os dados que forem enviados para a list;
        llmVertical = new LinearLayoutManager(getActivity());
        //Setamos a orientação dos itens;
        llmVertical.setOrientation(LinearLayoutManager.VERTICAL);
        //Atribuímos para o RecyclerView;
        mRecyclerView.setLayoutManager(llmVertical);

        Chamado chamado = new Chamado();
        mChamados.add(chamado);

        //Instanciamos o nosso Adapter;
        cAdapter = new ChamadoAdapter(getActivity(), mChamados);
        //Instanciamos o método de onClick na RecyclerView;
        cAdapter.setRecyclerViewOnClickListener(this);
        //Instanciamos o adapter dentro da nossa RecyclerView;
        mRecyclerView.setAdapter(cAdapter);

        pesquisarChamados();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.i("Script", "FragmentChamados.pesquisarChamados( postDelayed );");

                //Instanciamos o adapter atual do RecyclerView;
                ChamadoAdapter chamadoAdapter = (ChamadoAdapter) mRecyclerView.getAdapter();
                //Passamos os novos parâmetros para o adapter de atualização;
                chamadoAdapter.updateList(mChamados);

                desconnectVolleyRequest();
            }
        }, 3000);
    }

    public void initializeSpinners(View view) {

        //Instancia o spinner para o componente em variável;
        spnCriterioPesquisa = (Spinner) view.findViewById(R.id.spnCriterioPesquisa);

        //Atribui nomes pré-selecionados existentes como critério de escolha;
        criteriosPesquisa.add("Todos");
        criteriosPesquisa.add("Por Cidade");
        criteriosPesquisa.add("Por Nome");
        criteriosPesquisa.add("Por Material");
        criteriosPesquisa.add("Por Medida");

        //Criamos um ArrayAdapter para o spinner;
        ArrayAdapter<String> arrayAdaptercriterioPesquisa = new ArrayAdapter<String>(
                getActivity(), //Context;
                android.R.layout.simple_spinner_dropdown_item, //Style;
                criteriosPesquisa) {

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
        ArrayAdapter<String> spinnerArrayAdapterPesquisCriterio = arrayAdaptercriterioPesquisa;
        //Animação realizada ao clicar no botão;
        spinnerArrayAdapterPesquisCriterio.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Atribuição do arrayAdapter para o apinner;
        spnCriterioPesquisa.setAdapter(spinnerArrayAdapterPesquisCriterio);
    }

    public void initializeListeners(View view) {

        fabAtualizarList = view.findViewById(R.id.fabAtualizarList);
        fabAtualizarList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Script", "FragmentChamados.initializeListeners.onClick.fabAtualizarList");

                //Atualizamos o dataSet da list;
                pesquisarChamados();

                //Instanciamos o adapter atual do RecyclerView;
                ChamadoAdapter chamadoAdapter = (ChamadoAdapter) mRecyclerView.getAdapter();
                //Passamos os novos parâmetros para o adapter de atualização;
                chamadoAdapter.updateList(mChamados);
            }
        });

        //Acessando o Layout de Refresh para o RecyclerView;
        srlRecyclerView = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        srlRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Log.i("Script", "FragmentChamados.initializeListeners.onRefresh.srlRecyclerView");

                //O roda de carregamento é encerrada;
                srlRecyclerView.setRefreshing(false);

                //Atualizamos o dataSet da list;
                pesquisarChamados();

                //Instanciamos o adapter atual do RecyclerView;
                ChamadoAdapter chamadoAdapter = (ChamadoAdapter) mRecyclerView.getAdapter();
                //Passamos os novos parâmetros para o adapter de atualização;
                chamadoAdapter.updateList(mChamados);
            }
        });
    }

    //Preenchemos a list de acordo com o dataSet;
    public void pesquisarChamados() {

        Log.i("Script", "FragmentChamados.preencherPessoasCadastradas()");

        //Atribuímos um link de conexão Volley;
        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarSolicitacaoChamado.php";
        //Atribuímos o tipo de conexão a ser realizada;
        metodoNetworkConnection = "Consultar solicitações";
        //Instanciamos a conexão Volley;
        callVolleyRequest(urlConnection);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.i("Script", "FragmentChamados.pesquisarChamados( postDelayed );");

                //Verificamos se não houve resultados no banco de dados;
                if (mChamados.get(0).getIdSolicitacaoColeta() != 0) {

                    Log.i("Script", "FragmentChamados.pesquisarChamados( chamadoResultList preenchida );");

                    //Informamos ao usuário a situação atual;
                    Toast.makeText(getActivity(), messageResult, Toast.LENGTH_LONG).show();

                }
                //Se um dos campos não estiverem preenchidos, retornaremos uma mensagem;
                else {

                    Log.i("Script", "FragmentChamados.pesquisarChamados( chamadoResultList vazio );");

                    //Informamos ao usuário a situação atual;
                    Toast.makeText(getActivity(), messageResult, Toast.LENGTH_LONG).show();
                }
            }
        }, 3000);
    }

    public interface FragmentChamadosListener {

        public void onTranferirIntentChamados();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            Log.i("Script", "FragmentSolicitacaoMateriais.onAttach()");
            mCallback = (FragmentChamadosListener) context;
        } catch (ClassCastException e) {

            Log.i("Script", "FragmentSolicitacaoMateriais.onAttach("+ e +")");
            throw new ClassCastException(context.toString()
                    + " precisa implementar FragmentMateriaisListener");
        }
    }

    /*

        MÉTODOS DO TRANSACTION

    */

    //Método de conexão Volley.
    public void callVolleyRequest(String url) {

        Log.i("Script", "FragmentChamados.callVolleyRequest()");

        //Iniciamos uma conexão direta com o Volley - Inserir Jogador;
        NetworkConnection.getInstance(getActivity().getApplicationContext()).execute(
                this,
                RecicladorActivity.class.getName(),
                url);

    }

    //Método para desconectar o Volley;
    public void desconnectVolleyRequest() {

        Log.i("Script", "FragmentSolicitacaoMateriais.desconnectVolleyRequest()");
        //Desconectamos através do name da conexão;
        NetworkConnection.getInstance(getContext()).getRequestQueue().cancelAll( RecicladorActivity.class.getName() );
    }

    //Método iniciado antes da execução do networkConnection
    @Override
    public WrapObjToNetwork doBefore() {

        //Verificamos qual o tipo de conexão a ser estabelecida;
        //Caso seja para consulta de solicitações;
        if (metodoNetworkConnection.contains("Consultar materiais da solicitacao")) {

            Log.i("Script", "FragmentChamados.onBefore(Consulta Materiais)");

            //Instanciamos o objeto a ser usado para transmissão de parâmetros;
            SolicitacaoMateriais solicitacaoMateriais = new SolicitacaoMateriais();
            //Atribuimos o id respectivo da solicitação atual;
            solicitacaoMateriais.setFkSolicitacao(idSolicitacaoAtual);
            //Retornamos para a conexão Volley;
            return ( new WrapObjToNetwork(
                    solicitacaoMateriais,
                    "java-web-jor",
                    urlConnection) );

        } else
        //Caso seja para consulta de materiais de uma solicitação;
        if (metodoNetworkConnection.contains("Consultar solicitações")) {

            Log.i("Script", "FragmentChamados.onBefore(Consulta Solicitacoes)");

            //Instanciar o objeto de chamado;
            Chamado chamado = new Chamado();
            //Buscaremos pelas solicitações abordadas como em estado de aguarde;
            chamado.setEstadoAtualColeta("Aguardando atendimento...");
            //Retornamos para a conexão os parâmetros para o Volley;
            return ( new WrapObjToNetwork(
                    chamado,
                    "java-web-jor",
                    urlConnection) );
        } else {

            Log.i("Script", "FragmentChamados.onBefore(Não informado)");
            return null;
        }
    }

    @Override
    public void doAfter(JSONArray jsonArray) {

        Log.i("Script", "FragmentFormaReciclador.doAfter()");

        //Verificamos se a conexão retornou algum dado...
        if ( jsonArray != null) {

            Log.i("Script", "FragmentChamados.doAfter( "+ jsonArray +" )");

            //Verificamos qual o tipo de conexão a ser estabelecida;
            //Caso seja para consulta de solicitações;
            if (metodoNetworkConnection.contains("Consultar materiais da solicitacao")) {

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    //Atribuímos uma nova instanciação a list;
                    mSolicitacaoMateriaisList = new ArrayList<SolicitacaoMateriais>();

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
                            mSolicitacaoMateriaisList.add(solicitacaoMateriaisResult);
                        }

                        //Mensagem ao sistema que a operação foi realizada com sucesso.
                        messageResult = "Materiais consultados com sucesso";
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            } else
                //Caso seja para consulta de materiais de uma solicitação;
            if (metodoNetworkConnection.contains("Consultar solicitações")) {

                //Como iremos realizar operações com JSONArray, será necessário este try
                try {

                    //Atribuímos objeto de list;
                    mChamados = new ArrayList<>();

                    //Atribuimos o resultado da primeira linha para verificação da sua procedencia;
                    String estadoAtualColeta = jsonArray.getJSONObject(0).getString("estadoAtualColeta");

                    //Verificamos se este primeiro resultado NÃO possui mensagem de vazio;
                    if (!estadoAtualColeta.contains("Nenhum usuário foi encontrado")) {

                        //Verificamos todos os chamados a serem exibidos ao usuário;
                        for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {

                            //Atribuimos o objeto de chamado para inserção na list;
                            Chamado chamadoResult = new Chamado();

                            //Resultado - idSolicitacaoColeta;
                            chamadoResult.setIdSolicitacaoColeta(jsonArray.getJSONObject(i).getInt("idSolicitacaoColeta"));
                            //Resultado - estadoAtualColeta;
                            chamadoResult.setEstadoAtualColeta(jsonArray.getJSONObject(i).getString("estadoAtualColeta"));
                            //Resultado - inicioSolicitacaoColeta;
                            chamadoResult.setInicioSolicitacaoColeta(jsonArray.getJSONObject(i).getString("inicioSolicitacaoColeta"));
                            //Resultado - idContribuinteColeta;
                            chamadoResult.setIdContribuinteColeta(jsonArray.getJSONObject(i).getInt("idContribuinteColeta"));
                            //Resultado - nomeContribuinteColeta;
                            chamadoResult.setNomeContribuinteColeta(jsonArray.getJSONObject(i).getString("nomeContribuinteColeta"));
                            //Resultado - idContribuinteEnderecoColeta;
                            chamadoResult.setIdContribuinteEnderecoColeta(jsonArray.getJSONObject(i).getInt("idContribuinteEnderecoColeta"));
                            //Resultado - enderecoContribuinteColeta;
                            chamadoResult.setEnderecoContribuinteColeta(jsonArray.getJSONObject(i).getString("enderecoContribuinteColeta"));
                            //Adicionamos o objeto para manipulação futura;
                            mChamados.add(chamadoResult);
                        }

                        //Mensagem ao sistema que a operação foi realizada com sucesso.
                        messageResult = "Chamados consultados com sucesso";

                    }
                    //Estando vazio...
                    else {

                        //A variável de mensagem recebe o conteúdo a ser exibido ao usuário como ERRO;
                        messageResult = estadoAtualColeta;
                    }



                } catch (JSONException e) {

                    e.printStackTrace();
                }

            } else {

                Log.i("Script", "FragmentChamados.onBefore(Não informado)");
            }

        } else {

            Log.i("Script", "Algum problema foi detectado no Volley");
        }
    }
}
