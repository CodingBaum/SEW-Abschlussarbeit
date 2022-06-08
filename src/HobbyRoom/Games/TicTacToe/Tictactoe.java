package HobbyRoom.Games.TicTacToe;

import HobbyRoom.Client;
import HobbyRoom.Login;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

import static HobbyRoom.Client.setPos;

public class Tictactoe {
    private static TextArea output = new TextArea();
    private static Button[][] obama = new Button[3][3];

    private static int buttonheight = 100;
    private static int buttonwidth = 100;

    public static void setField(String input){
        Button current = obama[Integer.parseInt(input.charAt(0)+"")][Integer.parseInt(input.charAt(1)+"")];

        current.setDisable(true);
        current.setGraphic(new ImageView(new Image("HobbyRoom/Games/TicTacToe/pics/ttt2.png", 80, 80, true, true)));

        for (Button[] buttons : obama) {
            for (Button button : buttons) {
                if (button.getGraphic() == null) {
                    button.setDisable(false);
                }
            }
        }
    }

    public static void createStage(Client client, boolean first) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Tictactoe");


        AnchorPane grid = new AnchorPane();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = 350;
        double width = 350;

        for (int i = 0; i<obama.length;i++) {
            for (int j = 0; j < obama[i].length; j++) {
                obama[i][j] = new Button();
                obama[i][j].setPrefHeight(buttonheight);
                obama[i][j].setPrefWidth(buttonwidth);
                int finalI = i;
                int finalJ = j;
                obama[i][j].setOnAction(actionEvent -> {
                    output.setText("Gegner am Zug");
                    for (Button[] buttons : obama) {
                        for (Button button : buttons) {
                            if (button.getGraphic() == null) {
                                button.setDisable(true);
                            }
                        }
                    }
                    obama[finalI][finalJ].setGraphic(new ImageView(new Image("HobbyRoom/Games/TicTacToe/pics/ttt1.png", buttonwidth*0.8, buttonheight*0.8, true, true)));
                    new TictactoeServerInputHandler("ttt:SET:"+ finalI + finalJ, client).start();
                    output.setText("Du bist am Zug");
                });
            }
        }



        setPos(obama[0][0], 10, 10);
        setPos(obama[0][1], buttonwidth + 15, 10);
        setPos(obama[0][2], buttonwidth*2 + 20, 10);
        setPos(obama[1][0], 10, buttonheight + 15);
        setPos(obama[1][1], buttonwidth + 15, buttonheight + 15);
        setPos(obama[1][2], buttonwidth*2 + 20, buttonheight + 15);
        setPos(obama[2][0], 10, buttonheight*2 + 20);
        setPos(obama[2][1], buttonwidth + 15, buttonheight*2 + 20);
        setPos(obama[2][2], buttonwidth*2 + 20, buttonheight*2 + 20);
        output.setWrapText(true);
        output.setPrefHeight(1);
        output.setPrefWidth(width-10);
        output.setText("Spieler 1 am Zug");
        setPos(output, 10, height-30);

        grid.getChildren().addAll(obama[0][0], obama[0][1], obama[0][2], obama[1][0], obama[1][1], obama[1][2], obama[2][0], obama[2][1], obama[2][2], output);
        Scene scene = new Scene(grid, width, height);
        stage.setScene(scene);

        stage.show();

        if (!first) {
            enable(true, obama[0][0], obama[0][1], obama[0][2], obama[1][0], obama[1][1], obama[1][2], obama[2][0], obama[2][1], obama[2][2]);
            new TictactoeServerInputHandler("", client).start();
        }
    }

    public static void launchGame(Client client, String username, boolean first) {
        createStage(client, first);
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
            if (input.getText().equals(client.getName())) {
                Login.errorMessage("Du kannst dich nicht selbst herausfordern").show();
                return;
            }
            String ja = client.getFromServer("ttt", "ttt:CLNINI:"+input.getText());
            //waitingStage.close();
            if (ja.split(":")[1].equals("CLNACC")) {
                stage.close();
                launchGame(client, input.getText(), true);
            }
            if (ja.split(":")[1].equals("CLNREJ")) {
                stage.close();
                Login.errorMessage("Deine Herausforderung wurde von " + input.getText() + " abgelehnt!");
            }
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
        stage.setTitle("Warte auf Gegner");

        AnchorPane all = new AnchorPane();

        Label title = new Label("Warten auf eine Antwort");
        setPos(title, 30, 30);

        all.getChildren().addAll(title);

        Scene scene = new Scene(all, 280, 120);
        stage.setScene(scene);

        return stage;
    }

    public static void enable(boolean disable, Button ... buttons) {
        for (Button b :buttons) {
            b.setDisable(disable);
        }
    }
}
