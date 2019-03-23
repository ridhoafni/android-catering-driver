package ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.catering.R;
import com.example.anonymous.catering.config.ServerConfig;
import com.example.anonymous.catering.models.Guru;
import com.example.anonymous.catering.response.ResponGuruDetail;
import com.example.anonymous.catering.rests.ApiClient;
import com.example.anonymous.catering.rests.ApiInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuruDetailActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    double lat_sekarang, long_sekarang;
    Button btnRute;

    public static final String KEY_ID_MEMBER = "id_member";
    public static final String KEY_NAMA_MEMBER = "nama_member";
    public static final String KEY_NAMA_PAKET = "nama_paket";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_JUMLAH_PESANAN = "jumlah_pesanan";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_TGL_PEMESANAN = "tgl_pemesanan";
    public static final String KEY_PESAN_TAMBAHAN = "pesan_tambahan";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitude";

    public static final String URL = ServerConfig.API_ENDPOINT;
    TextView tv_nama_member, tv_nama_paket, tv_jumlah_pesanan, tv_total, tv_alamat, tv_tgl_pemesanan, tv_pesanan_tambahan, tv_status;;
    ImageView iv_foto, iv_foto2;
    Toolbar toolbarDetail;
    int id;
    double latitude, longitude;
    String total;
    String jumlah_pesanan;
    String nama_member, nama_paket, alamat, tgl_pesanan, pesan_tambahan, status;
    CollapsingToolbarLayout collapsingToolbar;
    ApiInterface apiService;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
//        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        this.getSupportActionBar().setHomeButtonEnabled(true);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        this.getSupportActionBar().setTitle("Detail Pesanan");

        setContentView(R.layout.activity_guru_detail);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        getLocation();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_nama_member = (TextView) findViewById(R.id.tv_nama_member);
        tv_nama_paket = (TextView) findViewById(R.id.tv_nama_paket);
        tv_alamat = (TextView) findViewById(R.id.tv_alamatisi);
        tv_jumlah_pesanan = (TextView) findViewById(R.id.tv_jumlah_pesanan_isi);
        tv_total = (TextView) findViewById(R.id.tv_total_isi);
        tv_tgl_pemesanan = (TextView) findViewById(R.id.tv_tgl_pesan_isi);
        tv_pesanan_tambahan = (TextView) findViewById(R.id.tv_pesan_isi);
        tv_status = (TextView) findViewById(R.id.tv_status_isi);
        btnRute = (Button) findViewById(R.id.btnRute);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("Pemesanan");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuruDetailActivity.this, MainTabActivity.class));
            }
        });

        Intent i = getIntent();
        id = i.getIntExtra(KEY_ID_MEMBER, 0);
        nama_member = i.getStringExtra(KEY_NAMA_MEMBER);
        nama_paket = i.getStringExtra(KEY_NAMA_PAKET);
        alamat = i.getStringExtra(KEY_ALAMAT);
        jumlah_pesanan = i.getStringExtra(KEY_JUMLAH_PESANAN);
        total = i.getStringExtra(KEY_TOTAL);
        tgl_pesanan = i.getStringExtra(KEY_TGL_PEMESANAN);
        pesan_tambahan = i.getStringExtra(KEY_PESAN_TAMBAHAN);
        status = i.getStringExtra(KEY_STATUS);
        latitude = i.getDoubleExtra(KEY_LAT,0);
        longitude = i.getDoubleExtra(KEY_LONG,0);


        tv_nama_paket.setText(nama_paket);
        tv_nama_member.setText(nama_member);
        tv_alamat.setText(alamat);
        tv_jumlah_pesanan.setText(jumlah_pesanan);
        tv_total.setText(total);
        tv_tgl_pemesanan.setText(tgl_pesanan);
        tv_pesanan_tambahan.setText(pesan_tambahan);
        tv_status.setText(status);

        System.out.println("ID ANDA: "+id);
        System.out.println("NAMA PAKET: "+nama_paket);
        System.out.println("NAMA MEMBER: "+nama_member);
        System.out.println("ALAMAT: "+alamat);
        System.out.println("JUMLAH PESANAN: "+jumlah_pesanan);
        System.out.println("TOTAL: "+total);
        System.out.println("TANGGAL PESANAN: "+tgl_pesanan);
        System.out.println("PESAN TAMBAHAN: "+pesan_tambahan);
        System.out.println("STATUS: "+status);
        System.out.println("LATITUDE: "+latitude);
        System.out.println("LONGITUDE: "+longitude);


        apiService  = ApiClient.getClient(URL).create(ApiInterface.class);
//        iv_foto     = findViewById(R.id.img_item_photo_detail);
//        tv_pen      = findViewById(R.id.tv_item_pendidikan_detail);
//        tv_bio      = findViewById(R.id.tv_item_bio_detail);
//
//        collapsingToolbar = findViewById(R.id.toolbar_layout);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setTitleTextColor(Color.BLUE);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        apiService.guruFindById(id).enqueue(new Callback<ResponGuruDetail>() {

            @Override
            public void onResponse(Call<ResponGuruDetail> call, Response<ResponGuruDetail> response) {
                System.out.println(response.toString());

                if (response.isSuccessful()){
                    System.out.println(response.body().toString());
                    System.out.println(id);
                    ArrayList<Guru> gurus = new ArrayList<>();
                    gurus.add(response.body().getMaster());
                    Guru guru = gurus.get(0);
                    collapsingToolbar.setTitle(guru.getNama());
//

                }
            }

            @Override
            public void onFailure(Call<ResponGuruDetail> call, Throwable t) {
                t.printStackTrace();
            }
        });
        System.out.println("Latitude GAGAL : "+lat_sekarang);
        System.out.println("Longitude GAGAL : "+long_sekarang);
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLocationChanged(Location location) {
//        locationcationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        System.out.println("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
//                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
            lat_sekarang = addresses.get(0).getLatitude();
            long_sekarang = addresses.get(0).getLongitude();

            btnRute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapRute(lat_sekarang, long_sekarang);
                }
            });

            System.out.println("Latitude Sekarang : "+lat_sekarang);
            System.out.println("Longitude Sekarang : "+long_sekarang);


        }catch(Exception e)
        {

        }

    }

    private void mapRute(double lat_sekarang, double long_sekarang) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+lat_sekarang+","+long_sekarang+"&daddr="+latitude+","+longitude+""));
        startActivity(intent);
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


//    private void mapRute() {
//        Log.d("LatPemesananSekarang",""+this.lat_pem);
//        System.out.println("Rute Lat :"+lat_sekarang);
//        System.out.println("Rute Lat :"+long_sekarang);
//        System.out.println("Rute Long :"+longitude);
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?saddr="+lat_sekarang+","+long_sekarang+"&daddr="+latitude+","+longitude+""));
//        startActivity(intent); Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
////                Uri.parse("http://maps.google.com/maps?saddr="+lat_sekarang+","+long_sekarang+"&daddr="+latitude+","+longitude+""));
////        startActivity(intent);
//    }
}
