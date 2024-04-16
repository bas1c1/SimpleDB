package org.sbd;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Remote server = new Remote();
        server.setUncaughtExceptionHandler((t, e) -> {
            Exception ex = new Exception(e);
            server.newError(ex);
        });

        server.setDaemon(true);
        server.start();

        StaticDataBase.db.readData();

        for (;;) {
            Scanner keyboard = new Scanner(System.in);
            String output = keyboard.nextLine();
            if (output == "stop") {
                System.out.println("stopped");
                server.newError(new Exception("stopped"));
            }
        }
    }
}