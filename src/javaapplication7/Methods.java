package javaapplication7;

import javax.swing.JOptionPane;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Methods {

    // === Arrays for Part 3 ===
    static ArrayList<String> sentMessages = new ArrayList<>();
    static ArrayList<String> storedMessages = new ArrayList<>();
    static ArrayList<String> disregardedMessages = new ArrayList<>();
    static ArrayList<String> messageHashes = new ArrayList<>();
    static ArrayList<String> messageIDs = new ArrayList<>();

    private static int totalMessagesSent = 0;

    // === Validation Methods ===
    public static boolean checkUsername(String username) {
        return username.length() <= 5 && username.contains("_");
    }

    public static Object validatePassword(String password) {
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[^a-zA-Z0-9].*");
        boolean isLongEnough = password.length() >= 8;

        if (hasUppercase && hasDigit && hasSpecialChar && isLongEnough) {
            return true;
        } else {
            return "Password is not correctly formatted.";
        }
    }

    public static Object validateCellphone(String cellphone) {
        if (cellphone.startsWith("+27") && cellphone.length() == 12) {
            String numberPart = cellphone.substring(3);
            if (numberPart.matches("\\d{9}")) {
                return true;
            }
        }
        return "Cellphone number incorrectly formatted or does not contain international code.";
    }

    public static boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public boolean loginUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("user.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String savedUsername = parts[0].trim();
                    String savedPassword = parts[1].trim();
                    if (savedUsername.equals(username) && savedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() == 10 && messageID.matches("\\d+");
    }

    public static boolean checkRecipientCell(String cellNumber) {
        return cellNumber != null && cellNumber.startsWith("0") && cellNumber.length() == 10 && cellNumber.matches("\\d+");
    }

    public static String createMessageHash(String messageID, int messageCount, String messageText) {
        String[] words = messageText.trim().split("\\s+");
        if (words.length < 2) return "INVALID MESSAGE";
        return (messageID.substring(0, 2) + ":" + messageCount + ":" + words[0] + words[words.length - 1]).toUpperCase();
    }

    public static String sendMessageOption() {
        Object[] options = {"Send", "Disregard", "Store"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Send Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        return (choice == 0) ? "Send" : (choice == 1) ? "Disregard" : "Store";
    }

    public static void incrementMessageCount() {
        totalMessagesSent++;
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public static void storeMessageToText(String messageID, String recipientCell, String messageText, String hash, String flag) {
        try {
            String message = String.format("ID: %s | To: %s | Message: %s | Hash: %s%n", messageID, recipientCell, messageText, hash);
            Files.write(Paths.get("messages.txt"), message.getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);

            if (flag.equals("Send")) {
                sentMessages.add(messageText);
            } else if (flag.equals("Store")) {
                storedMessages.add(messageText);
            } else {
                disregardedMessages.add(messageText);
            }

            messageIDs.add(messageID);
            messageHashes.add(hash);

            JOptionPane.showMessageDialog(null, "Message processed successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
        }
    }

    public static String readStoredMessages() {
        File file = new File("messages.txt");
        if (!file.exists()) return "No stored messages found.";

        try {
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            return "Error reading messages: " + e.getMessage();
        }
    }

    // === Part 3 Functionalities ===

    public static String displayLongestMessage() {
        String longest = "";
        for (String msg : sentMessages) {
            if (msg.length() > longest.length()) longest = msg;
        }
        return longest;
    }

    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                return sentMessages.get(i);
            }
        }
        return "Message ID not found.";
    }

    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> result = new ArrayList<>();
        for (String line : readStoredMessages().split("\n")) {
            if (line.contains("To: " + recipient)) {
                int msgIndex = line.indexOf("Message: ");
                if (msgIndex != -1) {
                    String message = line.substring(msgIndex + 9).split("\\|")[0].trim();
                    result.add(message);
                }
            }
        }
        return result;
    }

    public static String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String deleted = sentMessages.remove(i);
                messageHashes.remove(i);
                messageIDs.remove(i);
                return "Message \"" + deleted + "\" successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    public static String displaySentReport() {
        StringBuilder report = new StringBuilder("Sent Message Report:\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("Hash: ").append(messageHashes.get(i))
                    .append(" | To: ").append(messageIDs.get(i))
                    .append(" | Message: ").append(sentMessages.get(i)).append("\n");
        }
        return report.toString();
    }

    // === Unit Test Helper ===
    public static void populateTestMessages() {
        // Clear everything
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
        messageIDs.clear();
        messageHashes.clear();

        // Message 1 - Sent
        storeMessageToText("0000000001", "+27834557896", "Did you get the cake?", createMessageHash("0000000001", 1, "Did you get the cake?"), "Send");
        // Message 2 - Store
        storeMessageToText("0000000002", "+27838884567", "Where are you? You are late! I have asked you to be on time.", createMessageHash("0000000002", 2, "Where are you? You are late! I have asked you to be on time."), "Store");
        // Message 3 - Disregard
        storeMessageToText("0000000003", "+27834484567", "Yohoooo, I am at your gate", createMessageHash("0000000003", 3, "Yohoooo, I am at your gate"), "Disregard");
        // Message 4 - Sent
        storeMessageToText("0838884567", "0838884567", "It is dinner time!", createMessageHash("0838884567", 4, "It is dinner time!"), "Send");
        // Message 5 - Store
        storeMessageToText("0000000005", "+27838884567", "Ok, I am leaving without you", createMessageHash("0000000005", 5, "Ok, I am leaving without you"), "Store");
    }
}

        

  

