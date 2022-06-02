package com.example.pos_sederhana.callback;

import com.example.pos_sederhana.model.Transaksi;

public interface OrderCallback {

    void load(Transaksi transaksi);

    void remove(int id);
}
