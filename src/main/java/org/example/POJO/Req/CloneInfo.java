package org.example.POJO.Req;

public class CloneInfo {
    private String panel;
    private int inboundId;
    private long usage;
    private long expiry;
    private String clientName;
    private long chatId;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public int getInboundId() {
        return inboundId;
    }

    public void setInboundId(int inboundId) {
        this.inboundId = inboundId;
    }

    public long getUsage() {
        return usage;
    }

    public void setUsage(long usage) {
        this.usage = usage;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }
}
