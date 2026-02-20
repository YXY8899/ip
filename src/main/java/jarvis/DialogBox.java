package jarvis;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView and a Label.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;
    @FXML
    private VBox textContainer;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        dialog.setMinHeight(Label.USE_PREF_SIZE);
        displayPicture.setImage(img);
        applyCircleClip();
    }

    /**
     * Applies a circular clip to the profile picture.
     */
    private void applyCircleClip() {
        Circle clip = new Circle(17.5, 17.5, 17.5);
        displayPicture.setClip(clip);
    }

    /**
     * Flips the dialog box for Jarvis (image on left, text on right).
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Applies user styling to the dialog box.
     */
    private void applyUserStyle() {
        textContainer.getStyleClass().add("user-dialog");
    }

    /**
     * Applies Jarvis styling to the dialog box.
     */
    private void applyJarvisStyle() {
        textContainer.getStyleClass().add("jarvis-dialog");
    }

    /**
     * Applies error styling to the dialog box.
     */
    private void applyErrorStyle() {
        textContainer.getStyleClass().add("error-dialog");
    }

    /**
     * Creates a dialog box for the user.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.applyUserStyle();
        return db;
    }

    /**
     * Creates a dialog box for Jarvis.
     */
    public static DialogBox getJarvisDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        db.applyJarvisStyle();
        return db;
    }

    /**
     * Creates an error dialog box for Jarvis.
     */
    public static DialogBox getErrorDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        db.applyErrorStyle();
        return db;
    }
}
