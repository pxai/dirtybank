package io.pello.dirtybank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * App Main entry Point for the dirtybank app
 */
public class App {
	public static void main(String[] args) {
		Connection conn = null;

		do {
			System.out.println("Welcome to the Dirty Bank");
			Scanner reader = new Scanner(System.in);
			String line = "";
			float money = 0;
			int option = 0;

			// Check on db
			try {
				Class.forName("org.sqlite.JDBC");
				String url = "jdbc:sqlite:dirtybank.db";
				// create a connection to the database
				conn = DriverManager.getConnection(url);
				boolean isNotLogged = true;
				int customerId = -1;
				float balance = 0;
				ResultSet resultSet = null;
				PreparedStatement statement = null;

				// Login process
				do {

					// Validation /////
					System.out.println("Please enter your account name");
					String account = reader.nextLine();
					System.out.println("Please enter your password");
					String password = reader.nextLine();
					

					// Check if user exists
					statement = conn
							.prepareStatement("SELECT * FROM customer where login=? and password=?");
					statement.setString(1, account);
					statement.setString(2, password);
					resultSet = statement.executeQuery();

					if (resultSet.next()) {
						customerId = resultSet.getInt("id");

						System.out.println("Welcome Mr. " + resultSet.getString("name") + "["+customerId+"]");
						isNotLogged = false;
						// Get account info
						statement = conn
								.prepareStatement("SELECT * FROM account where customerid=?");
						statement.setInt(1, customerId);
						resultSet = statement.executeQuery();
						if (resultSet.next()) {
							balance = resultSet.getFloat("balance");
							System.out.println("Your current balance is: " + balance);
						} else {
							System.out.println("Account not found");
						}
					} else {
						System.err.println("Login incorrect");
						isNotLogged = true;
					}
				} while (isNotLogged);

				// Once logged, show menu and choose operation
				do {
					System.out.println("Choose operation: ");
					System.out.println("1. See available deposit");
					System.out.println("2. Deposit money");
					System.out.println("3. Withdraw money");
					System.out.println("4. Exit");
					System.out.print("Your choice");
					
					line = reader.nextLine();
					switch (line) {
					case "1":
						System.out.println("Showing deposit");
						statement = conn
								.prepareStatement("SELECT * FROM account where customerid=?");
						statement.setInt(1, customerId);
						resultSet = statement.executeQuery();
						if (resultSet.next()) {
							balance = resultSet.getFloat("balance");
							System.out.println("Your current balance is: " + balance);
						} else {
							System.out.println("Account not found");
						}
						
						break;
					case "2":
						System.out.println("Deposit money. \n How much?");
						line = reader.nextLine();
						balance = balance + Float.parseFloat(line);
						statement = conn
								.prepareStatement("update account set balance=? where customerid=?");
						statement.setFloat(1, balance);
						statement.setInt(2, customerId);
						statement.executeUpdate();
						System.out.println("Done. New balance: " + balance);
						break;
					case "3":
						System.out.println("Withdraw money. \n How much?");
						line = reader.nextLine();
						balance = balance - Float.parseFloat(line);
						statement = conn
								.prepareStatement("update account set balance=? where customerid=?");
						statement.setFloat(1, balance);
						statement.setInt(2, customerId);
						statement.executeUpdate();
						System.out.println("Done. New balance: " + balance);
						break;
					case "4":
						System.out.println("Login out");
						isNotLogged = true;
						break;
					default:	
						
						System.out.println("Incorrect option, try again");
						break;
					}
				} while (!isNotLogged);
			} catch (Exception e) {
				System.err.println("Error connecting to db : " + e.getMessage());
				e.printStackTrace();
			}

		} while (true);

	}
}
