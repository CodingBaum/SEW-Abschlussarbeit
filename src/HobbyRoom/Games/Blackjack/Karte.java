package HobbyRoom.Games.Blackjack;

public class Karte {
    private final String name;
    private int value;

    public Karte(String name) {
        this.name = name;
        name = name.substring(0, name.indexOf('-'));
        switch (name) {
            case "ASS" -> value = 11;
            case "König", "Dame", "Bube", "Zehn" -> value = 10;
            case "Neun" -> value = 9;
            case "Acht" -> value = 8;
            case "Sieben" -> value = 7;
            case "Sechs" -> value = 6;
            case "Fuenf" -> value = 5;
            case "Vier" -> value = 4;
            case "Drei" -> value = 3;
            case "Zwei" -> value = 2;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
