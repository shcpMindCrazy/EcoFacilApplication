package br.com.testes.ecofacil.AdaptersList.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.testes.ecofacil.R;

/**
 * Created by samue on 05/05/2018.
 */

public class ChamadoHistoricoAdapter extends RecyclerView.Adapter<ChamadoHistoricoAdapter.MyViewHolder> {

    private List<Chamado> mList;
    private LayoutInflater mLayoutInflater;

    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

    public ChamadoHistoricoAdapter (Context c, List<Chamado> l) {

        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Quando for necessário criar uma nova view;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Log.i("Script", "ChamadoHistoricoAdapter.onCreateViewHolder()");

        //Criamos um acesso direto ao layout;
        View view = mLayoutInflater.inflate(R.layout.recycler_item_historico_chamado, viewGroup, false);

        //Com base nos parametros criados neste método, usará a view atual para instanciar os objetos;
        MyViewHolder myViewHolder = new MyViewHolder(view);

        //Retornamos esta;
        return myViewHolder;
    }

    //Vincula os dados da nossa view
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.i("Script", "ChamadoHistoricoAdapter.onBindViewHolder("+ mList.size() +")");

        //Endereço completo (ContribuinteEndereco);
        String textFieldEnderecoCompleto = mList.get(position).getEnderecoContribuinteColeta();
        //Nome do contribuinte (Contribuinte);
        String textFieldNomeContribuinte = mList.get(position).getNomeContribuinteColeta();
        //Detalhes da solicitacao;
        String textFieldDescricao = mList.get(position).getDescricaoMaterialColeta();
        //Data e hora - Inicio (Solicitacao);
        String textFieldDataHoraInicio = mList.get(position).getInicioSolicitacaoColeta();
        //Data e hora - Final (Solicitacao);
        String textFieldDataHoraFinal = mList.get(position).getFinalSolicitacaoColeta();
        //Estado do serviço (Solicitação);
        String textFieldEstadoServico = mList.get(position).getEstadoAtualColeta();
        //Instanciamos as informações em suas respectivas TextFields;
        holder.tvNomeEndereco.setText(textFieldEnderecoCompleto);
        holder.tvNomePessoaEmail.setText(textFieldNomeContribuinte);
        holder.tvDescricaoSolicitacao.setText(textFieldDescricao);
        holder.tvInicioSolicitacao.setText(textFieldDataHoraInicio);
        holder.tvFinalSolicitacao.setText(textFieldDataHoraFinal);
        holder.tvStatusChamado.setText(textFieldEstadoServico);
    }

    //Tamanho da nossa list;
    @Override
    public int getItemCount() {

        Log.i("Script", "ChamadoHistoricoAdapter.getItemCount()");
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

    //Método para funcionamento do listener de click item RecyclerView;
    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener m) {

        mRecyclerViewOnClickListener = m;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvNomeEndereco;
        public TextView tvNomePessoaEmail;
        public TextView tvInicioSolicitacao;
        public TextView tvFinalSolicitacao;
        public TextView tvStatusChamado;
        public TextView tvDescricaoSolicitacao;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvNomeEndereco = (TextView) itemView.findViewById(R.id.tvHistoricoChamadoEnderecoContribuinte);
            tvNomePessoaEmail = (TextView) itemView.findViewById(R.id.tvHistoricoChamadoContribuinte);
            tvDescricaoSolicitacao = (TextView) itemView.findViewById(R.id.tvHistoricoChamadoDetalhes);
            tvInicioSolicitacao = (TextView) itemView.findViewById(R.id.tvHistoricoChamadoInicio);
            tvFinalSolicitacao = (TextView) itemView.findViewById(R.id.tvHistoricoChamadoFim);
            tvStatusChamado = (TextView) itemView.findViewById(R.id.tvHistoricoChamadoEstado);

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
