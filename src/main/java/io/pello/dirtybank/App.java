package io.pello.dirtybank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * App
 * Main entry Point for the dirtybank app
 */
public class App 
{
    public static void main( String[] args )
    {
        Connection conn = null;

        do {
            System.out.println("Welcome to the Dirty Bank");
            Scanner reader = new Scanner(System.in);
            String line = "";
            int option = 0;



            // Check on db
            try {
            	Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:dirtybank.db";
                // create a connection to the database
                 conn = DriverManager.getConnection(url);
            boolean isNotLogged = true;
           do{

            // Validation /////
            System.out.println("Please enter your account name");
            String account = reader.nextLine();
            System.out.println("Please enter your password");
            String password = reader.nextLine();

            // Check if user exists
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM customer where login=? and password=?");
            statement.setString(1, account);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            
            if  (resultSet.next()) {
            	System.out.println("Welcome Mr. " + resultSet.getString("name"));
            	isNotLogged = false;
            } else {
            	System.err.println("Login incorrect");
            	isNotLogged = true;
            }
           } while (isNotLogged);

            } catch (Exception e) {
                System.err.println("Error connecting to db : " + e.getMessage());
                e.printStackTrace();
            }

        } while (true);

    }
}
