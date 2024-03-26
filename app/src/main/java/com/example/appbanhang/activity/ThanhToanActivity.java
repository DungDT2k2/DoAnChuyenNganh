package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.appbanhang.model.CreateOrder;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.NotiSendData;
import com.example.appbanhang.retrofit.ApiPushNotification;
import com.example.appbanhang.retrofit.RetrofitClientNoti;
import com.google.gson.Gson;
import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsdt, txtemail;
    EditText txtdiachi;
    AppCompatButton btndathang, btnZaloPay;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem,iddonhang;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        //Zalopay
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        initView();
        countItem();
        initControll();
    }

    private void countItem() {
        totalItem = 0 ;
        for(int i = 0 ; i < Utils.mangmuahang.size(); i++){
            totalItem = totalItem+ Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initControll() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###" + " đ");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txttongtien.setText( decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsdt.setText(Utils.user_current.getSdt());

        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = txtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),  "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else{
                    // post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getSdt();
                    int id = Utils.user_current.getId();
                    Log.d(  "test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.donhang(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        if (messageModel.isSuccess()){
                                            pushNotiToUser();
                                            pushNotiToUser1();
                                            // clear manggiohang bang cach chay qua mangmuahang va clear item
                                            for(int i = 0; i<Utils.mangmuahang.size();  i++){
                                                GioHang gioHang = Utils.mangmuahang.get(i);
                                                if(Utils.manggiohang.contains(gioHang)){
                                                    Utils.manggiohang.remove(gioHang);
                                                }
                                            }
                                            Utils.mangmuahang.clear();
                                            Paper.book().write("giohang", Utils.manggiohang);
                                            Intent intent = new Intent(getApplicationContext(), DatHanhThanhCongActivity.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(getApplicationContext(), "Đặt hàng không thành công !", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Đặt hàng không thành công !", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            )
                    );
                }
            }
        });
        btnZaloPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = txtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else{
                    // post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getSdt();
                    int id = Utils.user_current.getId();
                    Log.d( "test", new Gson().toJson (Utils.mangmuahang));

                    compositeDisposable.add(apiBanHang.donhang(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        pushNotiToUser();
                                        pushNotiToUser1();
                                        Utils.mangmuahang.clear();
                                        iddonhang = Integer.parseInt(messageModel.getIddonhang());
                                        requestZaloPay ();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void requestZaloPay() {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder("100000");
            String code = data.getString("return_code");
            Log.d("test", code);
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                Log.d("test", token);
                ZaloPaySDK.getInstance().payOrder(ThanhToanActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        compositeDisposable.add(apiBanHang.updatezalopay(iddonhang, token)
                                .subscribeOn (Schedulers.io())
                                .observeOn (AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            if (messageModel.isSuccess()) {
                                                Intent intent = new Intent(getApplicationContext(), DatHanhThanhCongActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        },
                                        throwable -> {
                                            Log.d("error", throwable.getMessage());
                                        }
                                ));
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushNotiToUser() {
        //gettoken
        compositeDisposable.add(apiBanHang.getToken(1)
                .subscribeOn(Schedulers.io())
                .observeOn (AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                               for (int i = 0; i < userModel.getResult().size(); i++){
                                   Map<String, String> data = new HashMap<>();
                                   data.put("title",  "Thông báo");
                                   data.put("body",  "Bạn có đơn hàng mới");
                                   NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(), data);
                                   ApiPushNotification apiPushNofication = RetrofitClientNoti.getInstance().create(ApiPushNotification.class);
                                   compositeDisposable.add(apiPushNofication.sendNotification(notiSendData)
                                           .subscribeOn(Schedulers.io())
                                           .observeOn (AndroidSchedulers.mainThread())
                                           .subscribe(
                                                   notiResponse -> {
                                                   },
                                                   throwable -> {
                                                       Log.d(  "Logg", throwable.getMessage());
                                                   }
                                           ));
                               }
                            }
                        },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));
    }
    private void pushNotiToUser1() {
        //gettoken
        compositeDisposable.add(apiBanHang.getToken(0)
                .subscribeOn(Schedulers.io())
                .observeOn (AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                for (int i = 0; i < userModel.getResult().size(); i++){
                                    Map<String, String> data = new HashMap<>();
                                    data.put("title",  "Thông báo");
                                    data.put("body",  "Bạn đã đặt hàng thành công");
                                    NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(), data);
                                    ApiPushNotification apiPushNofication = RetrofitClientNoti.getInstance().create(ApiPushNotification.class);
                                    compositeDisposable.add(apiPushNofication.sendNotification(notiSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn (AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notiResponse -> {
                                                    },
                                                    throwable -> {
                                                        Log.d(  "Logg", throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));
    }
    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbarthanhtoan);
        txtdiachi = findViewById(R.id.txtdiachi);
        txtemail = findViewById(R.id.txtemail);
        txtsdt = findViewById(R.id.txtsdt);
        txttongtien = findViewById(R.id.txttongtien);
        btndathang = findViewById(R.id.btndathang);
        btnZaloPay = findViewById(R.id.btnzalopay);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}