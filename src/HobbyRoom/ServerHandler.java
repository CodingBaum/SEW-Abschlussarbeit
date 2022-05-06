package HobbyRoom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ServerHandler extends Thread {
    Client user;
    BufferedReader br;
    BufferedWriter wr;

    public ServerHandler(Client user, BufferedWriter wr, BufferedReader br) {
        this.user = user;
        this.br = br;
        this.wr = wr;
    }

    @Override
    public void run() {
        System.out.println("listening for Server Input");

        String input = "";

        while (true) {
            try {
                input = br.readLine().replaceAll("\n", "");
            } catch (IOException e) {
                e.printStackTrace();
            }

            MainPage.output(input);

            System.out.println(input);
        }
    }

}
