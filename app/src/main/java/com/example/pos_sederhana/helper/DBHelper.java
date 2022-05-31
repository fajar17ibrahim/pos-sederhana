package com.example.pos_sederhana.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pos_sederhana.model.Barang;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "pos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE barang_table(" +
                "id_barang INTEGER PRIMARY KEY, " +
                "kode_barang VARCHAR(50) UNIQUE NOT NULL, " +
                "nama_barang VARCHAR(225) NOT NULL," +
                "stok INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS barang_table");
    }

    public boolean saveBarang (Barang barang) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judul", barang.getKode_barang());
        cv.put("catatan", barang.getNama_barang());
        cv.put("catatan", barang.getStok());
        return db.insert("barang_table", null, cv) > 0;
    }
}
