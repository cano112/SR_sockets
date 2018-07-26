package pl.agh.edu.wiet.chat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class OutputThread extends Thread {
    private final Socket clientSocket;
    private final DatagramSocket datagramSocket;

    public OutputThread(Socket clientSocket, DatagramSocket datagramSocket) {
        this.clientSocket = clientSocket;
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        try(PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            Scanner scan = new Scanner(System.in);
            String message;
            while((message = scan.nextLine()) != null) {
                if(message.equals("U")) {
                    // send unicast UDP message
                    byte[] buf = Config.UDP_MESSAGE_1.getBytes();
                    datagramSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName(Config.SERVER_HOST), Config.SERVER_PORT));
                } else {
                    if(message.equals("M")) {
                        // send multicast UDP message
                        byte[] buf = Config.UDP_MESSAGE_2.getBytes();
                        datagramSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName(Config.MULTICAST_HOST), Config.MULTICAST_PORT));
                    } else {
                        out.println(message);
                    }

                }
            }
        } catch (IOException e) {
            System.err.println("Error occurred while sending message");
        } finally {
            datagramSocket.close();
        }
    }
}
