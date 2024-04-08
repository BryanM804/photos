package javafx.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import application.Application;
import application.album.Album;
import application.album.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchDialogController {

    private boolean createdNewAlbum;

    @FXML Button searchSubmitButton;
    @FXML Button createAlbumButton;
    @FXML TextField searchInput;
    @FXML ListView<Photo> photoList;

    @FXML
    public void initialize() {
        createdNewAlbum = false;

        this.photoList.setCellFactory(listView -> new ListCell<Photo>() {
            ImageView imageView = new ImageView();

            @Override
            public void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
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

    public void updatePhotoList(List<Photo> newPhotos) {
        ObservableList<Photo>  displayPhotos = FXCollections.observableArrayList(newPhotos);
        this.photoList.setItems(displayPhotos);
    }

    public void handleSearchClick(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == searchSubmitButton) {
            String input = searchInput.getText();

            if (input.length() == 0) return;

            List<Photo> matches = new ArrayList<>();

            for (Album album : Application.getInstance().getAlbumManager().getLoadedAlbums())
            {
                middle:
                for (Photo photo : album.getPhotos())
                {
                    if (photo.getCaption() == null)
                    {
                        continue;
                    }

                    if (photo.getCaption().toLowerCase().contains(input.toLowerCase()))
                    {
                        matches.add(photo);
                        continue;
                    }

                    for (Map.Entry<String, String> tag : photo.getTags().entrySet())
                    {
                        if (tag.getValue().toLowerCase().contains(input.toLowerCase()))
                        {
                            matches.add(photo);
                            continue middle;
                        }
                    }
                }
            }

            updatePhotoList(matches);
        }
    }

    public void handleCreateAlbum(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == createAlbumButton) {
            if (photoList.getItems().size() > 0) {
                TextInputDialog cAlbumPrompt = new TextInputDialog("Album");
                cAlbumPrompt.setTitle("Create Album");
                cAlbumPrompt.setHeaderText("Enter a name for the album:");

                Optional<String> res = cAlbumPrompt.showAndWait();
                res.ifPresent(s ->
                {
                    Album newAlbum = Application.getInstance().getAlbumManager().createAlbum(s);
                    this.createdNewAlbum = true;

                    List<Photo> albumContents = photoList.getItems();

                    // copy photos over
                    albumContents.forEach(photo -> newAlbum.addPhoto(photo, true));
                });
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("No photos");
                errorAlert.setHeaderText("No photos to create an album with!");
                errorAlert.showAndWait();
            }
        }
    }

    public boolean convertResult(ButtonType buttonType) {
        return createdNewAlbum;
    }
    
}
