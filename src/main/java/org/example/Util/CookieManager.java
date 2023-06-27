package org.example.Util;

import org.example.ApiService.Callback.CookieCallback;
import org.example.ApiService.RetroApiService;
import org.example.POJO.Panel.PInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CookieManager {
    private PInfo pInfo;
    public CookieManager(PInfo pInfo){
        this.pInfo = pInfo;
    }
    public void genCookie(CookieCallback cookieCallback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(pInfo.getDomain() + ":" + pInfo.getPort())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroApiService retroApiService = retrofit.create(RetroApiService.class);
        retroApiService.login(pInfo.getUsername(), pInfo.getPassword()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String setCookieHeader = response.headers().get("Set-Cookie");
                if (setCookieHeader != null) {
                    String sessionCookie = setCookieHeader.split(";")[0];
                    cookieCallback.onSuccess(sessionCookie, pInfo.getDomain(),pInfo.getPort());
                } else {
                    cookieCallback.onFailure(new Throwable("Error Happened: Couldn't find any cookie"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                cookieCallback.onFailure(t);
            }
        });
    }
}
