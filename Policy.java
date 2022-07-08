package capstone;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

/*
 * Policy contents:
 * PolicyDates() includes Effectivity and Expiration date (+6 months)
 * 
 * 
 */


public class Policy extends CustomerAccount{
	Scanner scan = new Scanner(System.in);

	Calendar calendar = Calendar.getInstance();
	DecimalFormat df = new DecimalFormat("#.00");
	final int currentYear = calendar.get(Calendar.YEAR);
	final Integer currentDay = calendar.get(Calendar.DATE);
	final int currentMonth = calendar.get(Calendar.MONTH) + 1;
	public int month, day, year;

	LocalDate validDate;

	double policyPremiumAmount;
	java.sql.Date date;
	java.sql.Date dateExp;

	int numberOfVehicle;
	int policyID; //
	int policy_owner; //
	String policy_effective_date;
	String policy_expiration_date;
	double policy_premium;

	boolean isChecker = false;

	// this constructor is created and used as temporary storage before saving to database if the policy is purchased.
	public Policy(int policyID, int policy_owner, String policy_effective_date, String policy_expiration_date,
			double policy_premium) {
		this.policyID = policyID;
		this.policy_owner = policy_owner;

		this.policy_effective_date = policy_effective_date;
		this.policy_expiration_date = policy_expiration_date;
		this.policy_premium = policy_premium;
	}

	public Policy() {

	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getPolicyID() {
		return policyID;
	}

	public void setPolicyID(int policyID) {
		this.policyID = policyID;
	}

	public int getPolicyOwner() {
		return policy_owner;
	}

	public void setPolicyOwner(int policy_owner) {
		this.policy_owner = policy_owner;
	}

	public String getPolicy_effective_date() {
		return policy_effective_date;
	}

	public void setPolicy_effective_date(String policy_effective_date) {
		this.policy_effective_date = policy_effective_date;
	}

	public String getPolicy_expiration_date() {
		return policy_expiration_date;
	}

	public void setPolicy_expiration_date(String policy_expiration_date) {
		this.policy_expiration_date = policy_expiration_date;
	}

	public double getPolicy_premium() {
		return policy_premium;
	}

	public void setPolicy_premium(double policy_premium) {
		this.policy_premium = policy_premium;
	}

	boolean isNumber = false, isCheck = false;


	PolicyHolder policyholder = new PolicyHolder();

	// declared vehicle array here and will used in vehicleDetails();
	Vehicle[] vehicleArray;

	
	// SEARCH ACCOUNT NUMBER IF EXISTED, IF NOT WILL DISPLAY TO CREATE AN ACCOUNT FIRST
	public void searchAccountNumber() {

		int accountNumber;
		boolean isCheck = false;
		try {
			Connection conn = DriverManager.getConnection(DB_URL2, getUSER(), getPASS());
			Statement stmt = conn.createStatement();
			
			System.out.println("Enter Account number: ");
			accountNumber = scan.nextInt();


			ResultSet rset = stmt
					.executeQuery("SELECT * from customers_create where account_number = '" + accountNumber + "'");

			if (!rset.next()) { //IF ACCOUNT NUMBER NOT EXISTED, IT WILL DISPLAY TO CREATE AN ACCOUNT FIRST
				System.err.println("No result found.\nCREATE ACCOUNT FIRST!");
				PASAppDriver.MainMenu();

			} else {

				int policyOwned = 0;
				ResultSet policyOwnedCounter = stmt
						.executeQuery("select COUNT(policy_owner) from policy where policy_owner = " + accountNumber);

				while (policyOwnedCounter.next()) {
					policyOwned = policyOwnedCounter.getInt(1);
				}
				System.out.println("Account Number: " + accountNumber);
				System.out.println("Policy Owned: " + policyOwned);
				this.policy_owner = accountNumber;

				// Quotation starts here
				String policyHolderSelector;
				isCheck = false;

				do {
					System.out.println("\nProceed for the Quotation.\nEnter 1 to continue or Any key to cancel.");
					System.out.print("Enter here: ");
					scan.nextLine();
					policyHolderSelector = scan.nextLine();
//					if (policyHolderSelector.equals("1")) {
//						isCheck = true;
//					} 

					if (policyHolderSelector.equals("1")) {
						policyDate(); // effectivity & expiration date
						policyholder.setAccount_policy_owner(accountNumber); // used to set account policy owner 
						policyholder.createPolicyHolder(); // creating policy holder method
						vehicleDetails(); // add vehicle method
						purchasePolicy(); // purchase policy method
						PASAppDriver.MainMenu(); 
					} else {
						System.err.println("Action cancelled..");
						PASAppDriver.MainMenu();
						isCheck = true;
					}
				} while (!isCheck);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			scan.nextLine();
		}
	}

	
	// effectivity and expiration date created here
	public void policyDate() {

		String select = "";
		boolean isCheck = false;

		System.out.println("\nCURRENT DATE: " + String.format("%02d", currentMonth) + "-"
				+ String.format("%02d", currentDay) + "-" + currentYear);
		policyID = policyIDNumberGenerator();
		System.out.println("Policy ID #: " + String.format("%06d", this.policyID));
		isCheck = false;
		do {
			try {

				System.out.println(
						"\nPlease select:\n[1] to Set Now.\n[2] for Advance Booking.\nEnter Any Key to cancel.\nEnter here: ");
				select = scan.nextLine();

				if (select.equals("1")) {
					bookNowPolicyDate();
					isCheck = true;

				} else if (select.equals("2")) {
					advanceBookPolicyDate();
					isCheck = true;

				} else {
					System.err.println("Action Cancelled...");
					PASAppDriver.MainMenu();
					isCheck = false;
				}
			} catch (Exception e) {
//				System.err.println("Invalid...");
//				PASAppDriver.MainMenu();
				isCheck = false;
			}

		} while (!isCheck);
		System.out.println("==========================================================");

	}

	//Date is set Today if selected.
	public void bookNowPolicyDate() {

		LocalDate effectivityDate = LocalDate.now();
		this.policy_effective_date = effectivityDate.toString();

		LocalDate expirationDate = effectivityDate.plusMonths(6);
		this.policy_expiration_date = expirationDate.toString();

		
		System.out.println("POLICY DATE:");
		System.out.println("EFFECTIVITY DATE: " + effectivityDate);
		System.out.println("EXPIRATION DATE : " + expirationDate);

	}

	//Date is set depends on the user's input
	public void advanceBookPolicyDate() {
		Scanner scanner = new Scanner(System.in);
		String monthStr = "", dayStr = "", yearStr = "", policyDateInput = null;
	
		LocalDate advanceBookEffectivityPolicyDate;
		LocalDate NewexpirationDate;
		try {
		System.out.println("\nENTER EFFECTIVITY DATE (YYYY-MM-dd): ");
	
		policyDateInput = scanner.nextLine();
	
		//input is disected here
		yearStr = policyDateInput.substring(0,policyDateInput.indexOf("-"));
		monthStr = policyDateInput.substring(policyDateInput.indexOf("-")+1,policyDateInput.lastIndexOf("-"));
		dayStr = Integer.toString(policyDateInput.lastIndexOf("-") + 1);
		}catch (Exception e) {
			System.err.println("Invalid...");
		}
		
		
		this.year = Integer.parseInt(yearStr);
		this.month = Integer.parseInt(monthStr);
		this.day = Integer.parseInt(dayStr);
//		
		advanceBookEffectivityPolicyDate = LocalDate.of(year,month,day);
		NewexpirationDate = advanceBookEffectivityPolicyDate.plusMonths(6);

		this.policy_effective_date = advanceBookEffectivityPolicyDate.toString();
		this.policy_expiration_date = NewexpirationDate.toString();
		
		System.out.println("Effectivity Date: " + advanceBookEffectivityPolicyDate);
		System.out.println("Expiration Date : " + NewexpirationDate);
		
		
		isCheck = true;
	}

	public boolean checker(String input) {
		boolean result = false;
		try {
			int Value = Integer.parseInt(input);
			result = true;
		} catch (NumberFormatException e) {
			System.err.println("INVALID INPUT");
			result = false;
		}
		return result;
	}

	// whitespace check
	public boolean whiteSpaceCheck(String str) {
		boolean result = false;
		String strInput = null;
		strInput = str;

		for (int i = 0; i < strInput.length(); i++) {
			if (strInput.matches(" ") == false) {
//			
				result = true;
			} else {
//			
				result = false;
			}
		}

		return result;
	}


	public void vehicleDetails() {
		int VehicleCounter;
		double policyPremium = 0;
		int selector;
		boolean isCheck = false;

		try {
			do {
				System.out.println("\nHOW MANY CAR(S) YOU WANT TO ADD?");

				System.out.println("ENTER: ");

				VehicleCounter = intValidator(VehicleCounter = 0);

				this.numberOfVehicle = VehicleCounter;

				vehicleArray = new Vehicle[VehicleCounter];
				try {
					for (int count = 0; count < VehicleCounter; count++) {
					
						System.out.println("Add Vehicle # " + (count + 1));

						System.out.println("DO YOU WANT TO PROCEED?");
						System.out.println("Enter 1 if Yes, Enter any key to cancel.");
						System.out.println("Enter: ");
						selector = scan.nextInt();

						if (selector == 1) {

							vehicleArray[count] = new Vehicle();
							vehicleArray[count].toLoad();
							vehicleArray[count].getVPF();
							vehicleArray[count].setVehiclePremium(policyholder.getDriversLicenseIssued());
							policyPremium += vehicleArray[count].getVehiclePremium();

							System.out.println("Successfully added!");
							isCheck = true;
						}
						else {
							System.err.println("Action Cancelled..");
							PASAppDriver.MainMenu();
						}
						
					}
				} catch (InputMismatchException e) {
					System.err.println("Action Cancelled..");
					PASAppDriver.MainMenu();
				}

				this.policy_premium = policyPremium;

				try {
					System.out.format("%6s %6s %12s", "BRAND", "\t\tMODEL", "\t\tVEHICLE PREMIUM CHARGE" + "\n");
					for (Vehicle vehicle : vehicleArray) {
//						System.out.println("=============================================================");
						vehicle.showVehicleDetails();

					}
					System.out.println("\nPOLICY PREMIUM: $" + String.format("%,.2f", +policyPremium));
				} catch (IllegalFormatConversionException e) {
					e.printStackTrace();
				}
			} while (!(isCheck));
		} catch (InputMismatchException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			scan.hasNext();
		}
	}

	public void purchasePolicy() {
		Scanner scanner = new Scanner(System.in);

		try {
			
			Connection conn = DriverManager.getConnection(DB_URL2, getUSER(), getPASS());
			Statement stmt = conn.createStatement();
			int selector;
			boolean checker = false;
			try {

				do {

					System.out.println("Would you like to purchase the policy?");
					System.out.print("Press 1 to proceed. Press any key to cancel purchase.\nEnter:");
					selector = scanner.nextInt();
					if (selector == 1) {

						// policy Details that will be saved to database
						System.out.println("POLICY DETAILS");
						System.out.println("Policy ID: " + getPolicyID());
						System.out.println("Policy Effectivity Date: " + getPolicy_effective_date());
						System.out.println("Policy Expiration Date: " + getPolicy_expiration_date());
						System.out.println("Policy Premium: $ " + String.format("%,.2f", getPolicy_premium()));
						System.out.println("Policy Owner/Account #: " + getPolicyOwner());
						System.out.println("=====================================================================");

						// Policy Holder Details that will be saved to database

						// System.out.println("Policy Holder ID: " +
						// policyholder.getPolicy_holder_id());
						System.out.println("POLICY HOLDER DETAILS");
						System.out.println("First name: " + policyholder.getFirstName());
						System.out.println("Last name: " + policyholder.getLastName());
						System.out.println("Birth of Date: " + policyholder.getDateOfBirth());
						System.out.println("Address: " + policyholder.getAddress());
						System.out.println("Driver's License No.: " + policyholder.getDriversLicenseNumber());
						System.out.println("Driver's License Issued Date: " + policyholder.getDriversLicenseIssued());
						System.out.println("Policy ID: " + getPolicyID());
						System.out.println("Account Policy owner: " + getPolicyOwner());
						System.out.println("=====================================================================");

						String addPolicyQuery = "INSERT INTO policy(policy_number,policy_effective_date,policy_expiration_date,policy_owner,policy_premium) VALUES ('"
								+ getPolicyID() + "','" + getPolicy_effective_date() + "','"
								+ getPolicy_expiration_date() + "','" + getPolicyOwner() + "','" + getPolicy_premium()
								+ "')";
						stmt.execute(addPolicyQuery);

						String addPolicyHolderQuery = "INSERT INTO policyholders (policy_holder_id,first_name,last_name,birth_date,address,driver_license_number,driver_license_issued,policy_id,account_policy_owner) VALUES ('"
								+ policyholder.getPolicy_holder_id() + "','" + policyholder.getFirstName() + "','"
								+ policyholder.getLastName() + "','" + policyholder.getDateOfBirth() + "','"
								+ policyholder.getAddress() + "','" + policyholder.getDriversLicenseNumber() + "','"
								+ policyholder.getDriversLicenseIssued() + "','" + getPolicyID() + "','"
								+ policyholder.getAccount_policy_owner() + "')";
						stmt.execute(addPolicyHolderQuery);

						System.out.println("VEHICLE DETAILS");
						for (int index = 0; index < this.numberOfVehicle; index++) {
							System.out.println("Make: " + vehicleArray[index].getMake() + "\n" + "Model: "
									+ vehicleArray[index].getModel() + "\n" + "Color: " + vehicleArray[index].getColor()
									+ "\n" + "Year(s) of Vehicles: " + vehicleArray[index].getVehicleYear() + "\n"
									+ "Vehicle Type: " + vehicleArray[index].getVehicle_type() + "\n" + "Fuel Type: "
									+ vehicleArray[index].getFuel_type() + "\n" + "Purchased Price: $"
									+ String.format("%,.2f", vehicleArray[index].getPurchase_price()) + "\n"
									+ "Vehicle Premium: $"
									+ String.format("%,.2f", vehicleArray[index].getVehiclePremium()));
							vehicleArray[index].saveVehicle(getUSER(), getPASS());
							System.out.println("=====================================================================");
						}
						System.out.println("Total Policy Premium: $" + String.format("%,.2f", getPolicy_premium()));

						System.out.println("POLICY SUCCESSFULLY PURCHASED!");
						checker = true;
					} else {
						System.out.println(
								"============================================================================================");
						System.out.println("\nYou choose to cancel the purchase.\n");
						System.out.println("Returning to home screen........");
						System.out.println(
								"============================================================================================");
						checker = true;
					}
				} while (!(checker));
				
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
//			pas.MainMenu();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	// #3 Cancel a specific policy
	public void cancelSpecificPolicy() {

		int selector;
		int policyNumSelector;
		boolean isCheck = false;
		String policyNumber = null;
		try {
			Connection conn = DriverManager.getConnection(DB_URL2, getUSER(), getPASS());
			Statement stmt = conn.createStatement();

			System.out.print("Enter Policy number: ");
			do {
				try {
					policyNumber =  scan.nextLine();
					
					if(checker(policyNumber) == true) {
						isCheck = true;
					}
					else {
						isCheck = false;
					}
				}catch (Exception e) {
					isCheck = false;
				}
				
			}while(!isCheck);

//	
			int policyNumberInt = Integer.parseInt(policyNumber);
			ResultSet rset = stmt.executeQuery("SELECT * from policy where policy_number = " + policyNumberInt + "");

			if (rset.isBeforeFirst()) {

				// ResultSet result = stmt.executeQuery("SELECT * FROM policy where policy_owner
				// = '" + policyNumber + "'");

				System.out.format("%4s %12s %16s %18s %18s", "Policy ID", "Effective Date", "Expiration Date",
						"Policy Premium", "Policy Status" + "\n");
				while (rset.next()) {
//					System.out.println(rset.getString(1));
					int policy_id = rset.getInt("policy_number");
					String policy_effective_date = rset.getString("policy_effective_date");
					String policy_expiration_date = rset.getString("policy_expiration_date");
					double policy_premium = rset.getDouble("policy_premium");
					String policy_status = rset.getString("policy_status");
					System.out.format("%4s %16s %16s %16s %16s", policy_id, policy_effective_date,
							policy_expiration_date, "\t$" + String.format("%,.2f", policy_premium),
							policy_status + "\n");

				}

				// Cancel a specific policy (i.e change the expiration date of a policy to an
				// earlier date than originally specified)

				System.out.println("Press 1 to change expiration date of a policy.");
				System.out.println("Press 0 to cancel");
				do {
					System.out.print("Enter here:");
					selector = scan.nextInt();
					if (selector == 1) {
						System.out.println("\nChanging an expiration date of policy from orginally specified date. ");

						System.out.print("Select Policy ID: ");
						policyNumSelector = scan.nextInt();
						isCheck = true;

						if (editPolicyDate(policyNumSelector) == false) {

							isChecker = false;
							cancelSpecificPolicy();
							scan.nextLine();
						} else {
							isChecker = true;
						}

					} else if (selector == 0) {
						PASAppDriver.MainMenu();
						isCheck = true;
					} else {
						System.out.println("ERROR!,You've entered an invalid input");
						isCheck = false;
					}
				} while (!(isCheck));
			} else {
				System.err.println("POLICY NUMBER NOT EXISITING");
				PASAppDriver.MainMenu();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // end of cancelSpecificPolicy method

	public boolean editPolicyDate(int policyNumSelector) {
		try {
			Connection conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			Statement stmt = conn.createStatement();

			String getSQLquery = "SELECT * FROM policy WHERE policy_number = '" + policyNumSelector
					+ "' and policy_status = 'ACTIVE'";
			ResultSet rset = stmt.executeQuery(getSQLquery);

			if (!rset.next()) {
				System.out.println("\nCan't use the Policy because it's already expired or not existing.");
				return false;
			}

			else {
				int policy_number = rset.getInt("policy_number");
				policy_effective_date = rset.getString("policy_effective_date");
				policy_expiration_date = rset.getString("policy_expiration_date");
				String policy_status = rset.getString("policy_status");

				System.out.format("%2s %12s %12s %12s %12s", "\nPolicy ID", "Effective Date", "Expiration Date",
						"Policy Owned", "Policy Premium" + "\n");
				System.out.format("%5s %16s %16s %16s", policy_number, policy_effective_date, policy_expiration_date,
						policy_status + "\n");

				isCheck = false;
				while (!isCheck) {
					System.out.print(
							"\nEnter a number from (1-6) it will minus the month depends on input for new Expiration Date: ");

					int newExpInput = scan.nextInt();

					LocalDate parseExpDate = LocalDate.parse(policy_expiration_date);
					LocalDate newExpDate = parseExpDate.minusMonths(newExpInput);
					// LocalDate effectDate = LocalDate.parse(policy_effective_date);

					if (newExpInput > 1 || newExpInput <= 6) {
						String editSQLquery = "UPDATE policy SET policy_expiration_date = '" + policy_effective_date
								+ "',policy_status = 'EXPIRED' WHERE policy_number = '" + policy_number + "'";
						stmt.executeUpdate(editSQLquery);
						System.out.println("Policy expiration date successfully changed to " + newExpDate);
						System.out.println("also changed to EXPIRED.");
						isCheck = true;
						return true;
					} else if (newExpInput > 6 || newExpInput < 0) {
						System.out.println("Input Out of Range!INPUT must be 1-6 only");
						isCheck = false;

					} else {
						System.out.println("Input Out of Range!INPUT must be 1-6 only");
						isCheck = false;
					}
				}

				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			scan.next();
		}
		return false;
	}

	public void searchPolicy() {
		String searchPolicy = null;
		int searchPolicyInt = 0;
		try {
			Connection conn = DriverManager.getConnection(DB_URL2, USER, PASS);
			Statement stmt = conn.createStatement();

			System.out.println("Database connection successful!");
			System.out.println(
					"============================================================================================");

			
			System.out.println("Enter Policy ID: ");
			do {
				try {
					searchPolicy =  scan.nextLine();
					
					if(checker(searchPolicy) == true) {
						searchPolicyInt = Integer.parseInt(searchPolicy);
						isCheck = true;
					}
					else {
						isCheck = false;
					}
				}catch (Exception e) {
					isCheck = false;
				}
				
			}while(!isCheck);
			
			ResultSet rset = stmt.executeQuery("SELECT * FROM policy where policy_number = " + searchPolicyInt);
			System.out.println("Result: ");
			if (rset.next()) {

				ResultSet result = stmt
						.executeQuery("SELECT * FROM policy where policy_number = '" + searchPolicyInt + "'");
				System.out.println(
						"=======================================Policy details===========================================");

				System.out.format("%2s \t%9s \t%10s \t%6s %6s %14s", "Policy ID", "Effective Date", "Expiration Date",
						"Policy owner", "Policy Premium", "Policy Status" + "\n");
				while (result.next()) {

					int policy_number = result.getInt("policy_number");
					String policy_effective_date = result.getString("policy_effective_date");
					String policy_expiration_date = result.getString("policy_expiration_date");
					int policy_owner = result.getInt("policy_owner");
					int policy_premium = result.getInt("policy_premium");
					String policy_status = result.getString("policy_status");

					System.out.format("%2s \t\t%9s \t%10s \t\t%6s %12s %16s", policy_number, policy_effective_date,
							policy_expiration_date, policy_owner, "$"+policy_premium, policy_status + "\n");

					System.out.println(
							"============================================================================================");
				}
			} else {
				System.out.println("No record found.");
				PASAppDriver.MainMenu();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// generate random number for policy number
	public int policyIDNumberGenerator() {
		Random rand = new Random();
		Integer generateNum = rand.nextInt(999999);

		return generateNum;

	}
	
	// Integer validator
	public int intValidator(int num) {
		boolean invalid = false;

		do {
			try {
				// String s = String.valueOf(num); // created to check space
				num = scan.nextInt();

				if (num <= 0) {
					System.err.println("Invalid Input");
					invalid = true;
				}

				else {
					invalid = false;
				}
			} catch (InputMismatchException e) {
				scan.nextLine();
				System.err.println("Invalid Input");
				invalid = true;
			}
		} while (invalid == true);
		return num;
	}

}
