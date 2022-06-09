package HobbyRoom.Games.TicTacToe;

import HobbyRoom.Client;
import javafx.application.Platform;

public class TictactoeServerInputHandler extends Thread {
    private String prefix = "ttt";

    private final String msg;
    private final Client client;

    public TictactoeServerInputHandler(String msg, Client client) {
        this.msg = msg;
        this.client = client;
    }

    @Override
    public void run() {
        String response = client.getFromServer(prefix, msg);
        Platform.runLater(() -> Tictactoe.setField(response.split(":")[2]));
    }
}
