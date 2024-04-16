package org.sbd;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Remote extends Thread{
    protected static final int port = 49187;
    private ServerSocket serverSocket;
    private Error serverError = new Error();
    private Socket socket = null;
    private BufferedWriter output = null;
    private BufferedReader input = null;

    public Remote() {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException ex) {
            serverError = new Error(ex);
            serverError.initException();
        }
    }

    public void newError(Exception ex) {
        serverError = new Error(ex);
        serverError.initException();
        serverError.executeException();
    }

    @Override
    public void run() {
        if (serverError.isError())
            serverError.executeException();

        System.out.println("SDB is listening on port " + port);

        while (!serverError.isError()) {

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                serverError = new Error(e);
                serverError.initException();
                continue;
            }

            System.out.println("New client connected");

            try {
                output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                serverError = new Error(e);
                serverError.initException();
                continue;
            }

            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                serverError = new Error(e);
                serverError.initException();
                continue;
            }

            try {
                String word = input.readLine();
                System.out.println(new Date().toString() + ": Executing command : " + word + "\n");
                output.write(execute(word));
                output.flush();
                execute(word);
            } catch (IOException e) {
                serverError = new Error(e);
                serverError.initException();
                continue;
            }


            //PrintWriter writer = new PrintWriter(output, true);

            // writer.println(new Date().toString());
        }

        serverError.executeException();
    }

    String execute(String command) {
        String output = new String();
        String[] lines = command.split(";");
        for (String line : lines){
            String[] words = line.split(" ");

            switch (words[0]) {
                case "read":
                    output += StaticDataBase.db.get(words[1]);
                    break;
                case "write":
                    output += "ALL_IS_GOOD\n";
                    StaticDataBase.db.put(words[1], words[2]);
                    break;
                case "reload":
                    output += "ALL_IS_GOOD\n";
                    StaticDataBase.db.readData();
                    break;
                case "safe":
                    output += "ALL_IS_GOOD\n";
                    StaticDataBase.db.writeData();
                    break;
            }
        }
        return output;
    }
}
