package jarvis;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Jarvis jarvis;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image jarvisImage = new Image(this.getClass().getResourceAsStream("/images/Jarvis.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        showWelcome();
    }

    /**
     * Injects the Jarvis instance.
     */
    public void setJarvis(Jarvis jarvis) {
        this.jarvis = jarvis;
    }

    /**
     * Shows the welcome message.
     */
    private void showWelcome() {
        String welcome = "Hello! I'm Jarvis.\nWhat can I do for you?";
        dialogContainer.getChildren().add(DialogBox.getJarvisDialog(welcome, jarvisImage));
    }

    /**
     * Handles user input and displays the response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isEmpty()) {
            return;
        }
        String response = jarvis.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getJarvisDialog(response, jarvisImage)
        );
        userInput.clear();
    }
}
