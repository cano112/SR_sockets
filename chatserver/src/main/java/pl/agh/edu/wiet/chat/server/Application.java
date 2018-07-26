package pl.agh.edu.wiet.chat.server;

import pl.agh.edu.wiet.chat.server.model.UserContext;
import pl.agh.edu.wiet.chat.server.service.TextMessageService;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {

        UserContext userContext = new UserContext();
        TextMessageService textMessageService = new TextMessageService();

        ChatServer server = null;
        try {
            System.out.println("---TCP Chat---");
            server = new TcpUdpChatServer(Config.SERVER_PORT, userContext, textMessageService);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(server != null) {
                server.stop();
            }
        }
    }
}
