package org.application;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Threads  extends Thread{
    DataInputStream in;
    DataOutputStream saida;
    private Socket socket;
    private JSONObject obj;

    public Threads(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensagemGroup(String mensagem) throws IOException {
        for(Threads client: ServidorConexao.getClients()){
            if(client != this){
               client.saida.writeUTF(mensagem);
            }
        }
    }

    public void mensagemPtoP(String mensagem){
        //
    }

    public void run() {
        while (true) {
            try {

                String mensagem = in.readUTF();
                System.out.println(mensagem);
                enviarMensagemGroup(mensagem);


            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

}
