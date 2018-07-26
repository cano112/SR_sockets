package pl.agh.edu.wiet.chat.server.model;

import java.net.InetAddress;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

public final class UserContext {

    private final Set<User> users;

    public UserContext() {
        this.users = new ConcurrentSkipListSet<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void reset() {
        users.clear();
    }

    public void forEachUserExceptOne(User user, Consumer<? super User> callback) {
        users.stream()
                .filter(u -> u != user)
                .forEach(callback);
    }

    public Optional<User> getUserByInetAddressAndPort(InetAddress address, int port) {
        return users
                .stream()
                .filter(u -> u.getAddress().equals(address) && u.getPort() == port)
                .findAny();
    }
}
