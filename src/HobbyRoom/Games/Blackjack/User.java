package HobbyRoom.Games.Blackjack;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Karte> hand = new ArrayList<>();
    private boolean stand = false;
    private boolean bust = false;
    private boolean blackjack = false;
    private int chips;
    private int bid;

    public User(int chips) {
        this.chips = chips;
    }

    public void abheben(Karte k) {
        if (!bust && !blackjack && !stand) {
            hand.add(k);
            if (getValues() == 21 && hand.size() == 2) {
                blackjack = true;
            } else if (getValues() > 21) {
                for (Karte karte:hand) {
                    if (karte.getValue() == 11) {
                        karte.setValue(1);
                        return;
                    }
                }
                bust = true;
            }
        }
    }

    public void stand() {
        this.stand = true;
    }

    public void changeChips(int chipChange) {
        this.chips = chipChange;
    }

    public void setBid(int bid) {
        if (chips < bid) {
            this.bid += chips;
            chips = 0;
        } else {
            chips -= bid;
            this.bid += bid;
        }
    }

    public int getBid() {
        return this.bid;
    }
    public int getValues() {
        int erg = 0;
        for (Karte karte : hand) {
            erg += karte.getValue();
        }
        return erg;
    }
    public Karte[] getKarten() {
        return hand.toArray(new Karte[0]);
    }
    public boolean getStand() {
        return this.stand;
    }
    public int getChips() {
        return this.chips;
    }
    public boolean getBust() {
        return bust;
    }
    public boolean getBlackjack() {
        return blackjack;
    }

    public void reset() {
        bid = 0;
        stand = false;
        bust = false;
        blackjack = false;
        hand = new ArrayList<>();
    }

    @Override
    public String toString() {
        String erg = "Hand: \n";
        for (Karte karte:hand) erg += karte + "\n";
        erg+="\nValue: " + getValues();
        erg+="\nStand: " + stand;
        erg+="\nChips: " + chips;
        erg+="\nCurrent Bid: " + bid;
        erg+="\nBust: " + bust;
        erg+="\nBlackjack: " + blackjack;
        return erg;
    }
}
