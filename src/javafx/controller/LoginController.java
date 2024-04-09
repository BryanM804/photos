package javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import launcher.Launcher;
import launcher.Session;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author Bryan Mulholland
 * @author Andrew Bonasera
 */
public class LoginController
{
    @FXML Button loginButton;
    @FXML TextField usernameInput;
    @FXML Text errorContainer;

    /**
     * Checks the entered username and starts a session if it is valid
     */
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

        if (session.isValidUser())
        {
            File userDataFile = session.getUserDataFile();
            if (userDataFile.exists())
            {
                session = Session.deserialize(userDataFile);
            } else if (username.equalsIgnoreCase("stock")) // unbelievable hack
            {
                session.setFirstTime(true);
            }

            Launcher.launch(session);
        } else {
            this.errorContainer.setText("User does not exist!");
        }
    }

    /**
     * Checks if the string contains only alphanumeric characters
     * @param str
     * @return true if str contains no special characters
     */
    public boolean isAlphaNumeric(String str)
    {
        return Pattern.matches("^[a-zA-Z0-9]+$", str);
    }
}
