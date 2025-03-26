package org.application;

import org.json.JSONException;
import org.json.JSONObject;
import org.telas.Telas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ServidorLogin {
    private static List<LoginThread> clients = new ArrayList<>();
    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(8000);
        System.out.println("Esperando o cliente se conectar");

        while (true){
            Socket socket = server.accept();
            System.out.println("Cliente conectado");
            LoginThread newLogin = new LoginThread(socket);
            clients.add(newLogin);
            newLogin.start();
        }

    }


}
