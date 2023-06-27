package org.example.POJO.Req;

public class obj {
    private boolean enable;
    private String remark;
    private String listen;
    private int port;
    private String protocol;
    private long expiryTime;
    private String settings;
    private String streamSettings;
    private String sniffing;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getListen() {
        return listen;
    }

    public void setListen(String listen) {
        this.listen = listen;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getStreamSettings() {
        return streamSettings;
    }

    public void setStreamSettings(String streamSettings) {
        this.streamSettings = streamSettings;
    }

    public String getSniffing() {
        return sniffing;
    }

    public void setSniffing(String sniffing) {
        this.sniffing = sniffing;
    }
}
