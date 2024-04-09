package javafx.controller;

import java.util.List;

import application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * @author Bryan Mulholland
 */
public class TagDialogController
{
    @FXML TextField customTagInput;
    @FXML TextField tagValueInput;
    @FXML Button addTagButton;
    @FXML Button CYOButton;
    @FXML ListView<String> possibleTagList;

    @FXML
    public void initialize()
    {
        updateTagList(Application.getInstance().getSession().getCustomTags());
    }

    /**
     * Shows the create your own tag inputs
     * @param e
     */
    public void handleCYOClick(ActionEvent e) {
        Button pButton = (Button)e.getSource();

        if (pButton == CYOButton) {
            customTagInput.setVisible(true);
            addTagButton.setVisible(true);
            tagValueInput.setVisible(false);
        }
    }

    /**
     * Adds the users tag to the list of tags
     * @param e
     */
    public void handleAddClick(ActionEvent e) {
        Button pButton = (Button)e.getSource();

        if (pButton == addTagButton) {
            customTagInput.setVisible(false);
            addTagButton.setVisible(false);
            tagValueInput.setVisible(true);

            String text = this.customTagInput.getText();

            if (text.isEmpty())
            {
                return;
            }

            Application.getInstance().getSession().getCustomTags().add(text);
            updateTagList(Application.getInstance().getSession().getCustomTags());
        }
    }

    /**
     * Updates the displayed list of tags
     * @param tags new tags to be displayed
     */
    public void updateTagList(List<String> tags)
    {
        ObservableList<String> displayTags = FXCollections.observableArrayList(tags);
        possibleTagList.setItems(displayTags);
    }
    
    /**
     * Converts the selected tag and entered value to an array of strings
     * @param buttonType
     * @return array of two strings for the tag and tag value
     */
    public String[] convertResult(ButtonType buttonType) {
        if (ButtonType.OK.equals(buttonType)) {
            if (tagValueInput.getText().length() > 0) {
                return new String[] {possibleTagList.getSelectionModel().getSelectedItem(), tagValueInput.getText()};
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid entry");
                alert.setHeaderText("Please enter a tag value!");
                alert.showAndWait();
            }
        }

        return null;
    }
}
