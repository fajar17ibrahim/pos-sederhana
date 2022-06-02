package com.example.pos_sederhana.callback;

import com.example.pos_sederhana.model.Barang;

public interface BarangCallback {

    void edit(Barang barang);
    void delete(Barang barang);
}
