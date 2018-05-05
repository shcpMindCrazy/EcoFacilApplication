package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.ContribuinteEndereco;
import br.com.testes.ecofacil.R;

/**
 * Created by samue on 12/03/2018.
 */

public class FragmentSolicitacaoEndereco extends Fragment implements Transaction {

    public static String enderecoEscolhido;
    public static ContribuinteEndereco contribuinteEnderecoAtual;
    public static String actionFragment;

    int witchEndereco;
    String[] listaEnderecos;
    String enderecoAtual;

    Button btnSolicitarEnderecos;
    Button btnEnviarEndereco;
    TextView txtSolicitarNomeEnderecoResultado;
    TextView txtSolicitarNumeroEnderecoResultado;
    TextView txtSolicitarCepEnderecoResultado;
    TextView txtSolicitarCidadeEnderecoResultado;
    TextView txtSolicitarEstadoEnderecoResultado;

    FragmentEnderecoListener mCallback;

    //Volley - Requisitos para consulta;
    Contribuinte contribuinteAtual;
    List<ContribuinteEndereco> contribuinteEnderecos;
    //Volley - Requisitos para conexão;
    String metodoNetworkConnection;
    String urlConnection = "";
    String messageResult = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Instanciamos a view para utilização do layout;
        View view = inflater.inflate(R.layout.fragment_contribuinte_endereco, container, false);

        Log.i("Script", "FragmentSolicitacaoEndereco.onCreateView();");
        //Atribuímos as variáveis anteriores neste contexto;
        requestResourceBundle();
        //Buscamos pelos endereços cadastrados pelo usuário;
        //procurarContribuinteEnderecos();
        //Inicializamos os componentes;
        initializeComponents(view);
        //Inicializamos os listeners dos botões;
        initializeListenersButton();
        //Retornamos a view para o método;
        return view;
    }
    /*

       Métodos para configuração dos componentes;

    */
    //Método para receber as variáveis da activity/fragment anterior;
    public void requestResourceBundle() {

        Log.i("Script", "ContribuinteActivity.requestResourceBundle();");
        //Instanciamos as variáveis vinda deste Fragment;
        Bundle bundleResult = getArguments();
        //Se o fragment não estiver nulo de arguments...
        if (bundleResult != null) {

            //Usuario contribuinte cadastrado;
            contribuinteAtual = (Contribuinte) bundleResult.getSerializable("Usuario");
            //Ação a ser realizada durante e após esta parte;
            actionFragment = bundleResult.getString("Action");
        }
    }
    //Método para instanciar componentes nesta classe;
    public void initializeComponents(View view) {

        //Buttons
        btnSolicitarEnderecos = (Button) view.findViewById(R.id.btnSolicitarEnderecos);
        btnEnviarEndereco = (Button) view.findViewById(R.id.btnEnderecoEnviar);
        //TextView's
        txtSolicitarNomeEnderecoResultado = (TextView) view.findViewById(R.id.txtSolicitarNomeEnderecoResultado);
        txtSolicitarNumeroEnderecoResultado  = (TextView) view.findViewById(R.id.txtSolicitarNumeroEnderecoResultado);;
        txtSolicitarCepEnderecoResultado  = (TextView) view.findViewById(R.id.txtSolicitarCepEnderecoResultado);;
        txtSolicitarCidadeEnderecoResultado  = (TextView) view.findViewById(R.id.txtSolicitarCidadeEnderecoResultado);;
        txtSolicitarEstadoEnderecoResultado  = (TextView) view.findViewById(R.id.txtSolicitarEstadoEnderecoResultado);;

    }
    //Método para inicializar os listeners dos botões;
    public void initializeListenersButton() {

        //Método para acionar uma janela de seleção de enderecos
        btnSolicitarEnderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Inicializamos a conexão Volley;
                urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarContribuinteEnderecoForUser.php";
                callVolleyRequest(urlConnection);
            }
        });

        //Método para contabilizar o endereco;
        btnEnviarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Caso algum endereco tenha sido selecionado...
                if (enderecoAtual != null) {

                    //Direcionamos aos materiais;
                    actionFragment = "materiais";
                    //E também, transmitimos o endereco selecionado para a proxima activity;
                    enderecoEscolhido = enderecoAtual;
                    //Iniciamos a transição de variáveis(Fragment - Activity Pai);
                    mCallback.onTranferirIntentEndereco();
                }
                //Se não...
                else {

                    //Mensagem para o usuário;
                    Toast.makeText(getActivity(), "Selecione um endereco para prosseguir clicando no botão 'Enderecos'", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*

        Métodos de rotina de execução;

    */
    //Método para iniciar a procura pelos enderecos cadastrados do contribuinte;
    public void mostrarAlertDialogEnderecos() {

        //Instanciamos as configurações para o alertDialog;
        DialogFragment dialogFragment = new AddressDialogFragment();
        //Iniciamos e mostramos os componentes da messageBox dando um nome a eles;
        dialogFragment.show(getActivity().getFragmentManager(), "adressDialog");
    }
    /*

        Métodos para transferência/acesso a activity;

    */
    public interface FragmentEnderecoListener {

        public void onTranferirIntentEndereco();

        public void onTransferirIntentEnderecoNovo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (FragmentEnderecoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " precisa implementar FragmentMateriaisListener");
        }
    }

    @SuppressLint("ValidFragment")
    public class AddressDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction

            //Instanciamos esta variável para receber os enderecos como string completa;
            //Final, por estar dentro de um builder a sua usabilidade;
            final String[] listaEnderecos = new String[contribuinteEnderecos.size() + 1];
            //Manipulamos o arrayList de objetos para um vetor, a ser inserido na caixa de diálogo logo depois;
            for (int i = 0; i < contribuinteEnderecos.size(); i++) {

                Log.i("Script", "FragmentSolicitacaoEndereco.onCreateDialog(" + contribuinteEnderecos.get(i).toString() + ")");
                //Adicionamos o endereco por extenso em formato do google maps e selecionamos aqui.
                listaEnderecos[i] = contribuinteEnderecos.get(i).toString();
            }
            listaEnderecos[contribuinteEnderecos.size()] = "Cadastrar outro endereço";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Escolha de retirada")
                    .setSingleChoiceItems(listaEnderecos, -1, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (listaEnderecos[which].contains("Cadastrar outro endereço")) {

                                        Toast.makeText(getActivity(), "Novo endereco a ser criado", Toast.LENGTH_SHORT).show();
                                        enderecoAtual = "novo";

                                    } else {

                                        //Informação ao usuário;
                                        Toast.makeText(getActivity(), "Endereco: " + listaEnderecos[which], Toast.LENGTH_SHORT).show();
                                        //O endereco atual em string é passado para esta variável;
                                        enderecoAtual = listaEnderecos[which];
                                        //A posição do vetor pertecente a este endereco também é transmitido;
                                        witchEndereco = which;
                                    }
                                }
                            })

                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (enderecoAtual.contains("novo")) {

                                Toast.makeText(getActivity(), "Redireciona a página de endereco criar", Toast.LENGTH_SHORT).show();

                                //Direcionamos a página para cadastrar um novo endereco;
                                actionFragment = "novoEndereco";
                                //Iniciamos a transição de variáveis(Fragment - Activity Pai);
                                mCallback.onTransferirIntentEnderecoNovo();

                            } else {

                                //Mensagem para o usuário;
                                Toast.makeText(getActivity(), "Endereço selecionado. Atualizando interface...", Toast.LENGTH_SHORT).show();

                                //Instanciamos um objeto de acordo com o resultado obtido na alertDialog;
                                ContribuinteEndereco contribuinteEnderecoSelecionado = contribuinteEnderecos.get(witchEndereco);

                                Log.i("Script", "FragmentSolicitacaoEndereco.onCreateDialog(" + contribuinteEnderecoSelecionado.getidContribuinteEndereco() + ")");

                                contribuinteEnderecoAtual = contribuinteEnderecoSelecionado;

                                //Atribuímos os valores retornados para as textView da interface;
                                txtSolicitarNomeEnderecoResultado.setText(contribuinteEnderecoSelecionado.getNomeEndereco());
                                txtSolicitarNumeroEnderecoResultado.setText(contribuinteEnderecoSelecionado.getNumeroEndereco());
                                txtSolicitarCepEnderecoResultado.setText(contribuinteEnderecoSelecionado.getCepEndereco());
                                txtSolicitarCidadeEnderecoResultado.setText(contribuinteEnderecoSelecionado.getCidadeEndereco());
                                txtSolicitarEstadoEnderecoResultado.setText(contribuinteEnderecoSelecionado.getEstadoEndereco());
                            }
                        }
                    })
                    .setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Toast.makeText(getActivity(), "Cancela", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        @Override
        public int show(FragmentTransaction transaction, String tag) {
            return super.show(transaction, tag);
        }
    }
    /*

        Métodos para conexão Volley;

    */
    //Método de conexão Volley.
    public void callVolleyRequest(String url) {

        Log.i("Script", "FragmentSolicitacaoEndereco.callVolleyRequest()");

        //Iniciamos uma conexão direta com o Volley - Inserir Jogador;
        NetworkConnection.getInstance(getActivity().getApplicationContext()).execute(
                this,
                ContribuinteSolicitacaoActivity.class.getName(),
                url);

    }
    //Método para desconectar o Volley;
    public void desconnectVolleyRequest() {

        //Desconectamos através do name da conexão;
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll( ContribuinteSolicitacaoActivity.class.getName() );
    }
    //Antes de se iniciar a conexão volley;
    @Override
    public WrapObjToNetwork doBefore() {

        Log.i("Script", "FragmentSolicitacaoEndereco.onBefore(Enderecos)");

        //Instanciamos o objeto de contribuinte;
        Contribuinte contribuinte = contribuinteAtual;

        //Verificamos se o usuário atual possui suas informações no objeto atual;
        if (contribuinte.getIdContribuinte() != 0) {

            //Iniciamos a conexão volley;
            return ( new WrapObjToNetwork(
                    contribuinte,
                    "java-web-jor",
                    urlConnection) );
        }
        //Caso contrário, houve algum erro interno do sistema;
        else {

            Log.i("Script", "FragmentSolicitacaoEndereco.doBefore(Enderecos - Erro)");

            return null;
        }
    }
    //Depois do resultado da conexão ao servidor;
    @Override
    public void doAfter(JSONArray jsonArray) {

        Log.i("Script", "FragmentSolicitacaoEndereco.doAfter()");

        //Verificamos se a conexão retornou algum dado...
        if ( jsonArray != null) {

            //Instanciamos a list para inserir os enderecos apartir do objeto de ContribuinteEndereco;
            contribuinteEnderecos = new ArrayList<ContribuinteEndereco>();

            Log.i("Script", "FragmentSolicitacaoEndereco.doAfter( "+ jsonArray +" )");
            //Como iremos realizar operações com JSONArray, será necessário este try
            try {

                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {

                    //Objeto a receber o endereco;
                    ContribuinteEndereco contribuinteEnderecoAtual = new ContribuinteEndereco();

                    //Instanciamos os valores do número da linha atual a ser tratada;
                    contribuinteEnderecoAtual.setidContribuinteEndereco(jsonArray.getJSONObject(i).getInt("idContribuinteEndereco"));
                    contribuinteEnderecoAtual.setfkContribuinte(jsonArray.getJSONObject(i).getInt("fkContribuinte"));
                    contribuinteEnderecoAtual.setNomeEndereco(jsonArray.getJSONObject(i).getString("nomeEndereco"));
                    contribuinteEnderecoAtual.setNumeroEndereco(jsonArray.getJSONObject(i).getString("numeroEndereco"));
                    contribuinteEnderecoAtual.setBairroEndereco(jsonArray.getJSONObject(i).getString("bairroEndereco"));
                    contribuinteEnderecoAtual.setCidadeEndereco(jsonArray.getJSONObject(i).getString("cidadeEndereco"));
                    contribuinteEnderecoAtual.setEstadoEndereco(jsonArray.getJSONObject(i).getString("estadoEndereco"));
                    contribuinteEnderecoAtual.setCepEndereco(jsonArray.getJSONObject(i).getString("cepEndereco"));
                    contribuinteEnderecoAtual.setComplementoEndereco(jsonArray.getJSONObject(i).getString("complementoEndereco"));

                    //Adicionamos o valor na lista de enderecos;
                    contribuinteEnderecos.add(contribuinteEnderecoAtual);
                }
            }
            catch (JSONException e) {

                Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter( " + e + " )");
                Toast.makeText(getActivity(), "Houve um erro na execução do aplicativo com a rede, contate o suporte", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            //Ao terminar a operação, desconetamos o volley;
            desconnectVolleyRequest();
            //Iniciamos a rotina para atribuir valores a list da AlertDialog;
            mostrarAlertDialogEnderecos();
        }
        else {

            Log.i("Script", "Algum problema foi detectado no Volley");
        }
    }
}
