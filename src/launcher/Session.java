package launcher;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Session implements Serializable
{
    private static final long serialVersionUID = -2257026348827015659L;

    private final String username;
    private final File userFile;
    private final File userDataFile;
    private final boolean userExists;

    private boolean isFirstTime;

    private final boolean admin;

    private final List<String> tags = new ArrayList<>();

    public Session(String username)
    {
        this.username = username;

        File workingDir = new File("").getAbsoluteFile();
        File userFile = new File(workingDir, "data/" + username);

        this.userDataFile = new File(userFile, username + ".dat");

        this.userFile = userFile;

        this.userExists = userFile.exists();

        this.admin = username.equalsIgnoreCase("admin");

        this.tags.add("Location");
        this.tags.add("Person");
    }

    public String getUsername()
    {
        return this.username;
    }

    public File getUserFile()
    {
        return this.userFile;
    }

    public File getUserDataFile()
    {
        return this.userDataFile;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void addTag(String newTag) {
        this.tags.add(newTag);
    }

    public boolean isValidUser() {
        return userExists;
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

    public void createUserFile(String username) {
        if (!this.isAdmin()) return;

        File workingDir = new File("").getAbsoluteFile();
        File userFile = new File(workingDir, "data/" + username);

        if (!userFile.mkdir())
        {
            throw new IllegalStateException();
        }
    }

    public void deleteUserFile(String username) {
        if (!this.isAdmin()) return;

        File workingDir = new File("").getAbsoluteFile();
        File userFile = new File(workingDir, "data/" + username);

        userFile.delete();
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

    public static Session deserialize(File dataFile)
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile)))
        {
            return (Session) ois.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean isFirstTime()
    {
        return this.isFirstTime;
    }

    public void setFirstTime(boolean firstTime)
    {
        this.isFirstTime = firstTime;
    }
}
