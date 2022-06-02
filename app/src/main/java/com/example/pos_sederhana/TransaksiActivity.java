package com.example.pos_sederhana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.pos_sederhana.adapter.TransaksiAdapter;
import com.example.pos_sederhana.callback.TransaksiCallback;
import com.example.pos_sederhana.helper.DBHelper;
import com.example.pos_sederhana.model.Barang;
import com.example.pos_sederhana.model.Cart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransaksiActivity extends AppCompatActivity implements TransaksiCallback {

    @BindView(R.id.rv_barang)
    RecyclerView rvBarang;

    @BindView(R.id.btn_checkout)
    Button btnCheckout;

    private DBHelper dbHelper;
    private TransaksiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        ButterKnife.bind(this);

        dbHelper = new DBHelper(getApplicationContext());
        setListBarang();
    }

    private void setListBarang() {
        List<Barang> barangList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllBarang();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                Barang barang = new Barang();
                barang.setId_barang((cursor.getInt(cursor.getColumnIndexOrThrow("id_barang"))));
                barang.setKode_barang((cursor.getString(cursor.getColumnIndexOrThrow("kode_barang"))));
                barang.setNama_barang((cursor.getString(cursor.getColumnIndexOrThrow("nama_barang"))));
                barang.setStok((cursor.getInt(cursor.getColumnIndexOrThrow("stok"))));
                barangList.add(barang);
                cursor.moveToNext();
            }
            dbHelper.close();
        }

        rvBarang.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransaksiAdapter(this, barangList);
        rvBarang.setAdapter(adapter);
    }

    @OnClick(R.id.btn_checkout)
    public void onClickCheckOut() {
        Intent intent = new Intent(TransaksiActivity.this, CheckoutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        Intent intent = new Intent(TransaksiActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_order)
    public void onClickOrder() {
        Intent intent = new Intent(TransaksiActivity.this, OrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void addToCart(Barang barang) {
        try {
            Cart cart = new Cart(barang.getId_barang(), 1);
            boolean saved = dbHelper.saveCart(cart);

            if (saved) {
                showToastMessage("Barang berhasil ditambahkan");
                Cursor cursor = dbHelper.getAllCart();
                btnCheckout.setText("Checkout (" + cursor.getCount() + ")");
            } else {
                showToastMessage("Barang gagal ditambahkan");
            }

            dbHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}