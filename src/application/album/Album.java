package application.album;

import application.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Album
{
    private String name;
    private final File albumFile;
    private final List<Photo> photos = new ArrayList<>();

    public Album(String name)
    {
        File userFile = Application.getInstance().getSession().getUserFile();
        File albumFile = new File(userFile, name);

        if (!albumFile.exists())
        {
            if (!albumFile.mkdir())
            {
                throw new IllegalStateException();
            }
        }

        this.name = name;
        this.albumFile = albumFile;
    }

    public void rename(String newName)
    {
        this.name = newName;

        File userFile = Application.getInstance().getSession().getUserFile();
        File newAlbumFile = new File(userFile, newName);

        if (!this.albumFile.renameTo(newAlbumFile))
        {
            throw new IllegalStateException();
        }
    }

    public String getName()
    {
        return this.name;
    }

    public File getAlbumFile()
    {
        return this.albumFile;
    }
}
