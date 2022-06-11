package HobbyRoom;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private String password;
    private List<ClientHandler> users;

    public Room(String name, String password) {
        this.name = name;
        this.password = password;
        users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<ClientHandler> getUsers() {
        return users;
    }

    public void addUser(ClientHandler client) {
        users.add(client);
    }

    public void removeUser(ClientHandler client) {
        users.remove(client);
    }
}
