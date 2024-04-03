package javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import launcher.Launcher;
import launcher.Session;

import java.util.regex.Pattern;

public class LoginController
{
    @FXML Button loginButton;
    @FXML TextField usernameInput;
    @FXML Text errorContainer;

    public void login()
    {
        String username = this.usernameInput.getText();

        if (username.length() == 0)
        {
            this.errorContainer.setText("Please enter a username.");
            return;
        }

        if (!isAlphaNumeric(username))
        {
            this.errorContainer.setText("No special characters!");
            return;
        }

        Session session = new Session(username);
        Launcher.launch(session);
    }

    public boolean isAlphaNumeric(String str)
    {
        return Pattern.matches("^[a-zA-Z0-9]+$", str);
    }
}
