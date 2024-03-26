package com.example.appbanhang.retrofit;

import com.example.appbanhang.model.DonHangModel;
import com.example.appbanhang.model.KhuyenMaiModel;
import com.example.appbanhang.model.LoaiSpModel;
import com.example.appbanhang.model.MessageModel;
import com.example.appbanhang.model.SanPhamMoiModel;
import com.example.appbanhang.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("khuyenmai.php")
    Observable<KhuyenMaiModel> getkhuyenmai();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("email") String email,
            @Field("matkhau") String matkhau,
            @Field("tenuser") String tenuser,
            @Field("sdt") String sdt,
            @Field("uid") String uid
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("matkhau") String matkhau
    );

    @POST("laylaimatkhau.php")
    @FormUrlEncoded
    Observable<UserModel> laylaimatkhau(
            @Field("email") String email
    );
    @POST("dathangthanhcong.php")
    @FormUrlEncoded
    Observable<UserModel> dathangthanhcong(
            @Field("email") String email
    );


    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel> donhang(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int iduser,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );

    @POST("viewdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> viewdonhang(
            @Field("iduser") int id
    );

    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> timkiem(
            @Field("timkiem") String timkiem
    );

    @POST("updatezalopay.php")
    @FormUrlEncoded
    Observable<MessageModel> updatezalopay(
            @Field("id") int id,
            @Field("token") String token
    );

    @POST("updateToken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token
    );

    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> getToken(
            @Field("status") int status
    );

    @POST("xoadonhang.php")
    @FormUrlEncoded
    Observable<MessageModel> deleteOrder(
            @Field("iddonhang") int id
    );
}
