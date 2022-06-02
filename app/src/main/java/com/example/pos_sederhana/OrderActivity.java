package com.example.pos_sederhana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.pos_sederhana.adapter.OrderAdapter;
import com.example.pos_sederhana.callback.OrderCallback;
import com.example.pos_sederhana.helper.DBHelper;
import com.example.pos_sederhana.helper.PreferencesHelper;
import com.example.pos_sederhana.model.Cart;
import com.example.pos_sederhana.model.Transaksi;
import com.example.pos_sederhana.utils.StringUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends AppCompatActivity implements OrderCallback {

    private static final String TAG = "OrderActivity";

    @BindView(R.id.rv_order)
    RecyclerView rvOrder;

    private FirebaseDatabase database;
    private DatabaseReference getReference;

    private DBHelper dbHelper;

    private OrderAdapter adapter;
    private List<Transaksi> trxList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        getReference = database.getReference();

        dbHelper = new DBHelper(getApplicationContext());

        getOrderFromFirebase();
    }

    private void getOrderFromFirebase() {
        trxList.clear();
        getReference.child("Order").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Transaksi transaksi = dataSnapshot.getValue(Transaksi.class);
                trxList.add(transaksi);
                Log.d(TAG,"Transaksi = " + transaksi.getDate_created());
                Log.d(TAG,"Transaksi = " + transaksi.getId_trx());

                setListOrder();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListOrder() {
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(this, trxList);
        rvOrder.setAdapter(adapter);
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        Intent intent = new Intent(OrderActivity.this, TransaksiActivity.class);
        startActivity(intent);
    }

    @Override
    public void load(Transaksi transaksi) {
        dbHelper.deleteAllCart();
        for (Cart cart : transaksi.getCartList()) {
            dbHelper.saveCart(cart);
        }
        dbHelper.close();

        PreferencesHelper preferencesHelper = new PreferencesHelper(this);
        preferencesHelper.setTrxId(transaksi.getId_trx());

        Intent intent = new Intent(OrderActivity.this, CheckoutActivity.class);
        startActivity(intent);
    }

    @Override
    public void remove(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Transaksi")
                .setMessage("Anda yakin ingin menghapus?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Query applesQuery = getReference.child("Order").orderByChild("id_trx").equalTo(id);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                                getOrderFromFirebase();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}