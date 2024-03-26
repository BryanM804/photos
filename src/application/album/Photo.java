package application.album;

import java.io.File;
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
    private final Map<String, String> tags;

    public Photo(File photoFile)
    {
        this.photoFile = photoFile;
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(photoFile.lastModified())
                .truncatedTo(ChronoUnit.SECONDS), ZoneId.systemDefault());
        this.tags = new HashMap<>();
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
}
