package com.example.pos_sederhana.model;

public class Cart {
    private int id_cart;
    private int id_barang;
    private int qty;

    public Cart(int id_barang, int qty) {
        this.id_barang = id_barang;
        this.qty = qty;
    }

    public Cart() {

    }

    public int getId_cart() {
        return id_cart;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
