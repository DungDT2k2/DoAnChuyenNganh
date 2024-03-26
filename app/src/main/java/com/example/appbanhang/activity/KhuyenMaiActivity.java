package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;

import org.w3c.dom.Text;

public class KhuyenMaiActivity extends AppCompatActivity {
    TextView noidung;
    ImageView imageView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyen_mai);
        initView();
        initData();
        ActionToolBar();

    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        String nd = getIntent().getStringExtra( "noidung");
        String url = getIntent().getStringExtra( "url");
        noidung.setText(nd);
        Glide.with( this).load(url).into(imageView);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarkm);
        noidung = findViewById(R.id.km_noidung);
        imageView = findViewById(R.id.km_image);
    }
}