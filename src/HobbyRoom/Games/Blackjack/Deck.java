package HobbyRoom.Games.Blackjack;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    public List<Karte> karten = new ArrayList<>();

    public Deck() {
        String[] a = {"Herz", "Karo", "Pik", "Kreuz"};
        String[] b = {"ASS", "König", "Dame", "Bube", "Zehn", "Neun", "Acht", "Sieben", "Sechs", "Fuenf", "Vier", "Drei", "Zwei"};

        for (String s : b) {
            for (String value : a) {
                karten.add(new Karte(s + "-" + value));
            }
        }
    }

    public Karte karteGeben() {
        if (karten.size() == 0) {
            return new Karte("ASS-Error!");
        }
        Karte erg = karten.get((int) (Math.random()*karten.size()));

        remove(erg);
        return erg;
    }

    public void remove(Karte k) {
        karten.remove(k);
    }
}
