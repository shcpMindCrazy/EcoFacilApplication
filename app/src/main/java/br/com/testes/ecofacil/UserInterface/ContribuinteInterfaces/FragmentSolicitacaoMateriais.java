package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.testes.ecofacil.AdaptersList.RecyclerView.ChamadoAdapter;
import br.com.testes.ecofacil.AdaptersList.RecyclerView.MateriaisAdapter;
import br.com.testes.ecofacil.AdaptersList.RecyclerView.RecyclerViewOnClickListener;
import br.com.testes.ecofacil.Domain.WrapObjToNetwork;
import br.com.testes.ecofacil.Network.NetworkConnection;
import br.com.testes.ecofacil.Network.Transaction;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.ContribuinteEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;
import br.com.testes.ecofacil.R;

/**
 * Created by samue on 12/03/2018.
 */

public class FragmentSolicitacaoMateriais extends Fragment implements RecyclerViewOnClickListener {

    public static Contribuinte contribuinteAtual;
    public static String actionFragment;
    public static String enderecoEscolhido;
    public static List<SolicitacaoMateriais> materiaisDoados;

    String action;

    FragmentMateriaisListener mCallback;

    private List<String> tipoMateriais = new ArrayList<String>();
    private List<String> tipoPesos = new ArrayList<String>();

    boolean listStatus = false;

    //Componentes Visuais;
    Button btnSolicitarTabelaMateriais;
    EditText etSolicitarPeso;
    Spinner spnSolicitarTipoPeso;
    Spinner spnSolicitarTipoMaterial;
    Button btnMateriaisEnviar;
    Button btnMateriaisAdicionar;
    //Componentes Visuais - RecyclerView;
    private RecyclerView recyclerViewMateriais;
    private LinearLayoutManager llManager;
    private MateriaisAdapter materiaisAdapter;
    private List<SolicitacaoMateriais> solicitacaoMateriaisList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewMaps = inflater.inflate(R.layout.fragment_materiais, container, false);

        Bundle bundleResult = getArguments();

        if (bundleResult != null) {

            actionFragment = bundleResult.getString("Action");
            enderecoEscolhido = bundleResult.getString("Endereco");
            contribuinteAtual = (Contribuinte) bundleResult.getSerializable("Usuario");

            Log.i("Script", "FragmentSolicitacaoMateriais.onCreateView( " + contribuinteAtual.getEmailContribuinte() + " && "+ actionFragment + ")");
            Log.i("Script", "FragmentSolicitacaoMateriais.onCreateView( " + enderecoEscolhido + ")");
        }

        initializeComponents(viewMaps);

        initializeButtons(viewMaps);

        return viewMaps;
    }

    //Método ao clicar em um item da lista;
    @Override
    public void onClickItemListener(View view, int position) {

    }

    //Método para instanciar componentes nesta classe;
    public void initializeComponents(View view) {

        //Buttons
        btnSolicitarTabelaMateriais = (Button) view.findViewById(R.id.btnSolicitarTabelaMateriais);
        btnMateriaisEnviar = (Button) view.findViewById(R.id.btnMateriaisEnviar);
        btnMateriaisAdicionar = (Button) view.findViewById(R.id.btnMateriaisAdicionar);
        //EditText's
        etSolicitarPeso = (EditText) view.findViewById(R.id.etSolicitarVolume);
        //Spinner's
        spnSolicitarTipoPeso = (Spinner) view.findViewById(R.id.spnSolicitarTipoVolume);
        spnSolicitarTipoMaterial = (Spinner) view.findViewById(R.id.spnSolicitarTipoMaterial);
        initializeSpinners(view);
        //Recicler View
        recyclerViewMateriais = (RecyclerView) view.findViewById(R.id.recyclerViewMateriais);
        initializeRecyclerView(view);

    }

    public void initializeButtons(View view) {

        //Listener sob o click do botão iniciar a solicitação;
        btnMateriaisEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Logcat
                Log.i("Script", "FragmentSolicitacaoMateriais.initializeButtons.setOnClickListener()");

                //Verificamos se a list não está vazia e também se foi adicionado algo após isso;
                if (!solicitacaoMateriaisList.isEmpty() && listStatus) {

                    //Capturamos o objeto a ser transferido para a activity;
                    materiaisDoados = solicitacaoMateriaisList;
                    //Informamos que apartir deste ponto, poderá ser criada a solicitação;
                    actionFragment = "solicitacao";
                    //Iniciamos a transferência das variáveis;
                    mCallback.onTranferirIntentMateriais(actionFragment);
                } else {

                    //Mensagem para o usuário;
                    Toast.makeText(getActivity(), "Adicione algum material a ser doado antes de prosseguir...", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listener sob o click do botão de adicionar material na list;
        btnMateriaisAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Instanciamos o adapter atual do RecyclerView;
                MateriaisAdapter materiaisAdapter = (MateriaisAdapter) recyclerViewMateriais.getAdapter();

                //Verificamos se a EditText e os spinners estão com algum conteúdo selecionado;
                if (validaCampoEditText(etSolicitarPeso) &&
                        validaCampoSpinner(spnSolicitarTipoPeso) &&
                        validaCampoSpinner(spnSolicitarTipoMaterial)) {

                    //Informamos ao sistema para alteração do estado de uso do método;
                    if (listStatus == false) {

                        //Limpamos os registros de demonstração de dados;
                        for (int i = solicitacaoMateriaisList.size()-1 ; i >= 0 ; i--){
                            materiaisAdapter.removeListItem(i);
                        }
                        solicitacaoMateriaisList.clear();

                        //Autorizamos a entrada de dados oficiais;
                        listStatus = true;
                    }
                    //Adicionamos o item a ser inserido na list;
                    preencherRecyclerMateriais();
                    //Passamos os novos parâmetros para o adapter de atualização;
                    materiaisAdapter.updateList(solicitacaoMateriaisList);
                } else {

                    //Mensagem para o usuário;
                    Toast.makeText(getActivity(), "Preencha as informações dos materiais a serem transportados...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initializeSpinners(View view) {

        /*
            SPINNER DE TIPO DE MATERIAIS;
         */

        //Instanciamos os nomes a serem usados como "material";
        tipoMateriais.add("Selecione o tipo aqui");
        tipoMateriais.add("Ferro");
        tipoMateriais.add("Aço");
        tipoMateriais.add("Cobre");
        tipoMateriais.add("Plástico");
        tipoMateriais.add("Vidro");

        //Criamos um arrayAdapter;
        ArrayAdapter<String> arrayAdapterTipoMaterial = new ArrayAdapter<String>(
                getActivity(), //Context
                android.R.layout.simple_spinner_dropdown_item, //Style;
                tipoMateriais) {

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
        ArrayAdapter<String> spinnerArrayAdapterTipoMaterial = arrayAdapterTipoMaterial;
        //O que poderá ser feito ao clikar no adapter;
        spinnerArrayAdapterTipoMaterial.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Setamos o ArrayAdapter confirado no spinner;
        spnSolicitarTipoMaterial.setAdapter(spinnerArrayAdapterTipoMaterial);

        /*
            SPINNER DE TIPO DE PESOS;
         */

        //Instanciamos os nomes a serem usados como "Peso";
        tipoPesos.add("Selecione o tipo de peso aqui");
        tipoPesos.add("Miligrama");
        tipoPesos.add("Kilograma");
        tipoPesos.add("Tonelada");

        //Criamos um arrayAdapter;
        ArrayAdapter<String> arrayAdapterTipoPeso = new ArrayAdapter<String>(
                getActivity(), //Context
                android.R.layout.simple_spinner_dropdown_item, //Style;
                tipoPesos) {

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
        ArrayAdapter<String> spinnerArrayAdapterTipoPeso = arrayAdapterTipoPeso;
        //O que poderá ser feito ao clikar no adapter;
        spinnerArrayAdapterTipoMaterial.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Setamos o ArrayAdapter confirado no spinner;
        spnSolicitarTipoPeso.setAdapter(spinnerArrayAdapterTipoPeso);
    }

    public void initializeRecyclerView(View view) {

        Log.i("Script", "FragmentSolicitacaoMateriais.initializeRecyclerView()");

        //Instanciamos a list que será usada neste RecyclerView;
        solicitacaoMateriaisList = new ArrayList<>();
        preencherRecyclerMateriais();

        //Como já foi instanciado anteriormente, apenas atribuímos a esta itens fixados.
        recyclerViewMateriais.setHasFixedSize(true);

        //LinearLayoutManager para tratar os dados que forem enviados para a list;
        llManager = new LinearLayoutManager(getActivity());
        //Setamos a orientação dos itens;
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        //Atribuímos para o RecyclerView;
        recyclerViewMateriais.setLayoutManager(llManager);

        //Instanciamos o nosso Adapter;
        materiaisAdapter = new MateriaisAdapter(getActivity(), solicitacaoMateriaisList);
        //Instanciamos o método de onClick na RecyclerView;
        materiaisAdapter.setRecyclerViewOnClickListener(this);
        //Instanciamos o adapter dentro da nossa RecyclerView;
        recyclerViewMateriais.setAdapter(materiaisAdapter);
    }

    public void preencherRecyclerMateriais() {

        //Caso esta list estiver vazia, ela é preenchida desta forma como demonstração;
        if (listStatus == false) {

            SolicitacaoMateriais solicitacaoMateriais;

            solicitacaoMateriais = new SolicitacaoMateriais(12, "Medida: ", "TipoMaterial: ");
            solicitacaoMateriaisList.add(solicitacaoMateriais);
            solicitacaoMateriais = new SolicitacaoMateriais(11, "quantos itens quiser ", "Deletar? Use o botão ao lado ");
            solicitacaoMateriaisList.add(solicitacaoMateriais);
        }
        //Quando algum item é adicionado;
        else if (listStatus) {

            //Criamos um item apartir do qual adicionado;
            SolicitacaoMateriais solicitacaoMateriais =
                    new SolicitacaoMateriais(
                            Integer.parseInt(etSolicitarPeso.getText().toString()),
                                    spnSolicitarTipoPeso.getSelectedItem().toString(),
                                    spnSolicitarTipoMaterial.getSelectedItem().toString()
                    );

            //E o adicionamos na list;
            solicitacaoMateriaisList.add(solicitacaoMateriais);
        }
    }

    protected boolean validaCampoEditText( EditText origem){

        if ( origem.getText().toString().isEmpty() ) {

            return false;
        }

        return true;
    }

    protected boolean validaCampoSpinner( Spinner origem){

        if ( origem.getSelectedItem().toString().contains("Selecione") ) {

            return false;
        }

        return true;
    }

    public interface FragmentMateriaisListener {

        public void onTranferirIntentMateriais(String action);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            Log.i("Script", "FragmentSolicitacaoMateriais.onAttach()");
            mCallback = (FragmentMateriaisListener) context;
        } catch (ClassCastException e) {

            Log.i("Script", "FragmentSolicitacaoMateriais.onAttach("+ e +")");
            throw new ClassCastException(context.toString()
                    + " precisa implementar FragmentMateriaisListener");
        }
    }
}
