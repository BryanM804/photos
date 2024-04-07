package javafx.controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class TagDialogController {
    @FXML TextField customTagInput;
    @FXML TextField tagValueInput;
    @FXML Button addTagButton;
    @FXML Button CYOButton;
    @FXML ListView<String> possibleTagList;

    @FXML
    public void initialize() {
        
    }

    public void handleCYOClick(ActionEvent e) {
        Button pButton = (Button)e.getSource();

        if (pButton == CYOButton) {
            customTagInput.setVisible(true);
            addTagButton.setVisible(true);
            tagValueInput.setVisible(false);
        }
    }

    public void handleAddClick(ActionEvent e) {
        Button pButton = (Button)e.getSource();

        if (pButton == addTagButton) {
            customTagInput.setVisible(false);
            addTagButton.setVisible(false);
            tagValueInput.setVisible(true);

            // add the tag to the tag list stored somewhere
        }
    }

    public void updateTagList(List<String> tags) {
        ObservableList<String> displayTags = FXCollections.observableArrayList(tags);
        possibleTagList.setItems(displayTags);
    }
    
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
