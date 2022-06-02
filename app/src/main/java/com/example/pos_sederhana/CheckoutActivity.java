package com.example.pos_sederhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pos_sederhana.adapter.CheckoutAdapter;
import com.example.pos_sederhana.callback.CheckoutCallback;
import com.example.pos_sederhana.helper.DBHelper;
import com.example.pos_sederhana.helper.PreferencesHelper;
import com.example.pos_sederhana.model.Barang;
import com.example.pos_sederhana.model.Cart;
import com.example.pos_sederhana.model.Transaksi;
import com.example.pos_sederhana.utils.DateUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutActivity extends AppCompatActivity implements CheckoutCallback {

    private static final String TAG = "CheckoutActivity";

    @BindView(R.id.rv_cart)
    RecyclerView rvCart;

    @BindView(R.id.progress)
    ProgressBar progress;

    private DBHelper dbHelper;
    private CheckoutAdapter adapter;

    private List<Cart> cartList = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        getReference = database.getReference();

        dbHelper = new DBHelper(getApplicationContext());
        setListCart();

    }

    private void setListCart() {

        Cursor cursor = dbHelper.getAllCart();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                Cart cart = new Cart();
                cart.setId_cart(cursor.getInt(cursor.getColumnIndexOrThrow("id_cart")));
                cart.setId_barang(cursor.getInt(cursor.getColumnIndexOrThrow("id_barang")));
                cart.setQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")));
                cartList.add(cart);
                cursor.moveToNext();
            }
            dbHelper.close();
        }

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CheckoutAdapter(this, cartList);
        rvCart.setAdapter(adapter);
    }

    @OnClick(R.id.btn_cancel)
    public void onClickCancel() {
        new AlertDialog.Builder(this)
                .setTitle("Batal Transaksi")
                .setMessage("Anda yakin ingin membatalkan?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbHelper.deleteAllCart();
                        dbHelper.close();

                        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }

    @OnClick(R.id.btn_add)
    public void onClickAddBarang() {
        Intent intent = new Intent(CheckoutActivity.this, TransaksiActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_save)
    public void onClickSaveTrx() {
        progress.setVisibility(View.VISIBLE);

        Random random = new Random();
        int trxId = random.nextInt(10000) + 1;
        String dateCreated = DateUtils.getCurrentDate();
        Transaksi transaksi = new Transaksi(trxId, dateCreated, cartList);
        getReference = database.getReference();
        getReference.child("Order")
                .push()
                .setValue(transaksi)
                .addOnSuccessListener(this, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        progress.setVisibility(View.GONE);
                        showToastMessage("Transaksi berhasil disimpan");

                        dbHelper.deleteAllCart();;
                        dbHelper.close();

                        Intent intent = new Intent(CheckoutActivity.this, OrderActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);
                showToastMessage("Transaksi gagal disimpan");
            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmitTrx() {
        progress.setVisibility(View.VISIBLE);
        try {
            Random random = new Random();
            int trxId = random.nextInt(10000) + 1;
            String dateCreated = DateUtils.getCurrentDate();
            Transaksi transaksi = new Transaksi(trxId, dateCreated, cartList);

            int count = 0;
            for (Cart cart : cartList) {
                boolean saved = dbHelper.saveCartTrx(trxId, cart);
                if (saved)
                    count++;
            }

            boolean saved = false;
            if (count == cartList.size())
                saved = dbHelper.saveTrx(transaksi);

            if (saved) {
                updateStock(cartList);
                dbHelper.deleteAllCart();
                removeTrxFromFirebase();
                showToastMessage("Transaksi berhasil disimpan");

                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                progress.setVisibility(View.GONE);
                showToastMessage("Transaksi gagal disimpan");
            }

            dbHelper.close();
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            e.printStackTrace();
        }

    }

    private void removeTrxFromFirebase() {
        PreferencesHelper preferencesHelper = new PreferencesHelper(this);
        int id = preferencesHelper.getTrxId();

        if (id > 0) {
            Query applesQuery = getReference.child("Order").orderByChild("id_trx").equalTo(id);

            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progress.setVisibility(View.GONE);
                    preferencesHelper.clearTrxId();
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progress.setVisibility(View.GONE);
                    preferencesHelper.clearTrxId();
                    showToastMessage(databaseError.getMessage());
                }
            });
        } else {
            Log.d(TAG, "removeTrxFromFirebase->id " + id);
        }
    }

    private void updateStock(List<Cart> cartList) {
        for (Cart cart : cartList) {
            Cursor cursor = dbHelper.getBarang(cart.getId_barang());
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                Barang barang = new Barang();
                barang.setId_barang(cursor.getInt(cursor.getColumnIndexOrThrow("id_barang")));
                barang.setKode_barang(cursor.getString(cursor.getColumnIndexOrThrow("kode_barang")));
                barang.setNama_barang(cursor.getString(cursor.getColumnIndexOrThrow("nama_barang")));
                barang.setStok(cursor.getInt(cursor.getColumnIndexOrThrow("stok")) - cart.getQty());
                dbHelper.updateBarang(barang);
                dbHelper.close();
            }
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Barang getBarang(int id) {
        Cursor cursor = dbHelper.getBarang(id);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Barang barang = new Barang();
            barang.setId_barang(cursor.getInt(cursor.getColumnIndexOrThrow("id_barang")));
            barang.setKode_barang(cursor.getString(cursor.getColumnIndexOrThrow("kode_barang")));
            barang.setNama_barang(cursor.getString(cursor.getColumnIndexOrThrow("nama_barang")));
            barang.setStok(cursor.getInt(cursor.getColumnIndexOrThrow("stok")));
            dbHelper.close();
            return barang;
        }
        return null;
    }

    @Override
    public void delete(Cart cart) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Cart")
                .setMessage("Anda yakin ingin menghapus?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbHelper.deleteCart(cart.getId_cart());
                        dbHelper.close();
                        setListCart();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}