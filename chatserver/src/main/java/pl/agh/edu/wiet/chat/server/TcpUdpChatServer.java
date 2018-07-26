package pl.agh.edu.wiet.chat.server;

import pl.agh.edu.wiet.chat.server.model.User;
import pl.agh.edu.wiet.chat.server.model.UserContext;
import pl.agh.edu.wiet.chat.server.service.TextMessageService;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpUdpChatServer implements ChatServer {

    private final ServerSocket serverSocket;
    private final DatagramSocket datagramSocket;
    private final UserContext userContext;
    private final TextMessageService textMessageService;
    private final ExecutorService executorService;

    public TcpUdpChatServer(int port, UserContext userContext, TextMessageService textMessageService) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.datagramSocket = new DatagramSocket(port);
        this.userContext = userContext;
        this.textMessageService = textMessageService;
        this.executorService = Executors.newFixedThreadPool(Config.MAX_THREADS_NUMBER);
    }

    public void start() {
        executorService.execute(new MultimediaDataHandler(userContext, datagramSocket));
        System.out.println("UDP server started");
        System.out.println("Listening for connections on port " + serverSocket.getLocalPort() + " ...");
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                User user = new User(clientSocket.getRemoteSocketAddress().toString(), clientSocket);
                userContext.addUser(user);
                executorService.execute(new TextMessageHandler(user, userContext, textMessageService));
                System.out.println("Connection with " + clientSocket.getRemoteSocketAddress().toString() + " established");

            } catch(IOException e) {
                System.err.println("Cannot accept a connection");
            }
        }
    }

    public void stop() {
        executorService.shutdownNow();
        userContext.reset();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error occurred while closing TCP socket");
        }
    }
}
