package HobbyRoom;

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

            String nameInput = "";

            do {

                nameInput = br.readLine();

                System.out.println(nameInput);

                if (Server.validateName(this, nameInput)) {
                    break;
                }
            } while (true);

            USERNAME = nameInput;

            System.out.println("gesetzt : " + USERNAME);

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
                    break;
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
        catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
        finally {
            try {
                disconnect();
            } catch (IOException ignored) {}
        }
    }

    private void disconnect() throws IOException {
        client.close();
        System.out.println("Client disconnected: " + client.getRemoteSocketAddress());
        Server.disconnectUser(this);
    }

    public void write(String msg) {
        try {
            wr.write(Server.STZ_DAVOR + msg + Server.STZ_DANACH);
            wr.flush();
        } catch (IOException ignored) {}
    }
}
