package org.telas;

import org.application.InOut;
import org.application.ServidorConexao;
import org.application.ServidorLogin;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Telas {
    Scanner in = new Scanner(System.in);
    private DataInputStream entrada;
    private DataOutputStream saida;
    private JSONObject obj;

    public Telas() throws IOException{
        Client client = new Client("localhost", 8000);
        entrada = new DataInputStream(client.getSocket().getInputStream());
        saida = new DataOutputStream(client.getSocket().getOutputStream());
    }


    public void Menu() throws IOException, SQLException {
        System.out.println("**************************");
        System.out.println(" 1 - LogIn");
        System.out.println(" 2 - SignIn");
        System.out.println("Escolha as opções: ");
        int op = in.nextInt();

        if(op == 1){
            logInMenu();
        }else if(op == 2){
            SignUp();
        }else{
            System.out.println("Opção inválida");
        }
    }

    public void logInMenu() throws SQLException, IOException {
        System.out.println("******************");
        System.out.println("E-mail: ");
        String email = in.next();
        System.out.println("Senha: ");
        String senha = in.next();

        //ServidorLogin servidorLogin = new ServidorLogin();
        String hashed = BCrypt.hashpw(senha, BCrypt.gensalt(12));
        try{
            obj = new JSONObject();
            obj.put("title","login");
            obj.put("email",email);
            obj.put("passwd",hashed);
            saida.writeUTF(obj.toString());

        }catch (JSONException e){
            System.out.println("Erro: " + e);
        }

        try {
            String jsonReceived = "";
            String passwd = "";
            String nick = "";
            if(entrada != null){
                 jsonReceived = entrada.readUTF();
            }

            obj = new JSONObject(jsonReceived);
            if(obj.getString("title").equals("rpasswd")){
                passwd = obj.getString("passwd");
                nick = obj.getString("nick");

                if(BCrypt.checkpw(senha, passwd)){
                    System.out.println("Logado com sucesso");
                    chat(nick);
                }else{
                    System.out.println("Credenciais Inválidas!! Tente novamente");
                }
            }
        }catch (JSONException e){
            System.out.println("Erro1: " + e);
        }

    }
    // tela de criação de contas
    public void SignUp() throws SQLException, IOException {
        System.out.println("******************");
        System.out.println("Digite seu nome");
        String nome = in.next();
        System.out.println("Informe um nick: ");
        String nick = in.next();
        System.out.println("Digite seu email: ");
        String email = in.next();
        System.out.println("Digite sua senha: ");
        String passwd = in.next();

        //String hashed = BCrypt.hashpw(passwd, BCrypt.gensalt(12));
        //ServidorLogin servidorLogin = new ServidorLogin(email,hashed,nome, nick, 2);

        obj = new JSONObject();
        obj.put("title", "signup");
        obj.put("name", nome);
        obj.put("nick", nick);
        obj.put("email", email);
        obj.put("passwd", passwd);

        saida.writeUTF(obj.toString());
        String result = entrada.readUTF();

        if (result.equals("login")){
            logInMenu();
        }

    }


    // tela principal
    public void MenuPrincipal() throws IOException{
        System.out.println("***************************");
        System.out.println(" 1 - Listar Amigos Online");
        System.out.println(" 2 - Adicionar amigos");
        System.out.println(" 3 - Mandar Mensagem");
        System.out.println("***************************");
        System.out.println("Escolha a opção: ");
        int op = in.nextInt();

        if(op == 1){
            //
        } else if(op == 2){
            addFriends();
        } else if(op == 3){
            //
        }

    }

    public void addFriends() throws IOException{
        System.out.println("**********************************");
        System.out.println(" Informe o Nick ou ID do usuario:");
        String info = in.next();

        if(!info.equals("")){

        }

    }

    // tela de chat
    public void chat(String nick) throws IOException {
        Client client = new Client("localhost", 8500);

        Thread output = new Thread(new InOut.Output(client.getSocket(),nick));
        Thread input = new Thread(new InOut.Input(client.getSocket()));
        input.start();
        output.start();

        System.out.println("**** CHAT ****");
        System.out.println("Escreva sua mensagem");


    }
}
