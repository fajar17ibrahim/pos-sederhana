package com.example.pos_sederhana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pos_sederhana.helper.DBHelper;
import com.example.pos_sederhana.model.Barang;
import com.example.pos_sederhana.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarangFormActivity extends AppCompatActivity {

    @BindView(R.id.et_code)
    EditText etCode;

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_stok)
    EditText etStock;

    private DBHelper dbHelper;
    private int id = 0;
    private Barang barang = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_form);
        ButterKnife.bind(this);

        dbHelper = new DBHelper(getApplicationContext());

        Intent intent = getIntent();
        id = intent.getIntExtra(StringUtils.ID, 0);

        if (id > 0) {
            setDataBarang(id);
        }
    }

    private void setDataBarang(int id) {
        Cursor cursor = dbHelper.getBarang(id);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            etCode.setText(cursor.getString(cursor.getColumnIndexOrThrow("kode_barang")));
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama_barang")));
            etStock.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("stok"))));
            dbHelper.close();
        }
    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        String code = etCode.getText().toString();
        String name = etName.getText().toString();
        String stok = etStock.getText().toString();

        if (code.isEmpty()) {
            showToastMessage("Kode tidak boleh kosong!");
        } else if (name.isEmpty()) {
            showToastMessage("Nama tidak boleh kosong!");
        } else if (stok.isEmpty()) {
            showToastMessage("Stok tidak boleh kosong!");
        } else {

            boolean saved = false;
            if (id > 0 && id > 0) {
                barang = new Barang(id, code, name, stok);
                saved = dbHelper.updateBarang(barang);
            } else {
                barang = new Barang(code, name, stok);
                saved = dbHelper.saveBarang(barang);
            }
            if (saved) {
                showToastMessage("Barang berhasil disimpan");
                Intent intent = new Intent(BarangFormActivity.this, BarangActivity.class);
                startActivity(intent);
            } else {
                showToastMessage("Barang gagal disimpan!");
            }
            dbHelper.close();
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}