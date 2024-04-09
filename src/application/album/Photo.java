package application.album;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public final class Photo implements Serializable
{
    private static final long serialVersionUID = -2257026348827015659L;

    private File photoFile;
    private File dataFile;
    private final LocalDateTime timestamp;
    private final Map<String, String> tags = new HashMap<>();

    private String caption;

    public Photo(File photoFile,
                 File albumDirectory)
    {
        this.photoFile = photoFile;
        this.dataFile = new File(albumDirectory, photoFile.getName() + ".dat");
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(photoFile.lastModified())
                .truncatedTo(ChronoUnit.SECONDS), ZoneId.systemDefault());
    }

    public void serialize(File dataFile)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile)))
        {
            oos.writeObject(this);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Photo deserialize(File dataFile)
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile)))
        {
            return (Photo) ois.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public File getPhotoFile()
    {
        return this.photoFile;
    }

    public File getDataFile()
    {
        return this.dataFile;
    }

    public void setPhotoFile(File file)
    {
        this.photoFile = file;
    }

    public void setDataFile(File file)
    {
        this.dataFile = file;
    }

    public LocalDateTime getTimestamp()
    {
        return this.timestamp;
    }

    public Map<String, String> getTags()
    {
        return this.tags;
    }

    public String getCaption()
    {
        return this.caption;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public static boolean isPhoto(File file)
    {
        if (file.isDirectory())
        {
            return false;
        }

        try
        {
            String contentType = Files.probeContentType(file.toPath());

            if (contentType == null)
            {
                return false;
            }

            String rawType = contentType.split("/")[0];
            if (!rawType.equals("image"))
            {
                return false;
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return true;
    }
}
