package com.example.pos_sederhana;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import com.example.pos_sederhana.adapter.BarangAdapter;
import com.example.pos_sederhana.callback.BarangCallback;
import com.example.pos_sederhana.helper.DBHelper;
import com.example.pos_sederhana.model.Barang;
import com.example.pos_sederhana.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarangActivity extends AppCompatActivity implements BarangCallback {

    @BindView(R.id.rv_barang)
    RecyclerView rvBarang;

    private DBHelper dbHelper;
    private BarangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);
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
        adapter = new BarangAdapter(this, barangList);
        rvBarang.setAdapter(adapter);
    }

    @OnClick(R.id.btn_tambah_barang)
    public void onClickAddBarang() {
        Intent intent = new Intent(BarangActivity.this, BarangFormActivity.class);
        startActivity(intent);
    }

    @Override
    public void edit(Barang barang) {
        Intent intent = new Intent(BarangActivity.this, BarangFormActivity.class);
        intent.putExtra(StringUtils.ID, barang.getId_barang());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void delete(Barang barang) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Barang")
                .setMessage("Anda yakin ingin menghapus?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbHelper.deleteBarang(barang.getId_barang());
                        dbHelper.close();
                        setListBarang();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        Intent intent = new Intent(BarangActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onClickBack();
    }
}