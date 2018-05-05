package br.com.testes.ecofacil.AdaptersList.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;
import br.com.testes.ecofacil.R;

/**
 * Created by samue on 16/04/2018.
 */

public class MateriaisAdapter extends RecyclerView.Adapter<MateriaisAdapter.MyViewHolder>{

    private List<SolicitacaoMateriais> mList;
    private LayoutInflater mLayoutInflater;

    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

    public MateriaisAdapter(Context c, List<SolicitacaoMateriais> l) {

        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Quando for necessário criar uma nova view;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Log.i("Script", "MateriaisAdapter.onCreateViewHolder()");

        //Criamos um acesso direto ao layout;
        View view = mLayoutInflater.inflate(R.layout.recycler_item_materiais, viewGroup, false);

        //Com base nos parametros criados neste método, usará a view atual para instanciar os objetos;
        MyViewHolder myViewHolder = new MyViewHolder(view);

        //Retornamos esta;
        return myViewHolder;
    }

    //Vincula os dados da nossa view
    @Override
    public void onBindViewHolder(MateriaisAdapter.MyViewHolder holder, int position) {

        Log.i("Script", "MateriaisAdapter.onBindViewHolder("+ mList.size() +")");

        String campoQuantidade = String.valueOf(mList.get(position).getQuantidadeSolicitacaoMaterial());
        String campoMedida = mList.get(position).getMedidaSolicitacaoMaterial();
        String campoTipo = mList.get(position).getTipoSolicitacaoMaterial();

        holder.tvQuantidadeMaterial.setText(campoQuantidade);
        holder.tvMedidaMaterial.setText(campoMedida);
        holder.tvTipoMaterial.setText(campoTipo);

        holder.btnRemoverMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Script", "MateriaisAdapter.MyViewHolder.setOnClickListener()");

                // remove your item from data base
                mList.remove(position);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item
            }
        });
    }

    //Tamanho da nossa list;
    @Override
    public int getItemCount() {

        Log.i("Script", "MateriaisAdapter.getItemCount()");
        return mList.size();
    }

    //Método para adicionar um item na list;
    public void addListItem(SolicitacaoMateriais material, int position) {

        //Adicionamos o item na lista;
        mList.add(material);
        //Depois inserimos um novo item;
        notifyItemInserted(position);
    }

    //Método para remover um item na list;
    public void removeListItem(int position) {

        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateList(List<SolicitacaoMateriais> mListRefresh) {

        //Verificamos se este não está sendo passado como resultado nulo...
        if (mListRefresh != null && mListRefresh.size() > 0) {
            //Instanciamos os novos parâmetros para o dataSet do RecyclerView
            this.mList = mListRefresh;
            //Atualizamos o RecyclerView depois da inserção da nova list;
            notifyDataSetChanged();
        }
    }

    public void clear(List<SolicitacaoMateriais> mListRefresh) {

        final int size = mListRefresh.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mListRefresh.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener m) {

        mRecyclerViewOnClickListener = m;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvQuantidadeMaterial;
        private TextView tvMedidaMaterial;
        private TextView tvTipoMaterial;
        private Button btnRemoverMaterial;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvQuantidadeMaterial = (TextView) itemView.findViewById(R.id.layout_lista_materiais_solicitacao_quantidade);
            tvMedidaMaterial = (TextView) itemView.findViewById(R.id.layout_lista_materiais_solicitacao_medida);
            tvTipoMaterial = (TextView) itemView.findViewById(R.id.layout_lista_materiais_solicitacao_tipomaterial);
            btnRemoverMaterial = (Button) itemView.findViewById(R.id.btnRemoverMaterial);

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
