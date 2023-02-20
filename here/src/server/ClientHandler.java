package server;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private PrintWriter out;
    private String username;
    private static Map<String, String> users;
    private static List<ClientHandler> clients;
    private List<String> loggedInUsers;
    private List<String> bannedUsers;
    boolean loggedIn = false;

    public ClientHandler(Socket socket, Map<String, String> users, List<ClientHandler> clients, List<String> loggedInUsers, List<String> bannedUsers) {
        this.socket = socket;
        ClientHandler.users = users;
        ClientHandler.clients = clients;
        this.loggedInUsers = loggedInUsers;
        this.bannedUsers = bannedUsers;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            //Login
            username = in.readLine();
            String password = in.readLine();
            if (!validateCredentials(username, password)) {
                out.println("Login Failed.");
                socket.close();
                return;
            }
            if (isAlreadyLoggedIn(username)) {
                out.println("Already Logged in.");
                JOptionPane.showMessageDialog(null, "You are already Logged In as " + username);
                socket.close();
                return;
            }
            if (isBanned(username)){
                out.println("You are banned from the server.");
                socket.close();
                return;
            }

            loggedInUsers.add(username);
            out.println("Login successful.");
            loggedIn = true;

            //User list
            List<String> userList = new ArrayList<>(users.keySet());
            out.println(userList.size());
            for (String user: userList) {
                out.println(user);
            }

            //Chat
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("/pm")) {
                    String[] tokens = inputLine.split(" ");
                    String recipient = tokens[1];
                    String message = inputLine.substring(5 + recipient.length());
                    sendPrivateMessage(username, recipient, message);
                } else if (inputLine.equals("/quit")) {
                    break;
                } else {
                    broadcastMessage(username + ": " + inputLine);
                }
            }
        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
                clients.remove(this);
                if (loggedIn) {
                    broadcastMessage(username + " has left the chat.");
                }
            } catch (Exception e) {
                System.out.println("Error closing the socket: " + e.getMessage());
            }
        }
    }
    private void sendPrivateMessage(String sender, String recipient, String message) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.loggedIn && clientHandler.username.equals(recipient)) {
                clientHandler.out.println("(Private) " + sender + ": " + message);
                out.println("(Private) " + sender + ": " + message);
                return;
            }
        }
        out.println("User " + recipient + " not found or not logged in.");
    }

    public static boolean validateCredentials(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
    private void broadcastMessage(String message) {
        System.out.println("Broadcasting message: " + message);
        for (ClientHandler clientHandler: clients) {
            if (clientHandler != this) {
                clientHandler.out.println(message);
            } else {
                out.println(message);
            }
        }
    }

    private boolean isAlreadyLoggedIn(String username) {
        return loggedInUsers.contains(username);
    }

    private boolean isBanned(String username){
        return bannedUsers.contains(username);
    }
    public String getUsername() {
        return username;
    }


}

