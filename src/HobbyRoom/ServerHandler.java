package HobbyRoom;

import HobbyRoom.Games.Tictactoe;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends Thread {
    public Client user;
    public BufferedReader br;
    public BufferedWriter wr;

    private Boolean running = true;

    // if entry exists some thread is waiting for a server input
    private final Map<String, String> awaited = new HashMap<>();

    public ServerHandler(Client user, BufferedWriter wr, BufferedReader br) {
        this.user = user;
        this.br = br;
        this.wr = wr;
    }

    @Override
    public void run() {
        System.out.println("listening for Server Input");

        String input = "";

        while (running) {
            try {
                input = br.readLine().replaceAll("\n", "").replaceAll("\r", "");
                if (awaited.containsKey(input.split(":")[0])) {
                    awaited.put(input.split(":")[0], input);
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Client disconnected!");
                running = false;
            }

            if (input.startsWith("ttt:CLNREQ")) {
                //Tictactoe.request(user, input.split(":")[2]);
                System.out.println("challenged to a game of tictactoe by " + input.split(":")[2]);
                String finalInput = input;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Tictactoe.request(user, finalInput.split(":")[2]);
                    }
                });
                //Tictactoe.request(user, input.split(":")[2]);
                //user.writeToServer("ttt:CLNACC");
                continue;
            }

            MainPage.output(input);

            System.out.println("Incoming from Server: " + input);
        }
    }

    public void close() {
        running = false;
        Thread.currentThread().interrupt();
    }

    public String receive(String prefix, String msg) { // tells the thread to return the next incoming stream from the server
        awaited.put(prefix, null);
        user.writeToServer(msg);
        while (awaited.get(prefix) == null) {
            try {
                sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String erg = awaited.get(prefix);
        awaited.remove(prefix);
        return erg;
    }
}
