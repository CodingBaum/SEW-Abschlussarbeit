package HobbyRoom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ServerHandler extends Thread {
    public Client user;
    public BufferedReader br;
    public BufferedWriter wr;

    private Boolean running = true;

    // if set true some thread is waiting for a server input
    private Boolean awaited = false;

    // output to other class
    private String out = null;

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
                if (awaited) {
                    awaited = false;
                    out = input;
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Client disconnected!");
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

    public String receive() { // tells the thread to return the next incoming stream from the server
        awaited = true;
        while (out == null) {
            try {
                sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String erg = out;
        out = null;
        return erg;
    }
}
