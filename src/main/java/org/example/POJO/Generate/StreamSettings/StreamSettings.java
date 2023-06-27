package org.example.POJO.Generate.StreamSettings;

public class StreamSettings {
    private String network;
    private String security;
    private RealitySettings realitySettings;
    private TcpSettings tcpSettings;
    private GrpcSettings grpcSettings;

    public GrpcSettings getGrpcSettings() {
        return grpcSettings;
    }

    public void setGrpcSettings(GrpcSettings grpcSettings) {
        this.grpcSettings = grpcSettings;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public RealitySettings getRealitySettings() {
        return realitySettings;
    }

    public void setRealitySettings(RealitySettings realitySettings) {
        this.realitySettings = realitySettings;
    }

    public TcpSettings getTcpSettings() {
        return tcpSettings;
    }

    public void setTcpSettings(TcpSettings tcpSettings) {
        this.tcpSettings = tcpSettings;
    }
}
