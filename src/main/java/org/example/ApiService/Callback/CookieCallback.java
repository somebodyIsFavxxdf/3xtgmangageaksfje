package org.example.ApiService.Callback;

public interface CookieCallback {
    public void onSuccess(String cookie , String url , int port);
    public void onCont();

    public void onFailure(Throwable t);
}
