package launcher;

import application.Application;

import java.util.Scanner;

public final class Launcher
{
    public static void main()
    {
        // TODO Load up JavaFX login screen here

        String username; // These will be retrieved from the GUI, but for debug I'm getting them from the command line
        String password; // not implementing this

        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");

        username = input[0];

        Session session = new Session(username);
        launch(session);
    }

    public static void launch(Session session)
    {
        // TODO Close login GUI

        Application application = new Application(session);
        application.openGUI();
    }
}
