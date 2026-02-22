package jarvis;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import jarvis.parser.Parser;

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

    /**
     * Initializes the main window.
     */
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
        String welcome = "Good day, Master. I am Jarvis, your devoted butler.\n"
                + "How may I be of service to you today?";
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
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));

        if (isErrorResponse(response)) {
            dialogContainer.getChildren().add(DialogBox.getErrorDialog(response, jarvisImage));
        } else {
            dialogContainer.getChildren().add(DialogBox.getJarvisDialog(response, jarvisImage));
        }
        userInput.clear();

        if (Parser.getCommand(input).equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }

    /**
     * Checks if the response is an error message.
     */
    private boolean isErrorResponse(String response) {
        return response.startsWith("Error:") || response.startsWith("My apologies");
    }
}
