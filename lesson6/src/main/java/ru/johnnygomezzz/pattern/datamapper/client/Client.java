package ru.johnnygomezzz.pattern.datamapper.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        try {
            System.out.println("Socket is starting up...");
            socket = new Socket("Localhost", 8888);
            System.out.println(socket);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            AtomicBoolean isDrop = new AtomicBoolean(false);

            new Thread(() -> {
                try {
                    while (true) {
                        String incomingMessage = in.readUTF();
                        if(incomingMessage.contains("cmd Exit")) {
                            System.out.println(incomingMessage);
                            System.out.println("Please press Enter to close command line...");
                            isDrop.set(true);
                            break;
                        }
                        System.out.println(incomingMessage);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            while (true){
                if (isDrop.get()) {
                    System.out.println("Closing a connection....");
                    break;
                }
                System.out.println("Please type in a message");
                out.writeUTF(scanner.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
