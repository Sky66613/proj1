package client;

import java.io.BufferedReader;

public class ClientReceiver implements Runnable {
    public BufferedReader in;

    public ClientReceiver (BufferedReader in) {
        this.in = in;
    }


    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                ChatGUI.appendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

