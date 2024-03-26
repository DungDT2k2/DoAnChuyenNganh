package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.appbanhang.R;

public class DatHanhThanhCongActivity extends AppCompatActivity {
    TextView txtxemdonhang, txtquaylaitrangchu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hanh_thanh_cong);
        initView();
        initControl();
    }

    private void initControl() {
        txtxemdonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewDonHangActivity.class);
                startActivity(intent);
            }
        });
        txtquaylaitrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        txtxemdonhang = findViewById(R.id.txtxemdonhang);
        txtquaylaitrangchu = findViewById(R.id.txtquaylaitrangchu);
    }
}