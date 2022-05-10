package HobbyRoom;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static HobbyRoom.Client.setPos;

public class Login {
    public static Stage loginPage(Client user) {
        Stage stage = new Stage();

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
        ipInput.setText("127.0.0.1");
        TextField portInput = new TextField();
        setPos(portInput, 280, 100);
        portInput.setText("42069");

        Button connect = new Button("connect");
        setPos(connect, 200, 180);

        all.getChildren().addAll(title, ip, port, ipInput, portInput, connect);

        connect.setOnAction(actionEvent -> {
            try {
                Socket client = new Socket(InetAddress.getByName(ipInput.getText()), Integer.parseInt(portInput.getText()));
                user.setClient(client);
            } catch (IOException e) {
                errorMessage("Keine Verbindung zu diesem Server möglich").show();
                e.printStackTrace();
                return;
            }

            stage.close();
            nameSelect(user).show();
        });

        Scene scene = new Scene(all, 500, 230);
        stage.setScene(scene);

        return stage;
    }

    public static Stage nameSelect(Client user) {
        Stage stage = new Stage();

        stage.setResizable(false);
        stage.setTitle("Hobby Room");
        AnchorPane all = new AnchorPane();

        Label title = new Label("Bitte geben Sie ihren Benutzernamen ein.");
        setPos(title, 30, 30);

        TextField input = new TextField();
        setPos(input, 40, 90);

        Button submit = new Button("submit");
        setPos(submit, 200, 90);

        all.getChildren().addAll(title, input, submit);

        submit.setOnAction(actionEvent -> {

            if (!input.getText().matches(user.nameRegex)) {
                errorMessage("Dieser Name ist nicht erlaubt!").show();
                return;
            }

            try {
                if (!user.setName(input.getText())) {
                    errorMessage("Dieser Name ist bereits vergeben!").show();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        });

        Scene scene = new Scene(all, 300, 170);
        stage.setScene(scene);

        return stage;
    }

    public static Stage errorMessage(String msg) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Hobby Room");

        AnchorPane all = new AnchorPane();

        Label title = new Label(msg);
        setPos(title, 30, 30);

        Button accept = new Button("ok");
        setPos(accept, 130, 80);

        all.getChildren().addAll(title, accept);

        accept.setOnAction(actionEvent -> stage.close());

        Scene scene = new Scene(all, 280, 120);
        stage.setScene(scene);

        return stage;
    }
}
