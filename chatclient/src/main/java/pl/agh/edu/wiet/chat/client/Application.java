package pl.agh.edu.wiet.chat.client;

import java.io.IOException;
import java.net.*;

public class Application {

    public static void main(String[] args) {
        Socket clientSocket = null;
        DatagramSocket datagramSocket = null;
        MulticastSocket multicastSocket = null;
        try {
            clientSocket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);
            datagramSocket = new DatagramSocket(clientSocket.getLocalPort());
            multicastSocket = new MulticastSocket(Config.MULTICAST_PORT);
            multicastSocket.joinGroup(InetAddress.getByName(Config.MULTICAST_HOST));
            multicastSocket.setLoopbackMode(false);
        } catch (IOException e) {
            System.err.println("Cannot connect to server");
        }

        if(clientSocket != null) {
            System.out.println("---Chat Client---");
            System.out.println(clientSocket.getLocalSocketAddress().toString());

            new OutputThread(clientSocket, datagramSocket).start();
            new TcpInputThread(clientSocket).start();
            new UdpInputThread(datagramSocket).start();
            new UdpMulticastInputThread(multicastSocket, clientSocket.getLocalSocketAddress()).start();
        }

    }
}
