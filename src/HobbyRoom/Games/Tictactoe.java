package HobbyRoom.Games;

import HobbyRoom.Client;
import HobbyRoom.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;

import static HobbyRoom.Client.setPos;

public class Tictactoe {
    private static TextArea output;

    public static Stage createStage(Client client) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Tictactoe");


        AnchorPane grid = new AnchorPane();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = 350;
        double width = 350;
        int buttonheight = 100;
        int buttonwidth = 100;


        Button A1 = new Button();
        A1.setPrefHeight(buttonheight);
        A1.setPrefWidth(buttonwidth);
        setPos(A1, 10, 10);
        Button A2 = new Button();
        A2.setPrefHeight(buttonheight);
        A2.setPrefWidth(buttonwidth);
        setPos(A2, buttonwidth + 15, 10);
        Button A3 = new Button();
        A3.setPrefHeight(buttonheight);
        A3.setPrefWidth(buttonwidth);
        setPos(A3, buttonwidth*2 + 20, 10);
        Button B1 = new Button();
        B1.setPrefHeight(buttonheight);
        B1.setPrefWidth(buttonwidth);
        setPos(B1, 10, buttonheight + 15);
        Button B2 = new Button();
        B2.setPrefHeight(buttonheight);
        B2.setPrefWidth(buttonwidth);
        setPos(B2, buttonwidth + 15, buttonheight + 15);
        Button B3 = new Button();
        B3.setPrefHeight(buttonheight);
        B3.setPrefWidth(buttonwidth);
        setPos(B3, buttonwidth*2 + 20, buttonheight + 15);
        Button C1 = new Button();
        C1.setPrefHeight(buttonheight);
        C1.setPrefWidth(buttonwidth);
        setPos(C1, 10, buttonheight*2 + 20);
        Button C2 = new Button();
        C2.setPrefHeight(buttonheight);
        C2.setPrefWidth(buttonwidth);
        setPos(C2, buttonwidth + 15, buttonheight*2 + 20);
        Button C3 = new Button();
        C3.setPrefHeight(buttonheight);
        C3.setPrefWidth(buttonwidth);
        setPos(C3, buttonwidth*2 + 20, buttonheight*2 + 20);
        output = new TextArea();
        output.setWrapText(true);
        output.setPrefHeight(1);
        output.setPrefWidth(width-10);
        output.setText("Spieler 1 am Zug");
        setPos(output, 10, height-30);

        grid.getChildren().addAll(A1, A2, A3, B1, B2, B3, C1, C2, C3, output);
        Scene scene = new Scene(grid, width, height);
        stage.setScene(scene);

        return stage;
    }

    public static void launchGame(Client client, String username) {
        createStage(client).show();
    }

    public static void launchGame(Client client) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Tictactoe");

        AnchorPane all = new AnchorPane();

        Label title = new Label("Wen willst du herausfordern?");
        setPos(title, 30, 30);

        TextField input =  new TextField();
        setPos(input, 50, 80);

        Button accept = new Button("ok");
        setPos(accept, 200, 80);

        all.getChildren().addAll(title, input, accept);

        accept.setOnAction(actionEvent -> {
            //Stage waitingStage = waiting();
            //waitingStage.show();
            String ja = client.getFromServer("ttt", "ttt:CLNINI:"+input.getText());
            //waitingStage.close();
            System.out.println(ja);
            if (ja.split(":")[1].equals("CLNACC")) {
                System.out.println("received challange accept!!!!");
                launchGame(client, input.getText());
            }
            if (ja.split(":")[1].equals("CLNREJ")) {
                System.out.println("get fucked noob");
                Login.errorMessage("Deine Herausforderung wurde von " + input.getText() + " abgelehnt!");
            }

            stage.close();
        });

        Scene scene = new Scene(all, 280, 120);
        stage.setScene(scene);

        stage.show();
    }

    public static void request(Client client, String challenger) {
        try {
            TictactoeRequest request = new TictactoeRequest(client, challenger);
            request.start(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage waiting() {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Tictactoe");

        AnchorPane all = new AnchorPane();

        Label title = new Label("Warten auf eine Antwort");
        setPos(title, 30, 30);

        all.getChildren().addAll(title);

        Scene scene = new Scene(all, 280, 120);
        stage.setScene(scene);

        return stage;
    }
}
