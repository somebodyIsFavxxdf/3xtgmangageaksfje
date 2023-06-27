package org.example.ApiService;

import org.example.ApiService.Callback.InboundCallback;
import org.example.POJO.Panel.PInfo;
import org.example.POJO.Req.CloneInfo;
import org.example.POJO.Req.Inbound;
import org.example.POJO.Req.obj;
import org.example.Util.CredentialManager;
import org.example.Util.Generate;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URL;

public class AddInboundApiService {
    private CloneInfo cloneInfo;


    public AddInboundApiService(CloneInfo cloneInfo) {
        this.cloneInfo = cloneInfo;
    }

    public void getInbound(InboundCallback callback) {
        PInfo pInfo = CredentialManager.getPanel(cloneInfo.getPanel());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(pInfo.getDomain() + ":" + pInfo.getPort())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroApiService retroApiService = retrofit.create(RetroApiService.class);
        retroApiService.getInbound(cloneInfo.getInboundId() , pInfo.getCookie()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Inbound inbound = new Inbound();
                if (response.body()!=null){
                    try {
                        System.out.println("response.body is not null");
                        String str = response.body().string();
                        System.out.println(str);
                        inbound = CredentialManager.gson.fromJson(str , Inbound.class);
                        callback.onSuccessGetInbound(inbound , cloneInfo.getChatId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void addInbound(InboundCallback callback , obj obj){
        PInfo pInfo = CredentialManager.getPanel(cloneInfo.getPanel());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(pInfo.getDomain() + ":" + pInfo.getPort())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroApiService retroApiService = retrofit.create(RetroApiService.class);
        retroApiService.addInbound(obj , pInfo.getCookie()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Inbound inbound = new Inbound();
                String domain = "";
                try {
                    URL url = new URL(response.raw().request().url().toString());
                    String baseUrl = url.getProtocol() + "://" + url.getHost();
                    domain = baseUrl;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.body()!=null){
                    try {
                        System.out.println("response.body is not null");
                        String str = response.body().string();
                        System.out.println(str);
                        inbound = CredentialManager.gson.fromJson(str , Inbound.class);
                        callback.onSuccessAddInbound(inbound , cloneInfo.getChatId() , domain);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public obj cloner(Inbound inbound){
        obj obj = inbound.getObj();
        obj.setPort(Generate.generateRandomPort());
        obj.setSettings(settingsMaker());
        obj.setEnable(true);
        obj.setRemark(cloneInfo.getClientName());
        return obj;
    }
    public String settingsMaker() {
        String modifiedObj = null;
        try {
            JSONObject inboundSettings = new JSONObject("{\n  \"clients\": [\n    {\n      \"id\": \"8977ed86-ed74-41c1-99b3-bd71034e3339\",\n      \"flow\": \"\",\n      \"email\": \"fifodd0f\",\n      \"limitIp\": 0,\n      \"totalGB\": 0,\n      \"expiryTime\": 0,\n      \"enable\": true,\n      \"tgId\": \"\",\n      \"subId\": \"\"\n    }\n  ],\n  \"decryption\": \"none\",\n  \"fallbacks\": []\n}");

            JSONArray clientsArray = inboundSettings.getJSONArray("clients");
            JSONObject modifiedSettings = new JSONObject();

            for (int i = 0; i < clientsArray.length(); i++) {
                JSONObject jClient = clientsArray.getJSONObject(i);
                jClient.put("email", cloneInfo.getClientName());
                jClient.put("enable", true);
                jClient.put("id", Generate.randomUUID());
                jClient.put("totalGB", cloneInfo.getUsage());
                jClient.put("expiryTime", cloneInfo.getExpiry());
                modifiedSettings.put("clients", new JSONArray().put(jClient));

            }
            modifiedObj = modifiedSettings.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modifiedObj;
    }
}
