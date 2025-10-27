
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Project2Fall2025 extends Application {

    private final KenoGame game = new KenoGame();

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        showWelcome(stage);
        stage.setTitle("Keno – CS342 Project 2");
        stage.show();
    }

    private void showWelcome(Stage stage) {
        WelcomeScene welcome = new WelcomeScene(
                () -> showGame(stage),
                () -> Dialogs.showInfo(
                        "Rules,",
                        "Pick 1, 4, 8, or 10 spots.\n" +
                                "Chose 1-4 drawings.\n" +
                                "Each drawing reveals 20 unique numbers from 1-80.\n" +
                                "Payout is based on matches."),
                () -> Dialogs.showInfo("Odds,",
                        "Odds for 1 spot: 1 in 4.00.\n" +
                                "Odds for 4 spots: 1 in 3.86.\n" +
                                "Odds for 8 spots: 1 in 9.77.\n" +
                                "Odds for 10 spots: 1 in 9.05."),
                () -> Platform.exit()
        );
        stage.setScene(new Scene(welcome, 900, 600));
    }

    private void showGame(Stage stage) {
        GameSceneView gameView = new GameSceneView(
                game,
                () -> Dialogs.showInfo(
                        "Rules,",
                        "Pick 1, 4, 8, or 10 spots.\n" +
                                "Chose 1-4 drawings.\n" +
                                "Each drawing reveals 20 unique numbers from 1-80.\n" +
                                "Payout is based on matches."),
                () -> Dialogs.showInfo("Odds,",
                        "Odds for 1 spot: 1 in 4.00.\n" +
                                "Odds for 4 spots: 1 in 3.86.\n" +
                                "Odds for 8 spots: 1 in 9.77.\n" +
                                "Odds for 10 spots: 1 in 9.05."),
                () -> toggleNewLook(stage),
                () -> Platform.exit(),
                () -> showWelcome(stage)
        );
        stage.setScene(new Scene(gameView.build(), 1050, 600));
    }

    private void toggleNewLook(Stage stage) {
        var root = stage.getScene().getRoot();
        boolean dark = Boolean.TRUE.equals(root.getUserData());
        root.setUserData(!dark);
        root.setStyle(dark ? "" : "-fx-base:#222; -fx-background:#111; -fx-text-fill:white;");
    }
}
