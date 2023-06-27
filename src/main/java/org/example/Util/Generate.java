package org.example.Util;

import org.example.POJO.Generate.InboundInfo;
import org.example.POJO.Generate.Settings.Client;
import org.example.POJO.Generate.StreamSettings.RealitySettings;

import java.util.Date;
import java.util.Random;

public class Generate {
    public static String randomUUID() {
        long currentTime = new Date().getTime();
        StringBuilder sb = new StringBuilder(36);

        String uuidPattern = "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx";
        Random random = new Random(currentTime);

        for (int i = 0; i < uuidPattern.length(); i++) {
            char c = uuidPattern.charAt(i);

            if (c == 'x') {
                int r = random.nextInt(16);
                sb.append(Integer.toHexString(r));
            } else if (c == 'y') {
                int r = random.nextInt(4) | 8;
                sb.append(Integer.toHexString(r));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static long dayConvertor(int days) {
        long milliseconds = -1L * days * 24L * 60L * 60L * 1000L;
        return milliseconds;
    }


    public static int generateRandomPort() {
        int minPort = 1024;
        int maxPort = 65000;
        Random random = new Random();
        int port;
        do {
            port = random.nextInt(maxPort - minPort + 1) + minPort;
        } while (isPortAvailable(port));

        return port;
    }

    public static boolean isPortAvailable(int port) {

        if (port == 8080) {
            return true;
        }

        return false;
    }


    public static String generateRandomName() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int NAME_LENGTH = 8;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < NAME_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String generateUrl(InboundInfo inboundInfo, String domain) {
        StringBuilder url = new StringBuilder();
        if (inboundInfo.getProtocol() != null)
            url.append(inboundInfo.getProtocol() + "://");

        Client client = null;
        if (inboundInfo.getSettings() != null && inboundInfo.getSettings().getClients() != null && !inboundInfo.getSettings().getClients().isEmpty()) {
            client = inboundInfo.getSettings().getClients().get(0);
        }
        if (client != null && client.getId() != null)
            url.append(client.getId() + "@" + domain);

        if (inboundInfo.getPort() != 0)
            url.append(":" + inboundInfo.getPort());

        if (inboundInfo.getStreamSettings() != null && inboundInfo.getStreamSettings().getNetwork() != null)
            url.append("?type=" + inboundInfo.getStreamSettings().getNetwork());

        if (inboundInfo.getStreamSettings() != null && inboundInfo.getStreamSettings().getTcpSettings() != null && inboundInfo.getStreamSettings().getTcpSettings().getHeader() != null
                && inboundInfo.getStreamSettings().getTcpSettings().getHeader().getRequest() != null && inboundInfo.getStreamSettings().getTcpSettings().getHeader().getRequest().getHeaders() != null
                && !inboundInfo.getStreamSettings().getTcpSettings().getHeader().getRequest().getHeaders().getHost().isEmpty()
                && inboundInfo.getStreamSettings().getTcpSettings().getHeader().getRequest().getHeaders().getHost().get(0) != null) {
            url.append("&path=%2F&host=" + inboundInfo.getStreamSettings().getTcpSettings().getHeader().getRequest().getHeaders().getHost().get(0));
        }


        if (inboundInfo.getStreamSettings() != null && inboundInfo.getStreamSettings().getTcpSettings() != null && inboundInfo.getStreamSettings().getTcpSettings().getHeader() != null
                && inboundInfo.getStreamSettings().getTcpSettings().getHeader().getType() != null) {
            url.append("&headerType=" + inboundInfo.getStreamSettings().getTcpSettings().getHeader().getType());
        }
        if (inboundInfo.getStreamSettings() !=null && inboundInfo.getStreamSettings().getGrpcSettings()!=null && inboundInfo.getStreamSettings().getGrpcSettings().getServiceName()!=null)
            url.append("&serviceName="+inboundInfo.getStreamSettings().getGrpcSettings().getServiceName());

        if (inboundInfo.getStreamSettings() != null && inboundInfo.getStreamSettings().getSecurity() != null)
            url.append("&security=" + inboundInfo.getStreamSettings().getSecurity());

        RealitySettings realitySettings = null;
        if (inboundInfo.getStreamSettings() != null && inboundInfo.getStreamSettings().getRealitySettings() != null) {
            realitySettings = inboundInfo.getStreamSettings().getRealitySettings();
        } else {
            if (client != null && client.getEmail() != null)
                url.append("#" + client.getEmail());
            return url.toString();
        }

        if (realitySettings.getSettings() != null && realitySettings.getSettings().getFingerprint() != null)
            url.append("&fp=" + realitySettings.getSettings().getFingerprint());

        if (realitySettings.getSettings() != null && realitySettings.getSettings().getPublicKey() != null)
            url.append("&pbk=" + realitySettings.getSettings().getPublicKey());

        if (realitySettings.getServerNames() != null && !realitySettings.getServerNames().isEmpty() && realitySettings.getServerNames().get(0) != null)
            url.append("&sni=" + realitySettings.getServerNames().get(0));

        if (realitySettings.getShortIds() != null && !realitySettings.getShortIds().isEmpty() && realitySettings.getShortIds().get(0) != null)
            url.append("&sid=" + realitySettings.getShortIds().get(0));

        if (realitySettings.getSettings() != null && realitySettings.getSettings().getSpiderX() != null)
            url.append("&spx=%2F");

        if (client != null && client.getEmail() != null)
            url.append("#" + client.getEmail());

        return url.toString();
    }


}
