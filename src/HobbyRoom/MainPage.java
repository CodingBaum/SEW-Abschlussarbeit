package HobbyRoom;

import HobbyRoom.Games.TicTacToe.Tictactoe;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import static HobbyRoom.Client.setPos;

public class MainPage {
    private static TextArea output;

    public static Stage mainStage(Client user){
        Stage stage = new Stage();
        stage.setResizable(false);

        stage.setTitle("Angemeldet als: " + user.getName());

        AnchorPane main = new AnchorPane();

        //finde die Höhe und Breite des screens
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = (screenSize.getHeight() -50)/2;
        double width = 700;

        //Input feld ganz unten
        TextArea input = new TextArea();
        input.setPrefWidth(width/1.5-20);
        input.setPrefHeight(height/8);
        input.setPromptText("Nachricht");
        setPos(input, 2, (height/8)*6.5);

        //erstellt den Button um die Nachricht zu versenden
        Button send = new Button("send");
        send.setStyle(
                "-fx-padding: 5;"
        );
        setPos(send, width/1.53, (height/8)*6.5+10);
        send.setOnAction(actionEvent -> {
            String in = input.getText();

            if (in.startsWith("/")) {
                in = in.substring(1);

                Object[] args = new Object[0];
                String command = in;

                if (in.contains(" ")) {
                    command = in.split(" ")[0];
                    args = in.split(" ");
                    String finalCommand = command;
                    args = Arrays.stream(args).filter(x -> !x.equals(finalCommand)).toList().toArray();
                }

                if (Client.commands.containsKey(command)) {
                    Client.commands.get(command).apply(args);
                } else {
                    output("[ERROR] Dieser Befehl existiert nicht!");
                }
            } else {
                user.writeToServer("msg:"+user.getName()+":"+input.getText());
                output("[PUBLIC] DU: " + input.getText());
            }

            input.setText("");
        });

        //Falls fenster geschlossen wird, wird Client disconnected
        stage.setOnCloseRequest(closeEvent -> user.disconnect());

        //Chatlog oben
        output = new TextArea();
        output.setWrapText(true);
        output.setPrefHeight(height/1.5);
        setPos(output, 2, 2);

        //erstellt eine VBox um den rand rechts zu erstellen
        VBox v = new VBox();
        v.setPrefHeight(height);
        v.setPrefWidth(95);
        setPos(v,600, 0);
        //benutzt fxcss um unter anderen eine border zu erstellen
        v.setStyle(
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;"
        );

        //Button um Minesweeper zu starten
        Button bosna = new Button();
        setPos(bosna, 615, 10);
        bosna.setGraphic(new ImageView(new Image("res/minesweeper.png", 50, 50, true, true)));
        bosna.setOnAction(actionEvent -> System.out.println("Launche Minesweeper"));

        //Button für Tic Tac Toe
        Button ttt = new Button();
        setPos(ttt, 615, 80);
        ttt.setGraphic(new ImageView(new Image("res/ttt.png", 50, 50, true, true)));
        ttt.setOnAction(actionEvent -> Tictactoe.launchGame(user));

        //Blackjack Button
        Button bj = new Button();
        setPos(bj, 615, 150);
        bj.setGraphic(new ImageView(new Image("res/bj.png", 50, 50, true, true)));
        bj.setOnAction(actionEvent -> Platform.runLater(() -> {
            HobbyRoom.Games.Blackjack.Main.startGame();
        }));

        main.getChildren().addAll(input, output, send, v, bosna, ttt, bj);

        MenuBar menuBar = new MenuBar();
        Menu verwalten = new Menu("Verwalten");
        Menu info = new Menu("Info");
        menuBar.getMenus().addAll(verwalten, info);

        MenuItem exit = new MenuItem("Server verlassen");
        exit.setOnAction(event -> Client.commands.get("quit").apply(null));

        MenuItem joinRoom = new MenuItem("Raum beitreten");
        MenuItem createRoom = new MenuItem("Raum erstellen");
        MenuItem leaveRoom = new MenuItem("Raum verlassen");
        verwalten.getItems().addAll(joinRoom, createRoom, leaveRoom, exit);

        MenuItem listUsers = new MenuItem("Benutzer anzeigen");
        listUsers.setOnAction(event -> {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            newStage.setTitle("Benutzer:");
            newStage.setWidth(400);
            newStage.setHeight(300);

            AnchorPane all = new AnchorPane();

            String users = user.getFromServer("LIST", "CMD:list").split(":")[1].substring(1);
            users = users.substring(0, users.length()-1);

            users = users.replaceAll(";", "\n");

            TextArea list = new TextArea(users);
            list.setPrefHeight(newStage.getHeight());
            list.setPrefWidth(newStage.getWidth());

            setPos(list, -2, -2);

            all.getChildren().addAll(list);

            Scene scene = new Scene(all, 400, 300);
            newStage.setScene(scene);

            newStage.show();
        });

        info.getItems().addAll(listUsers);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(main);

        Scene scene = new Scene(borderPane, width, height);
        stage.setScene(scene);
        return stage;
    }



    public static void output(String s) {
        if (output == null) return;
        if (s.startsWith("msg:")) {
            output.appendText("[PUBLIC] " + s.split(":")[1] + ": " + s.split(":")[2] + "\n");
        } else if (s.startsWith("sysmsg:")) {
            output.appendText("[SYSTEM] " + s.split(":")[1] + "\n");
        } else {
                output.appendText(s + "\n");
        }
    }
}
