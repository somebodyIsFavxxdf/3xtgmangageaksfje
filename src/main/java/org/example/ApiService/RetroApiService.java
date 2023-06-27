package org.example.ApiService;

import org.example.POJO.Req.ClientBody;
import org.example.POJO.Req.obj;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface RetroApiService {
    @FormUrlEncoded
    @POST("/login")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("/panel/api/inbounds/addClient")
    Call<ResponseBody> addClient(@Body ClientBody clientBody , @Header("Cookie") String cookie);

    @GET("/panel/api/inbounds/get/{id}")
    Call<ResponseBody> getInbound(@Path("id") int id , @Header("Cookie") String cookie);
    @POST("/panel/api/inbounds/add")
    Call<ResponseBody> addInbound(@Body obj obj , @Header("Cookie") String cookie);
}
