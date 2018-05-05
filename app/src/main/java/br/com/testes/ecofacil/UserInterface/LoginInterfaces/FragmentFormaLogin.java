package br.com.testes.ecofacil.UserInterface.LoginInterfaces;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.R;

/**
 * Created by samue on 08/04/2018.
 */

public class FragmentFormaLogin extends Fragment {

    //Variáveis globais;

    //Componentes Visuais;
    TextView etFormaLoginTitulo;
    Spinner spnFormaLoginTipo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forma_login, container, false);

        //Inicializamos os componentes;
        initializeComponents(view);

        return view;
    }

    public void initializeComponents(View view) {

        etFormaLoginTitulo = view.findViewById(R.id.etFormaLoginTitulo);
        spnFormaLoginTipo = view.findViewById(R.id.spnFormaLoginTipo);

        //Inicializamos e preenchemos o spinner;
        initializeSpinner(view);
    }

    public void initializeSpinner(View view) {

        //Iniciamos criando um ArrayAdapter;
        ArrayAdapter<String> arrayAdapterTipoUser = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.formas_login_usuario)) {

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

        //Instanciamos o array adapter em um exclusivo para o spinner
        ArrayAdapter<String> spinnerArrayAdapterTipoUser = arrayAdapterTipoUser;
        //O que poderá ser feito ao clikar no adapter;
        spinnerArrayAdapterTipoUser.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Setamos o ArrayAdapter confirado no spinner;
        spnFormaLoginTipo.setAdapter(spinnerArrayAdapterTipoUser);

        //Além de iniciarmos um método que irá redirecionar o usuário para o tipo de login escolhido;
        spnFormaLoginTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("Script", "FragmentFormaLogin.onItemSelected()");

                //Primeiro item, depois do item de informação;
                if (i == 1) {

                    Toast.makeText(getContext(), "Você foi direcionado para Contribuinte", Toast.LENGTH_SHORT).show();

                    // Pega o FragmentManager
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    //Abre uma transação e adiciona
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.flyVisualAccess, new FragmentFormaContribuinte()); //Carregando os arguments;
                    ft.commit(); //Iniciamos esta;

                }
                //Segundo item, depois do item de informação;
                else if (i == 2){

                    Toast.makeText(getContext(), "Você foi direcionado para Reciclador", Toast.LENGTH_SHORT).show();

                    // Pega o FragmentManager
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    //Abre uma transação e adiciona
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.flyVisualAccess, new FragmentFormaReciclador()); //Carregando os arguments;
                    ft.commit(); //Iniciamos esta;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Log.i("Script", "FragmentFormaLogin.onNothingSelected()");
            }
        });
    }
}
