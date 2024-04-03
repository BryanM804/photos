package application.album;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public final class Photo
{
    private final File photoFile;
    private final LocalDateTime timestamp;
    private final Map<String, String> tags = new HashMap<>();

    private String caption;

    public Photo(File photoFile)
    {
        this.photoFile = photoFile;
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(photoFile.lastModified())
                .truncatedTo(ChronoUnit.SECONDS), ZoneId.systemDefault());
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
