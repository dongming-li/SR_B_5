package edu.iastate.a309.darkplatform.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket socket;
    private int port;
    private String ip;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;

        try {
            socket = new Socket(ip, port);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToLobby() {

    }

    public void pullLobbyDataFromServer() {

    }

    public byte[] getLobbyData() {
        byte[] data = null;
        try {
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void parseLobbyData() {

    }

    public void sendData(byte[] data) {
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
