/*
this is a utility class that will be used to show the dialogs for the popup windows
 */


import javafx.scene.control.Alert;

public class Dialogs {
    private Dialogs() {}
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
