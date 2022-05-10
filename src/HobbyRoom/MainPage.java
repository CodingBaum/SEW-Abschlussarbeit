package HobbyRoom;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        setPos(input, 0, (height/8)*7);

        //erstellt den Button um die Nachricht zu versenden
        Button send = new Button("send");
        send.setStyle(
                "-fx-padding: 5;"
        );
        setPos(send, width/1.53, (height/8)*7+5);
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
                    args = Arrays.stream(args).filter(x -> !x.equals(finalCommand)).collect(Collectors.toList()).toArray();
                }

                if (Client.commands.containsKey(command)) {
                    Client.commands.get(command).apply(args);
                } else {
                    output("[ERROR] Dieser Befehl existiert nicht!");
                }
            } else {
                user.writeToServer(input.getText());
                output("[PUBLIC] DU: " + input.getText());
            }

            input.setText("");
        });

        //Falls fenster geschlossen wird, wird Client disconnected
        stage.setOnCloseRequest(closeEvent -> {
            user.disconnect();
        });

        //Chatlog oben
        output = new TextArea();
        output.setWrapText(true);
        output.setPrefHeight(height/1.5);

        //erstellt eine VBox um den rand rechts zu erstellen
        VBox v = new VBox();
        v.setPrefHeight(height);
        v.setPrefWidth(width-width/1.4);
        setPos(v,width/1.4, 0);
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
        setPos(bosna, width/1.3, 10);
        bosna.setGraphic(new ImageView(new Image("res/minesweeper.png")));
        bosna.setOnAction(actionEvent -> {
            System.out.println("Launche Minesweeper");
        });

        //Button für Tic Tac Toe
        Button ttt = new Button();
        setPos(ttt, width/1.3, 10+height/3);
        ttt.setGraphic(new ImageView(new Image("res/ttt.png")));

        main.getChildren().addAll(input, output, send, v, bosna, ttt);

        Scene scene = new Scene(main, width, height);
        stage.setScene(scene);
        return stage;
    }

    public static void output(String s) {
        if (output == null) return;
        //gibt nur ein einfaches /n hinter den text damit nicht alles in einer Zeile ist
        output.appendText(s + "\n");
    }
}
