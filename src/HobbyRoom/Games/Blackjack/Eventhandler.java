package HobbyRoom.Games.Blackjack;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

public class Eventhandler {
    public static boolean bidLock = false;

    //----------------------------------------------------------HIT EVENT------------------------------------------------------------
    public static void hitEvent(Button hit, Label text, ImageView[] k, GridPane grid, Button reset, Button stand, Stage bigStage, Button insurance) {
        Button restart = new Button("next round");
        Button exit = new Button("exit");

        Stage stage = stageBuilder(restart, exit, "load", "BUST! Du hast dich überkauft!");

        restart.setOnAction(event -> {
            reset.fire();
            stage.close();
        });
        exit.setOnAction(event -> {
            stage.close();
            bigStage.close();
        });

        hit.setOnAction(actionEvent -> {
            Main.PLAYER.abheben(Main.DECK.karteGeben());
            text.setText("\nDeine Chips: " + Main.PLAYER.getChips() +"\nDein Bid: " + Main.PLAYER.getBid() + "\nDein Kartenwert: " + Main.PLAYER.getValues() +  "\nDeine Karten: ");
            k[Main.PLAYER.getKarten().length-1] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/" + Main.PLAYER.getKarten()[Main.PLAYER.getKarten().length-1].getName() + ".png", 100, 100, true, true));
            grid.add(k[Main.PLAYER.getKarten().length-1], Main.PLAYER.getKarten().length-1, 5);
            if (Main.PLAYER.getBust()) {
                stage.show();
                unlockButtons(reset);
                lockButtons(stand, hit);
            } else if (Main.PLAYER.getValues() == 21) {
                stand.fire();
            }

            String musicFile = "src\\HobbyRoom\\Games\\Blackjack\\sounds\\card-deal-sound.mp3";
            Media media = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        });
    }

    //----------------------------------------------------------STAND EVENT------------------------------------------------------------
    public static void standEvent(Button stand, ImageView[] k1, GridPane grid, Button reset, Button hit, Label dealer, Stage bigStage, Button insurance, Label insuranceText) {
        Button restart = new Button("next round");
        Button exit = new Button("exit");

        AtomicReference<Stage> stage = new AtomicReference<>(stageBuilder(restart, exit, "load", "BUST! Du hast dich überkauft!"));

        restart.setOnAction(event -> {
            reset.fire();
            stage.get().close();
        });
        exit.setOnAction(event -> {
            stage.get().close();
            bigStage.close();
        });


        stand.setOnAction(actionEvent -> {
            while (Main.DEALER.getValues() < 17) {
                Main.DEALER.abheben(Main.DECK.karteGeben());
                k1[Main.DEALER.getKarten().length-1] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/" + Main.DEALER.getKarten()[Main.DEALER.getKarten().length-1].getName() + ".png", 100, 100, true, true));
                grid.add(k1[Main.DEALER.getKarten().length-1], Main.DEALER.getKarten().length-1, 7);
            }
            Main.PLAYER.stand();
            unlockButtons(reset);
            lockButtons(stand, hit, insurance);

            if (Main.DEALER.getBlackjack() && !insuranceText.getText().equals("")) {
                stage.set(stageBuilder(restart, exit, "", "DEALER HAT EIN BLACKJACK! Insurance wird ausgezahlt!"));
                Main.PLAYER.changeChips(Main.PLAYER.getChips()+(Main.PLAYER.getBid()*2));
                stage.get().show();
                return;
            }

            if (Main.DEALER.getBust()) {
                Main.PLAYER.changeChips(Main.PLAYER.getChips() + Main.PLAYER.getBid() * 2);
                stage.set(stageBuilder(restart, exit, "", "GEWONNEN!"));
                stage.get().show();
            } else if (Main.PLAYER.getValues() > Main.DEALER.getValues()) {
                Main.PLAYER.changeChips(Main.PLAYER.getChips() + Main.PLAYER.getBid() * 2);
                stage.set(stageBuilder(restart, exit, "", "GEWONNEN!"));
                stage.get().show();
            } else if (Main.PLAYER.getValues() < Main.DEALER.getValues()) {
                stage.set(stageBuilder(restart, exit, "", "VERLOREN!"));
                stage.get().show();
            } else if (Main.PLAYER.getValues() == Main.DEALER.getValues()) {
                stage.set(stageBuilder(restart, exit, "", "GLEICHSTAND!"));
                stage.get().show();
                Main.PLAYER.changeChips(Main.PLAYER.getBid()+Main.PLAYER.getChips());
            }
            dealer.setText("\nDealer Kartenwert: " + Main.DEALER.getValues() + "\nDealer Karten: ");
        });
    }

    //----------------------------------------------------------BID EVENT------------------------------------------------------------
    public static void bidEvent(Button bid, Label bidArea, Label err, Label text, ImageView[] k, ImageView[] k1, GridPane grid, ImageView chip10, ImageView chip50, ImageView chip100, Button allIn, Button stand, Button hit, Button reset, Stage bigStage, Button resetBid, Button insurance) {
        Button restart = new Button("next round");
        Button exit = new Button("exit");

        Stage stage = stageBuilder(restart, exit, "blackjack", "BLACKJACK!");

        restart.setOnAction(event -> {
            reset.fire();
            stage.close();
        });
        exit.setOnAction(event -> {
            stage.close();
            bigStage.close();
        });

        bid.setOnAction(actionEvent -> {
            if (bidArea.getText() != null && !bidArea.getText().equals("")) {
                err.setText("");
                int amount = Integer.parseInt(bidArea.getText());
                if (amount <= 0) {
                    err.setText("Bitte setzen Sie einen Wert größer als 0!");
                    return;
                }
                Main.PLAYER.setBid(amount);
                text.setText("\nDeine Chips: " + Main.PLAYER.getChips() +"\nDein Bid: " + Main.PLAYER.getBid() + "\nDein Kartenwert: " + Main.PLAYER.getValues() +  "\nDeine Karten: ");
                k[0] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/" + Main.PLAYER.getKarten()[0].getName() + ".png", 100, 100, true, true));
                k[1] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/" + Main.PLAYER.getKarten()[1].getName() + ".png", 100, 100, true, true));
                grid.add(k[0], 0, 5);
                grid.add(k[1], 1, 5);
                k1[0] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/" + Main.DEALER.getKarten()[0].getName() + ".png", 100, 100, true, true));
                grid.add(k1[0], 0, 7);
                lockButtons(bid, allIn);
                bidLock = true;
                if (Main.DEALER.getKarten()[0].getName().contains("ASS")) {
                    unlockButtons(stand, hit, insurance);
                } else {
                    unlockButtons(stand, hit);
                }

                String musicFile = "src\\HobbyRoom\\Games\\Blackjack\\sounds\\chip-sound-submit.mp3";
                Media media = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();

                if (Main.PLAYER.getBlackjack()) {
                    Main.PLAYER.changeChips(Main.PLAYER.getBid() * 3 + Main.PLAYER.getChips());
                    stage.show();
                    unlockButtons(reset);
                    lockButtons(stand, hit);
                }

                setInvisible(chip10, chip100, chip50, allIn, bid, bidArea, resetBid);
            } else {
                err.setText("Bitte geben Sie einen Wert ein!");
            }
        });

        chip10.setOnMouseClicked(event -> {
            if (!bidLock) {
                if (Integer.parseInt(bidArea.getText())+10 > Main.PLAYER.getChips()) {
                    bidArea.setText(Main.PLAYER.getChips()+"");
                } else {
                    bidArea.setText((Integer.parseInt(bidArea.getText())+10)+"");
                }
                String musicFile = "src\\HobbyRoom\\Games\\Blackjack\\sounds\\chip-sound-10.mp3";
                Media media = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }
        });
        chip50.setOnMouseClicked(event -> {
            if (!bidLock) {
                if (Integer.parseInt(bidArea.getText())+50 > Main.PLAYER.getChips()) {
                    bidArea.setText(Main.PLAYER.getChips()+"");
                } else {
                    bidArea.setText((Integer.parseInt(bidArea.getText())+50)+"");
                }
                String musicFile = "src\\HobbyRoom\\Games\\Blackjack\\sounds\\chip-sound-50.mp3";
                Media media = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }
        });
        chip100.setOnMouseClicked(event -> {
            if (!bidLock) {
                if (Integer.parseInt(bidArea.getText())+100 > Main.PLAYER.getChips()) {
                    bidArea.setText(Main.PLAYER.getChips()+"");
                } else {
                    bidArea.setText((Integer.parseInt(bidArea.getText())+100)+"");
                }

                String musicFile = "src\\HobbyRoom\\Games\\Blackjack\\sounds\\chip-sound-100.mp3";
                Media media = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }

        });
        allIn.setOnAction(event -> {
            bidArea.setText(Main.PLAYER.getChips()+"");
            bid.fire();
        });
        resetBid.setOnAction(event -> {
            bidArea.setText("0");
            String musicFile = "src\\HobbyRoom\\Games\\Blackjack\\sounds\\chip-sound-submit.mp3";
            Media media = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        });
    }

    //----------------------------------------------------------RESET EVENT------------------------------------------------------------
    public static void resetEvent(Button reset, Label err, Label bidArea, Label text, Label dealer, ImageView[] k, ImageView[] k1, GridPane grid, Button stand, Button hit, Button bid, Button allIn, Stage stage, ImageView chip10, ImageView chip50, ImageView chip100, Button resetBid, Label insuranceText) {
        reset.setOnAction(event -> {
            Main.DECK = new Deck();
            Main.PLAYER.reset();
            Main.DEALER.reset();
            Main.PLAYER.abheben(Main.DECK.karteGeben());
            Main.PLAYER.abheben(Main.DECK.karteGeben());
            Main.DEALER.abheben(Main.DECK.karteGeben());
            insuranceText.setText("");
            err.setText("");
            bidArea.setText("0");
            text.setText("\nDeine Chips: " + Main.PLAYER.getChips() +"\nDein Bid: " + Main.PLAYER.getBid() + "\n\nDeine Karten: ");
            dealer.setText("\n\nDEALER Karten: ");
            for (int i = 0; i < k.length; i++) {
                setInvisible(k[i], k1[i]);
            }
            k[0] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
            k[1] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
            k1[0] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
            k1[1] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
            grid.add(k1[1], 1, 7);
            grid.add(k1[0], 0, 7);
            grid.add(k[0], 0, 5);
            grid.add(k[1], 1, 5);

            lockButtons(stand, hit, reset);
            if (reset.getText().equals("restart")) {
                reset.setText("next round");
                Main.PLAYER.changeChips(1000);
                text.setText("\nDeine Chips: " + Main.PLAYER.getChips() +"\nDein Bid: " + Main.PLAYER.getBid() + "\n\nDeine Karten: ");
            }
            if (Main.PLAYER.getChips() == 0) {
                Button ja = new Button("restart");
                ja.setMinSize(60, 40);

                Button nein = new Button("exit");
                nein.setMinSize(60, 40);

                Stage resetStage = stageBuilder(ja, nein, "Game Over!", "Keine Chips mehr! Game Over!");
                resetStage.show();
                nein.setOnAction(event1 -> {
                    resetStage.close();
                    stage.close();
                });
                ja.setOnAction(event1 -> {
                    reset.setText("next round");
                    Main.PLAYER.changeChips(1000);
                    text.setText("\nDeine Chips: " + Main.PLAYER.getChips() +"\nDein Bid: " + Main.PLAYER.getBid() + "\n\nDeine Karten: ");
                    resetStage.close();
                    bidLock = false;
                    unlockButtons(bid, allIn);
                    setVisible(chip10, chip50, chip100, allIn, bid, bidArea, resetBid);
                });
                lockButtons(bid);
                reset.setText("restart");
                return;
            }
            bidLock = false;
            unlockButtons(bid, allIn);
            setVisible(chip10, chip50, chip100, allIn, bid, bidArea, resetBid);
        });
    }

    //----------------------------------------------------------SAVE EVENT------------------------------------------------------------
    public static void saveEvent(Button save, Button bid, Button hit) {

        Button ja = new Button();
        ja.setMinSize(60, 40);
        ja.setGraphic(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/checkmark.png", 60, 40, true, true)));

        Button nein = new Button();
        nein.setGraphic(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/cross.png", 60, 40, true, true)));
        nein.setMinSize(60, 40);

        Stage saveStage = stageBuilder(ja, nein, "save", "Wollen Sie den letzten \nSpeicherstand überschreiben?");

        save.setOnAction(event -> {
            if (!hit.isDisabled()) {
                saveStage.show();
            } else {
                Stage stage = new Stage();
                stage.setTitle("error");
                stage.setWidth(200);
                stage.setHeight(150);
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);

                Label text = new Label("Bitte speichern Sie den Spielstand zu einem validen Zeitpunkt ab!");
                text.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
                text.setWrapText(true);

                Button ok = new Button();
                ok.setGraphic(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/checkmark.png", 60, 40, true, true)));

                ok.setOnAction(event1 -> {
                    stage.close();
                });

                BorderPane border = new BorderPane();
                border.setTop(text);
                border.setCenter(ok);

                GridPane.setColumnSpan(text, 2);

                border.setStyle("-fx-background-image: url('HobbyRoom/Games/Blackjack/pictures/background.jpg'); -fx-background-size: cover; -fx-background-position: center");

                Scene scene = new Scene(border);
                stage.setScene(scene);

                stage.show();
            }
        });

        ja.setOnAction(event -> {
            saveFile();
            saveStage.close();
        });

        nein.setOnAction(event -> {
            saveStage.close();
        });
    }

    //----------------------------------------------------------LOAD EVENT------------------------------------------------------------
    public static void loadEvent(Button load, ImageView[] k, ImageView[] k1, Label text, GridPane grid, Button bid, Button hit, Button stand, Button allIn, ImageView chip10, ImageView chip50, ImageView chip100, Label bidArea, Button resetBid) {
        Stage loadStage = new Stage();
        loadStage.setTitle("load");
        loadStage.setWidth(500);
        loadStage.setHeight(500);
        loadStage.setResizable(false);
        loadStage.setAlwaysOnTop(true);
        loadStage.initStyle(StageStyle.UNDECORATED);

        Button ja = new Button();
        ja.setMinSize(60, 40);
        ja.setGraphic(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/checkmark.png", 60, 40, true, true)));

        Button nein = new Button();
        nein.setMinSize(60, 40);
        nein.setGraphic(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/cross.png", 60, 40, true, true)));

        Label confirmation = new Label("Wollen Sie folgenden Speicherstand laden?");
        confirmation.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        load.setOnAction(event -> {
            String[] line = loadFile();
            Label description = new Label();
            description.setText("Chips: "+line[0]+"\nBid: "+line[1]+"\nDeine Karten: ");
            description.setStyle(" -fx-text-fill: white");
            HBox playerKarten = new HBox();
            for (int i = 0; i < line[2].split("/").length; i++) {
                playerKarten.getChildren().add(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/"+line[2].split("/")[i]+".png", 100, 100, true, true)));
            }
            HBox dealerKarten = new HBox();
            for (int i = 0; i < line[3].split("/").length; i++) {
                dealerKarten.getChildren().add(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/"+line[3].split("/")[i]+".png", 100, 100, true, true)));
            }

            Label label = new Label("Dealer Karten: ");
            label.setStyle("-fx-text-fill: white");

            VBox vBox = new VBox();
            vBox.getChildren().addAll(confirmation, description, playerKarten, label, dealerKarten, ja, nein);
            vBox.setSpacing(10);
            vBox.setStyle("-fx-background-image: url('HobbyRoom/Games/Blackjack/pictures/background.jpg'); -fx-background-size: cover; -fx-background-position: center");
            Scene loadScene = new Scene(vBox);
            loadStage.setScene(loadScene);
            loadStage.show();
        });

        ja.setOnAction(event -> {
            String[] line = loadFile();
            //------resetting the PLAYER------------
            Main.PLAYER.reset();
            Main.DEALER.reset();
            //------------setting values for PLAYER----------------------
            Main.PLAYER.changeChips(Integer.parseInt(line[0])+Integer.parseInt(line[1]));
            Main.PLAYER.setBid(Integer.parseInt(line[1]));
            //------------------setting PLAYER Cards-----------------------
            String[] cardNames = line[2].split("/");
            for (String cardName : cardNames) {
                Main.PLAYER.abheben(new Karte(cardName));
            }
            //------------------setting Dealer Cards-----------------------
            String[] dealerCards = line[3].split("/");
            for (String dealerCard : dealerCards) {
                Main.DEALER.abheben(new Karte(dealerCard));
            }

            text.setText("\nDeine Chips: " + Main.PLAYER.getChips() +"\nDein Bid: " + Main.PLAYER.getBid() + "\nDein Kartenwert: " + Main.PLAYER.getValues() +  "\nDeine Karten: ");
            for (int i = 0; i < 10; i++) {
                if (i < Main.PLAYER.getKarten().length) {
                    setInvisible(k[i], k1[i]);
                    k[i] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/" + Main.PLAYER.getKarten()[i].getName() + ".png", 100, 100, true, true));
                    grid.add(k[i], i, 5);
                } else {
                    setInvisible(k[i], k1[i]);
                    k[i] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
                    grid.add(k[i], i, 5);
                    k[i].setVisible(false);
                }
            }
            k1[0] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/" + Main.DEALER.getKarten()[0].getName() + ".png", 100, 100, true, true));
            k1[1] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
            grid.add(k1[0], 0, 7);
            grid.add(k1[1], 1, 7);

            setInvisible(chip10, chip50, chip100, allIn, bidArea, bid, resetBid);
            unlockButtons(hit, stand);

            loadStage.close();
        });

        nein.setOnAction(event -> loadStage.close());
    }
    //----------------------------------------------------------INSURANCE EVENT------------------------------------------------------------
    public static void insuranceEvent(Button insurance, Label insuranceText) {
        insurance.setOnAction(event -> {
            int bid = Main.PLAYER.getBid();
            if (bid > Main.PLAYER.getChips()) {
                Stage stage = new Stage();
                stage.setTitle("error");
                stage.setWidth(200);
                stage.setHeight(150);
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);

                Label text = new Label("Sie haben nicht genug Chips!");
                text.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
                text.setWrapText(true);

                Button ok = new Button();
                ok.setGraphic(new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/checkmark.png", 60, 40, true, true)));

                ok.setOnAction(event1 -> {
                    stage.close();
                });

                BorderPane border = new BorderPane();
                border.setTop(text);
                border.setCenter(ok);

                GridPane.setColumnSpan(text, 2);

                border.setStyle("-fx-background-image: url('HobbyRoom/Games/Blackjack/pictures/background.jpg'); -fx-background-size: cover; -fx-background-position: center");

                Scene scene = new Scene(border);
                stage.setScene(scene);

                stage.show();
            } else {
                insuranceText.setText("Insurance gesetzt im Wert von: " + Main.PLAYER.getBid());
                lockButtons(insurance);
                Main.PLAYER.changeChips(Main.PLAYER.getChips() - Main.PLAYER.getBid());
            }
        });
    }


    //--------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------- METHODS ------------------------------------------------------------

    private static void lockButtons(Button ... b) {
        for (Button button : b) {
            button.setDisable(true);
        }
    }

    private static void unlockButtons(Button ... b) {
        for (Button button : b) {
            button.setDisable(false);
        }
    }

    private static void setInvisible(Node ... n) {
        for (Node node : n) {
            node.setVisible(false);
        }
    }

    private static void setVisible(Node ... n) {
        for (Node node : n) {
            node.setVisible(true);
        }
    }

    private static void saveFile() {
        try (BufferedWriter out = Files.newBufferedWriter(Paths.get(Main.savePath))) {
            //-----------------PLAYER Cards----------------------
            StringBuilder cardsP = new StringBuilder();
            for (int i = 0; i < Main.PLAYER.getKarten().length; i++) {
                cardsP.append(Main.PLAYER.getKarten()[i].getName()).append("/");
            }
            if (cardsP.length()!=0) {
                cardsP = new StringBuilder(cardsP.substring(0, cardsP.length() - 1));
            }
            //-----------------DEALER Cards----------------------
            StringBuilder cardsD = new StringBuilder();
            for (int i = 0; i < Main.DEALER.getKarten().length; i++) {
                cardsD.append(Main.DEALER.getKarten()[i].getName()).append("/");
            }
            if (cardsD.length()!=0) {
                cardsD = new StringBuilder(cardsD.substring(0, cardsD.length() - 1));
            }
            //------------------writing all values--------------------
            out.write(Main.PLAYER.getChips()+":"+Main.PLAYER.getBid()+":"+cardsP+":"+cardsD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] loadFile() {
        //------------renewing the deck--------------
        Main.DECK = new Deck();
        try(BufferedReader in = Files.newBufferedReader(Paths.get(Main.savePath))) {
            return in.readLine().split(":");
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    private static Stage stageBuilder(Button ja, Button nein, String title, String message) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setWidth(200);
        stage.setHeight(150);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);

        Label text = new Label(message);
        text.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        text.setWrapText(true);

        GridPane grid = new GridPane();
        grid.add(text, 0, 0);
        grid.add(ja, 0, 1);
        grid.add(nein, 1, 1);

        grid.setHgap(10);
        grid.setVgap(10);

        GridPane.setColumnSpan(text, 2);

        grid.setStyle("-fx-background-image: url('HobbyRoom/Games/Blackjack/pictures/background.jpg'); -fx-background-size: cover; -fx-background-position: center");

        Scene scene = new Scene(grid);
        stage.setScene(scene);

        return stage;
    }
}
