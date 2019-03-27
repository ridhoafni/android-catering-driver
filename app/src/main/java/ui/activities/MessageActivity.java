package ui.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anonymous.catering.R;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anonymous.catering.adapters.MessageAdapter;
import com.example.anonymous.catering.config.ServerConfig;
import com.example.anonymous.catering.models.Chat;
import com.example.anonymous.catering.models.Guru;
import com.example.anonymous.catering.models.Member;
import com.example.anonymous.catering.response.ResponGuruDetail;
import com.example.anonymous.catering.response.ResponseDetailMember;
import com.example.anonymous.catering.rests.ApiClient;
import com.example.anonymous.catering.rests.ApiInterface;
import com.example.anonymous.catering.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    public static final String KEY_ID_MEMBER = "id_member";
    SessionManager sessionManager;
    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    int id, userId ;
    Intent intent;
    ImageButton btn_send;
    EditText text_send;
    private MessageAdapter messageAdapter;
    private List<Chat> mChat;
    private RecyclerView recyclerView;
    ApiInterface apiService;
    public static final String URL = ServerConfig.API_ENDPOINT;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_message);

        sessionManager = new SessionManager(MessageActivity.this);

        apiService  = ApiClient.getClient(URL).create(ApiInterface.class);

        id = Integer.parseInt(sessionManager.getDriverProfile().get("id_driver"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        mChat = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userId = intent.getIntExtra(KEY_ID_MEMBER, 0);

        System.out.println("User id: "+userId);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(id, userId, msg);
                }else{
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

//        System.out.println("Reference user: "+reference);

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Member user = dataSnapshot.getValue(Member.class);
//                username.setText(user.getUsername());
////                if (user.getImageURL().equals("default")){
////                    profile_image.setImageResource(R.mipmap.ic_launcher_round);
////
////                }else{
////                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
////                }
//
//                System.out.println("getUid: "+firebaseUser.getUid());
//                System.out.println("userId: "+userId);
////                System.out.println("ImageURL: "+user.get());
//
//                readMessages(String.valueOf(id), userId);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        intent = getIntent();
        userId = intent.getIntExtra(KEY_ID_MEMBER, 0);
        apiService.memberFindById(userId).enqueue(new Callback<ResponseDetailMember>() {

            @Override
            public void onResponse(Call<ResponseDetailMember> call, Response<ResponseDetailMember> response) {
                System.out.println("Response member detail: "+response);
                if (response.isSuccessful()){
                    System.out.println(response.body().toString());
                    System.out.println(id);
                    ArrayList<Member> members = new ArrayList<>();
                    members.add(response.body().getMaster());
                    Member member = members.get(0);
//                  collapsingToolbar.setTitle(guru.getNama());
                    username.setText(member.getUsername());

                    readMessages(String.valueOf(id), String.valueOf(userId));

                }
            }

            @Override
            public void onFailure(Call<ResponseDetailMember> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendMessage(int sender, int receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", String.valueOf(sender));
        hashMap.put("receiver", String.valueOf(receiver));
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(final String id, final String userid){
        mChat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        System.out.println("data reference:"+reference);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("data snapshot:"+dataSnapshot);

                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    System.out.println("Data chat"+snapshot.getValue());

                    Log.d("TAG CHAT", chat.getMessage() + " / " +
                            chat.getReceiver() + " / " +
                            chat.getSender()+" /"+dataSnapshot.getValue());

                    System.out.println("IDNYA :"+id);

                    if (chat.getReceiver().equals(id) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(id)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Cancelled", databaseError.toString() );

            }
        });
    }
}

