package edu.iastate.a309.darkplatform.sockets;

import edu.iastate.a309.darkplatform.entity.Entity;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.serialization.DPDatabase;
import edu.iastate.a309.darkplatform.serialization.DPObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements Runnable {

    private ServerSocket serverSocket;

    private int port;

    private String lobbyName;

    private static final int MAX_CLIENTS = 4;
    private List<Socket> clients;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private List<Entity> lobbyEnts;
    private DPObject[] clientPlayers;

    public Lobby(int port, String name) {
        this.port = port;
        lobbyName = name;
        clients = new ArrayList<Socket>();
        lobbyEnts = new ArrayList<Entity>();
        clientPlayers = new DPObject[MAX_CLIENTS];

        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void transferHost() {

    }

    public void changeHost() {

    }

    public void updateHost() {

    }

    public void run() {
        while (serverSocket != null) {
            if (clients.size() < MAX_CLIENTS) {
                try {
                    System.out.println("Looking for clients");
                    Socket client = serverSocket.accept();
                    System.out.println("Found a client at: " + client.getRemoteSocketAddress());
                    clients.add(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized byte[][] read() {
        if (clients.size() == 2) {
            new Integer(5);
        }
        byte[][] data = null;
        try {
            data = new byte[clients.size()][];
            int i = 0;
            for (Socket client : clients) {
                inputStream = new DataInputStream(client.getInputStream());
                data[i] = new byte[inputStream.available()];
                inputStream.read(data[i]);
                DPDatabase database = DPDatabase.deserialize(data[i]);
                //System.out.println(database);
                if (database != null && database.objects.size() > 0)
                    clientPlayers[i] = database.objects.get(0);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public synchronized void write() {
        int i = 0;
        for (Socket client : clients) {
            try {
                DPDatabase lobbyDatabase = new DPDatabase("To Client");
                for (Entity e : lobbyEnts) {
                    lobbyDatabase.addObject(e.serialize());
                }
                for (int k = 0; k < clients.size(); k++) {
                    if (i == k) {
                        continue;
                    } else {
                        if (clientPlayers[k] != null)
                            lobbyDatabase.addObject(clientPlayers[k]);
                    }
                }
                byte[] data = new byte[lobbyDatabase.getSize()];
                lobbyDatabase.getBytes(data, 0);

                outputStream = new DataOutputStream(client.getOutputStream());
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    public synchronized void addEnt(Entity entity) {
        lobbyEnts.add(entity);
    }

    public void update(InputHandler input) {
        for (Entity e : lobbyEnts) {
            e.update(input);
        }
    }
}
