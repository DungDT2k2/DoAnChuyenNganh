package com.example.appbanhang.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.appbanhang.model.DonHang;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.LoaiSPAdapter;
import com.example.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.appbanhang.model.LoaiSP;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.model.User;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageSlider imageSlider;
    RecyclerView recyclerViewTrangChu;
    NavigationView navigationView;
    ListView listViewTrangChu;
    DrawerLayout drawerLayout;
    LoaiSPAdapter loaiSPAdapter;
    List<LoaiSP> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imagetimkiem, imgMessage;
    TextView SpMoi;
    CardView img_phone, img_laptop, img_device, img_oder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        getToken();
        Anhxa();
        ActionBar();
        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else {
            Toast.makeText(getApplicationContext(),"no", Toast.LENGTH_LONG).show();
        }
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getId(), s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {

                                            },
                                            throwable -> {
                                                Log.d("log", throwable.getMessage());
                                            }
                                    )
                            );
                        }
                    }
                });
        compositeDisposable.add(apiBanHang.getToken(  1)
                .subscribeOn(Schedulers.io())
                .observeOn (AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                Utils.ID_RECEIVED = String.valueOf(userModel.getResult().get(0).getId());
                            }
                        }, throwable -> {
                        }
                ));
    }

    private void getEventClick() {
        listViewTrangChu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 3:
                        Intent device = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        device.putExtra("loai",3);
                        startActivity(device);
                        break;
                    case 4:
                        Intent lienhe = new Intent(getApplicationContext(),LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 5:
                        Intent donhang = new Intent(getApplicationContext(), ViewDonHangActivity.class);
                        startActivity(donhang);
                        break;
                    case 6:
                        Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                        startActivity(dangnhap);
                        break;
                    case 7:
                        Paper.book().delete("user");
                        Intent dangxuat = new Intent(getApplicationContext(),DangNhapActivity.class);
                        startActivity(dangxuat);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        mangSpMoi = sanPhamMoiModel.getResult();
                        spAdapter = new SanPhamMoiAdapter (getApplicationContext(), mangSpMoi);
                        recyclerViewTrangChu.setAdapter(spAdapter);
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(), "Không kết nối được với sever" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                 loaiSpModel -> {
                     if (loaiSpModel.isSuccess()){
                         mangloaisp = loaiSpModel.getResult();
//                         mangloaisp.add(new LoaiSP("Đăng xuất", ""));
                         loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(),mangloaisp);
                         listViewTrangChu.setAdapter(loaiSPAdapter);
                     }
                 }
        ));
    }

    private void ActionViewFlipper(){
        List<SlideModel> slideModels = new ArrayList<>();
        compositeDisposable.add(apiBanHang.getkhuyenmai()
                .subscribeOn(Schedulers.io())
                .observeOn (AndroidSchedulers.mainThread())
                .subscribe(
                        khuyenMaiModel -> {
                            if (khuyenMaiModel.isSuccess()){
                                for (int i= 0; i< khuyenMaiModel.getResult().size(); i++) {
                                    slideModels.add(new SlideModel (khuyenMaiModel.getResult().get(i).getUrl(), null));
                                }
                                imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                                imageSlider.setItemClickListener(new ItemClickListener() {
                                    @Override
                                    public void onItemSelected(int i) {
                                        Intent intent = new Intent(getApplicationContext(), KhuyenMaiActivity.class);
                                        intent.putExtra("noidung", khuyenMaiModel.getResult().get(i).getThongtin());
                                        intent.putExtra("url", khuyenMaiModel.getResult().get(i).getUrl());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void doubleClick(int i) {

                                    }
                                });
                            }else {
                                Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Log.d("Log", throwable.getMessage());
                        }
                ));
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon (android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void Anhxa(){
        img_phone = findViewById(R.id.img_main_phone);
        img_laptop = findViewById(R.id.img_main_laptop);
        img_device = findViewById(R.id.img_main_device);
        img_oder = findViewById(R.id.img_main_oder);
        SpMoi = findViewById(R.id.sanphammoi);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        SpMoi.startAnimation(animation);
        imagetimkiem = findViewById(R.id.imgtiemkiem);
        imgMessage = findViewById(R.id.image_mess);
        toolbar = findViewById(R.id.toolbartrangchu);
        imageSlider = findViewById(R.id.image_slider);
        recyclerViewTrangChu = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewTrangChu.setLayoutManager(layoutManager);
        recyclerViewTrangChu.setHasFixedSize(true);
        listViewTrangChu = findViewById(R.id.listviewtrangchu);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        // khoi tao list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if(Paper.book().read("giohang") != null){
            Utils.manggiohang = Paper.book().read("giohang");
        }
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }else {
            int totalItem = 0 ;
            for(int i = 0 ; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang); }
        });
        imagetimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TImKiemActivity.class);
                startActivity(intent);
            }
        });
        imgMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DienThoaiActivity.class);
                intent.putExtra("loai",1);
                startActivity(intent);
            }
        });
        img_laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DienThoaiActivity.class);
                intent.putExtra("loai",2);
                startActivity(intent);
            }
        });
        img_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DienThoaiActivity.class);
                intent.putExtra("loai",3);
                startActivity(intent);
            }
        });
        img_oder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewDonHangActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void  onResume(){
        super.onResume();
        int totalItem = 0 ;
        for(int i = 0 ; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    // kiem tra ket noi internet
    private boolean isConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo (ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected()) ) {
             return true;
        }else {
            return false;
        }
    }
    @Override
    protected void onDestroy  (){
        compositeDisposable.clear();
        super.onDestroy();
    }
}





