package HobbyRoom;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.MalformedInputException;

public class Client extends Application {
    private Socket client;
    public String nameRegex;
    private static BufferedWriter wr;
    private static BufferedReader br;
    private String name;
    private ServerHandler serverHandler;

    public static void main(String[] args) throws IOException, InterruptedException {
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

        serverHandler = new ServerHandler(this, wr, br);
        serverHandler.start();

        nameRegex = br.readLine();
    }

    public void setName(String name) throws IOException {
        wr.write(name+"\n\r");
        wr.flush();
        this.name = name;

        MainPage.mainStage(this).show();
    }

    public void writeToServer(String s) throws IOException {
        wr.write(s + "\n");
        wr.flush();
    }
}
