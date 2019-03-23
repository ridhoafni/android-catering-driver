package ui.fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.catering.PlacePickerActivity;
import com.example.anonymous.catering.R;
import com.example.anonymous.catering.config.ServerConfig;
import com.example.anonymous.catering.models.Paket;
import com.example.anonymous.catering.response.ResponGuruDetail;
import com.example.anonymous.catering.response.ResponseCreatePemesanan;
import com.example.anonymous.catering.response.ResponsePaket;
import com.example.anonymous.catering.response.ResponsePaketById;
import com.example.anonymous.catering.rests.ApiClient;
import com.example.anonymous.catering.rests.ApiInterface;
import com.example.anonymous.catering.utils.PermissionUtils;
import com.example.anonymous.catering.utils.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Api;
import android.location.Location;
import android.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ui.activities.MainTabActivity;
import ui.activities.PembayaranActivity;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaketMenuFragment extends Fragment implements LocationListener {

    // LogCat tag

    EditText GetetFormJumlahTotalHarga, GetetFormJumlahPaket, GetEtFormPesanTambahan, GetEtFormAlamat;
    Button btnIncrease, btnDecrease, btnAlamatMaps, btnSimpan, btnTgl;
    String nama_paket, alamat_lengkap, pesanan_tambahan;
    double latitude, longitude;
    double GetLatitude, GetLongitude;
    int jumlah_pesanan, total;
    DatePickerDialog datePickerDialog;
    int minteger = 0, harga_satuan;
    int PLACE_PICKER_REQUEST = 1;
    LinearLayout linearLayoutPaket;
    LocationManager locationManager;
    SessionManager sessionManager;
    ApiInterface apiService;
    NumberFormat formatRupiah;
    TextView GetetFormHarga, GetTvPaket, GetTvPembuka, GetTvPenutup, GetTvPondok;
    Spinner spinnerPaket;
    String selectedName;
    double harga, i;
    Locale localeID;
    Integer status;
    int member_id, harga_total_db;
    public static MainTabActivity mainTabActivity;
    Context context;

    public PaketMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daftar_pesanan, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        linearLayoutPaket         = (LinearLayout)view.findViewById(R.id.LinerPaket);
        GetetFormJumlahTotalHarga = (EditText)view.findViewById(R.id.etFormJumlahTotalHarga);
        GetetFormJumlahPaket      = (EditText)view.findViewById(R.id.etFormJumlahPaket);
        btnIncrease               = (Button)view.findViewById(R.id.increase);
        btnDecrease               = (Button)view.findViewById(R.id.decrease);
        btnAlamatMaps             = (Button)view.findViewById(R.id.btnAlamatMaps);
        btnSimpan                 = (Button)view.findViewById(R.id.btn_simpan);
        GetetFormHarga            = (TextView)view.findViewById(R.id.etFormHarga);
        GetTvPaket                = (TextView)view.findViewById(R.id.tvPaket);
        GetEtFormPesanTambahan    = (EditText) view.findViewById(R.id.etFormPesanTambahan);
        GetTvPembuka              = (TextView)view.findViewById(R.id.tvPaketMenuPembuka);
        GetTvPenutup              = (TextView)view.findViewById(R.id.tvPaketMenuPenutup);
        GetTvPondok               = (TextView)view.findViewById(R.id.tvPaketMenuMakananPondok);
        GetEtFormAlamat           = (EditText)view.findViewById(R.id.etFormAlamat);
        spinnerPaket              = (Spinner)view.findViewById(R.id.spinnerJenisPaket);

        GetetFormJumlahTotalHarga.setEnabled(false);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataPemesanan();
            }
        });

        sessionManager = new SessionManager(getActivity());

        member_id = Integer.parseInt(sessionManager.getDriverProfile().get("id_member"));

        System.out.println("Member ID :"+member_id);
        apiService = ApiClient.getClient(ServerConfig.API_ENDPOINT).create(ApiInterface.class);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        btnAlamatMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


        initAutoComplateJenisPaket();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataPemesanan();
            }
        });

        localeID = new Locale("id", "ID");

        formatRupiah = NumberFormat.getCurrencyInstance(localeID);

//        harga_satuan = 34900;

        System.out.println("Harga :"+harga_satuan);

//        String number = Integer.toString(harga);
        GetetFormHarga.setText(formatRupiah.format((double)harga_satuan));


        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseInteger();
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseInteger();
            }
        });

        GetetFormJumlahPaket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GetetFormJumlahTotalHarga.setText(addNumbers());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        GetetFormHarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GetetFormJumlahTotalHarga.setText(addNumbers());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void initAutoComplateJenisPaket(){
        apiService.paketFindAll().enqueue(new Callback<ResponsePaket>() {
            @Override
            public void onResponse(Call<ResponsePaket> call, Response<ResponsePaket> response) {
                if (response.isSuccessful()){

                    System.out.println(response.body().toString());
//                    System.out.println(id);
                    List<Paket> pakets = response.body().getMaster();
                    List<String> jenis_paket = new ArrayList<>();

                    for (int i = 0; i < pakets.size(); i++) {
                    jenis_paket.add(pakets.get(i).getNamaPaket());
                }
                    ArrayAdapter<String> adapterJI = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, jenis_paket);
                    adapterJI.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPaket.setPrompt("Pilih Jenis Institusi");
                    spinnerPaket.setAdapter( adapterJI);
                }

                spinnerPaket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedName = parent.getItemAtPosition(position).toString();
//                        Toast.makeText(context, "Kamu memilih " + selectedName, Toast.LENGTH_SHORT).show();
                        System.out.println("Kamu memilih "+selectedName);

                        apiService.paketFindById(selectedName).enqueue(new Callback<ResponsePaketById>() {
                            @Override
                            public void onResponse(Call<ResponsePaketById> call, Response<ResponsePaketById> response) {
                                System.out.println("Response :"+response.toString());
                                if (response.isSuccessful()){
                                    linearLayoutPaket.setVisibility(View.VISIBLE);
                                    GetetFormHarga.setVisibility(View.VISIBLE);
                                    ArrayList<Paket> pakets = new ArrayList<>();
                                    pakets.add(response.body().getMaster());
                                    Paket dataPaket = pakets.get(0);
                                  GetetFormHarga.setText(formatRupiah.format((double)dataPaket.getHargaPaket()));
                                  String data = dataPaket.getMakananUtama().toString();
                                  String data_pembuka = dataPaket.getMakananPembuka().toString();
                                  String data_penutup = dataPaket.getMakananPenutup().toString();
                                  String data_pondok = dataPaket.getMakananPondok().toString();

                                    String[] dataString = data.split(", ");
                                    String[] dataPembuka = data_pembuka.split(", ");
                                    String[] dataPenutup = data_penutup.split(", ");
                                    String[] dataPondok = data_pondok.split(", ");

                                    for (int i = 0; i < dataPondok.length; i++){
                                        GetTvPondok.setText(calculatePondok(dataPondok));
                                    }

                                    for (int i = 0; i < dataPenutup.length; i++){
                                        GetTvPenutup.setText(calculatePenutup(dataPenutup));
                                    }

                                    for (int i = 0; i < dataPembuka.length; i++){
                                        GetTvPembuka.setText(calculatePembuka(dataPembuka));
                                    }

                                    for (int i = 0; i < dataString.length; i++){
                                        GetTvPaket.setText(calculate(dataString));

                                    }

                                  harga_satuan = dataPaket.getHargaPaket();
                                  System.out.println("Harga2 :"+harga_satuan);

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePaketById> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<ResponsePaket> call, Throwable t) {

            }
        });
    }

    private String calculatePondok(String[] dataPondok) {
        StringBuilder output = new StringBuilder();
        int no = 1;
        for (String s : dataPondok) {
            output.append("\n ").append(no++).append(". ").append(s);
//            no++;
        }
        return output.toString();
    }

    private String calculatePenutup(String[] dataPenutup) {
        StringBuilder output = new StringBuilder();
        int no = 1;
        for (String s : dataPenutup) {
            output.append("\n ").append(no++).append(". ").append(s);
//            no++;
        }
        return output.toString();
    }

    private String calculatePembuka(String[] dataPembuka) {
        StringBuilder output = new StringBuilder();
        int no = 1;
        for (String s : dataPembuka) {
            output.append("\n ").append(no++).append(". ").append(s);
//            no++;
        }
        return output.toString();
    }

    public String calculate(String[] a) {

        StringBuilder output = new StringBuilder();
        int no = 1;
        for (String s : a) {
            output.append("\n ").append(no++).append(". ").append(s);
//            no++;
        }
        return output.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(getActivity(), data);
                GetEtFormAlamat.setText(place.getAddress());
            }
        }
    }

    private String addNumbers() {
        double harga;
        int porsi;

        if (GetetFormHarga.getText().toString() != "" && GetetFormHarga.getText().length() > 0){
            harga = harga_satuan;
        }else {
            harga = 0;
        }

        if (GetetFormJumlahPaket.getText().toString() != "" && GetetFormJumlahPaket.getText().length() > 0){
            porsi = Integer.parseInt(GetetFormJumlahPaket.getText().toString());
        }else {
            porsi = 0;
        }

        int harga_total = (int) (harga * porsi);

        harga_total_db = (int) (harga * porsi);

        return formatRupiah.format(harga_total);
    }

    private void saveDataPemesanan() {
        nama_paket = selectedName;
        jumlah_pesanan = Integer.parseInt(GetetFormJumlahPaket.getText().toString());
        alamat_lengkap = GetEtFormAlamat.getText().toString();
        latitude = GetLatitude;
        longitude = GetLongitude;
        pesanan_tambahan = GetEtFormPesanTambahan.getText().toString();

        System.out.println("nama : "+nama_paket);
        System.out.println("jumlah: "+jumlah_pesanan);
        System.out.println("total: "+harga_total_db);
        System.out.println("alamat: "+alamat_lengkap);
        System.out.println("latitude: "+latitude);
        System.out.println("longitude: "+longitude);
        System.out.println("pesan: "+pesanan_tambahan);

        apiService = ApiClient.getClient(ServerConfig.API_ENDPOINT).create(ApiInterface.class);
        apiService.simpanPemesanan(member_id, nama_paket, jumlah_pesanan, harga_total_db, alamat_lengkap, latitude, longitude, pesanan_tambahan).enqueue(new Callback<ResponseCreatePemesanan>() {
            @Override
            public void onResponse(Call<ResponseCreatePemesanan> call, Response<ResponseCreatePemesanan> response) {
                if (response.isSuccessful()){
                    if (response.body().getCode().equals(200)){
                        // intent ke tab verifikasi pembayaran pakai ini azhar
//                        ((MainTabActivity)getActivity()).navigateFragment(4);
                        Intent pembayaran;
//                      pembayaran.putExtra("id")
                        pembayaran =new Intent(getActivity(), PembayaranActivity.class);
                        startActivity(pembayaran);
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCreatePemesanan> call, Throwable t) {

            }
        });
    }

    protected void increaseInteger(){
        minteger = minteger + 1;
        display(minteger);
    }

    protected void decreaseInteger(){
        minteger = minteger - 1;
        display(minteger);
    }

    private void display(int number) {
        GetetFormJumlahPaket.setText(""+number);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_pesanan, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onLocationChanged(Location location) {
//        GetEtFormAlamat.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        System.out.println("Lat :"+location.getLatitude());
        System.out.println("Lng :"+location.getLongitude());

        GetLatitude = location.getLatitude();
        GetLongitude = location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            GetEtFormAlamat.setText(GetEtFormAlamat.getText() + "\n"+addresses.get(0).getAddressLine(0));
            System.out.println("Address :"+addresses);
        }catch(Exception e)
        {

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

}
