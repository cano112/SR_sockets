package pl.agh.edu.wiet.chat.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketAddress;

public class UdpMulticastInputThread extends Thread {

    private final MulticastSocket multicastSocket;
    private final SocketAddress myRemoteAddress;

    public UdpMulticastInputThread(MulticastSocket multicastSocket, SocketAddress myRemoteAddress) {
        this.multicastSocket = multicastSocket;
        this.myRemoteAddress = myRemoteAddress;
    }

    @Override
    public void run() {
        try {
            while(true) {
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    multicastSocket.receive(receivePacket);
                    if(!receivePacket.getSocketAddress().equals(myRemoteAddress))
                        System.out.println(new String(receivePacket.getData()));
            }
        } catch (IOException e) {
            System.err.println("Cannot read UDP multicast message");
        } finally {
            multicastSocket.close();
        }
    }
}
