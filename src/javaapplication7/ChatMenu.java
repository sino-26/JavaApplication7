
package javaapplication7;

import javax.swing.JOptionPane;

public class ChatMenu {

    public static void launch(String username) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat, " + username + "!");

        String input = JOptionPane.showInputDialog("How many messages do you want to send?");
        int totalMessages = 0;

        try {
            totalMessages = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            System.exit(0);
        }

        int messagesSent = 0;

        while (true) {
            String menu = JOptionPane.showInputDialog(
                "Choose an option:\n" +
                "1) Send Message\n" +
                "2) Show Recently Sent Messages\n" +
                "3) Quit"
            );

            if (menu == null) {
                JOptionPane.showMessageDialog(null, "Canceled. Exiting.");
                break;
            }

            switch (menu.trim()) {
                case "1":
                    if (messagesSent < totalMessages) {
                        String messageID = JOptionPane.showInputDialog("Enter 10-digit Message ID:");
                        if (messageID == null || !Methods.checkMessageID(messageID.trim())) {
                            JOptionPane.showMessageDialog(null, "Invalid Message ID.");
                            break;
                        }

                        String recipientCell = JOptionPane.showInputDialog("Enter recipient's cell number (10 digits):");
                        if (recipientCell == null || !Methods.checkRecipientCell(recipientCell.trim())) {
                            JOptionPane.showMessageDialog(null, "Invalid recipient cell number.");
                            break;
                        }

                        String messageText = JOptionPane.showInputDialog("Enter your message:");
                        if (messageText == null || messageText.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Message cannot be empty.");
                            break;
                        }

                        String hash = Methods.createMessageHash(messageID.trim(), messagesSent + 1, messageText.trim());

                        String option = Methods.sendMessageOption();
                        if (option.equals("Send") || option.equals("Store")) {
                            Methods.storeMessageToText(messageID.trim(), recipientCell.trim(), messageText.trim(), hash, option);
                            if (option.equals("Send"))
                            messagesSent++;
                            Methods.incrementMessageCount();
                            JOptionPane.showMessageDialog(null, "Message sent (" + messagesSent + "/" + totalMessages + ")");
                        } else {
                            JOptionPane.showMessageDialog(null, "Message disregarded.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "You've reached the limit of " + totalMessages + " messages.");
                    }
                    break;

                case "2":
                    String storedMessages = Methods.readStoredMessages();
                    JOptionPane.showMessageDialog(null, storedMessages.isEmpty() ? "No stored messages." : storedMessages);
                    break;

                case "3":
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    System.exit(0);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
}
        
