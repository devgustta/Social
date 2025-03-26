package org.application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorConexao {
    private static List<Threads> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8500);
        System.out.println("Esperando cliente se conectar");

        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado");
            Threads clientsThread = new Threads(socket);
            clients.add(clientsThread);
            clientsThread.start();
        }
    }

    public static List<Threads> getClients(){
        return clients;
    }

}
