package org.example.POJO.Generate.StreamSettings;

public class TcpSettings {
    private boolean acceptProxyProtocol;
    private Header header;

    public boolean isAcceptProxyProtocol() {
        return acceptProxyProtocol;
    }

    public void setAcceptProxyProtocol(boolean acceptProxyProtocol) {
        this.acceptProxyProtocol = acceptProxyProtocol;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
