package org.example.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.POJO.BotInfo;
import org.example.POJO.Panel.PInfo;
import org.example.POJO.Panel.Panels;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CredentialManager {
    public static Gson gson = new GsonBuilder().create();
    public  static String appDir = System.getProperty("user.dir");

    BotInfo botInfo;

    public CredentialManager() {
        String folderPath = appDir;
        File config = new File(folderPath + "/tg.json");
        if (config.isFile() && config.getName().endsWith(".json")) {
            String configContent = null;
            try {
                configContent = new String((Files.readAllBytes(config.toPath())));
                botInfo = gson.fromJson(configContent, BotInfo.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public long addAdministrator(long numId) {
        List<Long> numIdList = botInfo.getUserNumericIdList();
        numIdList.add(numId);
        botInfo.setUserNumericIdList(numIdList);
        boolean isSuccess = writeTgInfo();
        if (isSuccess) return numId;
        else return 0;
    }

    public String getToken() {
        if (botInfo != null)
            return botInfo.getToken();
        else return "";
    }

    public String getBotUsername() {
        if (botInfo != null)
            return botInfo.getBotUsername();
        else return "";
    }

    public List<Long> getUserNumIdList() {
        if (botInfo != null)
            return botInfo.getUserNumericIdList();
        else return new ArrayList<>();
    }

    public boolean writeTgInfo() {
        Gson xGson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        String folderPath = appDir;
        File config = new File(folderPath + "/tg.json");
        String configContent = xGson.toJson(botInfo);
        try {
            FileWriter writer = new FileWriter(config, false);
            writer.write(configContent);
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public static void writePanelInfo(PInfo pInfo) {
        String folderPath = appDir;
        File config = new File(folderPath + "/panels.json");
        Panels xPanel = new Panels();
        if (config.isFile() && config.getName().endsWith(".json")) {
            String configContent = null;
            try {
                configContent = new String((Files.readAllBytes(config.toPath())));
                xPanel = gson.fromJson(configContent, Panels.class);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<PInfo> pInfos;
        if (xPanel.getPanels() != null) {
            pInfos = xPanel.getPanels();
        } else pInfos = new ArrayList<>();
        pInfos.add(pInfo);
        xPanel.setPanels(pInfos);


        Gson xGson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        File xConfig = new File(folderPath + "/panels.json");
        String configContent = xGson.toJson(xPanel);
        try {
            FileWriter writer = new FileWriter(xConfig, false);
            writer.write(configContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void updatePanelCookie(String cookie, String domain, int port) {
        String folderPath =appDir;
        File config = new File(folderPath + "/panels.json");
        Panels xPanel = new Panels();
        if (config.isFile() && config.getName().endsWith(".json")) {
            String configContent = null;
            try {
                configContent = new String((Files.readAllBytes(config.toPath())));
                xPanel = gson.fromJson(configContent, Panels.class);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<PInfo> pInfos;
        if (xPanel != null) {
            pInfos = xPanel.getPanels();
        } else pInfos = new ArrayList<>();
        for (PInfo pInfo : pInfos) {
            if (domain.equals(pInfo.getDomain()) && port == pInfo.getPort()) {
                pInfo.setCookie(cookie);
            }
        }
        xPanel.setPanels(pInfos);

        Gson xGson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        File xConfig = new File(folderPath + "/panels.json");
        String configContent = xGson.toJson(xPanel);
        try {
            FileWriter writer = new FileWriter(xConfig, false);
            writer.write(configContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static Panels getPanels() {
        String folderPath = appDir;
        File config = new File(folderPath + "/panels.json");
        Panels xPanel = new Panels();
        if (config.isFile() && config.getName().endsWith(".json")) {
            String configContent = null;
            try {
                configContent = new String((Files.readAllBytes(config.toPath())));
                return gson.fromJson(configContent, Panels.class);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else return new Panels();
    }

    public static List<KeyboardRow> getPanelRows() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        List<PInfo> pInfos = CredentialManager.getPanels().getPanels();
        int index = 1;
        KeyboardRow xKeyboardRow = new KeyboardRow();
        if (pInfos!=null){
            Iterator<PInfo> iterator = pInfos.iterator();

            while (iterator.hasNext()) {
                PInfo pInfo = iterator.next();

                xKeyboardRow.add(pInfo.getName());
                index++;

                if (index > 2 || !iterator.hasNext()) {
                    keyboardRows.add(xKeyboardRow);
                    xKeyboardRow = new KeyboardRow();
                    index = 1;
                }

                iterator.remove();
            }
        }


        return keyboardRows;
    }

    public static PInfo getPanel(String name) {
        Panels xPanels = new Panels();
        String folderPath = appDir;
        File config = new File(folderPath + "/panels.json");
        if (config.isFile() && config.getName().endsWith(".json")) {
            String configContent = null;
            try {
                configContent = new String((Files.readAllBytes(config.toPath())));
                xPanels = gson.fromJson(configContent, Panels.class);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        for (PInfo pInfo : xPanels.getPanels()) {
            if (name.equals(pInfo.getName())) return pInfo;
        }
        return new PInfo();

    }

    public static List<Long> getNumIDs(){
        String folderPath = appDir;
        File config = new File(folderPath + "/tg.json");
        BotInfo botInfo = new BotInfo();
        if (config.isFile() && config.getName().endsWith(".json")) {
            String configContent = null;
            try {
                configContent = new String((Files.readAllBytes(config.toPath())));
                botInfo = gson.fromJson(configContent, BotInfo.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return botInfo.getUserNumericIdList();
    }

    public static Long getFirstNumID(){
        String folderPath = appDir;
        File config = new File(folderPath + "/tg.json");
        BotInfo botInfo = new BotInfo();
        if (config.isFile() && config.getName().endsWith(".json")) {
            String configContent = null;
            try {
                configContent = new String((Files.readAllBytes(config.toPath())));
                botInfo = gson.fromJson(configContent, BotInfo.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return botInfo.getUserNumericIdList().get(0);
    }


}

