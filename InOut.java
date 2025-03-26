package org.application;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class InOut {
     public static Scanner in = new Scanner(System.in);

    public static class Input implements Runnable{
        DataInputStream entrada;

        public Input(Socket socket) throws IOException {
            this.entrada = new DataInputStream(socket.getInputStream());
        }

        String msg;
        String name;

        // metodo que roda primeiro toda vez que a thread é executada novamente
        @Override
        public void run() {

            while (true){
                try {
                    // Sempre pegar o dado de entrada do outro usuario, instanciar um novo json e passar o dado de entrada para o json
                    String jsonReceived = entrada.readUTF();
                    JSONObject obj = new JSONObject(jsonReceived);
                    // depois de passado para o json posso passar para as variaveis de cada valor
                    name = obj.getString("name");
                    msg = obj.getString("message");
                    System.out.println(name + ": " + msg);
                } catch (JSONException e){
                    System.out.println("Erro: " + e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


     public static class Output implements Runnable{
        DataOutputStream saida;
        JSONObject obj;
        String nick = "";
        public Output(Socket socket, String nick) throws IOException {
            this.saida = new DataOutputStream(socket.getOutputStream());
            this.nick = nick;
        }
        String msg;
      //  String name = "Alguem";
        // Metodo run roda primeiro toda vez que a thread roda
        @Override
        public void run(){

            while (true){
                obj = new JSONObject();
                msg = in.nextLine();
                obj.put("title", "msg");
                obj.put("name", nick);
                obj.put("message", msg);
                try {
                    saida.writeUTF(obj.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (JSONException e){
                    System.out.println("Erro: " + e);
                }
            }
        }
    }

}
