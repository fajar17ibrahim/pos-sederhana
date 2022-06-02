package com.example.pos_sederhana.callback;

import com.example.pos_sederhana.model.Barang;

public interface TransaksiCallback {

    void addToCart(Barang barang);
}
