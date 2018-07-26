package pl.agh.edu.wiet.chat.server;

import pl.agh.edu.wiet.chat.server.model.User;
import pl.agh.edu.wiet.chat.server.model.UserContext;
import pl.agh.edu.wiet.chat.server.service.TextMessageService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TextMessageHandler implements Runnable {
    private final User user;
    private final UserContext userContext;
    private final TextMessageService textMessageService;

    public TextMessageHandler(User user, UserContext userContext, TextMessageService textMessageService) {
        this.user = user;
        this.userContext = userContext;
        this.textMessageService = textMessageService;
    }

    @Override
    public void run() {
        Socket clientSocket = user.getClientTcpSocket();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            while (true) {
                String inputLine = in.readLine();
                if (inputLine == null) break;
                userContext.forEachUserExceptOne(user, u ->
                        textMessageService.sendTextMessage(u, user.getNickname() + ": " + inputLine));
            }

        } catch (IOException e) {
            System.out.println("Cannot connect with user: " + user.getNickname() + ".");
        } finally {
            try {
                userContext.removeUser(user);
                System.out.println("Removing user: " + user.getNickname() + " from server...");
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error occured while closing connection with user");
            }
        }
    }
}
