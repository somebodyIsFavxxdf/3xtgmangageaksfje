package org.example;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.example.ApiService.AddInboundApiService;
import org.example.ApiService.Callback.CookieCallback;
import org.example.ApiService.Callback.InboundCallback;
import org.example.POJO.Generate.InboundInfo;
import org.example.POJO.Generate.Settings.Settings;
import org.example.POJO.Generate.StreamSettings.StreamSettings;
import org.example.POJO.Panel.PInfo;
import org.example.POJO.Req.CloneInfo;
import org.example.POJO.Req.Inbound;
import org.example.POJO.Req.obj;
import org.example.Util.CookieManager;
import org.example.Util.CredentialManager;
import org.example.Util.Generate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TgBot extends TelegramLongPollingBot implements CookieCallback , InboundCallback {
    CredentialManager credentialManager;
    private static int CURRENT_STATE = 0;
    private static String CURRENT_PANEL = null;
    private static int CURRENT_PANEL_STATE = 0;
    private static int ADD_ADMIN_STATE = 0;
    private PInfo pInfo = new PInfo();
    private Inbound inbound;
    private CloneInfo cloneInfo = new CloneInfo();
    private AddInboundApiService addInboundApiService;


    public TgBot() {
        CredentialManager cManager = new CredentialManager();
        credentialManager = cManager;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            User user = update.getMessage().getFrom();
            long chatId = user.getId();
            List<Long> numIDs = credentialManager.getUserNumIdList();
            boolean found = false;
            for (Long id : numIDs) {
                if (id == chatId) {
                    found = true;
                    if (messageText.equals("/start")) {
                        CURRENT_STATE = 0;
                        CURRENT_PANEL = null;
                        CURRENT_PANEL_STATE = 0;
                        sendMainMenu(chatId);
                    }
                    if (messageText.equals("Cancel")) sendMainMenu(chatId);

                    if (ADD_ADMIN_STATE == 1){
                        long newAdminId = Long.parseLong(messageText);
                        long isAdminAdded = 0;
                        isAdminAdded = credentialManager.addAdministrator(newAdminId);
                        if (isAdminAdded>0){
                            sendMessage(chatId , "User with ID: " + isAdminAdded + " Successfully added to Administrators");
                            sendMainMenu(chatId);
                        }
                    }


                    if (CredentialManager.getPanels().getPanels() != null) {

                        for (PInfo zPInfo : CredentialManager.getPanels().getPanels()) {
                            if (CURRENT_PANEL == null) {
                                if (messageText.equals(zPInfo.getName())) {
                                    CURRENT_PANEL = zPInfo.getName();
                                }
                            }
                        }
                    }
                    if (CURRENT_PANEL != null && CURRENT_PANEL_STATE == 0) {
                        CURRENT_PANEL_STATE = 1;
                        cloneInfo.setPanel(CURRENT_PANEL);
                        sendInboundMenu(chatId);

                    } else if (CURRENT_PANEL != null && CURRENT_PANEL_STATE == 1) {
                        CURRENT_PANEL_STATE = 2;
                        cloneInfo.setInboundId(Integer.parseInt(messageText));
                        sendGbMenu(chatId);
                    } else if (CURRENT_PANEL != null && CURRENT_PANEL_STATE == 2) {
                        cloneInfo.setUsage((long) (Integer.parseInt(messageText) * Math.pow(1024, 3)));
                        CURRENT_PANEL_STATE = 3;
                        sendExpDayMenu(chatId);
                    } else if (CURRENT_PANEL != null && CURRENT_PANEL_STATE == 3) {
                        CURRENT_PANEL_STATE = 4;
                        cloneInfo.setExpiry(Generate.dayConvertor(Integer.parseInt(messageText)));
                        sendCancelBtn(chatId);

                    } else if (CURRENT_PANEL != null && CURRENT_PANEL_STATE == 4) {
                        cloneInfo.setClientName(messageText);
                        cloneInfo.setChatId(chatId);
                        addInboundApiService = new AddInboundApiService(cloneInfo);
                        addInboundApiService.getInbound(this);

                    } else if (CURRENT_PANEL != null && CURRENT_PANEL_STATE == 5) {
                        if (addInboundApiService != null && inbound != null) {
                            obj obj = addInboundApiService.cloner(inbound);
                            addInboundApiService.addInbound(this, obj);

                        } else {
                            sendMessage(chatId, "Error: Unable to receive inbound from the server successfully.");
                        }
                    }
                    if (messageText.equals("Add Admin")){
                        ADD_ADMIN_STATE = 1;
                        sendAddAdminMenu(chatId);
                    }


                    if (CURRENT_STATE == 1) {
                        pInfo = new PInfo();
                        if (messageText.startsWith("http")) {
                            pInfo.setDomain(messageText);
                            CURRENT_STATE = 2;
                            sendMessage(chatId, "please send port number");
                        } else sendMessage(chatId, "domain is not valid");
                    } else if (CURRENT_STATE == 2) {
                        pInfo.setPort(Integer.parseInt(messageText));
                        CURRENT_STATE = 3;
                        sendMessage(chatId, "please send username of panel");
                    } else if (CURRENT_STATE == 3) {
                        pInfo.setUsername(messageText);
                        CURRENT_STATE = 4;
                        sendMessage(chatId, "please send password of panel");
                    } else if (CURRENT_STATE == 4) {
                        pInfo.setPassword(messageText);
                        CURRENT_STATE = 5;
                        sendMessage(chatId, "send a name for panel");
                    } else if (CURRENT_STATE == 5) {
                        pInfo.setName(messageText);
                        CredentialManager.writePanelInfo(pInfo);
                        CURRENT_STATE = 0;
                        CookieManager cookieManager = new CookieManager(pInfo);
                        cookieManager.genCookie(this);
                        sendMessage(chatId, "Panel: " + pInfo.getName() + " Successfully added");
                        sendMainMenu(chatId);
                    }
                    if (messageText.equals("Add Panel")) {
                        CURRENT_STATE = 1;
                        pInfo = new PInfo();
                        sendMessage(chatId, "Please Send Domain or IP of 3xui panel (with http:// or https://)");
                    }


                }
            }
            if (!found) {
                sendMessage(chatId, "you are not an admin");
            }
        }
    }


    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode(ParseMode.HTML);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMainMenu(long chatId) {

        CURRENT_STATE = 0;
        CURRENT_PANEL = null;
        CURRENT_PANEL_STATE = 0;
        ADD_ADMIN_STATE = 0;
        pInfo = new PInfo();
        inbound = null;
        cloneInfo = new CloneInfo();
        addInboundApiService = null;
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Main Menu: Choose an option");
        KeyboardRow addPanelRow = new KeyboardRow();
        addPanelRow.add("Add Panel");

        KeyboardRow adminRow = new KeyboardRow();
        adminRow.add("Add Admin");

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(addPanelRow);
        keyboardRows.addAll(CredentialManager.getPanelRows());
        keyboardRows.add(adminRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendInboundMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Choose the inbound to clone");
        KeyboardRow inboundIdRow = new KeyboardRow();
        inboundIdRow.add("1");
        inboundIdRow.add("2");
        inboundIdRow.add("3");
        KeyboardRow cancelRow = new KeyboardRow();
        cancelRow.add("Cancel");


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(inboundIdRow);
        keyboardRows.add(cancelRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendContinueBtn(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Cloning...\nAre you sure you want to create a new inbound?\nPress 'Continue' to proceed.");
        KeyboardRow inboundIdRow = new KeyboardRow();
        inboundIdRow.add("Continue");
        inboundIdRow.add("Cancel");

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(inboundIdRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendCancelBtn(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("now send the Name of Client");
        KeyboardRow cancelRow = new KeyboardRow();
        cancelRow.add("Cancel");

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(cancelRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendAddAdminMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("send the numeric id of user");
        KeyboardRow cancelRow = new KeyboardRow();
        cancelRow.add("Cancel");

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(cancelRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendGbMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Send the Total Flow in GB");
        KeyboardRow GbRow = new KeyboardRow();
        GbRow.add("10");
        GbRow.add("15");
        GbRow.add("20");
        GbRow.add("30");
        KeyboardRow cancelRow = new KeyboardRow();
        cancelRow.add("Cancel");

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(GbRow);
        keyboardRows.add(cancelRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendExpDayMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Send the Expiry days");
        KeyboardRow expDayRow = new KeyboardRow();
        expDayRow.add("14");
        expDayRow.add("21");
        expDayRow.add("30");
        expDayRow.add("60");
        KeyboardRow cancelRow = new KeyboardRow();
        cancelRow.add("Cancel");

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(expDayRow);
        keyboardRows.add(cancelRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        TelegramBotsApi telegramBotsApi;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            BotSession botSession = telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendAlert(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(CredentialManager.getFirstNumID());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return credentialManager.getToken();
    }

    @Override
    public String getBotUsername() {
        return credentialManager.getBotUsername();
    }


    @Override
    public void onSuccess(String cookie, String url, int port) {
        CredentialManager.updatePanelCookie(cookie, url, port);
    }

    @Override
    public void onCont() {

    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void onSuccessGetInbound(Inbound inbound, long chatId) {
        this.inbound = inbound;
        CURRENT_PANEL_STATE = 5;
        sendContinueBtn(chatId);

    }

    @Override
    public void onSuccessAddInbound(Inbound inbound, long chatId , String domain) {
        sendMessage(chatId, "Inbound Added Successfully\nکانفیگ را در پیامک یا برنامه های ایرانی ارسال نکنید\nاحتمال فیلتر شدن ان وجود دارد");
        InboundInfo inboundInfo = new InboundInfo();
        inboundInfo.setPort(inbound.getObj().getPort());
        inboundInfo.setProtocol(inbound.getObj().getProtocol());
        StreamSettings streamSettings = CredentialManager.gson.fromJson(inbound.getObj().getStreamSettings(), StreamSettings.class);
        Settings settings = CredentialManager.gson.fromJson(inbound.getObj().getSettings(), Settings.class);
        inboundInfo.setSettings(settings);
        inboundInfo.setStreamSettings(streamSettings);
        String url = Generate.generateUrl(inboundInfo, domain);
        int width = 400;
        int height = 400;

        String format = "png";
        File outputFile = new File(CredentialManager.appDir + "/" +inboundInfo.getSettings().getClients().get(0).getEmail()+".png");

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height, null);
            MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
            MatrixToImageWriter.writeToPath(bitMatrix, format, outputFile.toPath(), config);

            System.out.println("QR code generated successfully!");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }


        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(outputFile));
        sendPhoto.setCaption("<pre>"+url+"</pre>");
        sendPhoto.setParseMode(ParseMode.HTML);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        sendMainMenu(chatId);
    }
}