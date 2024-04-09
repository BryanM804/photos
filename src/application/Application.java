package application;

import application.album.AlbumManager;
import javafx.controller.ApplicationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import launcher.Launcher;
import launcher.Session;

import java.io.File;

public final class Application
{
    private static Application instance;

    private final AlbumManager albumManager;
    private Session session;

    public Application(Session session)
    {
        instance = this;
        this.session = session;

        this.albumManager = new AlbumManager();

        if (session.isFirstTime())
        {
            this.albumManager.cacheStockPhotos();
        } else
        {
            this.albumManager.cacheAllAlbums();
        }
    }

    /**
     * Opens the main application GUI
     */
    public void openGUI(Stage mainStage, boolean isAdmin)
    {
        FXMLLoader loader = new FXMLLoader();

        if (isAdmin) {
            loader.setLocation(Launcher.class.getResource("../javafx/view/Admin.fxml"));
        } else {
            loader.setLocation(Launcher.class.getResource("../javafx/view/Application.fxml"));
        }

        try
        {
            BorderPane root = loader.load();
            Scene mainScene = new Scene(root);
            mainStage.setScene(mainScene);
            mainStage.setTitle("Photos");
            mainStage.setX(300.0);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        if (!isAdmin) ApplicationController.getInstance().updateAlbumList(this.albumManager.getLoadedAlbums());
    }

    /**
     * @return The current application instance
     */
    public static Application getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("No active application");
        }

        return instance;
    }

    /**
     * @return The current user session
     */
    public Session getSession()
    {
        if (this.session == null)
        {
            throw new IllegalStateException("No active session");
        }

        return this.session;
    }

    public AlbumManager getAlbumManager()
    {
        if (this.albumManager == null)
        {
            throw new IllegalStateException("No active album manager");
        }

        return this.albumManager;
    }

    /**
     * Closes & saves the application, logs the user out, and displays the login screen
     */
    public void logout()
    {
        this.albumManager.save();
        File userDataFile = session.getUserDataFile();
        session.setFirstTime(false);
        session.serialize(userDataFile);

        instance = null;
        session = null;

        Launcher.openLogin();
    }
}
