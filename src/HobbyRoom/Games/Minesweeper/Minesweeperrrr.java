package HobbyRoom.Games.Minesweeper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Minesweeperrrr {
    public static int minesAmount = 16;
    public static int flagsAmount = minesAmount;
    public static int buttonsPerRow = 10;
    public static int buttonsPerColumn = 10;
    public static int buttonSize = 70;
    public static List<bButton> buttons = new ArrayList<>();
    public static Button flagButton = new Button(flagsAmount+" Flags");
    public static int globalS = readHighScore("resources/highScore.txt")[2];
    public static int globalMin = readHighScore("resources/highScore.txt")[1];
    public static int globalH = readHighScore("resources/highScore.txt")[0];
    public static Button highScoreButton = new Button("Personal Best: "+globalH+" h "+globalMin+" min "+globalS+" s");
    public static boolean end = false;
    public static int counter = 0;
    public static boolean won = false;
    public static boolean resetTimer = false;

    public static Stage start() throws FileNotFoundException {
        Stage stage = new Stage();
        stage.setTitle("Minesweeper");
        stage.setMinWidth(700);
        stage.setMinHeight(700);
        if (globalS == 0 && globalMin == 0 && globalH == 0) {
            highScoreButton.setText("Personal Best: not set");
        }
        buttons = generateField();
        System.out.println(readHighScore("resources/highScore.txt")[0]);
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();
        for (int i = 0; i < buttonsPerColumn; i++) {
            for (int j = 0; j < buttonsPerRow; j++) {
                gridPane.add(buttons.get(i*buttonsPerColumn+j),buttons.get(i*buttonsPerColumn+j).x,buttons.get((i*buttonsPerColumn+j)).y);
            }
        }
        final int[] zeit = {0};
        Button timerButton = new Button();
        Button titleButton = new Button("Minesweeper");
        Button resetButton = new Button("🔄");
        resetAction(resetButton);
        titleButton.setStyle("-fx-background-color: transparent;"+"-fx-font-weight: bold;");
        flagButton.setStyle("-fx-background-color: transparent");
        resetButton.setStyle("-fx-background-color: transparent");
        highScoreButton.setStyle("-fx-background-color: transparent");
        Timer tmr = new Timer();
        tmr.scheduleAtFixedRate(new TimerTask() {
            int min = 0;
            int h = 0;
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!end) {
                        zeit[0]++;
                    } else if (won){
                        writeHighScore("resources/highScore.txt",zeit[0],min,h);
                    }
                    if (resetTimer) {
                        resetTimer = false;
                        zeit[0] = 0;
                        min = 0;
                        h = 0;
                        timerButton.setText(h + " h " + min + " min " + zeit[0] + " s");
                    }
                    if (zeit[0] == 60) {
                        zeit[0]=0;
                        min++;
                        timerButton.setText(h + " h " + min + " min " + zeit[0] + " s");
                    }
                    if (min == 60) {
                        min=0;
                        h++;
                        timerButton.setText(h + " h " + min + " min " + zeit[0] + " s");
                    }
                    if (h == 24) {
                        h = 0;
                        timerButton.setText(h + " h " + min + " min " + zeit[0] + " s");
                    }
                    timerButton.setText(h + " h " + min + " min " + zeit[0] + " s");
                    timerButton.setStyle("-fx-background-color: transparent");
                  });
            }
        },1000,1000);

        HBox hBox = new HBox();
        Pane spacer = new Pane();
        Pane spacer2 = new Pane();

        HBox hBox2 = new HBox();
        Pane spacer3 = new Pane();
        Pane spacer4 = new Pane();

        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        HBox.setHgrow(spacer4, Priority.ALWAYS);
        flagButton.setPrefWidth(100);
        resetButton.setPrefWidth(28);
        timerButton.setPrefWidth(128);
        hBox.getChildren().addAll(timerButton,spacer, titleButton, spacer2,flagButton,resetButton);
        borderPane.setTop(hBox);
        hBox2.getChildren().addAll(spacer3,gridPane,spacer4);
        borderPane.setCenter(hBox2);
        borderPane.setBottom(highScoreButton);
        Scene scene = new Scene(borderPane, 500, 500);
        for (int i = 0; i < buttonsPerRow*buttonsPerColumn; i++)   {
            action(buttons.get(i),buttons);
        }
        stage.setScene(scene);
        return stage;
    }

    public static void writeHighScore(String destFile, int s, int min, int h) {
        int sum1 = s+min*60+h*60*60;
        int sum2 =readHighScore("resources/highScore.txt")[2]+readHighScore("resources/highScore.txt")[1]*60+readHighScore("resources/highScore.txt")[0]*60*60;
        if (sum1 < sum2 || sum2 == 0) {
            try (BufferedWriter out = Files.newBufferedWriter(Paths.get(destFile),
                    StandardCharsets.UTF_8)
            ) {
                String line = h+" "+min+" "+s;
                out.write(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            highScoreButton.setText("Personal Best: "+h+ " h " + min + " min " + s + " s");
            globalMin = min;
            globalH = h;
            globalS = s;
        }
    }

    public static int[] readHighScore(String srcFile) {
        int[] erg = new int[3];
        String text = "";
        try {
            text = Files.readString(Path.of(srcFile));

        } catch (IOException e) {
            e.printStackTrace();
        }
        int c = 0;
        String temp = "";
        for (int i = 0; i < text.length(); i++) {

            if (Character.isWhitespace(text.charAt(i))) {
                erg[c] = Integer.parseInt(temp);
                temp = "";
                c++;
                continue;
            }
            temp += text.charAt(i);
            if (i == text.length() - 1) {
                erg[c] = Integer.parseInt(temp);
            }
        }
        return erg;
    }

    public static void resetAction(Button a) {
        a.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                remakeField(buttons);
            }
        });
    }

    public static void action(bButton a,List<bButton> b)  {
        a.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!a.c.revealed) {
                    counter++;
                }
                if (a.c.value == 10 && counter == 1) {
                    remakeField(buttons);
                    counter = 0;
                    return;
                }
                if (!a.c.flag) {
                    a.reveal();
                    if (a.c.value == 10) {
                        endGame(b);
                        return;
                    }
                    if (a.c.value == 0) {
                        revealOpenFields(a, b);
                        revealOpenFields(a, b);
                    }
                    if (counter == (buttonsPerColumn * buttonsPerRow - minesAmount)) {
                        end = true;
                        won = true;
                        System.out.println("gewonnen");
                    }
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (!a.c.revealed) {
                    if (a.c.flag) {
                        a.unsetFlag();
                        flagsAmount++;
                    } else {
                        if (minesAmount - flagsAmount == minesAmount) {
                            return;
                        }
                        a.setFlag();
                        flagsAmount--;
                    }
                    flagUpdate(flagButton);
                }
            }
            });
    }

    public static void flagUpdate(Button button) {
        button.setText(flagsAmount+" Flags");
    }

    public static void revealOpenFields(bButton a, List<bButton> b) {
        revealOuterCircle(a.x,a.y,b);
        int i = 1;
        while (true) {
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x-j, a.y+i).c.value == 0&& getByCoordinates(b, a.x-j, a.y+i).c.revealed) {
                    revealOuterCircle(a.x-j, a.y+i,b);
                }
            }
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x+j, a.y+i).c.value == 0&& getByCoordinates(b, a.x+j, a.y+i).c.revealed) {
                    revealOuterCircle(a.x+j, a.y+i,b);
                }
            }
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x-j, a.y-i).c.value == 0&& getByCoordinates(b, a.x-j, a.y-i).c.revealed) {
                    revealOuterCircle(a.x-j, a.y-i,b);
                }
            }
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x+j, a.y-i).c.value == 0&& getByCoordinates(b, a.x+j, a.y-i).c.revealed) {
                    revealOuterCircle(a.x+j, a.y-i,b);
                }
            }
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x-i, a.y+j).c.value == 0&& getByCoordinates(b, a.x-i, a.y+j).c.revealed) {
                    revealOuterCircle(a.x-i, a.y+j,b);
                }
            }
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x+i, a.y+j).c.value == 0&& getByCoordinates(b, a.x+i, a.y+j).c.revealed) {
                    revealOuterCircle(a.x+i, a.y+j,b);
                }
            }
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x-i, a.y-j).c.value == 0&& getByCoordinates(b, a.x-i, a.y-j).c.revealed) {
                    revealOuterCircle(a.x-i, a.y-j,b);
                }
            }
            for (int j = 0; j <= i; j++) {
                if (getByCoordinates(b, a.x+i, a.y-j).c.value == 0&& getByCoordinates(b, a.x+i, a.y-j).c.revealed) {
                    revealOuterCircle(a.x+i, a.y-j,b);
                }
            }
            i++;
            if (buttonsPerColumn>buttonsPerRow) {
                if (i == buttonsPerColumn) {
                    break;
                }
            } else if (i == buttonsPerRow) {
                break;
            }
        }

    }

    public static void revealOuterCircle(int x,int y, List<bButton> b) {
        bButton temp;
        int temp2;
        if (buttonsPerColumn > buttonsPerRow) {
            temp2=buttonsPerColumn-1;
        } else temp2 = buttonsPerRow-1;
        if (x + 1 <= temp2) {
            temp = getByCoordinates(b,x+1,y);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
        if (x - 1 >= 0) {
            temp = getByCoordinates(b,x-1,y);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
        if (y + 1 <= temp2) {
            temp = getByCoordinates(b,x,y+1);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
        if (y - 1 >= 0) {
            temp = getByCoordinates(b,x,y-1);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
        if (y + 1 <= temp2 && x + 1 <= temp2) {
            temp = getByCoordinates(b,x+1,y+1);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
        if(x-1>=0&&y -1>=0) {
            temp = getByCoordinates(b,x-1,y-1);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
        if (x + 1 <= temp2 && y - 1 >= 0) {
            temp = getByCoordinates(b,x+1,y-1);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
        if (y + 1 <= temp2 && x - 1 >= 0) {
            temp = getByCoordinates(b,x-1,y+1);
            if (!(temp.c.revealed)) {
                temp.reveal();
                temp.unsetFlag();
                counter++;
            }
            setByCoordinates(b,x,y,temp);
        }
    }

    public static Set<Integer> generateMines() {
        Set<Integer> erg = new TreeSet<>();
        int random;
        while (true) {
            random = (int) (Math.random()*(buttonsPerRow*buttonsPerColumn-1)+1);
            erg.add(random);
            if (erg.size() == minesAmount) {
                break;
            }
        } return erg;
    }

    public static bButton getByCoordinates(List<bButton> a, int x, int y) {
        for (bButton bButton : a) {
            if (bButton.x == x && bButton.y == y) {
                return bButton;
            }
        }
        return new bButton(new Cell(-1));
    }

    public static void setByCoordinates(List<bButton> a, int x, int y, bButton b) {
        for (int i = 0; i < a.size(); i++) {
            if (b.x == x && b.y == y) {
                a.set(i,b);
                //b.setText(b.c.toString());
            }
        }
    }

    public static void revealAll(List<bButton> a) {
        for (bButton bButton : a) {
            bButton.c.setRevealed(true);
            bButton.colors();
            bButton.setText(bButton.c.toString());
        }
    }

    public static void endGame(List<bButton> a) {
        end = true;
        revealAll(a);
        for (int i = 0; i < a.size(); i++) {
            a.get(i).setDisable(true);
        }
    }

    public static List<bButton> generateField() {
        Set<Integer> minesSet = generateMines();
        List<Integer> minesList = new ArrayList<>(minesSet);
        List<bButton> buttons = new ArrayList<>();
        for (int i = 0; i < buttonsPerColumn; i++) {
            for (int j = 0; j < buttonsPerRow; j++) {
                buttons.add(new bButton(j,i));
                buttons.get(i*buttonsPerColumn +j).setPrefSize(buttonSize,buttonSize);
            }
        }
        for (Integer integer : minesList) {
            buttons.get(integer).setC(new Cell(10));
        }
        int valueCount=0;
        bButton knopf = new bButton();
        for (int i = 0; i < buttons.size(); i++) {
            knopf = buttons.get(i);
            if (!(minesList.contains(i))) {
                if (getByCoordinates(buttons, knopf.x + 1, knopf.y).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x -1, knopf.y).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x, knopf.y+1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x, knopf.y-1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x+1, knopf.y+1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x-1, knopf.y-1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x+1, knopf.y-1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x-1, knopf.y+1).c.value==10) {
                    valueCount++;
                }
                buttons.get(i).setValue(valueCount);
                buttons.get(i).c.revealed=false;
                valueCount=0;
            }
        }
        return buttons;
    }

    public static void remakeField(List <bButton> buttons) {
        Set<Integer> minesSet = generateMines();
        List<Integer> minesList = new ArrayList<Integer>(minesSet);
        for (int i = 0; i < buttonsPerColumn; i++) {
            for (int j = 0; j < buttonsPerRow; j++) {
                buttons.get(i*buttonsPerColumn +j).setValue(0);
                buttons.get(i*buttonsPerColumn +j).c.revealed = false;
                buttons.get(i*buttonsPerColumn +j).c.flag = false;
                buttons.get(i*buttonsPerColumn +j).setDisable(false);
            }
        }
        for (Integer integer : minesList) {
            buttons.get(integer).setC(new Cell(10));
        }
        int valueCount=0;
        bButton knopf = new bButton();
        for (int i = 0; i < buttons.size(); i++) {
            knopf = buttons.get(i);
            if (!(minesList.contains(i))) {
                if (getByCoordinates(buttons, knopf.x + 1, knopf.y).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x -1, knopf.y).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x, knopf.y+1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x, knopf.y-1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x+1, knopf.y+1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x-1, knopf.y-1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x+1, knopf.y-1).c.value==10) {
                    valueCount++;
                }
                if (getByCoordinates(buttons, knopf.x-1, knopf.y+1).c.value==10) {
                    valueCount++;
                }
                buttons.get(i).setValue(valueCount);
                buttons.get(i).c.revealed=false;
                valueCount=0;
            }
            counter = 0;
            flagsAmount = minesAmount;
            flagUpdate(flagButton);
            resetTimer = true;
            end = false;
            won = false;
        }
    }
}
