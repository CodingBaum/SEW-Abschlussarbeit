package HobbyRoom;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

import static HobbyRoom.Client.setPos;

public class MainPage {
    private static TextArea output;

    public static Stage mainStage(Client user){
        Stage stage = new Stage();
        stage.setResizable(false);
        AnchorPane main = new AnchorPane();

        //finde die Höhe und Breite des screens
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = screenSize.getHeight() -50;
        double width = 700;

        TextArea input = new TextArea();
        input.setPrefWidth(width/1.5);
        input.setPrefHeight(height/8);
        setPos(input, 0, (height/8)*7);

        Button send = new Button("send");
        setPos(send, width/1.4, (height/8)*7);

        send.setOnAction(actionEvent -> {
            try {
                user.writeToServer(input.getText());
                output("[PUBLIC] DU: " + input.getText());

                input.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
