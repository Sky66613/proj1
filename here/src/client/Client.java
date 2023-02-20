package client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Client {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public Client() throws Exception {
        socket = new Socket("localhost", 1444);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        String username = JOptionPane.showInputDialog(null, "Enter username:");
        String password = JOptionPane.showInputDialog(null, "Enter password:");
        out.println(username);
        out.println(password);

        String response = in.readLine();
        if (!response.equals("Login successful.")) {
            socket.close();
            return;
        }

        int numUsers = Integer.parseInt(in.readLine());
        List<String> userList = new ArrayList<>();
        for (int i = 0; i < numUsers; i++) {
            String user = in.readLine();
            userList.add(user);
        }

        SwingUtilities.invokeLater(() -> new ChatGUI(out, userList));

        new Thread(new ClientReceiver(in)).start();
    }




    public static void main(String[] args) {
        try {
            new Client();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
