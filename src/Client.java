import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Client extends Application {
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
        stage.setResizable(false);
        stage.setTitle("Hobby Room");
        AnchorPane all = new AnchorPane();

        Label title = new Label("Willkommen bei Lobby Room");
        setPos(title, 150, 20);
        Label ip = new Label("IP-Adresse: ");
        setPos(ip, 20, 100);
        Label port = new Label("Port: ");
        setPos(port, 250, 100);

        TextField ipInput = new TextField();
        setPos(ipInput, 90, 100);
        TextField portInput = new TextField();
        setPos(portInput, 280, 100);

        Button connect = new Button("connect");
        setPos(connect, 200, 180);

        all.getChildren().addAll(title, ip, port, ipInput, portInput, connect);

        connect.setOnAction(actionEvent -> {
            try {
                Socket client = new Socket(InetAddress.getByName(ipInput.getText()), Integer.parseInt(portInput.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Scene scene = new Scene(all, 500, 230);
        stage.setScene(scene);
        stage.show();
    }

    public static void setPos(Node n, double x, double y) {
        AnchorPane.setLeftAnchor(n, x);
        AnchorPane.setTopAnchor(n, y);
    }
}
