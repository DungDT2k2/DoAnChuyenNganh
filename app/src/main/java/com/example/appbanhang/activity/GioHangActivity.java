package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.icu.text.DecimalFormat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.GioHangAdapter;
import com.example.appbanhang.model.EventBus.TotalEvent;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter gioHangAdapter;
    List<GioHang> gioHangList;
    long tongtiensp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        AnhXa();
        ActionBar();
        if(Utils.mangmuahang != null){
            Utils.mangmuahang.clear();
        }
        TotalMoney();
    }

    private void TotalMoney() {
        tongtiensp = 0;
        for (int i = 0; i <Utils.mangmuahang.size(); i++) {
            tongtiensp = tongtiensp + (Utils.mangmuahang.get(i).getGiasp() * Utils.mangmuahang.get(i).getSoluong());
        }
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###" + " đ");
            tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.manggiohang.size() ==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(gioHangAdapter);
        }
        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tongtien",tongtiensp);
//                Utils.manggiohang.clear();
                startActivity(intent);
            }
        });
    }

    private void AnhXa() {
        giohangtrong = findViewById(R.id.txtgiohangtrong);
        tongtien = findViewById(R.id.txttongtien);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerviewgiohang);
        btnmuahang = findViewById(R.id.btnmuahang);
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register( this);
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(  this); super.onStop();
    }
    @Subscribe(sticky= true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien (TotalEvent event){
        if (event !=null) { TotalMoney();
        }
    }
}