package HobbyRoom;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

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
        input.setPrefWidth(width/1.5);
        input.setPrefHeight(height/8);
        setPos(input, 0, (height/8)*7);

        //erstellt den Button um die Nachricht zu versenden
        Button send = new Button("send");
        setPos(send, width/1.4, (height/8)*7);
        send.setOnAction(actionEvent -> {
            user.writeToServer(input.getText());
            output("[PUBLIC] DU: " + input.getText());

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


        main.getChildren().addAll(input, output, send);

        Scene scene = new Scene(main, width, height);
        stage.setScene(scene);
        return stage;
    }

    public static void output(String s) {
        if (output == null) return;

        output.appendText(s + "\n");
    }
}
