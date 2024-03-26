package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appbanhang.Interface.ItemClickDeleteListener;
import com.example.appbanhang.R;
import com.example.appbanhang.model.DonHang;
import com.example.appbanhang.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonhang;
    ItemClickDeleteListener deleteListener;
    public DonHangAdapter(Context context, List<DonHang> listdonhang, ItemClickDeleteListener deleteListener) {
        this.context = context;
        this.listdonhang = listdonhang;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder.txtdonhang.setText("Đơn hàng số : " + donHang.getId());
        holder.txtdiachi.setText("Địa chỉ giao hàng : " + donHang.getDiachi());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tongtien.setText("Tổng Tiền : " + decimalFormat.format(Double.parseDouble(donHang.getTongtien())) + " đ");
        holder.trangthai.setText(Utils.trangThaiDon(donHang.getTrangthai()));
        holder.txtsdt.setText("SĐT : " + donHang.getSdt());
        holder.tenuser.setText("Tên người đặt : " + Utils.user_current.getTenuser());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteListener.onClickDelete(donHang.getId());
                return false;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rechitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());

        ChiTieTDonHangAdapter chiTieTDonHangAdapter = new ChiTieTDonHangAdapter(context,donHang.getItem());
        holder.rechitiet.setLayoutManager(layoutManager);
        holder.rechitiet.setAdapter(chiTieTDonHangAdapter);
        holder.rechitiet.setRecycledViewPool(viewPool);
        
    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtdonhang, tongtien, tenuser, trangthai, txtdiachi, txtsdt;
        RecyclerView rechitiet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            rechitiet = itemView.findViewById(R.id.recycleview_chitiet);
            tongtien = itemView.findViewById(R.id.donhang_tongtien);
            tenuser = itemView.findViewById(R.id.donhang_tenuser);
            trangthai = itemView.findViewById(R.id.trangthai);
            txtdiachi = itemView.findViewById(R.id.diachi);
            txtsdt = itemView.findViewById(R.id.sdt);
        }
    }
}
