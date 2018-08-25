package com.simple_p2p.p2p_engine.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.*;
import java.util.TreeMap;

public class NetworkEnvironment {

    private NetworkEnvironment(){};

    public static InetAddress getExternalAddress() {
        InetAddress amnIp = amazonaws();
        return amnIp;
    }

    public static InetAddress getLocalAddress() {
        InetAddress result = null;
        try {
            result = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] getLocalMacAddress() {
        byte[] macAddress = new byte[0];
        try {
            macAddress = NetworkInterface.getByInetAddress(getLocalAddress()).getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    public static String getLocalMacAddressReadable() {
        byte[] macAddress = getLocalMacAddress();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < macAddress.length; i++) {
            result.append(String.format("%02X%s", macAddress[i], (i < macAddress.length - 1) ? "-" : ""));
        }
        return result.toString();
    }

    private static InetAddress amazonaws() {
        InetAddress externalAdress = null;
        try {
            String text = getStringFromAPI("http://checkip.amazonaws.com");
            externalAdress = InetAddress.getByName(text);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return externalAdress;
    }

    private static InetAddress jsonip() {
        InetAddress externalAdress = null;
        TreeMap<String, String> json;
        try {
            String text = getStringFromAPI("https://jsonip.com/");
            Type type = new TypeToken<TreeMap<String, String>>() {
            }.getType();
            json = new Gson().fromJson(text, type);
            String ip = json.get("ip");
            externalAdress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return externalAdress;
    }

    private static String getStringFromAPI(String adress) {
        String text = null;
        try {
            URL whatismyip = new URL(adress);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            text = in.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
