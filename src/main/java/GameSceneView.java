/*
this will be the second scene of the app where the majority of things will be happening
 */

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameSceneView {

    private final KenoGame game;
    private final Runnable onRules, onOdds, onNewLook, onExit, onNewRound;

    private ToggleGroup spotsGroup, drawsGroup;
    private GridPane betCardGrid;
    private final Map<Integer, ToggleButton> numberButtons = new HashMap<>();
    private Button quickPickBtn, clearBtn, startBtn, continueBtn;
    private Label statusDraw, statusRevealed, statusMatches, statusMatchList, statusWin, statusTotal;

    public GameSceneView(KenoGame game, Runnable onRules, Runnable onOdds, Runnable onNewLook, Runnable onExit, Runnable onNewRound) {
        this.game = game;
        this.onRules = onRules;
        this.onOdds = onOdds;
        this.onNewLook = onNewLook;
        this.onExit = onExit;
        this.onNewRound = onNewRound;
    }

    public BorderPane build() {
        BorderPane root = new BorderPane();
        root.setTop(buildMenuBar());
        root.setCenter(buildCenter());
        root.setPadding(new Insets(8));
        return root;
    }

    private MenuBar buildMenuBar() {
        Menu menu = new Menu("Menu");
        MenuItem rules = new MenuItem("Rules");
        MenuItem odds  = new MenuItem("Odds");
        MenuItem newLook = new MenuItem("New Look");
        MenuItem exit  = new MenuItem("Exit");
        rules.setOnAction(e -> onRules.run());
        odds.setOnAction(e -> onOdds.run());
        newLook.setOnAction(e -> onNewLook.run());
        exit.setOnAction(e -> onExit.run());
        menu.getItems().addAll(rules, odds, newLook, new SeparatorMenuItem(), exit);
        return new MenuBar(menu);
    }

    private VBox buildCenter() {
        VBox controls = new VBox(10,
                labelBold("Spots to play"),
                spotsRow(),
                labelBold("Drawings"),
                drawingsRow(),
                buttonsColumn()
        );
        controls.setPadding(new Insets(8));
        controls.setPrefWidth(220);

        betCardGrid = buildBetCardGrid();
        VBox gridBox = new VBox(8, labelBold("Bet Card (1-80)"), betCardGrid);
        gridBox.setPadding(new Insets(8));

        VBox results = resultsStrip();

        VBox center = new VBox(10, new HBox(20, controls, gridBox), results);
        center.setPadding(new Insets(8));
        center.setAlignment(Pos.TOP_CENTER);
        return center;
    }

    private Label labelBold(String s) {
        Label l = new Label(s);
        l.setStyle("-fx-font-weight:bold; -fx-underline:true;");
        return l;
    }

    private HBox spotsRow() {
        spotsGroup = new ToggleGroup();
        HBox row = new HBox(6,
                makeToggle("1", spotsGroup),
                makeToggle("4", spotsGroup),
                makeToggle("8", spotsGroup),
                makeToggle("10", spotsGroup)
        );
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox drawingsRow() {
        drawsGroup = new ToggleGroup();
        ToggleButton d1 = makeToggle("1", drawsGroup);
        ToggleButton d2 = makeToggle("2", drawsGroup);
        ToggleButton d3 = makeToggle("3", drawsGroup);
        ToggleButton d4 = makeToggle("4", drawsGroup);
        d1.setSelected(true);
        HBox row = new HBox(6, d1, d2, d3, d4);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private VBox buttonsColumn() {
        quickPickBtn = new Button("Quick Pick");
        clearBtn     = new Button("Clear");
        startBtn     = new Button("Start Drawing");
        continueBtn  = new Button("Continue");
        continueBtn.setDisable(true);

        quickPickBtn.setOnAction(e -> doQuickPick());
        clearBtn.setOnAction(e -> doClear());
        startBtn.setOnAction(e -> doStart());
        continueBtn.setOnAction(e -> doContinue());

        VBox col = new VBox(6, quickPickBtn, clearBtn, startBtn, continueBtn);
        col.setAlignment(Pos.TOP_LEFT);
        return col;
    }

    private GridPane buildBetCardGrid() {
        GridPane gp = new GridPane();
        gp.setHgap(5); gp.setVgap(5);
        for (int n = 1; n <= 80; n++) {
            ToggleButton b = new ToggleButton(String.valueOf(n));
            b.setPrefSize(52, 36);
            b.setDisable(true);
            int picked = n;
            b.setOnAction(e -> onPick(picked));
            numberButtons.put(n, b);
            int col = (n - 1) % 10;
            int row = (n - 1) / 10;
            gp.add(b, col, row);
        }
        return gp;
    }

    private VBox resultsStrip() {
        statusDraw      = new Label("Draw 0 of 0");
        statusRevealed  = new Label("Revealed: {}");
        statusMatches   = new Label("Matches: 0");
        statusMatchList = new Label("Matched: {}");
        statusWin       = new Label("Win: $0");
        statusTotal     = new Label("Total: $0");

        String labelStyle = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;";
        statusDraw.setStyle(labelStyle);
        statusMatches.setStyle(labelStyle);
        statusMatchList.setStyle(labelStyle);
        statusWin.setStyle(labelStyle);
        statusTotal.setStyle(labelStyle);
        statusRevealed.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        HBox topRow = new HBox(20, statusDraw, statusMatches, statusMatchList, statusWin, statusTotal);
        topRow.setAlignment(Pos.CENTER);
        topRow.setPadding(new Insets(10));
        topRow.setStyle("-fx-border-color:#bbb; -fx-border-radius:6; -fx-padding:6;");

        HBox revealRow = new HBox(statusRevealed);
        revealRow.setAlignment(Pos.CENTER);
        revealRow.setPadding(new Insets(10));
        revealRow.setStyle("-fx-border-color:#bbb; -fx-border-radius:6; -fx-padding:6;");

        // Stack vertically
        VBox box = new VBox(topRow, revealRow);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(0);
        return box;
    }

    // interactions

    private ToggleButton makeToggle(String text, ToggleGroup group) {
        ToggleButton t = new ToggleButton(text);
        t.setToggleGroup(group);
        t.setOnAction(e -> onTopControlsChanged());
        return t;
    }

    private void onTopControlsChanged() {
        int spots = getSelected(spotsGroup);
        boolean enable = spots > 0 && !game.isDrawing();
        numberButtons.values().forEach(b -> b.setDisable(!enable));
        game.getBc().setSpots(spots);
        enforcePickLimit();
    }

    private void onPick(int n) {
        if (game.isDrawing()) return;
        BetCard bc = game.getBc();
        ToggleButton b = numberButtons.get(n);
        if (b.isSelected()) {
            boolean ok = bc.addPick(n);
            if (!ok) b.setSelected(false);
        } else {
            bc.removePick(n);
        }
        enforcePickLimit();
    }

    private void enforcePickLimit() {
        BetCard bc = game.getBc();
        int spots = bc.getSpots();
        long selected = bc.getPicks().size();
        boolean full = selected >= spots && spots > 0;
        numberButtons.forEach((n, b) -> {
            if (game.isDrawing()) { b.setDisable(true); return; }
            if (b.isSelected()) b.setDisable(false);
            else b.setDisable(full);
        });
    }

    private void doQuickPick() {
        if (game.isDrawing()) return;
        int spots = getSelected(spotsGroup);
        if (spots <= 0) return;
        doClear();
        List<Integer> pool = IntStream.rangeClosed(1, 80).boxed().collect(Collectors.toList());
        Collections.shuffle(pool, game.getRand());
        for (int i = 0; i < spots; i++) {
            int n = pool.get(i);
            numberButtons.get(n).setSelected(true);
            game.getBc().addPick(n);
        }
        enforcePickLimit();
    }

    private void doClear() {
        if (game.isDrawing()) return;
        game.getBc().clear();
        numberButtons.values().forEach(b -> b.setSelected(false));
        enforcePickLimit();
    }

    private void doStart() {
        int spots = getSelected(spotsGroup);
        int draws = getSelected(drawsGroup);
        if (spots <= 0 || draws <= 0 || !game.getBc().isComplete()) {
            Dialogs.showInfo("Missing Info", "Pick spots, fill the bet card, and choose drawings (1–4) first.");
            return;
        }
        lockSetup(true);
        game.startRound(draws);
        updateTotals();
        runOneDrawing();
    }

    private void doContinue() {
        if (!game.hasNextDrawing()) { onNewRound.run(); return; }
        runOneDrawing();
    }

    private void lockSetup(boolean lock) {
        Arrays.stream(spotsGroup.getToggles().toArray(new ToggleButton[0])).forEach(t -> t.setDisable(lock));
        Arrays.stream(drawsGroup.getToggles().toArray(new ToggleButton[0])).forEach(t -> t.setDisable(lock));
        numberButtons.values().forEach(b -> b.setDisable(true));
        quickPickBtn.setDisable(lock);
        clearBtn.setDisable(lock);
        startBtn.setDisable(lock);
    }

    private void runOneDrawing() {
        continueBtn.setDisable(true);
        statusDraw.setText("Draw " + (game.getDrawingsPlayed() + 1) + " of " + game.getDrawingsToPlay());

        var draw = game.draw20();
        SequentialTransition seq = new SequentialTransition();
        StringBuilder reveal = new StringBuilder();
        for (int i = 0; i < draw.size(); i++) {
            int idx = i;
            PauseTransition p = new PauseTransition(Duration.millis(200));
            p.setOnFinished(e -> {
                if (reveal.length() > 0) reveal.append(", ");
                reveal.append(draw.get(idx));
                statusRevealed.setText("Revealed: { " + reveal + " }");
            });
            seq.getChildren().add(p);
        }
        seq.setOnFinished(e -> {
            int matches = game.countMatches(draw);
            statusMatches.setText("Matches: " + matches);
            statusMatchList.setText("Matched: " + game.getLastMatched());
            long win = game.payout(game.getBc().getSpots(), matches);
            game.addWinnings(win);
            statusWin.setText("Win: $" + win);
            updateTotals();
            game.finishedDrawing();
            continueBtn.setText(game.hasNextDrawing() ? "Continue" : "New Round");
            continueBtn.setDisable(false);
        });
        seq.play();
    }

    private void updateTotals() {
        statusTotal.setText("Total: $" + game.getTotalWon());
    }

    private int getSelected(ToggleGroup g) {
        if (g.getSelectedToggle() == null) return 0;
        return Integer.parseInt(((ToggleButton) g.getSelectedToggle()).getText());
    }
}
