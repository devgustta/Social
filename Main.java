package org.telas;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main{
     static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, IOException {
        Telas telas = new Telas();
        telas.Menu();
    }

}