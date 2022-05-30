package HobbyRoom.Games.Blackjack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    public static User DEALER = new User(1000);
    public static User PLAYER = new User(1000);
    public static Deck DECK = new Deck();
    public static String savePath = "src/HobbyRoom/Games/Blackjack/save.txt";

    public static void main(String[] args) {
        PLAYER.abheben(DECK.karteGeben());
        PLAYER.abheben(DECK.karteGeben());
        DEALER.abheben(DECK.karteGeben());

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Blackjack");
        stage.setHeight(590);
        stage.setWidth(700);
        stage.setResizable(false);

        //--------------------------------------------------FX Elemente----------------------------------------------------------

        Button hit = new Button("hit");
        Button stand = new Button("stand");
        Button bid = new Button("submit bid:");
        Button reset = new Button("next round");
        Button save = new Button("save");
        Button load = new Button("load");
        Button insurance = new Button("insurance");
        hit.setMinSize(60, 40);
        stand.setMinSize(60, 40);
        bid.setMinSize(76, 40);
        reset.setMinSize(60, 40);
        save.setMinSize(60, 40);
        load.setMinSize(60, 40);
        insurance.setMinSize(60, 40);

        Label text = new Label("\nDeine Chips: " + PLAYER.getChips() +"\nDein Bid: " + PLAYER.getBid() + "\n\nDeine Karten: ");
        text.setStyle("-fx-text-fill: white;");
        Label dealer = new Label("\n\nDealer Karten: ");
        dealer.setStyle("-fx-text-fill: white");
        Label err = new Label("");
        err.setFont(new Font(16));
        err.setWrapText(true);
        err.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        Label bidArea = new Label();
        bid.setMaxSize(60, 50);
        bidArea.setText("0");

        Label insuranceText = new Label("");
        insuranceText.setWrapText(true);
        insuranceText.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        bidArea.setStyle("-fx-text-fill: white");

        ImageView chip10 = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/chip10.png", 50, 50, true, true));

        ImageView chip50 = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/chip50.png", 50, 50, true, true));

        ImageView chip100 = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/chip100.png", 50, 50, true, true));

        Button allIn = new Button("All-In");
        allIn.setMinSize(60, 60);

        Button resetBid = new Button("reset Bid");
        resetBid.setMinSize(60, 60);

        ImageView chipHaufen = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Chip-haufen.png", 200, 150, true, true));

        //--------------------------------------------------Kartenlisten--------------------------------------------------------

        ImageView[] k = new ImageView[10];
        ImageView[] k1 = new ImageView[10];

        for (int i = 0; i < k.length; i++) {
            k[i] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
            k[i].setVisible(false);
            k1[i] = new ImageView(new Image("HobbyRoom/Games/Blackjack/pictures/Karte-Rückseite.png", 100, 100, true, true));
            k1[i].setVisible(false);
        }

        k[0].setVisible(true);
        k[1].setVisible(true);
        k1[0].setVisible(true);
        k1[1].setVisible(true);

        //-------------------------------------------------Grid Pane---------------------------------------------------------
        GridPane grid = new GridPane();
        grid.add(hit, 0, 0);
        grid.add(stand, 1, 0);
        grid.add(insurance, 2, 0);
        grid.add(insuranceText, 3, 0);

        grid.add(bid, 5, 0);
        grid.add(bidArea, 7, 0);
        grid.add(chip10, 9, 0);
        grid.add(chip50, 9, 1);
        grid.add(chip100, 9, 2);
        grid.add(allIn, 8, 0);
        grid.add(resetBid, 5, 1);

        //grid.add(chipHaufen, 4, 0);

        grid.add(err, 0, 1);

        grid.add(text, 0, 2);
        for (int i = 0; i < 10; i++) {
            grid.add(k[i], i, 5);
            grid.add(k1[i], i, 7);
        }
        grid.add(dealer,0, 6);

        GridPane.setColumnSpan(text, 5);
        GridPane.setColumnSpan(dealer, 5);
        GridPane.setColumnSpan(err, 6);
        GridPane.setColumnSpan(chipHaufen, 3);
        GridPane.setRowSpan(chipHaufen, 3);
        GridPane.setColumnSpan(bid, 2);
        GridPane.setColumnSpan(reset, 2);
        GridPane.setColumnSpan(insuranceText, 2);

        stand.setDisable(true);
        hit.setDisable(true);
        reset.setDisable(true);
        insurance.setDisable(true);

        grid.setStyle("-fx-background-image: url('HobbyRoom/Games/Blackjack/pictures/background.jpg'); -fx-background-size: cover; -fx-background-position: center");

        //---------------------------------------------------MENU----------------------------------------------------------------
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("game");
        Menu saveMenu = new Menu("save & load");
        menuBar.getMenus().addAll(gameMenu, saveMenu);

        MenuItem loadBtn = new MenuItem("load");
        loadBtn.setOnAction(event -> {
            load.fire();
        });
        MenuItem saveBtn = new MenuItem("save");
        saveBtn.setOnAction(event -> {
            save.fire();
        });
        saveMenu.getItems().addAll(saveBtn, loadBtn);

        MenuItem exitMenu = new MenuItem("exit");
        exitMenu.setOnAction(event -> {
            stage.close();
        });
        MenuItem restartMenu = new MenuItem("restart");
        restartMenu.setOnAction(event -> {
            reset.setText("restart");
            reset.fire();
            reset.setText("next round");
        });
        gameMenu.getItems().addAll(restartMenu, exitMenu);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(grid);
        borderPane.setTop(menuBar);
        //---------------------------------------------------EVENTS--------------------------------------------------------
        Eventhandler.standEvent(stand, k1, grid, reset, hit, dealer, stage, insurance, insuranceText);
        Eventhandler.hitEvent(hit, text, k, grid, reset, stand, stage, insurance);
        Eventhandler.bidEvent(bid, bidArea, err, text, k, k1, grid, chip10, chip50, chip100, allIn, stand, hit, reset, stage, resetBid, insurance);
        Eventhandler.resetEvent(reset, err, bidArea, text, dealer, k, k1, grid, stand, hit, bid, allIn, stage, chip10, chip50, chip100, resetBid, insuranceText);
        Eventhandler.saveEvent(save, bid, hit);
        Eventhandler.loadEvent(load, k, k1, text, grid, bid, hit, stand, allIn, chip10, chip50, chip100, bidArea, resetBid);
        Eventhandler.insuranceEvent(insurance, insuranceText);
        //--------------------------------------------------------------------------------------------------------------------------------

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
}
