package javafx.controller;

import java.util.List;

import application.album.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Bryan Mulholland
 */
public class SlideShowController {
    
    @FXML Button forwardButton;
    @FXML Button backButton;
    @FXML ImageView imageDisplay;

    private List<Photo> photos;
    private int index;

    @FXML
    public void initialize() {
        this.index = 0;
    }

    /**
     * Sets the photo list for the slideshow and updates the first photo
     * @param photos
     */
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        this.updateDisplayPhoto(photos.get(index));
    }

    /**
     * Updates the displayed photo to the newPhoto
     * @param newPhoto
     */
    public void updateDisplayPhoto(Photo newPhoto) {
        Image image = new Image(newPhoto.getPhotoFile().toURI().toString(),
                1080, // width
                600, // height
                true, // preserve ratio
                true); // smooth rescaling
        this.imageDisplay.setImage(image);
    }

    /**
     * Increments index and updates the displayed photo to the next in the slideshow
     * @param e
     */
    public void handleForwardClick(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == forwardButton) {
            if (index < photos.size()) {
                this.updateDisplayPhoto(this.photos.get(index));
                if (this.index + 1 != photos.size())
                    this.index++;
            }
        }
    }

    /**
     * Decrements the index and updates the displayed photo to the previous in the slideshow
     * @param e
     */
    public void handleBackClick(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == backButton) {
            if (index > 0) {
                this.updateDisplayPhoto(this.photos.get(index));
                this.index--;
            }
        }
    }
}