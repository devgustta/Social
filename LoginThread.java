package org.application;

import org.json.JSONObject;
import org.telas.Telas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

public class LoginThread extends Thread{
    static String url = "jdbc:mysql://localhost:3306/login";
    static String userBD = "root";
    static String senha = "gustta37280";


    private DataInputStream entrada;
    private DataOutputStream saida;
    private Socket socket;
    private String title;
    private String email;
    private String passwd;
    private String nome;
    private String nick;
    private JSONObject obj;


    public LoginThread(Socket socket) throws IOException{
        this.socket = socket;
        entrada = new DataInputStream(socket.getInputStream());
        saida = new DataOutputStream(socket.getOutputStream());
    }

    public void LogIn(JSONObject obj) throws SQLException, IOException {
        String sql;
        PreparedStatement preparedStatement;
        String passwdHashed = "";
        String nick = "";
        //obj = new JSONObject(entrada.readUTF());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, userBD, senha);
            System.out.println("Conexão feita com sucesso1");

            email = obj.getString("email");
            sql = "SELECT passwd, nickName from user where email = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                passwdHashed = resultSet.getString("passwd");
                nick = resultSet.getString("nickName");
            }

        }catch(ClassNotFoundException e){
            System.out.println("Driver JDBC não encontrado: " + e);
        }

        obj.put("title", "rpasswd");
        obj.put("nick", nick);
        obj.put("passwd", passwdHashed);
        saida.writeUTF(obj.toString());
    }

    // Metodo de criação de conta
    public void signUp() throws IOException {
        String sql;
        PreparedStatement preparedStatement;
        int rowsAffected = 0;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, userBD, senha);
            System.out.println("Conexão feita com sucesso");

            sql = "SELECT email, nickName from user WHERE email = ? AND nickName = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, nick);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String tempEmail = resultSet.getString("email");
                String tempNick = resultSet.getString("nickName");
                Telas telas = new Telas();

                if (tempNick.equals(nick)) {
                    System.out.println("NickName em uso, tente outro");

                }
                if (tempEmail.equals(email)) {
                    System.out.println("Email em uso, Tente outro");
                }
                telas.SignUp();
            } else {
                sql = "INSERT into user (email, passwd, nome, nickName) VALUES (?,?,?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, this.email);
                preparedStatement.setString(2, this.passwd);
                preparedStatement.setString(3, this.nome);
                preparedStatement.setString(4, this.nick);
            }

            rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                saida.writeUTF("login");
            } else {
                System.out.println("Erro");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não encontrado: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true) {
            try {

                obj = new JSONObject(entrada.readUTF());
                title = obj.getString("title");

                if(title.equals("login")){
                    LogIn(obj);
                }else if(title.equals("signup")){
                    signUp();
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();

            }
        }
    }
}
