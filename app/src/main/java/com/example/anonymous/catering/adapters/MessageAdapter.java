package com.example.anonymous.catering.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anonymous.catering.R;
import com.example.anonymous.catering.models.Chat;
import com.example.anonymous.catering.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT= 1;
    private Context context;
    private List<Chat> mChat;

    SessionManager sessionManager;


    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> mChat){
        this.context = context;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
//        if (imageurl.equals("default")){
//            holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
//
//        }else{
//            Glide.with(context).load(imageurl).into(holder.profile_image);
//        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int position) {
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        System.out.println("firebaseUser getUid"+firebaseUser.getUid());
        sessionManager = new SessionManager(context);
        if (mChat.get(position).getSender().equals(sessionManager.getDriverProfile().get("id_driver"))){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}

