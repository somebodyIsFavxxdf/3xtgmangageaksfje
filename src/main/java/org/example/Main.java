package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Util.CredentialManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    public static TgBot tgBot = new TgBot();

    public static void main(String[] args) {
        startBot();
        sendAlert("app started");
        for (Long id : CredentialManager.getNumIDs()){
            tgBot.sendMainMenu(id);
        }
    }
    public static void startBot(){
        tgBot.start();
    }
    public static void sendAlert(String alert){
        tgBot.sendAlert(alert);
    }



}