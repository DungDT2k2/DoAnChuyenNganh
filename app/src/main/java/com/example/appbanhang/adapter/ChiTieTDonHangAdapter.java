package com.example.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.ImageClickListenner;
import com.example.appbanhang.R;
import com.example.appbanhang.model.EventBus.TotalEvent;
import com.example.appbanhang.model.Item;
import com.example.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class ChiTieTDonHangAdapter extends RecyclerView.Adapter<ChiTieTDonHangAdapter.MyViewHolder>{
    Context context;
    List<Item> itemList;

    public ChiTieTDonHangAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chittietdonhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtten.setText(item.getTensp() + " ");
        holder.txtsoluong.setText("Số Lượng: " + item.getSoluong() + " ");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long  gia = item.getSoluong() * item.getGia();
        holder.txtgia.setText(decimalFormat.format(gia )+  " đ");
        Glide.with(context).load(item.getHinhanh()).into(holder.imagechitet);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagechitet;
        TextView txtten, txtsoluong, txtgia;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitet = itemView.findViewById(R.id.item_imgchitiet);
            txtten = itemView.findViewById(R.id.item_tenspchitiet);
            txtsoluong = itemView.findViewById(R.id.item_soluongchitiet);
            txtgia = itemView.findViewById(R.id.item_giachitiet);
        }

    }


}
