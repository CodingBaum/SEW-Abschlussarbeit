package HobbyRoom;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {
    private Socket client;
    public String nameRegex;
    private static BufferedWriter wr;
    private static BufferedReader br;
    private String name;
    private ServerHandler serverHandler;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
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

    public boolean setName(String name) throws IOException {
        wr.write(name+"\n\r");
        wr.flush();

        try {
            serverHandler.wait(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String response = br.readLine();

        System.out.println(response);

        if (response.contains("ungültig") || response.contains("vergeben")) {
            return false;
        }

        this.name = name;

        MainPage.mainStage(this).show();

        return true;
    }

    public void writeToServer(String s) {
        try {
            wr.write(s + "\n");
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        serverHandler.close();
        writeToServer("quit\n");
    }

    public String getName() {
        return name;
    }
}
