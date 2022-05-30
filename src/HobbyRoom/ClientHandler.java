package HobbyRoom;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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
                    // SET     set value to field
                    // WIN     tell the winner of the game

                    String[] args = input.split(":");

                    if (args[1].equals("CLNINI")) {

                        if(Server.checkUser(args[2])) {
                            ClientHandler challenged = Server.clients.stream().filter(x -> x.USERNAME.equals(args[2])).findFirst().get();
                            challenged.write("ttt:CLNREQ:"+this.USERNAME + "\n");
                            System.out.println("request sent to " + args[2]);

                            List<ClientHandler> players = new ArrayList<>();

                            players.add(this);
                            players.add(challenged);

                            Server.tictactoeGames.put(players, null);
                        } else {
                            write("ttt:CLNREJ:User does not exist\n");
                        }
                    } else if (args[1].equals("CLNACC")) {
                        Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get().get(0).write("ttt:CLNACC\n"); // write accept to the user that initiated the challenge
                        List<ClientHandler> temp = new ArrayList<>();
                        temp.add(Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get().get(0)); // add the initiator of the challenge
                        temp.add(this);
                        Server.tictactoeGames.put(temp, new Integer[3][3]); // create tictactoe object
                    } else if (args[1].equals("CLNREJ")) {
                        Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get().get(0).write("ttt:CLNREJ:\n"); // write reject to the user that initiated the challenge
                        Server.tictactoeGames.remove(Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get()); // remove the rejected game from the game list
                    } else if (args[1].equals("SET")) {

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
                    System.out.println("DAS WIRD BROADCASTET: " + input);
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
