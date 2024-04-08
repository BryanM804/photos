package javafx.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.Application;
import application.album.Album;
import application.album.AlbumManager;
import application.album.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.dialogs.SearchDialog;
import javafx.dialogs.TagDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ApplicationController {
    
    private static ApplicationController instance;

    @FXML Button albumCButton;
    @FXML Button albumRNButton;
    @FXML Button albumDelButton;
    @FXML Button logoutButton;
    @FXML Button addTagButton;
    @FXML Button addPhotoButton;
    @FXML Button remTagButton;
    @FXML Button remPhotoButton;
    @FXML Button copyPhotoButton;
    @FXML Button movePhotoButton;
    @FXML Button searchButton;
    @FXML Button changeCapButton;
    @FXML ListView<Album> albumList;
    @FXML ListView<String> tagList;
    @FXML ListView<Photo> photoList;
    @FXML VBox photoBar;
    @FXML Label tagLabel;

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
        this.albumList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    albumRNButton.setVisible(newValue != null);
                    albumDelButton.setVisible(newValue != null);
                    addPhotoButton.setVisible(newValue != null);
                    photoBar.setVisible(newValue != null);
                    if (newValue != null) {
                        updatePhotoList(albumList.getSelectionModel().getSelectedItem().getPhotos());
                    }
                }
        );

        this.photoList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                addTagButton.setVisible(newValue != null);
                remPhotoButton.setVisible(newValue != null);
                copyPhotoButton.setVisible(newValue != null);
                movePhotoButton.setVisible(newValue != null);
                tagList.setVisible(newValue != null);
                tagLabel.setVisible(newValue != null);
                changeCapButton.setVisible(newValue != null);
            }
        );

        this.tagList.getSelectionModel().selectedItemProperty().addListener(
            (observably, oldValue, newValue) -> {
                remTagButton.setVisible(newValue != null);
            }
        );

        this.photoList.setCellFactory(listView -> new ListCell<Photo>()
        {
            final ImageView imageView = new ImageView();

            @Override
            public void updateItem(Photo photo, boolean empty)
            {
                super.updateItem(photo, empty);

                if (empty)
                {
                    setText(null);
                    setGraphic(null);
                } else
                {
                    Image image = new Image(photo.getPhotoFile().toURI().toString(),
                                            160, // width
                                            160, // height
                                            true, // preserve ratio
                                            true); // smooth rescaling
                    setPrefWidth(300);
                    setMaxWidth(300);
                    setWrapText(true);
                    imageView.setImage(image);
                    setGraphic(imageView);
                    setText(photo.getCaption());
                }
            }
        });
    }

    public void updateAlbumList(List<Album> albums)
    {
        ObservableList<Album> displayAlbums = FXCollections.observableArrayList(albums);
        this.albumList.setItems(displayAlbums);
    }

    public void updateTagList(Photo selectedPhoto) {
        List<String> tagStrings = new ArrayList<>();
        selectedPhoto.getTags().entrySet().forEach(entry -> {
            tagStrings.add(entry.getKey() + ": " + entry.getValue());
        });

        ObservableList<String> displayTags = FXCollections.observableArrayList(tagStrings);
        this.tagList.setItems(displayTags);
    }

    public void updatePhotoList(List<Photo> photos) {
        ObservableList<Photo> displayPhotos = FXCollections.observableArrayList(photos);
        this.photoList.setItems(displayPhotos);
    }

    public void handleSearchClick(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == searchButton) {
            SearchDialog searchDialog = new SearchDialog(pButton.getScene().getWindow());
            Optional<Boolean> res = searchDialog.showAndWait();

            res.ifPresent(b -> {
                if (b) updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
            });
        }
    }

    public void handleAlbumCreate(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();
        if (pButton == this.albumCButton)
        {
            TextInputDialog cAlbumPrompt = new TextInputDialog("Album");
            cAlbumPrompt.setTitle("Create Album");
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
            Album selectedAlbum = this.albumList.getSelectionModel().getSelectedItem();

            TextInputDialog renameAlbumPrompt = new TextInputDialog(selectedAlbum.getName());
            renameAlbumPrompt.setHeaderText("Enter a new name for the album:");

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

    public void handleAddTag(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == addTagButton) {
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
            
            TagDialog tDialog = new TagDialog(pButton.getScene().getWindow());
            Optional<String[]> res = tDialog.showAndWait();
            res.ifPresent(s -> System.out.println(s)); 
            // If the tag type exists it will do nothing if not it adds
            // to the list of tag types
        }
    }

    public void handleRemoveTag(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();

        if (pButton == remTagButton) {
            
        }
    }

    public void handleAddPhoto(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == addPhotoButton) {
            FileChooser photoPicker = new FileChooser();
            File photoFile = photoPicker.showOpenDialog(pButton.getScene().getWindow());

            if (photoFile != null) {
                Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
                selectedAlbum.addPhoto(new Photo(photoFile), false);
                updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
                updatePhotoList(selectedAlbum.getPhotos());
            }
        }
    }

    public void handleRemovePhoto(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == remPhotoButton) {
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
            Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();

            selectedAlbum.removePhoto(selectedPhoto);
            updatePhotoList(selectedAlbum.getPhotos());
            updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
        }
    }

    public void handleCopyPhoto(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == copyPhotoButton) {
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();

            TextInputDialog destDialog = new TextInputDialog();
            destDialog.setTitle("Copy photo to album");
            destDialog.setContentText("Enter the name of the album you would like to copy to");
            destDialog.setHeaderText(null);
            Optional<String> res = destDialog.showAndWait();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid album entry!");
            errorAlert.setTitle("Invalid album");

            res.ifPresent(s ->
            {
                Album enteredAlbum = Application.getInstance().getAlbumManager().getAlbumByName(s);
                if (enteredAlbum == null) {
                    errorAlert.showAndWait();
                } else {
                    enteredAlbum.addPhoto(selectedPhoto, true);
                    updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
                }
            });
        }
    }

    public void handleMovePhoto(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == movePhotoButton) {
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
            Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();

            TextInputDialog destDialog = new TextInputDialog();
            destDialog.setTitle("Copy photo to album");
            destDialog.setContentText("Enter the name of the album you would like to move to");
            destDialog.setHeaderText(null);
            Optional<String> res = destDialog.showAndWait();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid album entry!");
            errorAlert.setTitle("Invalid album");

            res.ifPresent(s -> {
                Album enteredAlbum = Application.getInstance().getAlbumManager().getAlbumByName(s);
                if (enteredAlbum == null) {
                    errorAlert.showAndWait();
                } else {
                    selectedAlbum.movePhoto(enteredAlbum, selectedPhoto);
                    updatePhotoList(selectedAlbum.getPhotos());
                    updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
                }
            });
        }
    }

    public void handleChangeCaption(ActionEvent e)
    {
        Button pButton = (Button)e.getSource();
        if (pButton == changeCapButton) {
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
            TextInputDialog captionPrompt = new TextInputDialog(selectedPhoto.getCaption());
            captionPrompt.setTitle("Change Caption");
            captionPrompt.setHeaderText("Enter a new caption for the photo");
            Optional<String> res = captionPrompt.showAndWait();

            res.ifPresent(s -> {
                selectedPhoto.setCaption(s);
                Album selectedAlbum = this.albumList.getSelectionModel().getSelectedItem();
                updatePhotoList(selectedAlbum.getPhotos());
            });
        }
    }

    public void handleLogout(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();

        if (pButton == logoutButton) {
            Application.getInstance().logout();
        }
    }
}
