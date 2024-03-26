package com.example.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email, matkhau, nhaplaimatkhau, sdt, tenuser;
    AppCompatButton  button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FirebaseAuth firebaseAuth;
    TextView txtdangnhap, txtlienhe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControll();
    }
    private void initControll() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangki();
            }
        });
        txtdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                startActivity(intent);
            }
        });
        txtlienhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LienHeActivity.class);
                startActivity(intent);
            }
        });
    }
    private void dangki() {
        String str_email = email.getText().toString().trim();
        String str_matkhau = matkhau.getText().toString().trim();
        String str_rematkhau = nhaplaimatkhau.getText().toString().trim();
        String str_sdt = sdt.getText().toString().trim();
        String str_tenuser = tenuser.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_tenuser)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Tên tài khoản", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_matkhau)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_rematkhau)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập xác nhận Mật Khẩu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_sdt)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập SDT", Toast.LENGTH_SHORT).show();
        } else {
            if (str_matkhau.equals(str_rematkhau)){
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(str_email, "456789")
                        .addOnCompleteListener(DangKyActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null){
                                        postData(str_email, str_matkhau, str_tenuser, str_sdt, user.getUid());
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), "Email đã tồn tại ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                Toast.makeText(getApplicationContext(), "Mật Khẩu không giống nhau", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postData(String str_email, String str_matkhau, String str_tenuser, String str_sdt, String uid){
        compositeDisposable.add(apiBanHang.dangky(str_email,str_matkhau,str_tenuser,str_sdt, uid)
                .subscribeOn(Schedulers.io())
                .observeOn (AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setMatkhau(str_matkhau);
                                Toast.makeText(getApplicationContext(),"Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Đăng ký không thành thành công", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        matkhau = findViewById (R.id.matkhau);
        nhaplaimatkhau = findViewById(R.id.nhaplaimatkhau);
        sdt = findViewById(R.id.sdt);
        tenuser = findViewById(R.id.tenuser);
        button = findViewById(R.id.btndangky);
        txtdangnhap = findViewById(R.id.txtdangnhap);
        txtlienhe = findViewById(R.id.txtlienhe);
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}