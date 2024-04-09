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
import javafx.dialogs.InspectImageDialog;
import javafx.dialogs.SearchDialog;
import javafx.dialogs.SlideShowDialog;
import javafx.dialogs.TagDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * @author Bryan Mulholland
 */
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
    @FXML Button inspectImageButton;
    @FXML Button slideShowButton;
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

    /**
     * @return the instance of the controller
     */
    public static ApplicationController getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("No active controller");
        }

        return instance;
    }

    /**
     * Adds listeners to the lists that show/hide buttons when they can/cannot be used.
     * Sets the cell factory for the photo list
     */
    @FXML
    public void initialize()
    {
        this.albumList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    albumRNButton.setVisible(newValue != null);
                    albumDelButton.setVisible(newValue != null);
                    addPhotoButton.setVisible(newValue != null);
                    slideShowButton.setVisible(newValue != null);
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
                inspectImageButton.setVisible(newValue != null);
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

    /**
     * Updates the ListView of albums
     * @param albums
     */
    public void updateAlbumList(List<Album> albums)
    {
        ObservableList<Album> displayAlbums = FXCollections.observableArrayList(albums);
        this.albumList.setItems(displayAlbums);
    }

    /**
     * Updates the ListView of tags for the selected photo
     * @param selectedPhoto
     */
    public void updateTagList(Photo selectedPhoto) {
        List<String> tagStrings = new ArrayList<>();
        selectedPhoto.getTags().entrySet().forEach(entry -> {
            tagStrings.add(entry.getKey() + ": " + entry.getValue());
        });

        ObservableList<String> displayTags = FXCollections.observableArrayList(tagStrings);
        this.tagList.setItems(displayTags);
    }

    /**
     * Updates the ListView of photos with the given photos
     * @param photos
     */
    public void updatePhotoList(List<Photo> photos) {
        ObservableList<Photo> displayPhotos = FXCollections.observableArrayList(photos);
        this.photoList.setItems(displayPhotos);
    }

    /**
     * Opens the search dialog
     * @param e
     */
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

    /**
     * Prompts the user for an album name and creates an album if the name is unique for that user
     * @param e
     */
    public void handleAlbumCreate(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();
        if (pButton == this.albumCButton)
        {
            TextInputDialog cAlbumPrompt = new TextInputDialog("Album");
            cAlbumPrompt.setTitle("Create Album");
            cAlbumPrompt.setHeaderText("Enter a name for the album:");

            Optional<String> res = cAlbumPrompt.showAndWait();
            res.ifPresent(s -> {
                if (Application.getInstance().getAlbumManager().getAlbumByName(s) != null) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Album name taken");
                    errorAlert.setHeaderText("Album name already in use!");
                    errorAlert.initOwner(pButton.getScene().getWindow());
                    errorAlert.showAndWait();
                } else {
                    Application.getInstance().getAlbumManager().createAlbum(s);
                }
            });
        }

        updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
    }

    /**
     * Prompts the user for a new album name and changes the name
     * @param e
     */
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

    /**
     * Prompts the user for confirmation then deletes the selected album
     * @param e
     */
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

    /**
     * Opens the add tag dialog for the selected photo
     * @param e
     */
    public void handleAddTag(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == addTagButton) {
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
            
            TagDialog tDialog = new TagDialog(pButton.getScene().getWindow());
            Optional<String[]> res = tDialog.showAndWait();
            res.ifPresent(s -> {
                selectedPhoto.addTag(s);
                this.updateTagList(selectedPhoto);
            }); 
        }
    }

    /**
     * Removes the selected tag from the selected photo
     * @param e
     */
    public void handleRemoveTag(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();

        if (pButton == remTagButton) {
            String selectedTag = tagList.getSelectionModel().getSelectedItem();
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();

            String[] tagData = selectedTag.split(":");

            selectedPhoto.removeTag(new String[] {tagData[0], tagData[1].substring(1)});

            this.updateTagList(selectedPhoto);
        }
    }

    /**
     * Opens a file chooser for the user to add an image to the selected album
     * @param e
     */
    public void handleAddPhoto(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == addPhotoButton) {
            FileChooser photoPicker = new FileChooser();
            File photoFile = photoPicker.showOpenDialog(pButton.getScene().getWindow());

            if (photoFile != null) {
                Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
                selectedAlbum.addPhoto(new Photo(photoFile, selectedAlbum.getAlbumFile()));
                updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
                updatePhotoList(selectedAlbum.getPhotos());
            }
        }
    }

    /**
     * Removes the selected photo from the current open album
     * @param e
     */
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

    /**
     * Prompts the user for an album name to copy the selected photo to
     * @param e
     */
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
                    enteredAlbum.addPhoto(selectedPhoto);
                    updateAlbumList(Application.getInstance().getAlbumManager().getLoadedAlbums());
                }
            });
        }
    }

    /**
     * Prompts the user for an album name to move the selected photo to
     * @param e
     */
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

    /**
     * Prompts the user for a new caption for the selected photo
     * @param e
     */
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

    /**
     * Opens the slide show dialog for the selected album
     * @param e
     */
    public void handleSlideShowClick(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == slideShowButton) {
            Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();

            if (selectedAlbum.getPhotos().isEmpty())
            {
                return;
            }

            SlideShowDialog slideDialog = new SlideShowDialog(pButton.getScene().getWindow(), selectedAlbum.getPhotos());
            slideDialog.showAndWait();
        }
    }

    /**
     * Opens the inspect image dialog for the selected image
     * @param e
     */
    public void handleInspectImageClick(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == inspectImageButton) {
            Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
            InspectImageDialog imageDialog = new InspectImageDialog(pButton.getScene().getWindow(), selectedPhoto);
            imageDialog.showAndWait();
        }
    }

    /**
     * Tells the application to log the user out
     * @param e
     */
    public void handleLogout(ActionEvent e)
    {
        Button pButton = (Button) e.getSource();

        if (pButton == logoutButton) {
            Application.getInstance().logout();
        }
    }
}
