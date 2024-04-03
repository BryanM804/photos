package javafx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import application.Application;
import application.album.Album;
import application.album.AlbumManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ApplicationController {
    
    private static ApplicationController instance;

    // This is all very cluttered because fxml only lets you have a javafx.controller on the root
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

    public ApplicationController()
    {
        instance = this;
    }

    public static ApplicationController getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("No active controller");
        }

        return instance;
    }

    @FXML
    public void initialize()
    {
        albumRNButton.setVisible(false);
        albumDelButton.setVisible(false);

        this.albumList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> albumRNButton.setVisible(newValue != null)
        );

        this.albumList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> albumDelButton.setVisible(newValue != null)
        );
    }

    public void updateAlbumList(List<Album> albums)
    {
        ObservableList<Album> displayAlbums = FXCollections.observableArrayList(albums);
        albumList.setItems(displayAlbums);
    }

    public void updateTagList(List<String> tags) {
        ObservableList<String> displayTags = FXCollections.observableArrayList(tags);
        tagList.setItems(displayTags);
    }

    public void updateAlbumView() {
        // Fill the grid with clickable photos (maybe)
        // How I envision this is to have you click an album from the list and
        // From there it fills the center with the thumbnails of the photos which
        // can be selected to change attributes
    }

    public void handleSearchSubmit(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();
        if (pButton == this.searchSubmitButton)
        {
            String search = this.searchInput.getText();

            List<Album> matches = Application.getInstance().getAlbumManager().getLoadedAlbums()
                    .stream()
                    .filter(a -> a.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());

            this.updateAlbumList(matches);
        }
    }

    public void handleAlbumCreate(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();
        if (pButton == this.albumCButton)
        {
            TextInputDialog cAlbumPrompt = new TextInputDialog("Album");
            cAlbumPrompt.setHeaderText("Enter a name for the album:");

            Optional<String> res = cAlbumPrompt.showAndWait();
            res.ifPresent(s -> Application.getInstance().getAlbumManager().createAlbum(s));
        }

        updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
    }

    public void handleAlbumRename(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();
        if (pButton == this.albumRNButton)
        {
            TextInputDialog renameAlbumPrompt = new TextInputDialog("Album");
            renameAlbumPrompt.setHeaderText("Enter a new name for the album:");

            Album selectedAlbum = this.albumList.getSelectionModel().getSelectedItem();

            Optional<String> res = renameAlbumPrompt.showAndWait();
            res.ifPresent(s -> Application.getInstance().getAlbumManager().renameAlbum(selectedAlbum, s));
        }

        updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
    }

    public void handleAlbumDelete(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();
        if (pButton == this.albumDelButton)
        {
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("Confirm Delete");
            confirmDelete.setHeaderText("Are you sure you want to delete this album?");
            confirmDelete.setContentText("This action cannot be undone.");

            ButtonType buttonYes = new ButtonType("Confirm");
            ButtonType buttonNo = new ButtonType("Cancel");
            confirmDelete.getButtonTypes().setAll(buttonYes, buttonNo);

            confirmDelete.showAndWait().ifPresent(response ->
            {
                if (response == buttonYes)
                {
                    Album album = this.albumList.getSelectionModel().getSelectedItem();
                    Application.getInstance().getAlbumManager().deleteAlbum(album);
                }
            });
        }

        updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
    }

    public void handleAlbumOpen(ActionEvent e) {
        // Request album data from backend and update photo javafx.view
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

    public void handleChangeCaption(ActionEvent e)
    {
        Button pButton = (Button)e.getSource();
        if (pButton == changeCapButton) {
            TextInputDialog captionPrompt = new TextInputDialog("Caption");
            captionPrompt.setHeaderText("Enter a new caption for the photo");
            captionPrompt.showAndWait();
            // tell the app to change the caption when this closes
        }
    }

    public void handleLogout(ActionEvent e)
    {
        Application.getInstance().logout();
    }
}
