package org.example.POJO.Generate.StreamSettings;

public class GrpcSettings {
    private String serviceName;
    private boolean multiMode;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isMultiMode() {
        return multiMode;
    }

    public void setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
    }
}
