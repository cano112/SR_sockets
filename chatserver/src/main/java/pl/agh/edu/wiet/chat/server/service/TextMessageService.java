package pl.agh.edu.wiet.chat.server.service;

import pl.agh.edu.wiet.chat.server.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TextMessageService {

    public void sendTextMessage(User user, String message) {
        Socket clientSocket = user.getClientTcpSocket();
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(message);
            System.out.println("Message to " + user.getNickname() + " sent");
        } catch (IOException e) {
            System.err.println("Cannot send UDP message to " + user.getNickname());
        }
    }
}
