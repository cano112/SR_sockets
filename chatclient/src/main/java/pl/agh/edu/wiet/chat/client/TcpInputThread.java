package pl.agh.edu.wiet.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpInputThread extends Thread {

    private final Socket clientSocket;

    public TcpInputThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            while(true) {
                String inputLine = in.readLine();
                if (inputLine == null) break;
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            System.err.println("Cannot read message from TCP socket");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error while closing TCP socket");
            }
        }
    }
}
