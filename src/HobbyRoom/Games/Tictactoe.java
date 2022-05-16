package HobbyRoom.Games;

import HobbyRoom.Client;
import HobbyRoom.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static HobbyRoom.Client.setPos;

public class Tictactoe {

    public static Stage createStage(Client client) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Tictactoe");

        GridPane grid = new GridPane();

        Scene scene = new Scene(grid, 600, 600);
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
