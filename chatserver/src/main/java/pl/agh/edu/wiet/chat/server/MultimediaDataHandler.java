package pl.agh.edu.wiet.chat.server;

import pl.agh.edu.wiet.chat.server.model.User;
import pl.agh.edu.wiet.chat.server.model.UserContext;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MultimediaDataHandler implements Runnable {

    private final UserContext userContext;
    private final DatagramSocket datagramSocket;

    public MultimediaDataHandler(UserContext userContext, DatagramSocket datagramSocket) {
        this.userContext = userContext;
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        byte[] receiveBuf = new byte[1024];
        while(true) {
            DatagramPacket receivedPacket = new DatagramPacket(receiveBuf, receiveBuf.length);

            try {
                datagramSocket.receive(receivedPacket);
                User sender = userContext
                        .getUserByInetAddressAndPort(receivedPacket.getAddress(), receivedPacket.getPort())
                        .orElse(null);

                userContext.forEachUserExceptOne(sender, u -> {
                    DatagramPacket sendPacket =
                            new DatagramPacket(receiveBuf, receiveBuf.length, u.getAddress(), u.getPort());
                    try {
                        datagramSocket.send(sendPacket);
                    } catch (IOException e) {
                        System.err.println("Cannot send UDP message to " + u.getNickname());
                    }
                });
            } catch (IOException e) {
                System.err.println("Cannot receive UDP message");
            }
        }
    }
}
