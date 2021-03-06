package ja;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class ClientHandler extends Thread {
    public String USERNAME = null;
    public Socket client;
    private BufferedReader br;
    private BufferedWriter wr;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            wr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            wr.write("Willkommen im Chatroom!\n\r\n\r");
            wr.flush();

            wr.write("Bitte geben Sie ihren Benutzernamen ein: \n\r");
            wr.flush();

            String nameInput = "jaaaa";

            try {
                nameInput = br.readLine().substring(21);
            } catch (IndexOutOfBoundsException ignored) {}


            while (true) {
                if (Server.validateName(this, nameInput)) {
                    break;
                }
                nameInput = br.readLine();
            }

            USERNAME = nameInput;

            while (true) {
                wr.write(USERNAME + "> ");
                wr.flush();
                String input = br.readLine();

                if (input.isBlank()) {
                    continue;
                }

                if (input.equals("list")) {
                    wr.write(Server.getAllUsers() + "\n\r");
                    wr.flush();
                } else if (input.equals("quit")) {
                    wr.close();
                    br.close();
                    disconnect();
                } else if (input.matches(Server.privateMsgSyntax)) {
                    Pattern pattern = Pattern.compile(Server.privateMsgSyntax);
                    String target = pattern.matcher(input).replaceFirst(x -> x.group(1));
                    String msg = pattern.matcher(input).replaceFirst(x -> x.group(2));
                    Server.sendMessage(this, target, msg);
                } else if (input.equals("stat")) {
                    wr.write(Server.printStats() + "\n\r");
                    wr.flush();
                } else {
                    Server.broadCast(this, input);
                }
            }

        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        catch (NullPointerException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    private void disconnect() {
        System.out.println("ja.HobbyRoom.Client disconnected: " + client.getRemoteSocketAddress());
        Server.disconnectUser(this);
    }

    public void write(String msg) {
        try {
            wr.write(Server.STZ_DAVOR + msg + Server.STZ_DANACH);
            wr.flush();
        } catch (IOException ignored) {}
    }
}
