package HobbyRoom.Games.Minesweeper;

public class Cell {
    int value;
    boolean flag=false;
    boolean revealed=false;

    public Cell() {

    }

    public Cell(int value) {
        this.value = value;
    }

    public Cell(int value, boolean revealed) {
        this.value = value;
        this.revealed = revealed;
    }



    public void reveal() {
        this.revealed = true;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public String toString() {
        if (flag) {
            return "\uD83C\uDFF4";
        }
        if (revealed) {
            if (value == 0) {
                return "";
            }
            if (value == 10) {
                return "\uD83D\uDCA3";
            }
            return value+"";
        }
        return "";
    }
}
