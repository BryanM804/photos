package application.album;

import application.Application;
import javafx.controller.ApplicationController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class AlbumManager
{
    private final List<Album> loadedAlbums = new ArrayList<>();

    /**
     * Loads all photos from each album and creates their Java representative object
     */
    public void cacheAllAlbums()
    {
        File[] albums = Application.getInstance().getSession().getUserFile().listFiles();

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

            File[] photos = albumFile.listFiles();

            if (photos == null)
            {
                continue;
            }

            Album album = new Album(albumFile.getName());

            for (File photoFile : photos)
            {
                if (!Photo.isPhoto(photoFile))
                {
                    continue;
                }

                Photo photo = new Photo(photoFile);

                System.out.println(photo.getTimestamp());

                album.addPhoto(photo);
            }

            this.loadedAlbums.add(album);
        }
    }

    public void createAlbum(String name)
    {
        Album album = new Album(name);
        this.loadedAlbums.add(album);
    }

    public void renameAlbum(Album album,
                            String newName)
    {
        album.rename(newName);
    }

    public void deleteAlbum(Album album)
    {
        try
        {
            Files.delete(album.getAlbumFile().toPath());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        this.loadedAlbums.remove(album);
    }

    public List<Album> getLoadedAlbums()
    {
        return this.loadedAlbums;
    }
}
