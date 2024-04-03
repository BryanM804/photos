package launcher;

import application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class Launcher extends javafx.application.Application
{
    private static Stage mainStage;
    private static Scene loginScene;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../javafx/view/Login.fxml"));

        VBox root = loader.load();
        loginScene = new Scene(root);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        primaryStage.show();

        mainStage = primaryStage;
    }

    public static void main()
    {
        // This is the javafx launch method not the one below
        launch();
    }

    public static void openLogin()
    {
        mainStage.setScene(loginScene);
        mainStage.setTitle("Login");
    }

    public static void launch(Session session)
    {
        Application application = new Application(session);
        application.openGUI(mainStage);
    }
}
