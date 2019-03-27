package com.example.anonymous.catering.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anonymous.catering.R;
import com.example.anonymous.catering.models.Member;
import com.example.anonymous.catering.utils.CustomOnItemClickListener;

import java.util.List;

import ui.activities.GuruDetailActivity;
import ui.activities.MessageActivity;

public class MemberAdapter  extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{

    private Context context;
    private List<Member> members;
    int id;

    public MemberAdapter(Context context, List<Member> members){
        this.context = context;
        this.members = members;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item, viewGroup, false);
        return new MemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Member user = members.get(i);
        viewHolder.username.setText(user.getEmail());
        id = user.getIdMember();
//        if (user.get().equals("default")){
//            viewHolder.profile_image.setImageResource(R.drawable.man);
//
//        }else{
//            Glide.with(context).load(user.getImageURL()).into(viewHolder.profile_image);
//        }

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("id member :"+id);
//                Intent intent = new Intent(context, MessageActivity.class);
//                intent.putExtra(MessageActivity.KEY_ID_MEMBER, id);
//                context.startActivity(intent);
//            }
//        });
        viewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                int idMember = user.getIdMember();
                System.out.println("Id member :"+idMember);
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra(MessageActivity.KEY_ID_MEMBER, idMember);
                context.startActivity(intent);
            }
        }));
    }


    @Override
    public int getItemCount() {
        return members.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }
}