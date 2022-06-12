package HobbyRoom.Games.Minesweeper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class copy {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println("a.get("+i+").setOnAction(new EventHandler<ActionEvent>() {\n" +
                    "            @Override\n" +
                    "            public void handle(ActionEvent actionEvent) {\n" +
                    "                a.get("+i+").reveal();\n" +
                    "                if (a.get("+i+").c.value == 1000) {\n" +
                    "                    return;\n" +
                    "                }\n" +
                    "            }\n" +
                    "        });");
        }
    }
}
