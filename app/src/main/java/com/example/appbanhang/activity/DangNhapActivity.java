package com.example.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import org.w3c.dom.Text;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangky, txtquenmatkhau, txtlienhe;
    EditText email, matkhau;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Boolean isLogin = false;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DangKyActivity.class);
                startActivity(intent);
            }
        });
        txtquenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuenMatKhauActivity.class);
                startActivity(intent);
            }
        });
        txtlienhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LienHeActivity.class);
                startActivity(intent);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email= email.getText().toString().trim();
                String str_matkhau = matkhau.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Chưa nhập Email", Toast.LENGTH_SHORT).show(); }
                else if (TextUtils.isEmpty(str_matkhau)){
                    Toast.makeText(getApplicationContext(),  "Chưa nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
                }else {
                    Paper.book().write("email",str_email);
                    Paper.book().write("matkhau",str_matkhau);
                    if (firebaseUser != null){
                        //user dang dang nhap firebase
                        dangNhap(str_email, str_matkhau);
                    }else {
                        //user da dang xuat
                        firebaseAuth.signInWithEmailAndPassword(str_email,"456789")
                                .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            dangNhap(str_email, str_matkhau);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangky = findViewById(R.id.txtdangky);
        txtquenmatkhau = findViewById(R.id.txtquenmatkhau);
        txtlienhe = findViewById(R.id.txtlienhe);
        email = findViewById(R.id.email);
        matkhau = findViewById(R.id.matkhau);
        btndangnhap = findViewById(R.id.btndangnhap);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //read data
        if(Paper.book().read("email") != null && Paper.book().read("matkhau") != null){
           email.setText(Paper.book().read("email"));
           matkhau.setText(Paper.book().read("matkhau"));
           if(Paper.book().read("isLogin") != null){
               Boolean flag = Paper.book().read("isLogin");
               if(flag){
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
//                            dangNhap(Paper.book().read("email"),Paper.book().read("matkhau"));
                       }
                   }, 1000);
               }
           }
        }
    }

    private void dangNhap(String email, String matkhau) {
        compositeDisposable.add(apiBanHang.dangnhap(email, matkhau)
                .subscribeOn (Schedulers.io())
                .observeOn (AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                Utils.user_current = userModel.getResult().get(0);
                                Paper.book().write("user",userModel.getResult().get(0));
                                Toast.makeText(getApplicationContext(),"Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getMatkhau() != null) {
            email.setText(Utils.user_current.getEmail());
            matkhau.setText(Utils.user_current.getMatkhau());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
