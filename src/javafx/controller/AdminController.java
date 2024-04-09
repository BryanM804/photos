package javafx.controller;

import java.util.List;
import java.util.regex.Pattern;

import application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * @author Bryan Mulholland
 */
public class AdminController {
    
    @FXML Button logoutButton;
    @FXML Button createUserButton;
    @FXML Button delUserButton;
    @FXML TextField usernameInput;
    @FXML ListView<String> userList;

    @FXML
    public void initialize() {
        updateUserList(Application.getInstance().getSession().getAllUsernames());
    }

    /**
     * Updates the ListView of users
     * @param users
     */
    public void updateUserList(List<String> users) {
        ObservableList<String> usernames = FXCollections.observableArrayList(users);
        userList.setItems(usernames);
    }

    /**
     * Tells the application to log the user out
     * @param e event
     */
    public void handleLogout(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == logoutButton) {
            Application.getInstance().logout();
        }
    }

    /**
     * Creates a user if the username is valid
     * @param e event
     */
    public void handleCreateUser(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == createUserButton) {
            String username = usernameInput.getText();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Invalid username");
            errorAlert.initOwner(pButton.getScene().getWindow());

            if (username.length() < 1) {
                errorAlert.setHeaderText("Please enter a username!");
                errorAlert.showAndWait();
                return;
            } else if(!isAlphaNumeric(username)) {
                errorAlert.setHeaderText("No special characters!");
                errorAlert.showAndWait();
                return;
            }

            Application.getInstance().getSession().createUserFile(username);
            updateUserList(Application.getInstance().getSession().getAllUsernames());
        }
    }

    /**
     * Deletes the selected user if there is one
     * @param e event
     */
    public void handleDeleteUser(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == delUserButton) {
            String selectedUsername = userList.getSelectionModel().getSelectedItem();

            if (selectedUsername == null) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("No selected user");
                errorAlert.setHeaderText("Please select a user to delete");
                errorAlert.initOwner(pButton.getScene().getWindow());
                errorAlert.showAndWait();
                return;
            }

            Application.getInstance().getSession().deleteUserFile(selectedUsername);
            updateUserList(Application.getInstance().getSession().getAllUsernames());
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
