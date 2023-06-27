package org.example.POJO;

import java.util.List;

public class BotInfo {
    private String token;
    private List<Long> userNumericIdList;
    private String botUsername;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Long> getUserNumericIdList() {
        return userNumericIdList;
    }

    public void setUserNumericIdList(List<Long> userNumericIdList) {
        this.userNumericIdList = userNumericIdList;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }
}
