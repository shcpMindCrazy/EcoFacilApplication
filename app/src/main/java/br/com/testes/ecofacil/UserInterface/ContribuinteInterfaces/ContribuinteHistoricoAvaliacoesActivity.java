package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import br.com.testes.ecofacil.R;

public class ContribuinteHistoricoAvaliacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribuinte_historico_avaliacoes);

        Log.i("Script", "ContribuinteHistoricoAvaliacoesActivity.onCreate()");
    }
}
