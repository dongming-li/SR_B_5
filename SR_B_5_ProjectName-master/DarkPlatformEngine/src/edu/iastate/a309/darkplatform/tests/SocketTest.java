package edu.iastate.a309.darkplatform.tests;

import edu.iastate.a309.darkplatform.input.InputHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketTest {

    private InputHandler input;
    private Socket client;
    private DataOutputStream outputStream;

    public SocketTest(InputHandler input) {
        this.input = input;

        try {
            client = new Socket("localhost", 12345);
            outputStream = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo() {
        try {
            if (input.keyboard.isKeyPressed(input.keyboard.keyW)) {
                outputStream.writeChars("Client pressed W key!\n");

            } else if (input.keyboard.isKeyPressed(input.keyboard.keyA)) {
                outputStream.writeChars("Client pressed A key!\n");

            } else if (input.keyboard.isKeyPressed(input.keyboard.keyS)) {
                outputStream.writeChars("Client pressed S key!\n");

            } else if (input.keyboard.isKeyPressed(input.keyboard.keyD)) {
                outputStream.writeChars("Client pressed D key!\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
