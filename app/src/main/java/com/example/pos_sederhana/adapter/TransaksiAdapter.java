package com.example.pos_sederhana.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_sederhana.R;
import com.example.pos_sederhana.callback.TransaksiCallback;
import com.example.pos_sederhana.model.Barang;

import java.util.ArrayList;
import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder>{

    private List<Barang> barangList = new ArrayList<>();
    private TransaksiCallback callback;

    public TransaksiAdapter(TransaksiCallback callback, List<Barang> barangList) {
        this.callback = callback;
        this.barangList = barangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_transaksi, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Barang barang = barangList.get(position);
        holder.tvCode.setText(barang.getKode_barang());
        holder.tvBarang.setText(barang.getNama_barang());
        holder.tvStok.setText("Stok : " + barang.getStok());

        holder.btnAddToCart.setOnClickListener(v -> callback.addToCart(barang));
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvBarang;
        TextView tvCode;
        TextView tvStok;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCode = itemView.findViewById(R.id.tv_code);
            tvBarang = itemView.findViewById(R.id.tv_barang);
            tvStok = itemView.findViewById(R.id.tv_stock);

            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }

    }
}
