package edu.iastate.a309.darkplatform.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PhoneConnection implements Runnable {

    private static ServerSocket serverSocket;
    private Socket clientSocket;

    public static final int ANDROID_PORT = 12345;

    public DataInputStream inputDataStream;
    public PrintStream outputDataStream;

    public static boolean open = false;

    public PhoneConnection() {
        if (open) {
            try {
                System.out.println("Lookin");
                clientSocket = serverSocket.accept();

                System.out.println("Client connected. IP:" + clientSocket.getRemoteSocketAddress().toString());
                inputDataStream = new DataInputStream(clientSocket.getInputStream());
                outputDataStream = new PrintStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Found");
        }
    }

    public static void init() {
        try {
            serverSocket = new ServerSocket(ANDROID_PORT);

            open = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveInput() {
        String line = "";
        while (line != null && !line.matches("Exit")) {
            try {
                System.out.println("wow");

                line = inputDataStream.readLine();
                System.out.println(line);
                outputDataStream.println(line);
            } catch (IOException e) {
                e.printStackTrace();
                line = "Exit";
            }
        }
    }

    private void closeConnection() {
        try {
            inputDataStream.close();
            outputDataStream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        receiveInput();
    }
}
