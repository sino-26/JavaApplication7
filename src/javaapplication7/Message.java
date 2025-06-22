/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication7;

import java.io.FileWriter;
import java.io.IOException;

public class Message {
    private String sender;
    private String recipient;
    private String content;

    public Message(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    public String toString() {
        return "From: " + sender + "\nTo: " + recipient + "\nMessage: " + content + "\n---";
    }

    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(this.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing message to file: " + e.getMessage());
        }
    }
}