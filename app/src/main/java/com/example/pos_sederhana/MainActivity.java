package com.example.pos_sederhana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.btn_barang)
    Button btnBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_barang)
    public void onClickBarang() {
        Log.d(TAG, "onClickBarang");
        Intent intent = new Intent(MainActivity.this, BarangActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_transaksi)
    public void onClickTrx() {
        Intent intent = new Intent(MainActivity.this, TransaksiActivity.class);
        startActivity(intent);
    }
}