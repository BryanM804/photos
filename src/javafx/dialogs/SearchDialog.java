package javafx.dialogs;

import javafx.controller.SearchDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

public class SearchDialog extends Dialog<Boolean>{
    
    public SearchDialog(Window owner) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/SearchDialog.fxml"));
       
        try {
            DialogPane root = loader.load();
            SearchDialogController controller = loader.getController();
            setDialogPane(root);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setTitle("Search for photos");

            setResultConverter(controller::convertResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
