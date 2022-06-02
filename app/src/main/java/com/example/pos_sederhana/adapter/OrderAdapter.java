package com.example.pos_sederhana.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_sederhana.R;
import com.example.pos_sederhana.callback.OrderCallback;
import com.example.pos_sederhana.model.Transaksi;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private List<Transaksi> transaksiList = new ArrayList<>();
    private OrderCallback callback;

    public OrderAdapter(OrderCallback callback, List<Transaksi> transaksiList) {
        this.callback = callback;
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaksi transaksi = transaksiList.get(position);
        int label = position + 1;
        holder.tvCode.setText(String.valueOf(transaksi.getId_trx()));
        holder.tvOrder.setText("Order " + label);

        holder.btnLoad.setOnClickListener(v -> callback.load(transaksi));
        holder.btnDel.setOnClickListener(v -> callback.remove(transaksi.getId_trx()));
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvOrder;
        TextView tvCode;
        Button btnLoad;
        Button btnDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCode = itemView.findViewById(R.id.tv_code);
            tvOrder = itemView.findViewById(R.id.tv_order);

            btnLoad = itemView.findViewById(R.id.btn_load);
            btnDel = itemView.findViewById(R.id.btn_del);
        }

    }
}
