/*
this is the intro scene when you first open the app
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import java.awt.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WelcomeScene extends BorderPane {
    public WelcomeScene(Runnable onStart, Runnable onRules, Runnable onOdds, Runnable onExit) {
        setTop(buildMenu(onRules, onOdds, onExit));
        setCenter(buildCenter(onStart));
        setBottom(credits());
        setPadding(new Insets(12));
    }

    private MenuBar buildMenu(Runnable onRules, Runnable onOdds, Runnable onExit) {
        Menu menu = new Menu("Menu");
        MenuItem rules = new MenuItem("Rules");
        MenuItem odds = new MenuItem("Odds");
        MenuItem exit = new MenuItem("Exit");
        rules.setOnAction(e -> onRules.run());
        odds.setOnAction(e -> onOdds.run());
        exit.setOnAction(e -> onExit.run());
        menu.getItems().addAll(rules, odds, exit);
        return new MenuBar(menu);
    }

    private VBox buildCenter(Runnable onStart) {
        Label title = new Label("K E N O");
        title.setStyle("-fx-font-size:42px; -fx-font-weight:bold;");
        Label sub = new Label("Welcome! Press Start to Play");
        Button start = new Button("Start Playing");
        start.setOnAction(e -> onStart.run());
        VBox box = new VBox(16, title, sub, start);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(24));
        return box;
    }

    private VBox credits() {
        Label credit = new Label("Made by Hassan Hassan");
        credit.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");

        VBox box = new VBox(credit);
        box.setAlignment(Pos.CENTER); // center horizontally
        box.setPadding(new Insets(10));

        return box;
    }
}
