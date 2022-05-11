package HobbyRoom;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

            wr.write(Server.nameValidation+"\n");
            wr.flush();

            String nameInput;

            do {
                nameInput = br.readLine();
            } while (!Server.validateName(this, nameInput));

            USERNAME = nameInput;

            while (true) {
                String input = br.readLine();

                System.out.println(input);

                if (input.isBlank()) {
                    continue;
                }

                if (input.startsWith("ttt:")) {
                    // tictactoe handling

                    // CLNINI  initiate
                    // CLNACC  accept
                    // CLNREJ  reject
                    // CLNREQ  request

                    String[] args = input.split(":");

                    System.out.println(Arrays.toString(args));

                    if (args[1].equals("CLNINI")) {
                        wr.write("ttt:CLNACC\n");
                        wr.flush();
                        //write("ttt:CLNACC");

                        /*if (!Server.checkUser(args[2])) {
                            write("ttt:CLNREJ:User does not exist");
                        } else {
                            Server.clients.stream().filter(x -> x.USERNAME.equals(args[2])).findFirst().get().write("ttt:CLNREQ:" + USERNAME);
                            List<ClientHandler> temp = new ArrayList<>();
                            temp.add(this);
                            Server.tictactoeGames.add(temp);
                        }*/
                    } else if (args[1].equals("CLNACC")) {

                    }
                    continue;
                }

                if (input.equals("list")) {
                    wr.write(Server.getAllUsers() + "\n");
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
                    wr.write(Server.printStats() + "\n");
                    wr.flush();
                } else {
                    Server.broadCast(this, input);
                }
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
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
            wr.write(msg);
            wr.flush();
        } catch (IOException ignored) {}
    }
}
