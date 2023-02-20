package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;



public class ChatGUI {

    private static JTextArea chatBox;
    private static JTextArea messageField;
    private static PrintWriter out;
    private static List<String> userList;

    public ChatGUI(PrintWriter out, List<String> userList) {
        ChatGUI.out = out;
        ChatGUI.userList = userList;

        JFrame chatFrame = new JFrame("Chat");
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.white);


        //Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(200, 500));

        //Search Button
        JButton searchButton = new JButton("Search User");
        searchButton.setSize(70, 80);
        searchButton.addActionListener(e -> {
            String recipient = (String) JOptionPane.showInputDialog(chatFrame, "Showing list of users ",
                    "Search User", JOptionPane.PLAIN_MESSAGE, null, userList.toArray(), userList.get(0));
        });

        //Group Chat Button
        JButton groupChatButton = new JButton("Group Chat");
        groupChatButton.setSize(70, 80);
        groupChatButton.addActionListener(e -> {});

        //Private Message Button
        JButton privateMessageButton = new JButton("Private Message");
        privateMessageButton.setSize(70, 80);
        privateMessageButton.addActionListener(e -> {
            String recipient = (String) JOptionPane.showInputDialog(chatFrame, "Select a user to private message:",
                    "Private Message", JOptionPane.PLAIN_MESSAGE, null, userList.toArray(), userList.get(0));
            if (recipient != null) {
                new PrivateChatGUI(recipient, out);
            }
        });

        JPanel leftPanel = new JPanel();
        leftPanel.setSize(300, 700);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(userList);
        JList<String> users = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(users);
        listScrollPane.setPreferredSize(new Dimension(300, 700));
        leftPanel.add(listScrollPane);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setSize(500,700);

        chatBox = new JTextArea();
        JScrollPane chat = new JScrollPane(chatBox);
        chat.setPreferredSize(new Dimension(500, 500));
        chatBox.setEditable(false);

        JPanel messagePanel = new JPanel(new FlowLayout());
        messageField = new JTextArea();
        messageField.setLineWrap(true);
        JScrollPane message = new JScrollPane(messageField);
        message.setPreferredSize(new Dimension(400, 50));
        JButton sendButton = new JButton("Send");
        sendButton.setSize(100, 50);
        sendButton.addActionListener(e -> {
            String message1 = ChatGUI.getMessageText();
            if (!message1.isEmpty()) {
                out.println(message1);
                ChatGUI.clearMessageText();
            }
        });

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    String message1 = ChatGUI.getMessageText();
                    if (!message1.isEmpty()) {
                        out.println(message1);
                        ChatGUI.clearMessageText();
                        e.consume();
                    }
                }
            }
        });

        buttonsPanel.add(searchButton);
        buttonsPanel.add(privateMessageButton);
        buttonsPanel.add(groupChatButton);
        mainPanel.add(buttonsPanel, BorderLayout.EAST);

        messagePanel.add(message);
        messagePanel.add(sendButton);


        chatPanel.add(chat);
        chatPanel.add(messagePanel);
        mainPanel.add(chatPanel, BorderLayout.CENTER);

        chatFrame.setSize(1000, 700);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.add(mainPanel);
        chatFrame.setVisible(true);
    }

    public static String getMessageText() {
        return messageField.getText();
    }

    public static void clearMessageText() {
        messageField.setText("");
    }

    public static void appendMessage(String message) {
        chatBox.append(message + "\n");
    }
}
