package io.wifi.joinmsg;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import io.wifi.joinmsg.commands.*;

public final class joinmsg implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors
    public static MyConfig MyConfig = null;
    public static String url = "";
    public static String joinMsg_pre = "";
    public static String joinMsg_aft = "";
    public static String leaveMsg_pre = "";
    public static String leaveMsg_aft = "";
    public static String serverStart = "";

    public static void reloadConfig() {
        if (MyConfig == null) {
            MyConfig = new MyConfig("./config/joinmsg.txt");
        } else {
            MyConfig.reloadConfig("./config/joinmsg.txt");
        }
        url = MyConfig.getValue("url");
        joinMsg_pre = MyConfig.getValue("join_prefix");
        joinMsg_aft = MyConfig.getValue("join_after");
        leaveMsg_pre = MyConfig.getValue("leave_prefix");
        leaveMsg_aft = MyConfig.getValue("leave_after");
        serverStart = MyConfig.getValue("server_start");
    }

    public static final Logger LOGGER = LoggerFactory.getLogger("wifijoinmsg");

    public static boolean sendPostMessage(String msg) {
        boolean flag = true;
        String uri = url;
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setRequestMethod("POST");
            urlcon.setDoOutput(true);
            urlcon.setDoInput(true);
            // urlcon.connect();// 获取连接
            out = new PrintWriter(urlcon.getOutputStream());
            out.print(msg);
            out.flush();
            // urlcon.connect();
            if (200 == urlcon.getResponseCode()) {
                in = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                flag = false;
                System.out.println("[REQUEST] ResponseCode is an error code: " + urlcon.getResponseCode());
            }
        } catch (Exception e) {
            flag = false;
            System.out.println("[REQUEST][URL: " + uri + "][ERROR MESSAGE: " + e.getMessage() + "]");
            LOGGER.error("--------Network Exception-------->:", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e2) {
                flag = false;
                System.out.println("[IOClose IOException][Message: " + e2.getMessage() + "]");
            }
        }
        // System.out.println(result.toString());
        return flag;
    }

    public void regCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            commandMiraimc.register(dispatcher); // 注册
        });
    }

    @Override
    public void onInitialize() {
        reloadConfig();
        regCommands();
        LOGGER.info(serverStart);
        sendPostMessage(serverStart);
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Loaded Minecraft Join Message.");
    }
}
