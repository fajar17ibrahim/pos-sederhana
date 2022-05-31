package com.example.pos_sederhana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BarangFormActivity extends AppCompatActivity {

    private EditText etCode, etName, etStock;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_form);

        etCode = findViewById(R.id.et_code);
        etName = findViewById(R.id.et_name);
        etStock = findViewById(R.id.et_stok);
        btnSubmit = findViewById(R.id.btn_submit);
    }
}