package HobbyRoom;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

import static HobbyRoom.Client.setPos;

public class MainPage {
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

        main.getChildren().addAll(input);

        Scene scene = new Scene(main, width, height);
        stage.setScene(scene);
        return stage;
    }
}
