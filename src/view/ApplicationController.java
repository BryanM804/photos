package view;

import java.util.ArrayList;

import application.Application;
import application.album.Album;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

public class ApplicationController {
    
    private static ApplicationController instance;

    // This is all very cluttered because fxml only lets you have a controller on the root
    // element and I did not feel like making nested elements right now since the UI might change
    // around later and I will likely do that later when we are more set in stone with things.

    @FXML Button albumCButton;
    @FXML Button albumRNButton;
    @FXML Button albumDelButton;
    @FXML Button searchSubmitButton;
    @FXML Button logoutButton;
    @FXML Button albumOpenButton;
    @FXML Button addTagButton;
    @FXML Button addPhotoButton;
    @FXML Button remTagButton;
    @FXML Button remPhotoButton;
    @FXML Button changeCapButton;
    @FXML ListView<Album> albumList;
    @FXML ListView<String> tagList;
    @FXML GridPane albumContainer;
    @FXML TextField searchInput;

    public ApplicationController() {
        instance = this;
    }

    public static ApplicationController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No active controller");
        }

        return instance;
    }

    public void updateAlbumList(ArrayList<Album> albums) {
        ObservableList<Album> displayAlbums = FXCollections.observableArrayList(albums);
        albumList.setItems(displayAlbums); // not sure what this will do yet, will probably need a seperate list of just the names
    }

    public void updateTagList(ArrayList<String> tags) {
        ObservableList<String> displayTags = FXCollections.observableArrayList(tags);
        tagList.setItems(displayTags);
    }

    public void updateAlbumView() {
        // Fill the grid with clickable photos (maybe)
        // How I envision this is to have you click an album from the list and
        // From there it fills the center with the thumbnails of the photos which
        // can be selected to change attributes
    }

    public void handleSearchSubmit(ActionEvent e) {
        // Send search query to backend
    }

    public void handleAlbumCreate(ActionEvent e) {
        // Open album creation dialog
        Button pButton = (Button)e.getSource();
        if (pButton == albumCButton) {
            TextInputDialog cAlbumPrompt = new TextInputDialog("Album");
            cAlbumPrompt.setHeaderText("Enter a name for the album:");
            cAlbumPrompt.showAndWait();
            // tell the app to create an album with the name when this closes
        }
    }

    public void handleAlbumRename(ActionEvent e) {
        // Open rename dialog
        Button pButton = (Button)e.getSource();
        if (pButton == albumRNButton) {
            TextInputDialog renameAlbumPrompt = new TextInputDialog("Album");
            renameAlbumPrompt.setHeaderText("Enter a new name for the album:");
            renameAlbumPrompt.showAndWait();
            //
        }
    }

    public void handleAlbumDelete(ActionEvent e) {
        // Tell backend to delete album
    }

    public void handleAlbumOpen(ActionEvent e) {
        // Request album data from backend and update photo view
    }

    public void handleAddTag(ActionEvent e) {
        // Add a tag to selected photo
    }

    public void handleRemoveTag(ActionEvent e) {
        // Remove tag from photo
    }

    public void handleAddPhoto(ActionEvent e) {
        // Open add photo dialog probably
    }

    public void handleRemovePhoto(ActionEvent e) {
        // Remove photo from album
    }

    public void handleChangeCaption(ActionEvent e) {
        Button pButton = (Button)e.getSource();
        if (pButton == changeCapButton) {
            TextInputDialog captionPrompt = new TextInputDialog("Caption");
            captionPrompt.setHeaderText("Enter a new caption for the photo");
            captionPrompt.showAndWait();
            // tell the app to change the caption when this closes
        }
    }

    public void handleLogout(ActionEvent e) {
        // Tell backend to logout and reopen login screen
        Application.getInstance().logout();
    }
}
