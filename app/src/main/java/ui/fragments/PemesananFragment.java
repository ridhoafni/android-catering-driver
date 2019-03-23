package ui.fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anonymous.catering.R;
import com.example.anonymous.catering.adapters.GuruAdapter;
import com.example.anonymous.catering.adapters.PemesananAdapter;
import com.example.anonymous.catering.config.ServerConfig;
import com.example.anonymous.catering.models.Guru;
import com.example.anonymous.catering.models.Pemesanan;
import com.example.anonymous.catering.response.ResponseGuru;
import com.example.anonymous.catering.response.ResponsePemesanan;
import com.example.anonymous.catering.rests.ApiClient;
import com.example.anonymous.catering.rests.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ui.activities.GuruActivity;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class PemesananFragment extends Fragment implements LocationListener {


    public PemesananFragment() {
        // Required empty public constructor
    }
    public static final String URL = ServerConfig.API_ENDPOINT;
    private List<Pemesanan> datas = new ArrayList<>();
    private PemesananAdapter pemesananAdapter;

    RecyclerView recyclerViewPemesanan;
    ProgressBar progressBar;
    Context context;
    ApiInterface apiService;
    LocationManager locationManager;
    double lat_pemesanan, long_pemesanan;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pemesanan, container, false);

        recyclerViewPemesanan = (RecyclerView)view.findViewById(R.id.recyclerViewPemesanan);
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar_pemesanan);

        getLocation();

        pemesananAdapter = new PemesananAdapter(getActivity(), datas, lat_pemesanan, long_pemesanan );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewPemesanan.setLayoutManager(layoutManager);
        recyclerViewPemesanan.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPemesanan.setAdapter(pemesananAdapter);
        apiService = ApiClient.getClient(ServerConfig.API_ENDPOINT).create(ApiInterface.class);
        loadDataPemesanan();
        Log.d("percobaan1",""+lat_pemesanan);
        // check location
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        return view;
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
//        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        this.lat_pemesanan = location.getLatitude();
        this.long_pemesanan = location.getLongitude();
        System.out.println("Latitude Sekarang: " + lat_pemesanan + "\n Longitude Sekarang: " + long_pemesanan);
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
//                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
            System.out.println("\n"+addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)

        {

        }

    }

    private void loadDataPemesanan() {
        apiService.pemesananStatus("Diproses").enqueue(new Callback<ResponsePemesanan>() {
            @Override
            public void onResponse(Call<ResponsePemesanan> call, Response<ResponsePemesanan> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body().getMaster().size()>0){
                        datas = response.body().getMaster();
                        pemesananAdapter = new PemesananAdapter(getActivity(), datas, lat_pemesanan, long_pemesanan);
                        recyclerViewPemesanan.setAdapter(pemesananAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePemesanan> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
