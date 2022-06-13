package HobbyRoom;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class ClientHandler extends Thread {
    public String USERNAME = null;
    public Socket client;
    private BufferedReader br;
    private BufferedWriter wr;
    public String currentRoomName;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    /*
        Protokoll optionen


        tictactoe handling

        ttt:

        CLNINI  initiate
        CLNACC  accept
        CLNREJ  reject
        CLNREQ  request
        SET     set value to field
        WIN     tell the winner of the game


        CMD     user befehlen wie z.b. list



        msg:    norale User Nachrichten

        sysmsg: nachrichten vom Server an die clients



        room:       fuer private chatrooms

        CREATE      erstellen eines raums
        CONNECT     verbinden eines users auf einen server
        DISCONENCT  verbindung von einem user aufheben
        ERROR       falls etwas nicht moeglich ist um eine fehlermeldung an den client zu liefern
        CONFIRM     Bestaetigt dass die Aktion durchgefuehrt wurde
     */

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
                    String[] args = input.split(":");

                    switch (args[1]) {
                        case "CLNINI":

                            if (Server.checkUser(args[2])) {
                                ClientHandler challenged = Server.clients.stream().filter(x -> x.USERNAME.equals(args[2])).findFirst().get();
                                challenged.write("ttt:CLNREQ:" + this.USERNAME + "\n");
                                System.out.println("request sent to " + args[2]);

                                List<ClientHandler> players = new ArrayList<>();

                                players.add(this);
                                players.add(challenged);

                                Server.tictactoeGames.put(players, null);
                            } else {
                                write("ttt:CLNREJ:User does not exist\n");
                            }
                            break;
                        case "CLNACC":
                            Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get().get(0).write("ttt:CLNACC\n"); // write accept to the user that initiated the challenge

                            List<ClientHandler> temp = new ArrayList<>();
                            temp.add(Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get().get(0)); // add the initiator of the challenge

                            temp.add(this);
                            Server.tictactoeGames.put(temp, new Integer[3][3]); // create tictactoe object

                            break;
                        case "CLNREJ":
                            Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get().get(0).write("ttt:CLNREJ\n"); // write reject to the user that initiated the challenge

                            Server.tictactoeGames.remove(Server.tictactoeGames.keySet().stream().filter(x -> x.get(1).equals(this)).findFirst().get()); // remove the rejected game from the game list

                            break;
                        case "SET":
                            List<ClientHandler> players = Server.tictactoeGames.keySet().stream().filter(x -> x.contains(this)).findFirst().get();

                            Integer[][] game = Server.tictactoeGames.get(players);

                            players.remove(this);
                            players.get(0).write(input + "\n");
                            players.add(this);
                            game[Integer.parseInt(args[2].charAt(0) + "")][Integer.parseInt(args[2].charAt(1) + "")] = 1;
                            Server.tictactoeGames.put(players, game);
                            break;
                    }
                    continue;
                }

                if (input.startsWith("room:")) {
                    switch (input.split(":")[1]) {
                        case "CREATE" -> {
                            if (Server.roomList.stream().filter(x -> x.getName().equals(input.split(":")[2])).toList().size() != 0) {
                                write("room:ERROR:Dieser Name ist bereits vergeben\n");
                            } else {
                                System.out.println("ok");
                                String password = input.split(":")[3].equals("null") ? null : input.split(":")[3];
                                Room room = new Room(input.split(":")[2], password);
                                Server.roomList.add(room);
                                currentRoomName = room.getName();
                                write("room:CONFIRM:\n");
                            }
                        }
                        case "CONNECT" -> {
                            if (currentRoomName != null) {
                                Server.roomList.forEach(x -> {
                                    if (x.getName().equals(currentRoomName)) {
                                        x.removeUser(this);
                                    }
                                });
                            }
                            Server.roomList.forEach(x -> {
                                if (x.getName().equals(input.split(":")[2])) {
                                    boolean temp;
                                    if (input.split(":").length != 4) {
                                        temp = false;
                                    } else {
                                        temp = x.getPassword().equals(input.split(":")[3]);
                                    }
                                    if (x.getPassword() == null || temp) {
                                        x.addUser(this);
                                        write("room:CONFIRM:\n");
                                    } else {
                                        write("room:ERROR:Falsches Passwort\n");
                                    }
                                } else {
                                    write("room:ERROR:Es existiert kein Raum mit diesem Namen\n");
                                }
                            });
                            currentRoomName = input.split(":")[2];
                        }
                        case "DISCONNECT" -> {
                            AtomicReference<Room> temp = null;
                            Server.roomList.forEach(x -> {
                                if (x.getName().equals(currentRoomName)) {
                                    x.removeUser(this);
                                    if (x.getUsers().size() == 0) {
                                        temp.set(x);
                                    }
                                }
                            });

                            if (temp.get() == null) {
                                Server.roomList.remove(temp.get());
                            }

                            currentRoomName = null;
                        }
                    }
                    continue;
                }

                if (input.startsWith("CMD:")) {
                    if (input.split(":")[1].startsWith("list")) {
                        wr.write("LIST:"+Server.getAllUsers() + "\n");
                        wr.flush();
                    } else if (input.split(":")[1].startsWith("quit")) {
                        wr.close();
                        br.close();
                        break;
                    }
                } else {
                    if (currentRoomName != null) {
                        System.out.println("AN " + currentRoomName + ": " + input);
                        Optional<Room> temp = Server.roomList.stream().filter(x -> x.getName().equals(currentRoomName)).findFirst();
                        System.out.println(temp.get().getUsers());
                        temp.ifPresent(room -> room.getUsers().stream().filter(x -> !x.equals(this)).forEach(x -> {
                            x.write(input + "\n");
                        }));
                    } else {
                        Server.broadCast(this, input);
                        System.out.println("DAS WIRD BROADCASTET: " + input);
                    }
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
