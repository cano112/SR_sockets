package pl.agh.edu.wiet.chat.server.model;

import java.net.InetAddress;
import java.net.Socket;

public final class User implements Comparable<User> {
    private final String nickname;
    private final Socket clientTcpSocket;

    public User(String nickname, Socket clientTcpSocket) {
        this.nickname = nickname;
        this.clientTcpSocket = clientTcpSocket;
    }

    public String getNickname() {
        return nickname;
    }

    public Socket getClientTcpSocket() {
        return clientTcpSocket;
    }

    public InetAddress getAddress() {
        return clientTcpSocket != null ? clientTcpSocket.getInetAddress() : null;
    }

    public int getPort() {
        return clientTcpSocket != null ? clientTcpSocket.getPort() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!nickname.equals(user.nickname)) return false;
        return clientTcpSocket.equals(user.clientTcpSocket);
    }

    @Override
    public int hashCode() {
        int result = nickname.hashCode();
        result = 31 * result + clientTcpSocket.hashCode();
        return result;
    }

    @Override
    public int compareTo(User o) {
        return this.getNickname().compareTo(o.getNickname());
    }
}
