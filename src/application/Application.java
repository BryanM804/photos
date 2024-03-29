package application;

import application.album.Album;
import application.album.Photo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import launcher.Launcher;
import launcher.Session;
import util.FileUtil;

import java.io.File;

public final class Application
{
    private static Application instance;
    private Session session;

    public Application(Session session)
    {
        instance = this;
        this.session = session;

        cacheAllAlbums();
    }

    /**
     * Loads all photos from each album and creates their Java representative object
     */
    private void cacheAllAlbums()
    {
        File[] albums = this.session.getUserFile().listFiles();

        if (albums == null)
        {
            throw new IllegalStateException();
        }

        for (File albumFile : albums)
        {
            if (!albumFile.isDirectory())
            {
                continue;
            }

            /**
             *  Album album = new Album();
             *
             *  Here we will make a new album and then add the photos to it
             *
             *  But I haven't figured out a good way to store the albums by user
             */

            File[] photos = albumFile.listFiles();

            for (File photoFile : photos)
            {
                if (!FileUtil.isPhoto(photoFile))
                {
                    continue;
                }

                Photo photo = new Photo(photoFile);

                System.out.println(photo.getTimestamp());

                /**
                 * album.addPhoto(photo);
                 */
            }
        }
    }

    /**
     * Opens the main application GUI
     */
    public void openGUI(Stage mainStage)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Launcher.class.getResource("../view/Application.fxml"));

        try {
            BorderPane root = (BorderPane)loader.load();
            Scene mainScene = new Scene(root);
            mainStage.setScene(mainScene);
            mainStage.setTitle("Photos");
            mainStage.setX(300.0);

        } catch(Exception e) {
            System.out.println(e); // handle this better later
        }
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
        if (session == null)
        {
            throw new IllegalStateException("No active session");
        }

        return this.session;
    }

    /**
     * Closes the application, logs the user out, and displays the login screen
     */
    public void logout()
    {
        instance = null;
        session = null;

        Launcher.openLogin();
    }
}
