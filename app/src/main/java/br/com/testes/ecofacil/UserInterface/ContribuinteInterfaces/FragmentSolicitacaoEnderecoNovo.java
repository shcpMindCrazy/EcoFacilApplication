package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class FragmentSolicitacaoEnderecoNovo extends Fragment implements Transaction {

    //Variaveis Globais;
    String a;

    //Volley - Requisitos para consulta;
    Contribuinte contribuinteAtual;
    List<ContribuinteEndereco> contribuinteEnderecos;
    //Volley - Requisitos para conexão;
    String urlConnection = "";
    String messageResult = "";
    String statusQuery = "";

    //Componentes Visuais;
    Button btnEnderecoCadastrar;
    EditText etEnderecoNome;
    EditText etEnderecoNumero;
    EditText etEnderecoBairro;
    EditText etEnderecoCep;
    EditText etEnderecoCidade;
    EditText etEnderecoEstado;
    EditText etEnderecoComplemento;


    FragmentEnderecoNovoListener mCallback;

    public static String actionFragment;

    ArrayList<EditText> editTextsEndereco = new ArrayList<>(5);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Instanciamos a view para utilização do layout;
        View view = inflater.inflate(R.layout.fragment_contribuinte_endereco_novo, container, false);

        //Instanciamos as variáveis vinda deste Fragment;
        Bundle bundleResult = getArguments();

        //Se o fragment não estiver nulo de arguments...
        if (bundleResult != null) {

            //Usuario contribuinte cadastrado;
            contribuinteAtual = (Contribuinte) bundleResult.getSerializable("Usuario");
            //Ação a ser realizada durante e após esta parte;
            actionFragment = bundleResult.getString("Action");
            //LogCat;
            Log.i("Script", "FragmentSolicitacaoEnderecoNovo.onCreateView( Usuário: "+ contribuinteAtual.getNomeContribuinte() + " - Email: " + contribuinteAtual.getEmailContribuinte() +" Action: " + actionFragment + ") ");
        }

        //Iniciamos os componentes visuais;
        initializeComponents(view);
        //Iniciamos os listeners de buttons;
        initializeListenersButtons(view);

        return view;
    }

    public interface FragmentEnderecoNovoListener {

        public void onTransferirIntentEnderecoNovoReturn();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (FragmentSolicitacaoEnderecoNovo.FragmentEnderecoNovoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " precisa implementar FragmentEnderecoNovoListener");
        }
    }

    /*@Override
    public void finish() {

        //Inicializamos o intent;
        Intent intentResult = new Intent();
        //Gravamos a variável de endereco no Serializable de endereco
        intentResult.putExtra("endereco", etEnderecoNome.getText().toString());
        //Atribuimos como resultCode 1
        setResult(1, intentResult);
        //Encerramos esta activity;
        super.finish();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }*/

    public void initializeComponents(View view) {

        btnEnderecoCadastrar = (Button) view.findViewById(R.id.btnContribuinteEnderecoCadastrar);
        etEnderecoNome = (EditText) view.findViewById(R.id.etContribuinteEnderecoNome);
        etEnderecoNumero = (EditText) view.findViewById(R.id.etContribuinteEnderecoNumero);
        etEnderecoBairro = (EditText) view.findViewById(R.id.etContribuinteEnderecoBairro);
        etEnderecoCep = (EditText) view.findViewById(R.id.etContribuinteEnderecoCep);
        etEnderecoCidade = (EditText) view.findViewById(R.id.etContribuinteEnderecoCidade);
        etEnderecoEstado = (EditText) view.findViewById(R.id.etContribuinteEnderecoEstado);
        etEnderecoComplemento = (EditText) view.findViewById(R.id.etContribuinteEnderecoComplemento);

        editTextsEndereco.add(etEnderecoNome);
        editTextsEndereco.add(etEnderecoNumero);
        editTextsEndereco.add(etEnderecoBairro);
        editTextsEndereco.add(etEnderecoCep);
        editTextsEndereco.add(etEnderecoCidade);
        editTextsEndereco.add(etEnderecoEstado);
        editTextsEndereco.add(etEnderecoComplemento);
    }

    public void initializeListenersButtons(View view) {

        //Verificação LOG;
        Log.i("Script", "FragmentSolicitacaoEnderecoNovo.initializeListenersButtons();");

        //Método de click no botão de cadastrar novo endereço;
        btnEnderecoCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Verificamos se os campos de texto estão preenchidos ou não;
                if (validaCampo(editTextsEndereco)) {

                    //Atribuímos a string de conexão;
                    urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/InserirContribuinteEnderecoNovo.php";
                    //Instanciamos a conexão volley;
                    callVolleyRequest(urlConnection);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Log.i("Script", "FragmentSolicitacaoEnderecoNovo.initializeListenersButtons( postDelayed );");
                            if (messageResult.contains("sucesso")) {

                                Log.i("Script", "FragmentSolicitacaoEnderecoNovo.doAfter(Sucesso)");
                                Toast.makeText(getActivity(), messageResult, Toast.LENGTH_SHORT).show();
                            } else
                            if (messageResult.contains("Erro")){

                                Log.i("Script", "FragmentSolicitacaoEnderecoNovo.doAfter(Erro)");
                                Toast.makeText(getActivity(), messageResult, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getActivity(), "Houve algum problema interno com o sistema, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                            }
                            //Encerramos a conexão volley;
                            desconnectVolleyRequest();
                            //Atribuimos o action de endereco/materiais novamente;
                            actionFragment = "endereco/materiais";
                            //Assim, retornamos a seleção de endereco a ser retirado os materiais;
                            mCallback.onTransferirIntentEnderecoNovoReturn();
                        }
                    }, 3000);
                } else {

                    //Informamos que falta informações para prosseguimento;
                    Toast.makeText(getActivity(), "Verifique se todos os campos estão preenchidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected boolean validaCampo( ArrayList<EditText> etTodas){

        //Contador de campos preenchidos;
        int contVerify = 0;

        //Entramos em um loop para verificar se as caixas de texto estão preenchidas
        for (EditText origem : etTodas) {

            //Caso a caixa de texto estiver vazia...
            if ( origem.getText().toString().equals("") ) {

                //Já retornamos com erro ao método;
                return false;
            }
            //Senão...
            else {

                //Adicionamos um ao contador de caixas preenchidas;
                contVerify += 1;
            }
        }

        //Se todas as caixas estiverem preenchidas (No total de 5)
        if (contVerify > 4) {

            //Retornamos que tudo está ok para o método;
            return true;

        }
        //Se não...
        else {

            //Já retornamos com erro ao método;
            return false;
        }
    }

    /*
    *
    *  MÉTODOS DE CONNECTION VOLLEY;
    *
    * */
    //Método de conexão Volley.
    public void callVolleyRequest(String url) {

        Log.i("Script", "FragmentSolicitacaoEnderecoNovo.callVolleyRequest()");

        //Iniciamos uma conexão direta com o Volley - Inserir Jogador;
        NetworkConnection.getInstance(getActivity().getApplicationContext()).execute(
                this,
                ContribuinteSolicitacaoActivity.class.getName(),
                url);

    }
    //Método para desconectar o Volley;
    public void desconnectVolleyRequest() {

        Log.i("Script", "FragmentSolicitacaoEnderecoNovo.desconnectVolleyRequest()");
        //Desconectamos através do name da conexão;
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll( ContribuinteSolicitacaoActivity.class.getName() );
    }
    //Antes de se iniciar a conexão volley;
    @Override
    public WrapObjToNetwork doBefore() {

        Log.i("Script", "FragmentSolicitacaoEnderecoNovo.onBefore(Enderecos)");

        //Instanciamos o objeto de endereco do contribuinte;
        ContribuinteEndereco contribuinteEndereco = new ContribuinteEndereco();

        //Atribuímos os valores do endereco para este objeto;
        contribuinteEndereco.setfkContribuinte(contribuinteAtual.getIdContribuinte());
        contribuinteEndereco.setNomeEndereco(etEnderecoNome.getText().toString());
        contribuinteEndereco.setNumeroEndereco(etEnderecoNumero.getText().toString());
        contribuinteEndereco.setBairroEndereco(etEnderecoBairro.getText().toString());
        contribuinteEndereco.setCepEndereco(etEnderecoCep.getText().toString());
        contribuinteEndereco.setCidadeEndereco(etEnderecoCidade.getText().toString());
        contribuinteEndereco.setEstadoEndereco(etEnderecoEstado.getText().toString());
        contribuinteEndereco.setComplementoEndereco(etEnderecoComplemento.getText().toString());

        //Iniciamos a conexao Volley;
        return ( new WrapObjToNetwork(
                contribuinteEndereco,
                "jar-web-jor",
                urlConnection
        ));
    }
    //Depois do resultado da conexão ao servidor;
    @Override
    public void doAfter(JSONArray jsonArray) {

        Log.i("Script", "FragmentSolicitacaoEnderecoNovo.doAfter()");

        //Verificamos se a conexão retornou algum dado...
        if ( jsonArray != null) {

            Log.i("Script", "FragmentSolicitacaoEnderecoNovo.doAfter( "+ jsonArray +" )");

            //Como iremos realizar operações com JSONArray, será necessário este try
            try {

                //Transferimos o resultado vindo do banco;
                messageResult = jsonArray.getJSONObject(0).getString("resultQuery");
            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }
        else {

            Log.i("Script", "Algum problema foi detectado no Volley");
        }
    }
}
