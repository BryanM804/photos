package javafx.controller;

import application.album.Photo;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InspectImageController {
    
    @FXML ImageView imageDisplay;
    @FXML TextArea infoArea;

    @FXML
    public void initialize() {

    }

    public void setImage(Photo photo) {
        Image image = new Image(photo.getPhotoFile().toURI().toString(),
                800, // width
                450, // height
                true, // preserve ratio
                true); // smooth rescaling
        this.imageDisplay.setImage(image);

        StringBuilder info = new StringBuilder();
        info.append("Caption: \"" + photo.getCaption() + "\"\n");
        info.append("Date: " + photo.getTimestamp().toString() + "\n");
        info.append("Tags: ");

        String infoString = info.toString();

        infoArea.setText(infoString);
    }
}
