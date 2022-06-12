package HobbyRoom.Games.Minesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Minesweeper extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("GridPane Sample");
        stage.setMinWidth(500);
        stage.setMinHeight(500);

        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 500, 500); // w, h
        stage.setScene(scene);
        List<Cell> cells = new ArrayList<Cell>();
        Set<Integer> minesSet = generateMines();
        List<Integer> minesList = new ArrayList<Integer>(minesSet);
        System.out.println(minesList);
        int c = 0;

        for (int i = 0; i < 100; i++) {
            if (c == 14) {
                cells.add(new Cell());
                break;
            }
            if (i + 1 == minesList.get(c)) {
                cells.add(new Cell(1000));
                c++;
                continue;
            }
            cells.add(new Cell());
        }
        for (int i = cells.size()-1; i < 100; i++) {
            cells.add(new Cell());
        }
        revealAll(cells);
        Button[][] buttons = new Button[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                buttons[i][j] = new Button(cells.get(Integer.parseInt(i+""+j)).toString());
                buttons[i][j].setPrefSize(50,50);
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridPane.add(buttons[i][j],i,j);
            }
        }

        stage.show();
    }

    public static Set<Integer> generateMines() {
        Set<Integer> erg = new TreeSet<>();
        int random=0;
        while (true) {
            random = (int) (Math.random()*100+1);
            erg.add(random);
            if (erg.size() == 14) {
                break;
            }
        } return erg;
    }

    public static void revealAll(List<Cell> a) {
        for (int i = 0; i < a.size(); i++) {
            a.get(i).revealed = true;
        }
    }
}
