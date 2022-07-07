package capstone;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;


/*
 * Customer Account created here
Each Customer Account must have the following information:

A unique numeric 4-digit account number - ok
The account owner (first name, last name, and address) - ok
A list of policies owned by that account
A list of policy holders associated with the policies in the account
 * 
 * */

public class CustomerAccount {
	Scanner scan = new Scanner(System.in);

	public static PASAppDriver pas = new PASAppDriver();

	public void createAccount() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			System.out.println("Database connection successful!");

			String fName;
			String lName;
			String address;
			String accountNumber;
			boolean isCheck = true;
			String selector;

			do {
				System.out.print("Firstname: ");
				fName = scan.next();
				fName += scan.nextLine();
				if (validateEmptyString(fName) == true) {
					isCheck = false;
				} else if ((strInputNumValidator(fName)  || symbolValidator(fName)) == true) {
					System.err.println("Invalid input, Enter letters only.");
					isCheck = false;
				} else {
					isCheck = true;
				}
			} while (!isCheck);

			isCheck = false;

			do {
				System.out.print("Lastname: ");
				lName = scan.next();
				lName += scan.nextLine();
				if (validateEmptyString(lName) == true) {
					isCheck = false;
				} else if ((strInputNumValidator(lName)  || symbolValidator(lName)) == true) {
					System.err.println("Invalid input, Enter letters only.");
					isCheck = false;
				} else {
					isCheck = true;
				}
			} while (!isCheck);

			isCheck = false;
			do {
				System.out.print("Address: ");
				address = scan.nextLine();
				if (address == null || address.equals("") || address.isEmpty()) {
					System.err.println("Please do not leave blank");
					isCheck = false;
				} else {
					isCheck = true;
				}

			} while (!isCheck);

			accountNumber = accountNumberGenerator();

			System.out.println(
					"Do you want to add this customer to Database?\nEnter 1 if Yes or Enter any key to cancel.");

			System.out.print("Enter:");
			selector = scan.next();

			if (selector.equals("1")) {
				System.out.println("Inserting data to database.");
				Statement stmt = conn.createStatement();
				String addToDB = "INSERT INTO customers_create (account_number,first_name,last_name,address) VALUES ('"
						+ accountNumber + "','" + fName + "','" + lName + "','" + address + "')";

				stmt.execute(addToDB);
				System.out.println("Data successfully inserted!.");
				System.out.println("Account successfully created.");
				
//				String.format("%04d", accountNumber);
				System.out.println("Account #: " + accountNumber);

				String show = "SELECT * FROM customers_create ORDER BY account_number =" + accountNumber
						+ " DESC LIMIT 1";
				ResultSet rset = stmt.executeQuery(show);

				while (rset.next()) {

					int id = rset.getInt("account_number");
					String showFirstName = rset.getString("first_name");
					String showLastName = rset.getString("last_name");
					String showAddress = rset.getString("address");
					System.out.println(
							"===================================Account details==========================================");
					System.out.format("%6s %12s %12s %18s", "Account ID#", "First Name", "Last Name", "Address \n");
					System.out.format("%2s %15s %12s %18s", id, showFirstName, showLastName, showAddress + "\n");
					System.out.println(
							"============================================================================================");
				}
			} else {
				System.err.println("Account not saved.");
				PASAppDriver.MainMenu();
			}
		} catch (SQLException e) {
//			e.printStackTrace();
		System.err.println("Invalid input!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchAccount() {
		String firstName, lastName;
		// boolean isCheck = true;
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			Statement stmt = conn.createStatement();

			System.out.println("Database connection successful!");
			System.out.println(
					"============================================================================================");

			System.out.print("First Name: ");
			firstName = scan.next();
			firstName += scan.nextLine();
			System.out.print("Last Name: ");
			lastName = scan.next();
			lastName += scan.nextLine();
			
			System.out.println("Result: ");
			String showUsingAccountNumber = "SELECT * FROM customers_create where first_name ='" + firstName
					+ "' AND last_name = '" + lastName + "'";
			ResultSet result = stmt.executeQuery(showUsingAccountNumber);

			if (!result.next()) {
				
				System.out.println("No record found.");
				PASAppDriver.MainMenu();
				
			} else {
				System.out.println(
						"===================================Account details==========================================");
				System.out.format("%-11s %-10s %-9s %-10s", "Account No.", "\tFirst Name", "\tLast Name",
						"\tAddress" + "\n");
				int account_number = result.getInt("account_number");
				String first_Name = result.getString("first_Name");
				String last_Name = result.getString("last_Name");
				String address = result.getString("address");
				System.out.format("%-11s %-10s %-9s %-10s", account_number, first_Name, last_Name, address + "\n");
			}
			System.out.println("=======================================================================================");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}/// end of searchAccount

	// this method will generate account number
	public String accountNumberGenerator() {
		Random rand = new Random();
        Integer generateNum = rand.nextInt(9999);
        String accountNum = String.format("%04d", generateNum);
		return accountNum;
	}


	// checker if input has number
	public boolean strInputNumValidator(String input) {
		String str = input;
		boolean flagStatus = false;
		for (int i = 0; i < str.length(); i++) {
			Boolean flag = Character.isDigit(str.charAt(i));
			if (flag == true) {
				flagStatus = true;
			} else {
				flagStatus = false;
			}
		}
		return flagStatus;
	}

	// check if the input has symbol
	public boolean symbolValidator(String str) {
		String strChar = str;
		boolean output = false;
		for (int index = 0; index < str.length(); index++) {
			strChar = Character.toString(str.charAt(index));

			if (strChar.matches("[!@#%&*()'+,-.\\/:;<=>?[]^`{|}]]")) {
				output = true;
			} else {
				output = false;
			}
		}
		return output;
	}

	// check if of the input has empty
	public boolean validateEmptyString(String str) {
		Boolean isNotEmpty = false;
		
			if (str.equals("")) {
				isNotEmpty = true;
			} else {
				isNotEmpty = false;
			}
		return isNotEmpty;
	}
}// End
