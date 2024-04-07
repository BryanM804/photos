package javafx.dialogs;

import javafx.controller.TagDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

public class TagDialog extends Dialog<String[]>{

    public TagDialog(Window owner) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/TagDialog.fxml"));
       
        try {
            DialogPane root = loader.load();
            TagDialogController controller = loader.getController();
            setDialogPane(root);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);

            setResultConverter(controller::convertResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
