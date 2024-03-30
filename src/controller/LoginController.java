package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import launcher.Launcher;
import launcher.Session;

public class LoginController {

    @FXML Button loginButton;
    @FXML TextField usernameInput;
    @FXML Text errorContainer;

    public void login() {
        String username = usernameInput.getText();

        if (username.length() < 1) {
            errorContainer.setText("Please enter a username.");
        } else if (!username.equals("admin")) {
            Session session = new Session(username);
            Launcher.launch(session);
        } else {
            // open admin subsystem
        }
    }
}
