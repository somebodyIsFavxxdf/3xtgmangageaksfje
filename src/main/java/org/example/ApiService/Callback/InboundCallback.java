package org.example.ApiService.Callback;

import org.example.POJO.Req.Inbound;

public interface InboundCallback {
    public void onSuccessGetInbound(Inbound inbound , long chatId);
    public void onSuccessAddInbound(Inbound inbound , long chatId , String domain);
}
