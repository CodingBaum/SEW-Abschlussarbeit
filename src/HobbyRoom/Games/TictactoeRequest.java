package HobbyRoom.Games;

import HobbyRoom.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static HobbyRoom.Client.setPos;

public class TictactoeRequest extends Application {
    private Client client;
    private String challenger;

    public TictactoeRequest(Client client, String challenger) {
        this.client = client;
        this.challenger = challenger;
    }

    @Override
    public void start(Stage stage) {
        stage = new Stage();

        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Tictactoe");

        AnchorPane all = new AnchorPane();

        Label title = new Label("Du wurdest von " + challenger + " zu tictactoe herausgefordert!");
        setPos(title, 30, 30);

        Button accept =  new Button("annehmen");
        setPos(accept, 50, 80);

        Button reject = new Button("ablehnen");
        setPos(reject, 200, 80);

        all.getChildren().addAll(title, reject, accept);

        Stage finalStage = stage;
        accept.setOnAction(actionEvent -> {
            client.writeToServer("ttt:CLNACC:"+challenger);
            Tictactoe.createStage(client, false);
            finalStage.close();
        });

        Stage finalStage1 = stage;
        reject.setOnAction(actionEvent -> {
            client.writeToServer("ttt:CLNREJ:"+challenger);
            finalStage1.close();
        });

        Scene scene = new Scene(all, 350, 120);
        stage.setScene(scene);

        stage.show();
    }
}
