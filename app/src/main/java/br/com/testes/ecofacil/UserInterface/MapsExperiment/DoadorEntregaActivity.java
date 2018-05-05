package br.com.testes.ecofacil.UserInterface.MapsExperiment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import br.com.testes.ecofacil.R;

public class DoadorEntregaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doador_entrega);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PontosRecladores);

        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.macEtMapsBuscar);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        initializeMaps();
    }

    public void initializeComponents() {


    }

    //Metodo para chamar o mapa;
    public void initializeMaps() {

        //Iniciamos o fragmentManager
        FragmentManager fragmentMaps = getSupportFragmentManager();
        // Abre uma transação e adiciona
        FragmentTransaction ft = fragmentMaps.beginTransaction();
        //Incluímos o MapsFragment dentro do FrameLayout na Activity;
        ft.add(R.id.maps_content, new MapsFragment());
        //Iniciamos o processo armazenado na transaction;
        ft.commit();
    }

    private static final String[] PontosRecladores = new String[] {

            "Ponto A, Tatuapé", "Ponto B, Tatuapé", "Ponto C, Mogi", "Ponto D, Suzano", "Ponto E, Suzano"
    };

}
