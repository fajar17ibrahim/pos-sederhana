package com.example.pos_sederhana.model;

import java.util.List;

public class    Transaksi {

    private int id_trx;

    private String date_created;

    private List<Cart> cartList;

    public Transaksi() { }

    public Transaksi(int trxId, String dateCreated, List<Cart> cartList) {
        this.id_trx = trxId;
        this.date_created = dateCreated;
        this.cartList = cartList;
    }

    public int getId_trx() {
        return id_trx;
    }

    public void setId_trx(int id_trx) {
        this.id_trx = id_trx;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

}
