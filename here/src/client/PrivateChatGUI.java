package client;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

public class PrivateChatGUI {
    private String recipient;
    private JTextArea chatBox;
    private JTextArea inputField;
    private PrintWriter out;

    public PrivateChatGUI(String recipient, PrintWriter out) {
        this.recipient = recipient;
        this.out = out;

        JFrame chatFrame = new JFrame("Private Chat with " + recipient);
        JPanel mainPanel = new JPanel(new BorderLayout());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        JScrollPane chat = new JScrollPane(chatBox);
        chat.setPreferredSize(new Dimension(400, 400));

        JPanel messagePanel = new JPanel(new FlowLayout());
        inputField = new JTextArea();
        inputField.setLineWrap(true);
        JScrollPane message = new JScrollPane(inputField);
        message.setPreferredSize(new Dimension(300, 50));
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendPrivateMessage());

        messagePanel.add(message);
        messagePanel.add(sendButton);

        mainPanel.add(chat, BorderLayout.CENTER);
        mainPanel.add(messagePanel, BorderLayout.SOUTH);

        chatFrame.setSize(500, 500);
        chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.add(mainPanel);
        chatFrame.setVisible(true);
    }

    private void sendPrivateMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println("/pm " + recipient + " " + message);
            appendMessage("Me: " + message);
            inputField.setText("");
        }
    }

    public void appendMessage(String message) {
        chatBox.append(message + "\n");
    }
}
