package javafx.dialogs;

import java.util.List;

import javax.lang.model.type.NullType;

import application.album.Photo;
import javafx.controller.SlideShowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

public class SlideShowDialog extends Dialog<NullType>{

    public SlideShowDialog(Window owner, List<Photo> albumContents) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/SlideShowDialog.fxml"));
       
        try {
            DialogPane root = loader.load();
            SlideShowController controller = loader.getController();
            controller.setPhotos(albumContents);
            setDialogPane(root);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setTitle("Album Slideshow");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
