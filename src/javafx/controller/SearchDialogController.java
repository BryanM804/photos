package javafx.controller;

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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Bryan Mulholland
 * @author Andrew Bonasera
 */
public class SearchDialogController {

    private boolean createdNewAlbum;
    private boolean dateMode; // false for tag mode
    private boolean conjunction; // false for disjunction

    @FXML Button searchSubmitButton;
    @FXML Button createAlbumButton;
    @FXML ButtonBar dateBar;
    @FXML ButtonBar tag1Bar;
    @FXML ButtonBar tag2Bar;
    @FXML TextField tag1tag;
    @FXML TextField tag2tag;
    @FXML TextField tag1val;
    @FXML TextField tag2val;
    @FXML TextField fromDate;
    @FXML TextField toDate;
    @FXML RadioButton andRadio;
    @FXML RadioButton orRadio;
    @FXML Button dateButton;
    @FXML Button tagButton;
    @FXML ListView<Photo> photoList;

    /**
     * Sets the cell factory for the ListView of images
     */
    @FXML
    public void initialize() {
        createdNewAlbum = false;
        dateMode = false;
        conjunction = false;

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

    /**
     * Updates the ListView of photos with the given photos
     * @param newPhotos
     */
    public void updatePhotoList(List<Photo> newPhotos) {
        ObservableList<Photo>  displayPhotos = FXCollections.observableArrayList(newPhotos);
        this.photoList.setItems(displayPhotos);
    }

    /**
     * Searches for photos that meet the search input
     * @param e
     */
    public void handleSearchClick(ActionEvent e) {
        Button pButton = (Button) e.getSource();

        if (pButton == searchSubmitButton) {
            String input = searchSubmitButton.getText();

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

    /**
     * Creates an album from the current photos in the photo list
     * @param e
     */
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
                    albumContents.forEach(newAlbum::addPhoto);
                });
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("No photos");
                errorAlert.setHeaderText("No photos to create an album with!");
                errorAlert.showAndWait();
            }
        }
    }

    public void handleDateClick(ActionEvent e) {
        dateBar.setVisible(true);
        tag1Bar.setVisible(false);
        tag2Bar.setVisible(false);
        andRadio.setVisible(false);
        orRadio.setVisible(false);
        dateMode = true;

    }

    public void handleTagClick(ActionEvent e) {
        tag1Bar.setVisible(true);
        tag2Bar.setVisible(true);
        andRadio.setVisible(true);
        orRadio.setVisible(true);
        dateBar.setVisible(false);
        dateMode = false;

    }

    public void handleAndOrToggle(ActionEvent e) {
        RadioButton pButton = (RadioButton) e.getSource();

        if (pButton == andRadio) {
            orRadio.setSelected(false);
            conjunction = true;
        } else if (pButton == orRadio) {
            andRadio.setSelected(false);
            conjunction = false;
        }
    }

    /**
     * @param buttonType
     * @return true if a new album was created
     */
    public boolean convertResult(ButtonType buttonType) {
        return createdNewAlbum;
    }
    
}
