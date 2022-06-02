package com.example.pos_sederhana.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pos_sederhana.model.Barang;
import com.example.pos_sederhana.model.Cart;
import com.example.pos_sederhana.model.Transaksi;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "pos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE barang_table(" +
                    "id_barang INTEGER PRIMARY KEY, " +
                    "kode_barang VARCHAR(50) UNIQUE NOT NULL, " +
                    "nama_barang VARCHAR(225) NOT NULL," +
                    "stok INTEGER NOT NULL)");

            db.execSQL("CREATE TABLE cart_table(" +
                    "id_cart INTEGER PRIMARY KEY, " +
                    "id_barang INTEGER NOT NULL, " +
                    "qty INTEGER NOT NULL)");

            db.execSQL("CREATE TABLE trx_table(" +
                    "id_trx INTEGER PRIMARY KEY, " +
                    "date_created INTEGER NOT NULL)");

            db.execSQL("CREATE TABLE trx_cart_table(" +
                    "id_trx_cart INTEGER PRIMARY KEY, " +
                    "id_trx INTEGER NOT NULL, " +
                    "id_barang INTEGER NOT NULL, " +
                    "qty INTEGER NOT NULL)");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS barang_table");
        db.execSQL("DROP TABLE IF EXISTS cart_table");
        db.execSQL("DROP TABLE IF EXISTS trx_table");
        db.execSQL("DROP TABLE IF EXISTS trx_cart_table");
    }

    public boolean saveBarang (Barang barang) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("kode_barang", barang.getKode_barang());
        cv.put("nama_barang", barang.getNama_barang());
        cv.put("stok", barang.getStok());
        return db.insert("barang_table", null, cv) > 0;
    }

    public Cursor getAllBarang () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "barang_table", null);
    }

    public Cursor getBarang (int id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "barang_table where id_barang=" + id, null);
    }

    public boolean updateBarang(Barang barang) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("kode_barang", barang.getKode_barang());
        cv.put("nama_barang", barang.getNama_barang());
        cv.put("stok", barang.getStok());
        return db.update("barang_table", cv, "id_barang" + "=" + barang.getId_barang(), null) > 0;
    }

    public void deleteBarang(int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("barang_table", "id_barang" + "=" + id, null);
    }

    public void deleteAllBarang() {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("barang_table", null, null);
    }

    public boolean saveCart (Cart cart) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_barang", cart.getId_barang());
        cv.put("qty", cart.getQty());
        return db.insert("cart_table", null, cv) > 0;
    }

    public Cursor getAllCart () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "cart_table", null);
    }

    public void deleteCart(int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("cart_table", "id_cart" + "=" + id, null);
    }

    public void deleteAllCart() {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("cart_table", null, null);
    }

    public boolean saveTrx (Transaksi transaksi) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date_created", transaksi.getDate_created());
        return db.insert("trx_table", null, cv) > 0;
    }

    public Cursor getAllTrx () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "trx_table", null);
    }

    public boolean saveCartTrx (int id_trx, Cart cart) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_trx", id_trx);
        cv.put("id_barang", cart.getId_barang());
        cv.put("qty", cart.getQty());
        return db.insert("trx_cart_table", null, cv) > 0;
    }

    public Cursor getAllCartTrx () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "trx_cart_table", null);
    }

}
