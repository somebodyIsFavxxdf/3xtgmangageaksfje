package org.example.POJO.Generate;

import org.example.POJO.Generate.Settings.Settings;
import org.example.POJO.Generate.StreamSettings.StreamSettings;

public class InboundInfo {

    private int port;
    private String protocol;
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
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

    private StreamSettings streamSettings;

    public StreamSettings getStreamSettings() {
        return streamSettings;
    }

    public void setStreamSettings(StreamSettings streamSettings) {
        this.streamSettings = streamSettings;
    }
}



