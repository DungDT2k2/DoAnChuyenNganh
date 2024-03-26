package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.ChatMessage;

import java.lang.reflect.Type;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessages;
    private String sendid;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVED = 2;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages, String sendid) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.sendid = sendid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.item_send_gui, parent,  false);
            return new SendMessViewHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_received_nhan, parent,  false);
            return new ReceivedViewHolder (view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SEND) {
            ((SendMessViewHolder) holder).txtmessend.setText(chatMessages.get(position).mess);
            ((SendMessViewHolder) holder).txttimesend.setText(chatMessages.get(position).datetime);
        }else {
            ((ReceivedViewHolder) holder).txtmessreceived.setText(chatMessages.get(position).mess);
            ((ReceivedViewHolder) holder).txttimereceived.setText(chatMessages.get(position).datetime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).sendid.equals(sendid)){
            return TYPE_SEND;
        }else {
            return TYPE_RECEIVED;
        }
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder{
        TextView txtmessend, txttimesend;

        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmessend = itemView.findViewById(R.id.txtmesssend);
            txttimesend = itemView.findViewById(R.id.txttimesend);
        }
    }

    class  ReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView txtmessreceived, txttimereceived;
        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmessreceived = itemView.findViewById(R.id.txtmessreceived);
            txttimereceived = itemView.findViewById(R.id.txttimereceived);
        }
    }
}
