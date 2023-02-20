package server;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServerGUI extends JFrame {
    private static final String WINDOW_TITLE = "Chat Server - Connected Clients";
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final int MAX_VISIBLE_CLIENTS = 20;

    private JLabel clientsLabel;
    private JLabel numClientsLabel;
    private JTextArea clientsTextArea;

    public ServerGUI(List<ClientHandler> clients) {
        super(WINDOW_TITLE);

        // Initialize components
        clientsLabel = new JLabel("Connected Clients:");
        numClientsLabel = new JLabel();
        clientsTextArea = new JTextArea(MAX_VISIBLE_CLIENTS, 50);
        JScrollPane scrollPane = new JScrollPane(clientsTextArea);
        clientsTextArea.setEditable(false);

        // Set layout
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(clientsLabel);
        topPanel.add(numClientsLabel);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        // Configure frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);

        // Initialize client list
        updateClients(clients);
    }

    public void updateClients(List<ClientHandler> clients) {
        clientsTextArea.setText("");
        numClientsLabel.setText(Integer.toString(clients.size()));
        for (ClientHandler client : clients) {
            clientsTextArea.append(client.getUsername() + "\n");
        }
    }
}
