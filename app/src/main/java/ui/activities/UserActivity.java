package ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.anonymous.catering.R;
import com.example.anonymous.catering.adapters.MemberAdapter;
import com.example.anonymous.catering.adapters.PemesananAdapter;
import com.example.anonymous.catering.config.ServerConfig;
import com.example.anonymous.catering.models.Member;
import com.example.anonymous.catering.models.Pemesanan;
import com.example.anonymous.catering.response.ResponseMember;
import com.example.anonymous.catering.response.ResponsePemesanan;
import com.example.anonymous.catering.rests.ApiClient;
import com.example.anonymous.catering.rests.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    private List<Member> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemberAdapter memberAdapter;
    private List<Member> members = new ArrayList<>();
    public static final String URL = ServerConfig.API_ENDPOINT;

    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat");

        apiService = ApiClient.getClient(ServerConfig.API_ENDPOINT).create(ApiInterface.class);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(memberAdapter);

        members = new ArrayList<>();

        readMembers();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), MainTabActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void readMembers() {
        apiService.memberFindAll().enqueue(new Callback<ResponseMember>() {
            @Override
            public void onResponse(Call<ResponseMember> call, Response<ResponseMember> response) {
//                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body().getMaster().size()>0){
                        datas = response.body().getMaster();
                        memberAdapter = new MemberAdapter(UserActivity.this, datas);
                        recyclerView.setAdapter(memberAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMember> call, Throwable t) {

            }
        });
    }
}
