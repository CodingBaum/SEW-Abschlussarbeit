package HobbyRoom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ServerHandler extends Thread {
    public Client user;
    public BufferedReader br;
    public BufferedWriter wr;

    private Boolean running = true;

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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Client disconnected!");
                System.out.println(running);
                running = false;
            }

            MainPage.output(input);

            System.out.println("Incoming from Server: " + input);
        }
    }

    public void close() {
        running = false;
        Thread.currentThread().interrupt();
    }
}
