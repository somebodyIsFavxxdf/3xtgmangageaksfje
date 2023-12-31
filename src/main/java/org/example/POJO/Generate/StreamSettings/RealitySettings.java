package org.example.POJO.Generate.StreamSettings;

import java.util.List;

public class RealitySettings {
    private boolean show;
    private int xver;
    private String dest;
    private List<String> serverNames;
    private String privateKey;
    private String minClient;
    private String maxClient;
    private int maxTimediff;
    private List<String> shortIds;
    private Settings settings;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getXver() {
        return xver;
    }

    public void setXver(int xver) {
        this.xver = xver;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public List<String> getServerNames() {
        return serverNames;
    }

    public void setServerNames(List<String> serverNames) {
        this.serverNames = serverNames;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getMinClient() {
        return minClient;
    }

    public void setMinClient(String minClient) {
        this.minClient = minClient;
    }

    public String getMaxClient() {
        return maxClient;
    }

    public void setMaxClient(String maxClient) {
        this.maxClient = maxClient;
    }

    public int getMaxTimediff() {
        return maxTimediff;
    }

    public void setMaxTimediff(int maxTimediff) {
        this.maxTimediff = maxTimediff;
    }

    public List<String> getShortIds() {
        return shortIds;
    }

    public void setShortIds(List<String> shortIds) {
        this.shortIds = shortIds;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
