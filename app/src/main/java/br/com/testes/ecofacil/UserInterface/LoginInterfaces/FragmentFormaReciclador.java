package br.com.testes.ecofacil.UserInterface.LoginInterfaces;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;

import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.Reciclador;
import br.com.testes.ecofacil.R;
import br.com.testes.ecofacil.TextFieldMasked.MaskUtil;
import br.com.testes.ecofacil.TextFieldValidacoes.ValidaCNPJ;
import br.com.testes.ecofacil.UserInterface.RecicladorInterfaces.RecicladorActivity;

/**
 * Created by samue on 08/04/2018.
 */

public class FragmentFormaReciclador extends Fragment implements Transaction{

    //Componentes Visuais;
    TextView tvSolicitarSenha;
    TextView tvSolicitarCadastro;
    EditText etUsuario;
    EditText etPassword;
    RadioGroup rgFormaLogin;
    RadioButton rbFormaUserCnpj;
    RadioButton rbFormaUserEmail;
    Button btnIniciarSessao;

    //Variáveis Globais;
    //Volley - Requisitos para consulta;
    Reciclador user_Reciclador;
    String loginUsuario = "";
    String senhaUsuario = "";
    boolean verificacaoCNPJ = true;
    //Volley - Requisitos para conexão;
    String metodoNetworkConnection;
    String urlConnection = "";
    String messageResult = "";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forma_reciclador, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //Inicializamos os componentes;
        initializeComponents(view);

        //Inicializamos o método de click de Iniciar Sessão;
        initializeButtonLogin(view);

        //Inicializamos o método de troca de RadioButton;
        formaLoginForSelectedRadioButton();
    }

    public void initializeComponents(View view) {

        Log.i("Script", "FragmentFormaReciclador.initializeComponents();");

        //Instanciando componentes visuais;
        tvSolicitarCadastro = (TextView) view.findViewById(R.id.tvSolicitarCadastro);
        tvSolicitarSenha = (TextView) view.findViewById(R.id.tvSolicitarSenha);

        etUsuario = (EditText) view.findViewById(R.id.etUsuario);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        rgFormaLogin = (RadioGroup) view.findViewById(R.id.rgFormaLogin);
        rbFormaUserEmail = (RadioButton) view.findViewById(R.id.rbUserRecicladorEmail);
        rbFormaUserCnpj = (RadioButton) view.findViewById(R.id.rbUserRecicladorCnpj);
        btnIniciarSessao = (Button) view.findViewById(R.id.btnIniciarSessao);
    }

    public void initializeButtonLogin(View view) {

        Log.i("Script", "FragmentFormaReciclador.initializeButtonLogin();");

        btnIniciarSessao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Verificamos se o usuário e senha foram fornecidos;
                if (validaCampoPreenchido(etUsuario) && validaCampoPreenchido(etPassword)) {

                    //Verificação LOG;
                    Log.i("Script", "FragmentFormaReciclador.initializeButtonLogin('Contribuinte');");

                    //É recebido o nome e senha do usuário para confirmação no banco de dados;
                    loginUsuario = etUsuario.getText().toString();
                    senhaUsuario = etPassword.getText().toString();

                    //Instanciamos o endereco do Connection Volley;
                    urlConnection = "http://192.168.0.105:8080/EcoFacil_Server/ConsultarRecicladorForLoginEmail.php";

                    //Verificação do CNPJ
                    if (rbFormaUserCnpj.isChecked()) {

                        //Campo de usuário contendo somente os números para validação;
                        String documentoNumber = apenasNumeros(etUsuario.getText().toString());

                        if (!ValidaCNPJ.isCNPJ(documentoNumber)) {

                            //Impedimos o prosseguimento da verificação no banco;
                            verificacaoCNPJ = false;

                            //Informamos ao usuário sobre o cnpj informado inválido;
                            Toast.makeText(getActivity(), "CNPJ informado inválido", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Caso o CPNJ for válido ou seja o login por email, prosseguiremos com o processo de login;
                    if (verificacaoCNPJ) {

                        //Iniciamos a conexão volley;
                        callVolleyRequest(urlConnection);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Log.i("Script", "FragmentFormaReciclador.clickButtonIniciarSessao( postDelayed );");

                                //Verificamos se não houve resultados no banco de dados;
                                if (user_Reciclador.getIdReciclador() != 0) {

                                    //Informamos ao usuário a situação atual;
                                    Toast.makeText(getActivity(), "Logado como Ecoponto", Toast.LENGTH_SHORT).show();

                                    //Iniciamos a tela do ecoponto;
                                    Intent intentDoador = new Intent(getActivity(), RecicladorActivity.class);
                                    intentDoador.putExtra("Reciclador", user_Reciclador);
                                    startActivity(intentDoador);

                                }
                                //Se um dos campos não estiverem preenchidos, retornaremos uma mensagem;
                                else {

                                    //Verificação LOG;
                                    Log.i("Script", "FragmentFormaReciclador.clickButtonIniciarSessao('Parametros Vazios');");

                                    //Informamos ao usuário a situação atual;
                                    Toast.makeText(getActivity(), messageResult, Toast.LENGTH_LONG).show();
                                }
                            }
                        }, 3000);


                    }
                }
            }
        });

    }

    //Método de click para o radioGroup "rgFormaLogin"
    public void formaLoginForSelectedRadioButton() {

        //Verificação LOG;
        Log.i("Script", "FragmentFormaReciclador.formaLoginForSelectedRadioButton();");

        final TextWatcher twDefault = MaskUtil.defaultText(etUsuario);
        final TextWatcher twMask = MaskUtil.insert(etUsuario);

        //Já estipulamos uma forma de login inicial;
        rbFormaUserEmail.setChecked(true);
        etUsuario.addTextChangedListener(twDefault);
        etUsuario.setHint("Email do Reciclador");
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
                    etUsuario.setHint("Email do Reciclador");
                    //Informamos o tipo de login atual a ser pesquisado;
                    metodoNetworkConnection = "Email";
                }
                //Se a forma de login do ecoponto for ativado... faça
                else if (rbFormaUserCnpj.isChecked()) {

                    //Verificação LOG;
                    Log.i("Script", "AccessActivity.onCheckedChanged('Ecoponto')");
                    //Instanciamos a mascara de texto para cpf/cnpj no campo de Usuário;
                    //Esvaziamos o usuário e senha ao mudar de tipo;
                    etUsuario.setText("");
                    etPassword.setText("");
                    etUsuario.removeTextChangedListener(twDefault);
                    etUsuario.addTextChangedListener(twMask);
                    //Adaptamos a informação para o ecoponto;
                    etUsuario.setHint("CNPJ do Reciclador");
                    //Informamos o tipo de login atual a ser pesquisado;
                    metodoNetworkConnection = "CNPJ";
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

     /*

        MÉTODOS DO FRAGMENT

    */

    @Override
    public void onDestroy() {

        super.onDestroy();
        NetworkConnection.getInstance(getActivity().getApplicationContext()).getRequestQueue().cancelAll( AccessActivity.class.getName() );
    }
     /*

        MÉTODOS DO TRANSACTION

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

    //Método iniciado antes da execução do networkConnection
    @Override
    public WrapObjToNetwork doBefore() {

        Log.i("Script", "FragmentFormaReciclador.onBefore(Consulta Usuario)");

        //Instanciamos o objeto de contribuinte;
        Reciclador reciclador = new Reciclador();

        //Instanciamos os parâmetros necessário para a consulta;
        reciclador.setSenhaReciclador(senhaUsuario);

        //Verificamos se a forma de login é com o Email...
        if (metodoNetworkConnection.equals("Email")) {

            reciclador.setEmailReciclador(loginUsuario);
        } else
        //Verificamos se a forma de login é com o CPF
        if ((metodoNetworkConnection.equals("CPNJ"))) {

            reciclador.setCnpjReciclador(loginUsuario);
        }

        return ( new WrapObjToNetwork(
                reciclador,
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
                    user_Reciclador = new Reciclador();

                    //Como só existe apenas um usuário retornado, usamos o 0 para capturar os dados do mesmo.
                    user_Reciclador.setIdReciclador(jsonArray.getJSONObject(i).getInt("idReciclador"));

                    //Verificamos se o usuário retornado é válido ou não;
                    if (user_Reciclador.getIdReciclador() != 0) {

                        user_Reciclador.setRazaoSocialReciclador(jsonArray.getJSONObject(i).getString("razaoSocialReciclador"));
                        user_Reciclador.setNomeFantasiaReciclador(jsonArray.getJSONObject(i).getString("nomeFantasiaReciclador"));
                        user_Reciclador.setSegmentoReciclador(jsonArray.getJSONObject(i).getString("segmentoReciclador"));
                        user_Reciclador.setEmailReciclador(jsonArray.getJSONObject(i).getString("emailReciclador"));
                        user_Reciclador.setCnpjReciclador(jsonArray.getJSONObject(i).getString("cnpjReciclador"));
                        user_Reciclador.setSenhaReciclador(jsonArray.getJSONObject(i).getString("senhaReciclador"));
                    } else {

                        messageResult = jsonArray.getJSONObject(0).getString("nomeFantasiaReciclador");
                    }
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

        } else {

            Log.i("Script", "Algum problema foi detectado no Volley");
        }
    }

     /*

        MÉTODOS DE VALIDAÇÃO
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
