package com.example.anonymous.catering.rests;

import com.example.anonymous.catering.response.ResponGuruDetail;
import com.example.anonymous.catering.response.ResponseCreateDriver;
import com.example.anonymous.catering.response.ResponseCreatePemesanan;
import com.example.anonymous.catering.response.ResponseDetailMember;
import com.example.anonymous.catering.response.ResponseGuru;
import com.example.anonymous.catering.response.ResponseLogin;
import com.example.anonymous.catering.response.ResponseLoginDriver;
import com.example.anonymous.catering.response.ResponseLoginMember;
import com.example.anonymous.catering.response.ResponseMatpel;
import com.example.anonymous.catering.response.ResponseCreateMurid;
import com.example.anonymous.catering.response.ResponseMember;
import com.example.anonymous.catering.response.ResponsePaket;
import com.example.anonymous.catering.response.ResponsePaketById;
import com.example.anonymous.catering.response.ResponsePemesanan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("matpel/find-all")
    Call<ResponseMatpel> matpelFindAll();

    @GET("guru/find-all")
    Call<ResponseGuru> guruFindAll();

    @GET("guru/find-by-id")
    Call<ResponGuruDetail> guruFindById(@Query("id") int id);

    @GET("paket/find-all")
    Call<ResponsePaket> paketFindAll();

    @GET("member/find-all")
    Call<ResponseMember> memberFindAll();

    @GET("member/find-by-id")
    Call<ResponseDetailMember> memberFindById(@Query("id") int id);


    @GET("paket/find-by-id")
    Call<ResponsePaketById> paketFindById(@Query("nama_paket") String nama_paket);

    @FormUrlEncoded
    @POST("pemesan/create")
    Call<ResponseCreatePemesanan> simpanPemesanan(@Field("member_id") int member_id,
                                                  @Field("nama_paket") String nama_paket,
                                                  @Field("jumlah_pesanan") int jumlah_pesanan,
                                                  @Field("total") int total,
                                                  @Field("alamat_lengkap") String alamat_lengkap,
                                                  @Field("latitude") Double latitude,
                                                  @Field("longitude") Double longitude,
                                                  @Field("pesan_tambahan") String pesan_tambahan);

    @FormUrlEncoded
    @POST("driver/register")
    Call<ResponseCreateDriver> simpanDriver(@Field("username") String username,
                                               @Field("email") String email,
                                               @Field("password")String password,
                                                @Field("nama") String nama);


//    @FormUrlEncoded
//    @POST("pemesan/create")
//    Call<ResponseCreateMember> simpanMember(@Field("member_id") int member_id,
//                                                  @Field("nama_paket") String nama_paket,
//                                                  @Field("jumlah_pesanan") int jumlah_pesanan,
//                                                  @Field("total") int total,
//                                                  @Field("alamat_lengkap") String alamat_lengkap,
//                                                  @Field("latitude") Double latitude,
//                                                  @Field("longitude") Double longitude,
//                                                  @Field("pesan_tambahan") String pesan_tambahan);

    @FormUrlEncoded
    @POST("murid/update")
    Call<ResponseCreateMurid> updateMurid(@Field("id") Integer id,
                                          @Field("nama") String nama,
                                          @Field("no_hp") String no_hp,
                                          @Field("email") String email,
                                          @Field("alamat") String alamat,
                                          @Field("jk") String jk,
                                          @Field("nisn") String nisn,
                                          @Field("kelas") String kelas,
                                          @Field("nama_sekolah") String nama_sekolah,
                                          @Field("photo") String photo);

    //login member
    @FormUrlEncoded
    @POST("login/member")
    Call<ResponseLoginMember> memberLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login/driver")
    Call<ResponseLoginDriver> driverLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("pemesanan/lihat-pemesanan")
    Call<ResponsePemesanan> pemesananStatus(@Query("status") String status);
}

