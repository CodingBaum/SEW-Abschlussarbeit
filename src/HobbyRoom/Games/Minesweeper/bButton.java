package HobbyRoom.Games.Minesweeper;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class bButton extends Button {
    public static int IS_BOMB = 9;
    public static int IS_FLAG = 10;

    private static String[] COLORS = {
            "black", // leer
            "blue", // 1
            "darkgreen", // 2
            "red", // 3
            "purple", // 4
            "gold", // 5
            "darkblue", // 6
            "darkred", // 7
            "orange", // 8
            "", // 9 IS_BOMB
            "black"  // 10 IS_FLAG
    };

    int x;
    int y;
    Cell c;
    int n;

    public bButton(int x, int y, Cell c) {
        this.x = x;
        this.y = y;
        this.c = new Cell();
        this.c = c;
        colors();
    }

    public bButton() {
    }

    public bButton(int x, int y) {
        this.c = new Cell();
        this.x = x;
        this.y = y;
        colors();
    }

    public bButton(Cell cell) {
        this.c = new Cell();
        colors();
    }

    public void setC(Cell c) {
        this.c = c;
        colors();
        setText(c.toString());
    }

    @Override
    public String toString() {
        return this.c.toString();
    }

    public void reveal() {
        this.c.revealed = true;
        colors();
        setText(c.toString());
    }

    public void setValue(int value) {
        this.c.value = value;
        colors();
        setText(c.toString());
    }

    public void setFlag() {
        this.c.flag = true;
        colors();
        setText(c.toString());
    }

    public void unsetFlag() {
        this.c.flag = false;
        colors();
        setText(c.toString());
    }

    public void colors() {
        if (c.revealed == false) {
            setStyle("-fx-background-color: linear-gradient(#b6bbc2,#abaeb3);"+"-fx-border-color: gray;"+"-fx-border-radius: 4%;");
            return;
        }
        if (!c.flag) {
            setStyle("-fx-text-fill: "+COLORS[c.value]+";"+"-fx-font-family: VT323;"+"-fx-font-size: 150%;"+"-fx-font-weight: bold;");
        } else {
            setStyle("-fx-text-fill: black;" + "-fx-font-weight: bold;");
        }
        setText(c.toString());
    }
}
