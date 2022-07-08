package capstone;


import java.util.*;

public class PASAppDriver {
	public static Scanner scan = new Scanner(System.in);

	public static String[] selectionArr = { "Create a new Customer Account.", "Get a policy quote and buy the policy.",
			"Cancel a specific policy", "File an accident claim against a policy.", "Search for a Customer account ",
			"Search for and display a specific policy", "Search for and display a specific claim",
			"Exit the PAS System" };
	public static InitializeDatabase database = new InitializeDatabase();
	public static CustomerAccount account = new CustomerAccount();
	
	public static Policy policy = new Policy();
	public static Vehicle vehicles = new Vehicle();
	public static Claims claims = new Claims();
	

	public static void main(String[] args) {
		
		database.createDatabase();
		
		PASAppDriver.MainMenu();
	}// end of main method

	
	// display the main menu of the program
	public static void MainMenu() {
		boolean isCheck = false;

		do {	// database object has been passed to these extended classes
			account.returnCreds(database);
			policy.returnCreds(database);
			vehicles.returnCreds(database);
			claims.returnCreds(database);
		try {
				String select;
				System.out.println(
						"============================================================================================");
				System.out.println("AUTOMOBILE INSURANCE POLICY AND CLAIMS ADMINISTRATION SYSTEM (PAS)");
				System.out.println(
						"============================================================================================");
				System.out.println("Welcome! Please select your option:");
				//displays the menu selections
				for (int index = 0; index < selectionArr.length; index++) {
					System.out.format("%6d %6s", (index + 1), selectionArr[index] + "\n");
				}
				
				System.out.print("Enter here: ");
				select = scan.nextLine();

				if (select.equals("1")) {
					System.out.println(
							"============================================================================================");
					System.out.println("You select Create a new Customer Account.");
					account.createAccount();
				} else if (select.equals("2")) {
					System.out.println(
							"============================================================================================");
					System.out.println("Get a policy quote and buy the policy.");
					policy.searchAccountNumber();
				} else if (select.equals("3")) {
					System.out.println(
							"============================================================================================");
					System.out.println("Cancel a specific policy.");
					policy.cancelSpecificPolicy();
				} else if (select.equals("4")) {
					System.out.println(
							"============================================================================================");
					System.out.println("File an accident claim against a policy.");
					claims.processClaims();
				} else if (select.equals("5")) { 
					System.out.println(
							"============================================================================================");
					System.out.println("Search for a Customer account");
					account.searchAccount();
				} else if (select.equals("6")) {
					System.out.println(
							"============================================================================================");
					System.out.println("Search for and display a specific policy.");
					policy.searchPolicy();
				} else if (select.equals("7")) {
					System.out.println("Search for and display a specific claim");
					claims.searchPolicyClaims();
				} else if (select.equals("8")) {
					System.out.println("Exit the PAS System");
					System.exit(0);
				}
				else {
					System.out.println("Invalid Input, Please enter 1-8 only.");
				}

		} catch (Exception e) {
			System.out.println("Invalid Input,try again ");
			scan.nextLine();
			e.printStackTrace();
		}
		} while (!isCheck);

	}


}	//end of PASAppDriver
