package javafx.dialogs;

import javax.lang.model.type.NullType;

import application.album.Photo;
import javafx.controller.InspectImageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

public class InspectImageDialog extends Dialog<NullType>{

    public InspectImageDialog(Window owner, Photo image) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/InspectImageDialog.fxml"));
       
        try {
            DialogPane root = loader.load();
            InspectImageController controller = loader.getController();
            controller.setImage(image);
            setDialogPane(root);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setTitle("Photo Details");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
