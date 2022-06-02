package com.example.pos_sederhana.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_sederhana.R;
import com.example.pos_sederhana.callback.CheckoutCallback;
import com.example.pos_sederhana.callback.TransaksiCallback;
import com.example.pos_sederhana.model.Barang;
import com.example.pos_sederhana.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder>{

    private List<Cart> cartList = new ArrayList<>();
    private CheckoutCallback callback;

    public CheckoutAdapter(CheckoutCallback callback, List<Cart> cartList) {
        this.callback = callback;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_check_out, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        Barang barang = callback.getBarang(cart.getId_barang());
        holder.tvCode.setText(barang.getKode_barang());
        holder.tvBarang.setText(barang.getNama_barang());
        holder.tvQty.setText("Jml : " + cart.getQty());

        holder.btnDel.setOnClickListener(v -> callback.delete(cart));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvBarang;
        TextView tvCode;
        TextView tvQty;
        Button btnDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCode = itemView.findViewById(R.id.tv_code);
            tvBarang = itemView.findViewById(R.id.tv_barang);
            tvQty = itemView.findViewById(R.id.tv_qty);

            btnDel = itemView.findViewById(R.id.btn_del);
        }

    }
}
