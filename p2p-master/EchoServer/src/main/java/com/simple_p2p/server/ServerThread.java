package com.simple_p2p.server;

import com.simple_p2p.controller.IPController;
import com.simple_p2p.entity.UserConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerThread extends Thread {
    private PrintStream printStream;
    private BufferedReader bufferedReader;
    private InetAddress address;

    IPController mainController = new IPController();
    UserConnection userConnection = new UserConnection();

    public ServerThread(Socket socket) throws IOException {
        printStream = new PrintStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        address = socket.getInetAddress();
    }

    public void run() {
        String string;
        try {
            mainController.setOnlineUser(address.getHostAddress(), userConnection);
            while ((string = bufferedReader.readLine()).equals("CONNECT")) {
                if ("CONNECT".equals(string)) {
                    printStream.println("CONNECT");
                }
                System.out.println("Connect" + " with " + address.getHostAddress());
            }

        } catch (IOException e) {
            System.err.println("Disconnect");
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        try {
            if (printStream != null) {
                printStream.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            System.out.println(address.getHostAddress() + " disconnect");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mainController.setOfflineUser(userConnection);
            this.interrupt();
        }
    }
}
