package application.album;

import application.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AlbumManager
{
    private final List<Album> loadedAlbums = new ArrayList<>();

    /**
     * Loads all photos from each album and creates their Java representative object
     */
    public void cacheAllAlbums()
    {
        File[] albums = Application.getInstance().getSession().getUserFile().listFiles();

        Map<File, Photo> filePhotoMap = new HashMap<>(); // hacky fix

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

            for (File dataFile : photos)
            {
                if (!dataFile.getName().endsWith(".dat"))
                {
                    continue;
                }

                Photo photo = Photo.deserialize(dataFile);

                if (filePhotoMap.containsKey(photo.getPhotoFile()))
                {
                    album.addPhoto(filePhotoMap.get(photo.getPhotoFile()));
                } else
                {
                    filePhotoMap.put(photo.getPhotoFile(), photo);
                    album.addPhoto(photo);
                }
            }

            this.loadedAlbums.add(album);
        }
    }

    public Album createAlbum(String name)
    {
        Album album = new Album(name);
        this.loadedAlbums.add(album);

        return album;
    }

    public void renameAlbum(Album album,
                            String newName)
    {
        album.rename(newName);
    }

    public void deleteAlbum(Album album)
    {
        // Have to delete subfiles first
        recursivelyDelete(album.getAlbumFile());

        try
        {
            Files.delete(album.getAlbumFile().toPath());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        this.loadedAlbums.remove(album);
    }

    public void recursivelyDelete(File folder)
    {
        if (folder.listFiles() == null)
        {
            return;
        }

        for (File subFile : folder.listFiles())
        {
            if (subFile.isDirectory())
            {
                recursivelyDelete(subFile);
            }

            if (subFile.getName().endsWith(".dat"))
            {
                if (!subFile.delete())
                {
                    throw new IllegalStateException();
                }
            }
        }
    }

    public Album getAlbumByName(String name) {
        for (Album a : loadedAlbums) {
            if (a.getName().equals(name)) 
                return a;
        }

        return null;
    }

    public List<Album> getLoadedAlbums()
    {
        return this.loadedAlbums;
    }

    public void save()
    {
        for (Album album : this.loadedAlbums)
        {
            for (Photo photo : album.getPhotos())
            {
                File dataFile = new File(album.getAlbumFile(), photo.getDataFile().getName());
                photo.serialize(dataFile);
            }
        }
    }
}
