package HobbyRoom;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Client extends Application {
    private Socket client;
    public String nameRegex;
    private static BufferedWriter wr;
    private static BufferedReader br;
    private String name;
    private ServerHandler serverHandler;
    private Stage mainStage;

    public static Map<String, UnaryOperator<Object[]>> commands = new HashMap<>();

    {
        commands.put("quit", x -> {
            disconnect();
            return null;
        });

        commands.put("msg", x -> {
            String user = x[0].toString();

            x[0] = "";

            writeToServer(user + ":" + Arrays.stream(x).map(Object::toString).collect(Collectors.joining("")));

            return null;
        });
    }

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

        nameRegex = br.readLine();
    }

    public boolean setName(String name) throws IOException {
        wr.write(name+"\n");
        wr.flush();

        String response = br.readLine();

        if (response.contains("ungültig") || response.contains("vergeben")) {
            return false;
        }

        serverHandler = new ServerHandler(this, wr, br);
        serverHandler.start();

        this.name = name;

        mainStage = MainPage.mainStage(this);

        mainStage.show();

        MainPage.output(response);

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
        writeToServer("quit\n");
        System.exit(0);
    }

    public String getName() {
        return name;
    }

    public String getFromServer(String prefix, String msg) {
        return serverHandler.receive(prefix, msg);
    }
}
