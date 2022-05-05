package HobbyRoom;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {
    private Socket client;
    private String nameRegex;
    private BufferedWriter wr;
    private BufferedReader br;

    public static void main(String[] args) throws IOException, InterruptedException {
        /*Socket client = new Socket(InetAddress.getByName("127.0.0.1"), 42069);

        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        wr.write("hallo\r\n");
        wr.flush();

        System.out.println(br.readLine());
        System.out.println(br.readLine());
        System.out.println(br.readLine());*/

        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Login.loginPage(this).show();
    }

    public static void setPos(Node n, double x, double y) {
        AnchorPane.setLeftAnchor(n, x);
        AnchorPane.setTopAnchor(n, y);
    }

    public void setClient(Socket client) throws IOException {
        this.client = client;
        br = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        wr = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
    }
}
