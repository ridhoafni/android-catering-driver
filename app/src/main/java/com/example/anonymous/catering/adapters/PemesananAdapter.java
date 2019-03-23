package com.example.anonymous.catering.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.catering.R;
import com.example.anonymous.catering.models.Pemesanan;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ui.activities.GuruDetailActivity;

public class PemesananAdapter extends RecyclerView.Adapter<PemesananAdapter.PemesananViewHolder> implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{
    LocationManager locationManager;
    private static final String KEY_ID_MEMBER = "id_member";
    private static final String KEY_NAMA_MEMBER = "nama_member";
    private Context context;
    private List<Pemesanan> getAllDataPemesanan;
    double lat;
    double lng;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;

    double latitude;
    double longitude;
    double long_pem,lat_pem;
    int id;
    String nama_member, nama_paket, total, alamat, tgl_pemesanan, pesanan_tambahan, status, jumlah_pesanan;

//    LocationHelper locationHelper;


    public PemesananAdapter(Context context, List<Pemesanan> getAllDataPemesanan, double lat_pem, double long_pem){
        this.context        = context;
        this.getAllDataPemesanan = getAllDataPemesanan;
        this.long_pem = long_pem;
        this.lat_pem = lat_pem;

//        locationHelper = new LocationHelper(context);
//        locationHelper.checkpermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location!=null){
            Toast.makeText(context, "Lokasi Ditemukan", Toast.LENGTH_SHORT).show();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng MyLocation = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(MyLocation).title("Latitude: "+location.getLatitude()).snippet("Longitude : "+location.getLongitude()));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation,13));
        }else{
            Toast.makeText(context, "Lokasi Tidak Di temukan", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public PemesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_pemesanan, parent, false);
        return new PemesananViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PemesananViewHolder pemesananViewHolder, int i) {
        Pemesanan pemesanan = getAllDataPemesanan.get(i);
//        Glide.with(context)
//                .load(ServerConfig.GURU_PATH+guru.getPhoto_profile())
//                .apply(new RequestOptions().override(100, 100))
//                .into(guruViewHolder.imgViewPhoto);
        lat = Double.parseDouble(pemesanan.getLatitude());

        System.out.println("Lat : "+lat);
        System.out.println("Latitude oke : "+latitude);
        System.out.println("Long : "+lng);
        System.out.println("Longitude oke : "+longitude);

        lng = Double.parseDouble(pemesanan.getLongitude());
        id = Integer.parseInt(pemesanan.getMemberId());
        nama_member = pemesanan.getNamaLengkap();
        nama_paket = pemesanan.getNamaPaket();
        alamat = pemesanan.getAlamatLengkap();
        jumlah_pesanan = pemesanan.getJumlahPesanan();
        total = String.valueOf(pemesanan.getTotal());
        tgl_pemesanan= pemesanan.getTglPesanan();
        pesanan_tambahan = pemesanan.getPesanTambahan();
        status = pemesanan.getStatus();
        latitude = Double.parseDouble(pemesanan.getLatitude());
        longitude = Double.parseDouble(pemesanan.getLongitude());

        id = Integer.parseInt(pemesanan.getMemberId());
        id = Integer.parseInt(pemesanan.getMemberId());
        pemesananViewHolder.textViewNamaMember.setText(pemesanan.getAlamatLengkap());
        pemesananViewHolder.textViewNamaPaket.setText(pemesanan.getNamaPaket());
        pemesananViewHolder.textViewStatus.setText(pemesanan.getStatus());
//        pemesananViewHolder.textViewJumlahPesanan.setText(pemesanan.getJumlahPesanan());
        pemesananViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), GuruDetailActivity.class);
                i.putExtra(GuruDetailActivity.KEY_ID_MEMBER, id);
                i.putExtra(GuruDetailActivity.KEY_NAMA_MEMBER, nama_member);
                i.putExtra(GuruDetailActivity.KEY_NAMA_PAKET, nama_paket);
                i.putExtra(GuruDetailActivity.KEY_ALAMAT, alamat);
                i.putExtra(GuruDetailActivity.KEY_JUMLAH_PESANAN, jumlah_pesanan);
                i.putExtra(GuruDetailActivity.KEY_TOTAL, total);
                i.putExtra(GuruDetailActivity.KEY_TGL_PEMESANAN, tgl_pemesanan);
                i.putExtra(GuruDetailActivity.KEY_PESAN_TAMBAHAN, pesanan_tambahan);
                i.putExtra(GuruDetailActivity.KEY_STATUS, status);
                i.putExtra(GuruDetailActivity. KEY_LAT, latitude);
                i.putExtra(GuruDetailActivity. KEY_LONG, longitude);



//                i.putExtra(GuruDetailActivity.KEY_NAMA_MEMBER, nama_member);
                context.startActivity(i);
            }
        });
//        pemesananViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                mLastLocation=locationHelper.getLocation();
////                mapRute();
//                  aksiDetailPemesanan();
//            }
//        });
    }

//    private void aksiDetailPemesanan() {
//        Intent detailPesanan = new Intent(context, GuruDetailActivity.class );
//        detailPesanan.putExtra(GuruDetailActivity.KEY_ID_GURU, id);
//    }

    private void mapRute() {
        Log.d("LatPemesananSekarang",""+this.lat_pem);
        System.out.println("Rute Lat :"+this.lat_pem);
        System.out.println("Rute Long :"+this.long_pem);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+this.lat_pem+","+this.long_pem+"&daddr="+lat+","+lng+""));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return getAllDataPemesanan.size();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public class PemesananViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_nama_member)
        TextView textViewNamaMember;
        @BindView(R.id.tv_nama_paket) TextView textViewNamaPaket;
        @BindView(R.id.tv_jumlah_pesanan) TextView textViewJumlahPesanan;
        @BindView(R.id.tv_status) TextView textViewStatus;
        @BindView(R.id.btn_detail)
        Button btnDetail;
        public PemesananViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}