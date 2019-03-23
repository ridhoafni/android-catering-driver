package ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anonymous.catering.R;
import com.example.anonymous.catering.config.ServerConfig;
import com.example.anonymous.catering.utils.SessionManager;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private Context context;
    TextView tvNama, tvEmail, tvJk, tvNisn, tvHP;
    ImageView imgProfile;
    SessionManager sessionManager;
    private String SESSION_NAMA, SESSION_HP, SESSION_EMAIL,SESSION_ALAMAT, SESSION_NISN, SESSION_KELAS, SESSION_JK, SESSION_NAMA_SEKOLAH, SESSION_PHOTO;
    int SESSION_ID_MURID;
    private static final String KEY_ID_MURID        = "id";
    private static final String KEY_NAMA            = "nama";
    private static final String KEY_HP              = "no_hp";
    private static final String KEY_EMAIL           = "email";
    private static final String KEY_ALAMAT          = "alamat";
    private static final String KEY_JK              = "jk";
    private static final String KEY_NISN            = "nisn";
    private static final String KEY_KELAS           = "kelas";
    private static final String KEY_NAMA_SEKOLAH    = "nama_sekolah";
    private static final String KEY_PHOTO           = "photo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile  = findViewById(R.id.img_profile);
        tvNama      = findViewById(R.id.tv_nama);
        tvEmail     = findViewById(R.id.tv_email);
        tvHP        = findViewById(R.id.tv_no_hp);
        tvJk        = findViewById(R.id.tv_jk);
        tvNisn      = findViewById(R.id.tv_nisn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile Murid");

        sessionManager = new SessionManager(this);

        tvNama.setText(sessionManager.getDriverProfile().get("nama"));
        tvEmail.setText(sessionManager.getDriverProfile().get("email"));
        tvHP.setText(sessionManager.getDriverProfile().get("no_hp"));
        tvJk.setText(sessionManager.getDriverProfile().get("jk"));

        SESSION_ID_MURID        = Integer.parseInt(sessionManager.getDriverProfile().get("id"));
        SESSION_NAMA            = sessionManager.getDriverProfile().get("nama");
        SESSION_HP              = sessionManager.getDriverProfile().get("no_hp");
        SESSION_EMAIL           = sessionManager.getDriverProfile().get("email");
        SESSION_ALAMAT          = sessionManager.getDriverProfile().get("alamat");
        SESSION_JK              = sessionManager.getDriverProfile().get("jk");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), MainTabActivity.class);
                startActivity(i);
                break;

            case R.id.action_edit_profile:
                Intent edit = new Intent(getApplicationContext(), UploadImage.class);
                edit.putExtra(ProfileActivity.KEY_ID_MURID,     SESSION_ID_MURID);
                edit.putExtra(ProfileActivity.KEY_NAMA,         SESSION_NAMA);
                edit.putExtra(ProfileActivity.KEY_HP,           SESSION_HP);
                edit.putExtra(ProfileActivity.KEY_EMAIL,        SESSION_EMAIL);
                startActivity(edit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
