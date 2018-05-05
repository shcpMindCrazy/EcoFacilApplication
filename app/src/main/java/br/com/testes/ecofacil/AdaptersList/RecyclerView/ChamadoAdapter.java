package br.com.testes.ecofacil.AdaptersList.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.testes.ecofacil.R;

/**
 * Created by samue on 22/03/2018.
 */

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.MyViewHolder> {

    private List<Chamado> mList;
    private LayoutInflater mLayoutInflater;

    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

    public ChamadoAdapter(Context c, List<Chamado> l) {

        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Quando for necessário criar uma nova view;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Log.i("Script", "PessoaAdapter.onCreateViewHolder()");

        //Criamos um acesso direto ao layout;
        View view = mLayoutInflater.inflate(R.layout.recycler_item_chamado, viewGroup, false);

        //Com base nos parametros criados neste método, usará a view atual para instanciar os objetos;
        MyViewHolder myViewHolder = new MyViewHolder(view);

        //Retornamos esta;
        return myViewHolder;
    }

    //Vincula os dados da nossa view
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.i("Script", "PessoaAdapter.onBindViewHolder("+ mList.size() +")");

        //Endereço completo (ContribuinteEndereco);
        String textFieldEnderecoCompleto = mList.get(position).getEnderecoContribuinteColeta();
        //Data e hora (Solicitacao);
        String textFieldDataHora = mList.get(position).getInicioSolicitacaoColeta();
        //Estado do serviço (Solicitação);
        String textFieldEstadoServico = mList.get(position).getEstadoAtualColeta();
        //Nome do contribuinte (Contribuinte);
        String textFieldNomeContribuinte = mList.get(position).getNomeContribuinteColeta();
        //Instanciamos as informações em suas respectivas TextFields;
        holder.tvNomeEndereco.setText(textFieldEnderecoCompleto);
        holder.tvDataHora.setText(textFieldDataHora);
        holder.tvStatusChamado.setText(textFieldEstadoServico);
        holder.tvNomePessoaEmail.setText(textFieldNomeContribuinte);

    }

    //Tamanho da nossa list;
    @Override
    public int getItemCount() {

        Log.i("Script", "PessoaAdapter.getItemCount()");
        return mList.size();
    }

    public void addListItem(Chamado chamado, int position) {

        //Adicionamos o item na lista;
        mList.add(chamado);
        //Depois inserimos um novo item;
        notifyItemInserted(position);
    }

    public void removeListItem(int position) {

        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateList(List<Chamado> mListRefresh) {

        //Verificamos se este não está sendo passado como resultado nulo...
        if (mListRefresh != null && mListRefresh.size() > 0) {
            //Instanciamos os novos parâmetros para o dataSet do RecyclerView
            this.mList = mListRefresh;
            //Atualizamos o RecyclerView depois da inserção da nova list;
            notifyDataSetChanged();
        }
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener m) {

        mRecyclerViewOnClickListener = m;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvNomeEndereco;
        public TextView tvNomePessoaEmail;
        public TextView tvMaterial;
        public TextView tvDataHora;
        public TextView tvStatusChamado;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvNomeEndereco = (TextView) itemView.findViewById(R.id.txtChamadoEndereco);
            tvNomePessoaEmail = (TextView) itemView.findViewById(R.id.txtChamadoPessoa);
            tvMaterial = (TextView) itemView.findViewById(R.id.txtChamadoMateriais);
            tvDataHora = (TextView) itemView.findViewById(R.id.txtChamadoDataHora);
            tvStatusChamado = (TextView) itemView.findViewById(R.id.txtChamadoStatus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mRecyclerViewOnClickListener != null) {

                mRecyclerViewOnClickListener.onClickItemListener(view, getAdapterPosition());
            }
        }

    }
}
