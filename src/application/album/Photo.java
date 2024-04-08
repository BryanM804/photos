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
    private final File photoFile;
    private final File dataFile;
    private final LocalDateTime timestamp;
    private final Map<String, String> tags = new HashMap<>();

    private String caption;

    public Photo(File photoFile)
    {
        this.photoFile = photoFile;
        this.dataFile = new File(photoFile.getParent(), photoFile.getName() + ".dat");
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(photoFile.lastModified())
                .truncatedTo(ChronoUnit.SECONDS), ZoneId.systemDefault());
    }

    public void serialize()
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(this.dataFile)
            );

            oos.writeObject(this);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void deserialize()
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(this.dataFile)
            );

            Photo $this = (Photo) ois.readObject();

            this.tags.putAll($this.tags);
        } catch (IOException | ClassNotFoundException e)
        {
            System.out.printf("Warning: No data found for photo %s. ");
        }
    }

    public File getPhotoFile()
    {
        return this.photoFile;
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
