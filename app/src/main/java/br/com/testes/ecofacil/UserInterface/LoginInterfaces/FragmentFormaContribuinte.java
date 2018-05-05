package br.com.testes.ecofacil.UserInterface.LoginInterfaces;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.R;
import br.com.testes.ecofacil.TextFieldMasked.MaskUtil;
import br.com.testes.ecofacil.TextFieldValidacoes.ValidaCNPJ;
import br.com.testes.ecofacil.TextFieldValidacoes.ValidaCPF;
import br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces.ContribuinteActivity;
import br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces.ContribuinteHistoricoSolicitacoesActivity;

/**
 * Created by samue on 08/04/2018.
 */

public class FragmentFormaContribuinte extends Fragment implements Transaction {

    //Componentes Visuais;
    TextView tvSolicitarSenha;
    TextView tvSolicitarCadastro;
    EditText etUsuario;
    EditText etPassword;
    RadioGroup rgFormaLogin;
    RadioButton rbFormaUserCpf;
    RadioButton rbFormaUserEmail;
    Button btnIniciarSessao;

    //Variáveis Globais;
    //Volley - Requisitos para consulta;
    Contribuinte user_contribuinte;
    String loginUsuario = "";
    String senhaUsuario = "";
    boolean verificacaoCPF = true;
    //Volley - Requisitos para conexão;
    String metodoNetworkConnection;
    String urlConnection = "";
    String messageResult = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forma_contribuinte, container, false);

        //Instanciamos os componentes visuais deste layout;
        initializeComponents(view);
        //Instanciamos o componente Button para login do usuário;
        initializeListenerButtonLogin(view);
        //Para manipulação do tipo de login selecionado;
        initializeListenerRadioButtons();

        return view;
    }
    /*

       Métodos para configuração dos componentes;

    */
    //Método para instanciar os componentes visuais sem listeners;
    public void initializeComponents(View view) {

        Log.i("Script", "FragmentFormaReciclador.initializeComponents();");
        //TextView's;
        tvSolicitarCadastro = (TextView) view.findViewById(R.id.tvSolicitarCadastro);
        tvSolicitarSenha = (TextView) view.findViewById(R.id.tvSolicitarSenha);
        //EditText's;
        etUsuario = (EditText) view.findViewById(R.id.etUsuario);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        //RadioGroup e RadioButton's;
        rgFormaLogin = (RadioGroup) view.findViewById(R.id.rgFormaLogin);
        rbFormaUserEmail = (RadioButton) view.findViewById(R.id.rbUserContribuinteEmail);
        rbFormaUserCpf = (RadioButton) view.findViewById(R.id.rbUserContribuinteCpf);
    }
    //Método para instanciar o botão de logar contribuinte;
    public void initializeListenerButtonLogin(View view) {

        Log.i("Script", "FragmentFormaReciclador.initializeListenerButtonLogin();");
        //Instanciamos o button para a classe;
        btnIniciarSessao = (Button) view.findViewById(R.id.btnIniciarSessao);
        //Inicializamos o método listener no button;
        btnIniciarSessao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Verificamos se o usuário e senha foram fornecidos;
                if (validaCampoPreenchido(etUsuario) && validaCampoPreenchido(etPassword)) {

                    //É recebido o nome e senha do usuário para confirmação no banco de dados;
                    loginUsuario = etUsuario.getText().toString();
                    senhaUsuario = etPassword.getText().toString();

                    Log.i("Script", "FragmentFormaReciclador.initializeButtonLogin('Contribuinte');");
                    //Verificamos qual a forma de login sendo requisitada;
                    if (metodoNetworkConnection.contains("Email")) {

                        Log.i("Script", "FragmentFormaReciclador.initializeListenerButtonLogin("+ metodoNetworkConnection +")");
                        //Instanciamos o endereco do Connection Volley para uso do email como login;
                        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarContribuinteForLoginEmail.php";
                        //urlConnection = "http://ecofacil.com.br/serverapp/ConsultarContribuinteForLoginEmail.php";
                    } else
                    //Caso, não ser o email, será o cpf;
                    if (metodoNetworkConnection.contains("CPF")) {

                        Log.i("Script", "FragmentFormaReciclador.initializeListenerButtonLogin("+ metodoNetworkConnection +")");
                        //Instanciamos o endereco do Connection Volley para uso do CPF como login;
                        urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarContribuinteForLoginCPF.php";

                        //Verificamos se este cpf digitado é considerado válido;
                        if (rbFormaUserCpf.isChecked()) {

                            //Campo de usuário contendo somente os números para validação;
                            String documentoNumber = apenasNumeros(etUsuario.getText().toString());

                            if (!ValidaCPF.isCPF(documentoNumber)) {

                                //Impedimos o prosseguimento da verificação no banco;
                                verificacaoCPF = false;

                                //Informamos ao usuário sobre o cnpj informado inválido;
                                Toast.makeText(getActivity(), "CPF informado inválido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    //Caso o cpf tenha sido verificado como verdadeiro, ou nem tenha sido verificado, prosseguimos...
                    if (verificacaoCPF) {

                        //Iniciamos a conexão volley;
                        callVolleyRequest(urlConnection);
                    }
                }
            }
        });

    }
    //Método para instanciar os radioButtons para alteração de forma de login;
    public void initializeListenerRadioButtons() {

        //Verificação LOG;
        Log.i("Script", "AccessActivity.initializeListenerRadioButtons();");

        final TextWatcher twDefault = MaskUtil.defaultText(etUsuario);
        final TextWatcher twMask = MaskUtil.insert(etUsuario);

        //Já estipulamos uma forma de login inicial;
        rbFormaUserEmail.setChecked(true);
        etUsuario.addTextChangedListener(twDefault);
        etUsuario.setHint("Email do Contribuinte");
        metodoNetworkConnection = "Email";

        //Quando algum radioButton for alterado de seleção;
        rgFormaLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                //Verificação LOG;
                Log.i("Script", "AccessActivity.onCheckedChanged()");

                //Se a forma de login do doador for ativado... faça
                if (rbFormaUserEmail.isChecked()) {

                    //Verificação LOG;
                    Log.i("Script", "AccessActivity.onCheckedChanged('Contribuinte')");
                    //Esvaziamos o usuário e senha ao mudar de tipo;
                    etUsuario.setText("");
                    etPassword.setText("");
                    etUsuario.removeTextChangedListener(twMask);
                    etUsuario.addTextChangedListener(twDefault);
                    //Adaptamos a informação para o usuário doador;
                    etUsuario.setHint("Email do Contribuinte");
                    //Informamos o tipo de login atual a ser pesquisado;
                    metodoNetworkConnection = "Email";
                }
                //Se a forma de login do ecoponto for ativado... faça
                else if (rbFormaUserCpf.isChecked()) {

                    //Verificação LOG;
                    Log.i("Script", "AccessActivity.onCheckedChanged('Ecoponto')");

                    //Instanciamos a mascara de texto para cpf/cnpj no campo de Usuário;
                    //Esvaziamos o usuário e senha ao mudar de tipo;
                    etUsuario.setText("");
                    etPassword.setText("");
                    etUsuario.removeTextChangedListener(twDefault);
                    etUsuario.addTextChangedListener(twMask);
                    //Adaptamos a informação para o ecoponto;
                    etUsuario.setHint("CPF do Contribuinte");
                    //Informamos o tipo de login atual a ser pesquisado;
                    metodoNetworkConnection = "CPF";
                }
                //Caso não tenha nenhum selecionado;
                else {

                    //Desativamos a caixa de texto e damos um hint a ela. (Reseta o text)
                    etUsuario.setText("");
                    etUsuario.setEnabled(false);
                    etUsuario.setHint("Selecione a forma de login");

                    etPassword.setText("");
                }
            }
        });
    }
    //Quando a TextView de Esqueci a senha for clicado;
    public void clickTextViewEsqueciSenha(View view) {

        //Verificação LOG;
        Log.i("Script", "AccessActivity.clickTextViewEsqueciSenha();");
    }
    //Quando a TextView de Criar uma conta for ativada;
    public void clickTextViewCriarConta(View view) {

        //Verificação LOG;
        Log.i("Script", "AccessActivity.clickTextViewCriarConta();");
    }
    /*

        Métodos de rotina de execução;

    */
    //Método pós-volley, para verificação dos dados retornados do usuário;
    public void acessoUsuarioContribuinte(Contribuinte contribuinteAtual) {

        Log.i("Script", "FragmentFormaReciclador.acessoUsuarioContribuinte();");

        //Verificamos se não houve resultados no banco de dados;
        if (contribuinteAtual.getIdContribuinte() != 0) {

            //Informamos ao usuário a situação atual;
            Toast.makeText(getActivity(), "Logado como Contribuinte", Toast.LENGTH_SHORT).show();
            //Iniciamos a tela do ecoponto;
            Intent intentDoador = new Intent(getActivity(), ContribuinteActivity.class);
            intentDoador.putExtra("Contribuinte", contribuinteAtual);
            startActivity(intentDoador);

        }
        //Se um dos campos não estiverem preenchidos, retornaremos uma mensagem;
        else {

            Log.i("Script", "FragmentFormaReciclador.clickButtonIniciarSessao('Parametros Vazios');");
            //Informamos ao usuário a situação atual;
            Toast.makeText(getActivity(), "Usuario ou senha informado está incorreto, tente novamente", Toast.LENGTH_LONG).show();
        }
    }
    /*

        Métodos para conexão Volley;

    */
    //Método de conexão Volley.
    public void callVolleyRequest(String url) {

        Log.i("Script", "FragmentFormaReciclador.callVolleyRequest()");

        //Iniciamos uma conexão direta com o Volley - Inserir Jogador;
        NetworkConnection.getInstance(getActivity().getApplicationContext()).execute(
                this,
                AccessActivity.class.getName(),
                url);

    }
    //Método para desconectar o Volley;
    public void desconnectVolleyRequest() {

        Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.desconnectVolleyRequest()");
        //Desconectamos através do name da conexão;
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll( AccessActivity.class.getName() );
    }
    //Método iniciado antes da execução do networkConnection
    @Override
    public WrapObjToNetwork doBefore() {

        Log.i("Script", "FragmentFormaReciclador.onBefore("+ metodoNetworkConnection +")");

        //Instanciamos o objeto de contribuinte;
        Contribuinte contribuinte = new Contribuinte();

        //Instanciamos os parâmetros necessário para a consulta;
        contribuinte.setSenhaContribuinte(senhaUsuario);

        //Verificamos se a forma de login é com o Email...
        if (metodoNetworkConnection.equals("Email")) {

            contribuinte.setEmailContribuinte(loginUsuario);
        } else
        //Verificamos se a forma de login é com o CPF
        if ((metodoNetworkConnection.equals("CPF"))) {

            contribuinte.setCpfContribuinte(loginUsuario);
        }

        return ( new WrapObjToNetwork(
                contribuinte,
                "java-web-jor",
                urlConnection) );
    }
    @Override
    public void doAfter(JSONArray jsonArray) {

        Log.i("Script", "FragmentFormaReciclador.doAfter()");

        //Verificamos se a conexão retornou algum dado...
        if ( jsonArray != null) {

            Log.i("Script", "FragmentFormaReciclador.doAfter( "+ jsonArray +" )");

            //Como iremos realizar operações com JSONArray, será necessário este try
            try {
                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {

                    //Instanciamos o objeto de reciclador para receber os dados do banco;
                    user_contribuinte = new Contribuinte();

                    //Como só existe apenas um usuário retornado, usamos o 0 para capturar os dados do mesmo.
                    user_contribuinte.setIdContribuinte(jsonArray.getJSONObject(i).getInt("idContribuinte"));

                    //Verificamos se o usuário retornado é válido ou não;
                    if (user_contribuinte.getIdContribuinte() != 0) {

                        user_contribuinte.setNomeContribuinte(jsonArray.getJSONObject(i).getString("nomeContribuinte"));
                        user_contribuinte.setSobrenomeContribuinte(jsonArray.getJSONObject(i).getString("sobrenomeContribuinte"));
                        user_contribuinte.setEmailContribuinte(jsonArray.getJSONObject(i).getString("emailContribuinte"));
                        user_contribuinte.setCelularContribuinte(jsonArray.getJSONObject(i).getString("celularContribuinte"));
                        user_contribuinte.setGeneroContribuinte(jsonArray.getJSONObject(i).getString("generoContribuinte"));
                        user_contribuinte.setEstadoCivilContribuinte(jsonArray.getJSONObject(i).getString("estadoCivilContribuinte"));
                        user_contribuinte.setCpfContribuinte(jsonArray.getJSONObject(i).getString("cpfContribuinte"));
                        user_contribuinte.setSenhaContribuinte(jsonArray.getJSONObject(i).getString("senhaContribuinte"));
                    } else {

                        messageResult = jsonArray.getJSONObject(0).getString("nomeContribuinte");
                    }
                }

            } catch (JSONException e) {

                Log.i("Script", "ContribuinteHistoricoSolicitacoesActivity.doAfter( " + e + " )");
                Toast.makeText(getActivity(), "Houve um erro na execução do aplicativo com a rede, contate o suporte", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            //Desconectamos o volley da rede;
            desconnectVolleyRequest();
            //Verificamos se o usuário informou seus dados corretamente;
            acessoUsuarioContribuinte(user_contribuinte);

        } else {

            Log.i("Script", "Algum problema foi detectado no Volley");
        }
    }

    /*

        Métodos para validação de rotinas;

    */

    //Quando necessária a validação de um campo (EditText);
    protected boolean validaCampoPreenchido(EditText origem){

        if ( origem.getText().toString().equals("") ) {

            return false;
        }
        else {

            return true;
        }
    }

    public String apenasNumeros(String text) {

        text = text.replace( " " , ""); //tira espaço em branco
        text = text.replace( "." , ""); //tira ponto
        text = text.replace( "/" , ""); //tira barra
        text = text.replace( "-" , ""); //tira hífen

        return text;
    }
}
