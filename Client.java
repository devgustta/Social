package org.telas;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;
    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public Socket getSocket(){
        return socket;
    }
    // meotodo de fechamento do socket
    public void fechar() throws IOException{
        if((socket != null) && !socket.isClosed()){
            socket.close();
        }
    }
}
