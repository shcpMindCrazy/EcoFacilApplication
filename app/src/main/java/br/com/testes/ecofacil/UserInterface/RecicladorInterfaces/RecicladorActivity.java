package br.com.testes.ecofacil.UserInterface.RecicladorInterfaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.Reciclador;
import br.com.testes.ecofacil.R;

public class RecicladorActivity extends AppCompatActivity implements FragmentChamados.FragmentChamadosListener{

    Reciclador recicladorAtual;
    Contribuinte contribuinteSolicitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecoponto);

        Intent intentNovo = getIntent();
        Bundle bundleNovo = intentNovo.getExtras();

        if (bundleNovo != null) {

            recicladorAtual = (Reciclador) bundleNovo.getSerializable("Reciclador");

            if (bundleNovo.containsKey("Contribuinte")) {

                contribuinteSolicitado = (Contribuinte) bundleNovo.getSerializable("Contribuinte");

                initializeActivityChamada();

            } else {

                initializeFragmentChamados();
            }
        }


    }

    @Override
    public void onTranferirIntentChamados() {

        Log.i("Script", "RecicladorActivity.onTranferirIntentChamados()");

        Intent intentChamada = new Intent(this, RecicladorSolicitacaoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Reciclador", FragmentChamados.recicladorAtual);
        //intentChamada.putExtra("Contribuinte", FragmentChamados.contribuinteSolicitado);
        bundle.putInt("idContribuinte", FragmentChamados.idContribuinteSolicitado);
        bundle.putString("Destino", FragmentChamados.destinoRetirada);
        bundle.putSerializable("Materiais", (Serializable) FragmentChamados.mSolicitacaoMateriaisList);
        intentChamada.putExtra("Bundle", bundle);
        startActivity(intentChamada);
    }

    public void initializeFragmentChamados() {

        //Iniciamos o bundle para inserção das variáveis;
        Bundle args = new Bundle();
        args.putSerializable("Reciclador", recicladorAtual);

        //Instanciamos o fragment;
        FragmentChamados fragmentChamados = new FragmentChamados();
        fragmentChamados.setArguments(args); //Em conjunto aos fragments;

        //Iniciamos o processo do fragment;
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_ponto_reciclador, fragmentChamados); //Junto com os args;
        ft.commit();
    }

    public void initializeActivityChamada() {


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
                        RecicladorActivity.super.onBackPressed();
                    }
                    //Iniciamos toda a rotina e mostramos na tela;
                }).create().show();
    }
}
