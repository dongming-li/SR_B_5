package edu.iastate.a309.darkplatform;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class MainActivity extends Activity {

    private Socket socket;

    private static final int PORT = 12345;
    private static final String IP = "10.20.20.122";

    private static String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new ClientThread()).start();
        new Thread(new ClientWriter()).start();
    }

    public void onClick(View view){
        EditText et = (EditText) findViewById(R.id.edit_text);
        msg = et.getText().toString();
    }

    class ClientThread implements Runnable{
        @Override
        public void run() {
            try{
                InetAddress address = InetAddress.getByName(IP);

                socket = new Socket(IP, PORT);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    class ClientWriter implements Runnable{

        @Override
        public void run() {
            while (true) {
                PrintWriter out = null;
                if(!msg.matches("")){
                    try {
                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                        out.write(msg + "\n");
                        out.flush();
                        msg = "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
