package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class FileUtil
{
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
