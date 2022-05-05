import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    public static final String STZ_DAVOR =
            new String(new byte[] { 0x0b, 0x1b, '[', '1', 'A', 0x1b, '7', 0x1b,
                    '[', '1', 'L', '\r' });

    public static final String STZ_DANACH =
            new String(new byte[] { 0x1b, '8', 0x1b, '[', '1', 'B' });

    public static List<ClientHandler> clients = new LinkedList<>();
    public static String nameValidation = "[a-zA-Z0-9]{3,20}";
    public static String privateMsgSyntax = "(" + nameValidation + "):(.*)";

    private static Map<ClientHandler, Integer> stats = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(22333))
        {
            System.out.println("Server online and ready to connect");

            while (true) {
                Socket client = server.accept();
                System.out.println("New Client connected: " + client.getRemoteSocketAddress());
                new ClientHandler(client).start();
            }

        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static String getAllUsers() {
        List<String> erg = new LinkedList<>();

        for (ClientHandler s:clients) {
            if (s.USERNAME != null) {
                erg.add(s.USERNAME);
            }
        }

        return "\n\rCurrent users: \n\r" + erg.stream().collect(Collectors.joining("\n\r")) + "\n\r";
    }

    public static void broadCast(ClientHandler sender, String msg) {
        List<ClientHandler> ja = sender == null ? clients : clients.stream().filter(x -> !x.equals(sender)).collect(Collectors.toList());

        if (sender != null) {
            stats.put(sender, stats.getOrDefault(sender, 0)+1);
        }

        for (ClientHandler c :ja) {
            if (sender == null) {
                c.write(msg + "\n\r");
            } else {
                c.write("[PUBLIC] " + sender.USERNAME + ": " + msg + "\n\r");
            }
        }
    }

    public static void sendMessage(ClientHandler sender, String target, String msg) {
        ClientHandler to = clients.stream().filter(x -> x.USERNAME.equals(target)).findFirst().orElse(null);

        msg = msg.trim();

        if (to == null) {
            sender.write("[SYSTEM] Dieser Benutzer existiert nicht!" + "\n\r");
        } else {
            to.write("[PRIVATE] " + sender.USERNAME + ": " + msg + "\n\r");
            stats.put(sender, stats.getOrDefault(sender, 0)+1);
        }
    }

    public static void disconnectUser(ClientHandler client) {
        if (client.USERNAME != null) {
            clients.remove(client);
            broadCast(null, "[SYSTEM] " + client.USERNAME + " hat den Raum verlassen.\n\r");
        }
    }

    public static boolean validateName(ClientHandler client, String name) {
        if (!name.matches(nameValidation)) {
            client.write("[SYSTEM] Dieser Name ist ungültig!\n\r");
            return false;
        } else if (clients.stream().filter(x -> x.USERNAME.equals(name)).map(x -> x.USERNAME).collect(Collectors.toList()).contains(name)) {
            client.write("[SYSTEM] Dieser Name ist bereits vergeben!\n\r");
            return false;
        } else {
            client.write("[SYSTEM] Willkommen " + name + "!\n\r");
            broadCast(null, "[SYSTEM] " + name + " hat den Raum betreten.\n\r");
            clients.add(client);
            stats.put(client, 0);
            return true;
        }
    }

    public static String printStats() {
        return "\n\rStatistics: \n\r" + stats.keySet().stream().sorted(new Comparator<ClientHandler>() {
            @Override
            public int compare(ClientHandler o1, ClientHandler o2) {
                return o1.USERNAME.compareTo(o2.USERNAME);
            }
        }).map(x -> "   " + x.USERNAME + ": " + stats.getOrDefault(x, 0)).collect(Collectors.joining("\n\r")) + "\n\r";
    }
}
