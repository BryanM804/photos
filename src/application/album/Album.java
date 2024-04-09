package application.album;

import application.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Album
{
    private String name;
    private File albumFile;
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

        this.albumFile = newAlbumFile;

        for (Photo photo : this.photos)
        {
            File newPhotoFile = new File(this.albumFile, photo.getPhotoFile().getName());
            photo.setPhotoFile(newPhotoFile);

            File newDataFile = new File(this.albumFile, photo.getDataFile().getName());
            photo.setDataFile(newDataFile);
        }
    }

    public void addPhoto(Photo photo)
    {
        this.photos.add(photo);
    }

    public void removePhoto(Photo photo)
    {
        try
        {
            if (photo.getDataFile().exists())
            {
                Files.delete(photo.getDataFile().toPath());
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        this.photos.remove(photo);
    }

    public void movePhoto(Album dst,
                          Photo photo)
    {
        removePhoto(photo);
        dst.addPhoto(photo);

        photo.setDataFile(new File(dst.getAlbumFile(), photo.getDataFile().getName()));
    }

    public int getNumPhotos()
    {
        return this.photos.size();
    }

    /**
     * @return An array of two {@link LocalDateTime}s where a[0] is the earliest and a[1] is the latest
     * or null if no photos in the album
     */
    public LocalDateTime[] getDateRanges()
    {
        if (this.photos.size() <= 0)
        {
            return null;
        }

        List<Photo> clone = new ArrayList<>(this.photos);

        clone.sort(Comparator.comparing(Photo::getTimestamp));

        LocalDateTime first = clone.get(0).getTimestamp();
        LocalDateTime last = clone.get(clone.size() - 1).getTimestamp();

        return new LocalDateTime[] { first, last };
    }

    public String getName()
    {
        return this.name;
    }

    public File getAlbumFile()
    {
        return this.albumFile;
    }

    public List<Photo> getPhotos()
    {
        return this.photos;
    }

    @Override
    public String toString()
    {
        if (this.getNumPhotos() == 0)
        {
            return this.name + "\n\tPhotos: 0";
        } else
        {
            return this.name + "\n\tPhotos: " + this.getNumPhotos() + "\n\tFrom: " + getDateRanges()[0] + "\n\tTo: " + getDateRanges()[1];
        }
    }
}
