package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.R;
import br.com.testes.ecofacil.UserInterface.LoginInterfaces.AccessActivity;

public class ContribuinteActivity extends AppCompatActivity {

    //Variáveis Globais;
    Contribuinte contribuinteAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribuinte);

        //Atribuímos as variáveis anteriores neste contexto;
        Intent intentResultAccess = getIntent();
        requestResourceBundle(intentResultAccess);
    }

    /*

       Métodos para configuração dos componentes;

    */
    //Método para receber as variáveis da activity/fragment anterior;
    public void requestResourceBundle(Intent intentResult) {

        Log.i("Script", "ContribuinteActivity.requestResourceBundle("+ intentResult +");");
        //Capturamos os dados requisitados no intent;
        Bundle bundleResultAccess = intentResult.getExtras();
        //Se estes não estiverem vazios... As variáveis respectivas recebem...
        if (bundleResultAccess != null) {

            Log.i("Script", "ContribuinteActivity.onCreate( "+ bundleResultAccess +" - success)");
            //Atribuímos a variável de usuário desta activity;
            contribuinteAtual = (Contribuinte) bundleResultAccess.getSerializable("Contribuinte");
        } else {

            Log.i("Script", "ContribuinteActivity.onCreate( "+ bundleResultAccess +" - failed )");
        }
    }
    //Quando é pressionado o botão "back key"
    @Override
    public void onBackPressed() {

        //Criamos uma rotina de caixa de alerta
        new AlertDialog.Builder(this)
                //Atribuímos um titulo;
                .setTitle("Deseja mesmo sair?")
                //Atribuímos uma mensagem abaixo;
                .setMessage("Ao fazer isso, voltará para a tela de login")
                //Atribuímos uma opção negativa;
                .setNegativeButton("Não", null)
                //Atribuímos uma opção positiva;
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    //Método quando a ação for positiva;
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Ativamos o comportamento default do back key;
                        //ContribuinteActivity.super.onBackPressed();
                        //Ao sair deste usuário, deve ser redirecionado a tela de seleção de login;
                        Intent intentHome = new Intent(ContribuinteActivity.this, AccessActivity.class);
                        startActivity(intentHome);
                    }
                    //Iniciamos toda a rotina e mostramos na tela;
                }).create().show();
    }

    /*

        Métodos de listeners do xml;

    */
    //Quando for solicitado uma retirada;
    public void clickButtonCriarSolicitacao(View view) {

        //Verificador de Log;
        Log.i("Script", "ContribuinteActivity.clickButtonCriarSolicitacao()");

        Intent intentSolicitarColetor = new Intent(this, ContribuinteSolicitacaoActivity.class);
        intentSolicitarColetor.putExtra("Usuario", contribuinteAtual); //Dados do Contribuinte;
        intentSolicitarColetor.putExtra("Servico", "Solicitacao"); //Ação a ser executada;
        intentSolicitarColetor.putExtra("Action", "endereco"); //Por qual fragment será iniciado;
        startActivity(intentSolicitarColetor);
    }

    public void clickButtonBuscarPontoProximo(View view) {

        //Verificador de Log;
        Log.i("Script", "ContribuinteActivity.clickButtonBuscarPontoProximo()");

        Intent intentSolicitarColetor = new Intent(this, ContribuinteSolicitacaoActivity.class);
        intentSolicitarColetor.putExtra("Usuario", contribuinteAtual); //Dados do Contribuinte;
        intentSolicitarColetor.putExtra("Servico", "Ponto"); //Ação a ser executada;
        intentSolicitarColetor.putExtra("Action", "materiais"); //Por qual fragment será iniciado;
        startActivity(intentSolicitarColetor);
    }

    public void clickButtonSolicitacoesContribuinte (View view) {

        //Verificador de Log;
        Log.i("Script", "ContribuinteActivity.clickButtonSolicitacoesContribuinte()");
        Intent intentSolicitacaoContribuinte = new Intent(this, ContribuinteHistoricoSolicitacoesActivity.class);
        intentSolicitacaoContribuinte.putExtra("Usuario", contribuinteAtual); //Dados do Contribuinte;
        startActivity(intentSolicitacaoContribuinte);
    }

    public void clickButtonAvaliacoesRecicladores (View view) {

        //Verificador de Log;
        Log.i("Script", "ContribuinteActivity.clickButtonAvaliacoesRecicladores()");
        Intent intentAvaliacaoContribuinte = new Intent(this, ContribuinteHistoricoAvaliacoesActivity.class);
        intentAvaliacaoContribuinte.putExtra("Usuario", contribuinteAtual); //Dados do Contribuinte;
        startActivity(intentAvaliacaoContribuinte);
    }
}
