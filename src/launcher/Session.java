package launcher;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class Session
{
    private final String username;
    private final File userFile;

    private final boolean admin;

    public Session(String username)
    {
        this.username = username;

        File workingDir = new File("").getAbsoluteFile();
        File userFile = new File(workingDir, "data/" + username);

        if (!userFile.exists())
        {
            if (!userFile.mkdir())
            {
                throw new IllegalStateException();
            }
        }

        this.userFile = userFile;

        this.admin = username.equalsIgnoreCase("admin");
    }

    public String getUsername()
    {
        return this.username;
    }

    public File getUserFile()
    {
        return this.userFile;
    }

    public boolean isAdmin()
    {
        return this.admin;
    }

    public List<String> getAllUsernames() {
        if (!this.isAdmin()) return null;

        File workingDir = new File("").getAbsoluteFile();
        File users = new File(workingDir, "data/");

        String [] userStrings = users.list();

        return Arrays.asList(userStrings);
    }
}
