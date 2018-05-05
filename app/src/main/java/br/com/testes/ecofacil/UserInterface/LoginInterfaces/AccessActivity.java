package br.com.testes.ecofacil.UserInterface.LoginInterfaces;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import br.com.testes.ecofacil.R;

public class AccessActivity extends AppCompatActivity {

    ImageView ivLogoEcoFacil;
    FrameLayout frameLayoutAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        //Inicializamos os componentes;
        initializeComponents();

        //Inicializamos o fragment para escolha do tipo de login;
        initializeFragmentForma();
    }

    public void initializeComponents() {

        ivLogoEcoFacil = findViewById(R.id.ivLogoEcoFacil);
        frameLayoutAccess = findViewById(R.id.flyVisualAccess);
    }

    public void initializeFragmentForma() {

        //Instanciamos o mapsFragment com o Bundle das variáveis passadas;
        FragmentFormaLogin formaLoginFragment = new FragmentFormaLogin();

        // Pega o FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //Abre uma transação e adiciona
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flyVisualAccess, formaLoginFragment); //Carregando os arguments;
        ft.commit(); //Iniciamos esta;
    }



    /*

        MÉTODOS DE CLICK BUTTON E TEXTVIEW
     */




    //Networking connection;



    @Override
    public void onStop() {

        super.onStop();

    }


}
