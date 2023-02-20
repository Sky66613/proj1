package server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private List<ClientHandler> clients;
    private ServerSocket serverSocket;
    private Map<String, String> users;
    private List<String> loggedInUsers;
    private List<String> bannedUsers;

    public Server() {
        clients = new ArrayList<>();
        ServerGUI serverGUI = new ServerGUI(clients);
        users = readUsersFromXML();
        loggedInUsers = new ArrayList<>();
        bannedUsers = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(1444);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, users, clients, loggedInUsers, bannedUsers);
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                // Update the GUI
                serverGUI.updateClients(clients);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }
    public static void main(String[] args){
        new Server();

    }

    private Map<String, String> readUsersFromXML() {
        Map<String, String> users = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("res/users.xml");
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String username = element.getElementsByTagName("username").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();
                users.put(username, password);
            }
        } catch (Exception e) {
            System.out.println("Error reading users from XML: " + e.getMessage());
        }
        return users;
    }


}
