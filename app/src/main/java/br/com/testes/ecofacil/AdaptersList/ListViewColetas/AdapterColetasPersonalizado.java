package br.com.testes.ecofacil.AdaptersList.ListViewColetas;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.testes.ecofacil.R;

/**
 * Created by samue on 28/02/2018.
 */

public class AdapterColetasPersonalizado extends BaseAdapter {

    private final List<Coleta> coletas;
    private final Activity act;

    public AdapterColetasPersonalizado(List<Coleta> cursos, Activity act) {
        this.coletas = cursos;
        this.act = act;
    }

    @Override
    public int getCount() {

        return coletas.size();
    }

    @Override
    public Object getItem(int position) {

        return coletas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.layout_lista_resultados_coletas, parent, false);

        Coleta coleta = coletas.get(position);

        //Pegando as referências das Views
        TextView nomeDoador = (TextView)
                view.findViewById(R.id.layout_lista_resultados_coletas_nome);
        TextView enderecoDoador = (TextView)
                view.findViewById(R.id.layout_lista_resultados_coletas_endereco);
        TextView tipoDoador = (TextView)
                view.findViewById(R.id.layout_lista_resultados_coletas_material);
        TextView distancia = (TextView)
                view.findViewById(R.id.layout_lista_resultados_coletas_distancia);
        TextView peso = (TextView)
                view.findViewById(R.id.layout_lista_resultados_coletas_peso);

        //Inserindo as informações nas TextViews;
        nomeDoador.setText(coleta.getNomeDoador());
        enderecoDoador.setText(coleta.getEnderecoDoador());
        tipoDoador.setText(coleta.getTipoMaterialDoador());
        distancia.setText(coleta.getDistanciaDoador());
        peso.setText(coleta.getPesoDoador());

        return view;
    }
}
