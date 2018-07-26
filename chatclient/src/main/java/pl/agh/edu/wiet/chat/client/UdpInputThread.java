package pl.agh.edu.wiet.chat.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpInputThread extends Thread {

    private final DatagramSocket datagramSocket;

    public UdpInputThread(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        try {
            while(true) {
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    datagramSocket.receive(receivePacket);
                    System.out.println(new String(receivePacket.getData()));
            }
        } catch (IOException e) {
            System.err.println("Cannot read UDP unicast message");
        } finally {
            datagramSocket.close();
        }
    }
}
