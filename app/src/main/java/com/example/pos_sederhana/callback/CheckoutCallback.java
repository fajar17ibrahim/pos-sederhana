package com.example.pos_sederhana.callback;

import com.example.pos_sederhana.model.Barang;
import com.example.pos_sederhana.model.Cart;

public interface CheckoutCallback {

    Barang getBarang(int id);

    void delete(Cart cart);
}
