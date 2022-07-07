package capstone;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class Policy {
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

	// public static PASAppDriver pas = new PASAppDriver();
	PolicyHolder policyholder = new PolicyHolder();

	// Vehicle vehicle = new Vehicle();
	Vehicle[] vehicleArray;

	public void searchAccountNumber() {

		String accountNumber = null;
		boolean isCheck = false;
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			Statement stmt = conn.createStatement();

			System.out.println("Enter Account number: ");
			accountNumber = scan.nextLine();
//				accountNumber += scan.nextLine();

			int accountNumberInt = Integer.parseInt(accountNumber);
			ResultSet rset = stmt
					.executeQuery("SELECT * from customers_create where account_number = '" + accountNumber + "'");

			if (!rset.next()) {
				System.err.println("No result found.\nCREATE ACCOUNT FIRST!");
				PASAppDriver.MainMenu();

			} else {

				int policyOwned = 0;
				ResultSet policyOwnedCounter = stmt
						.executeQuery("select COUNT(policy_owner) from policy where policy_owner = " + accountNumber);

				while (policyOwnedCounter.next()) {
					policyOwned = policyOwnedCounter.getInt(1);
				}
				System.out.println("Account Number: " + accountNumberInt);
				System.out.println("Policy Owned: " + policyOwned);
				this.policy_owner = accountNumberInt;

				// Quotation starts here
				String policyHolderSelector;
				isCheck = false;

				do {
					System.out.println("\nProceed for the Quotation.\nEnter 1 to continue or Any key to cancel.");
					System.out.print("Enter here: ");
					policyHolderSelector = scan.nextLine();
//					if (policyHolderSelector.equals("1")) {
//						isCheck = true;
//					} 

					if (policyHolderSelector.equals("1")) {
						policyDate(); // effectivity & expiration date
						policyholder.setAccount_policy_owner(accountNumberInt);
						policyholder.createPolicyHolder(); // creating policy holder method
						vehicleDetails(); // add vehicle method
						purchasePolicy(); // purchase policy method
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
				System.err.println("Invalid...");
//				PASAppDriver.MainMenu();
				isCheck = false;
			}

		} while (!isCheck);
		System.out.println("==========================================================");

	}

	public void bookNowPolicyDate() {

		LocalDate effectivityDate = LocalDate.now();
		this.policy_effective_date = effectivityDate.toString();

		LocalDate expirationDate = effectivityDate.plusMonths(6);
		this.policy_expiration_date = expirationDate.toString();

		System.out.println("Effectivity Date: " + effectivityDate);
		System.out.println("Expiration Date : " + expirationDate);

	}

	public void advanceBookPolicyDate() {
		Scanner scanner = new Scanner(System.in);
		String monthStr = "";
		String dayStr = "";
		String yearStr = "";
		boolean isCheck = false;
		LocalDate advanceBookEffectivityPolicyDate;
		LocalDate advanceBookExpirationPolicyDate;
		System.out.println("\nEnter Effectivity Date (YYYY-MM-dd): ");

		String test = scan.nextLine();
		
		yearStr = test.substring(0,test.indexOf("-"));
		monthStr = test.substring(test.indexOf("-")+1,test.lastIndexOf("-"));
		dayStr = Integer.toString(test.lastIndexOf("-") + 1);
		
		this.year = Integer.parseInt(yearStr);
		this.month = Integer.parseInt(monthStr);
		this.day = Integer.parseInt(dayStr);
//		LocalDate advanceBookEffectivityPolicyDate = LocalDate.of(getYear(), month, day);
		advanceBookEffectivityPolicyDate = LocalDate.of(year,month,day);
		advanceBookExpirationPolicyDate = LocalDate.of(year, month + 6, day);
//		processPolicyQuery(month, day, year);

		System.out.println("Effectivity Date: " + advanceBookEffectivityPolicyDate);
		System.out.println("Expiration Date : " + advanceBookExpirationPolicyDate);
		
		
		isCheck = true;
//		do {
//			try {
//				LocalDate date = LocalDate.now();
//
//				System.out.println("Year: ");
//				yearStr = scanner.nextLine();
//				boolean x = checker(yearStr);
//				int yearInt = Integer.parseInt(yearStr);
//
//				if (x == true) {
//
//					if (yearInt < currentYear) {
//						isCheck = false;
//					} else {
//						setYear(yearInt);
//						isCheck = true;
//					}
//
//				}
//
//			} catch (InputMismatchException e) {
//				System.err.println("INVALID INPUT");
//				scanner.nextLine();
//
//				isCheck = false;
//
//			}
////			catch (DateTimeException e) {
////				System.err.println("INVALID INPUT");
////				scanner.nextLine();
////				
////				isCheck = false;
////			}finally{
////				isCheck = true;
////				
////			}
//		} while (!isCheck);
//		
//		isCheck = false;
//		
//		do {
//
//			try {
//				System.out.println("Month: ");
//				monthStr = scanner.nextLine();
//
//				boolean x = checker(monthStr);
//				System.out.println("Output: " + x);
//
//				if (x == true) {
//					int Value = Integer.parseInt(monthStr);
//					if (Value > 12 || Value < 1) {
//						System.err.println("Invalid Input");
//						isCheck = false;
//					} else {
//						this.month = Value;
//						isCheck = true;
//					}
//				}
//
//			} catch (InputMismatchException e) {
//				System.err.println("INVALID INPUT");
//				scanner.nextLine();
//
//				isCheck = false;
//				scanner.reset();
//			}
//
//		} while (!isCheck);
//
//		isCheck = false;
//		
//		do {
//		try {
//			System.out.println("Day: ");
//			dayStr = scanner.nextLine();
//			boolean y = checker(dayStr);
//			
//			System.out.println("Output: " +y);
//			if (y == true) {
//				int Value = Integer.parseInt(dayStr);
//				if(Value > 31 || Value < 1) {
//					System.err.println("Invalid Input");
//					isCheck = false;
//				}
//				else {
//					this.day = Value;
//					isCheck = true;
//				}				
//			}
//		}catch (InputMismatchException e) {
//			System.err.println("INVALID INPUT");
//			scanner.nextLine();
//			
//			isCheck = false;
//			scanner.reset();
//		}
//	}while(!isCheck);
		
		
		


	}

	public boolean checker(String input) {
		boolean result = false;
		try {
			int Value = Integer.parseInt(input);
			result = true;
		} catch (NumberFormatException e) {
			System.err.println("Input String cannot be parsed to Integer.");
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
//				System.err.println("ERROR! input has whitespace");
				result = true;
			} else {
//				System.out.println("no whitespace");
				result = false;
			}
		}

		return result;
	}

//	public int setNowPolicyDate(String inputDate) {
//		 int result = 0;                                     //initial value
//	        if(inputDate.equals("")) {
//	            result = 0;
//	        }
//	        else {
//	        	try {
//	        	 LocalDate validDate = LocalDate.parse(inputDate);
//	             LocalDate LocaldateNow = LocalDate.now();
//	             if(validDate.compareTo(LocaldateNow) == 0) {                // to compare present
//	                    this.validDate = validDate;
//	                    result = 1;
//	             }
//	             else if(validDate.compareTo(LocaldateNow) < 0) {        // to compare to past
//	                    this.validDate = validDate;
//	                    result = 2;
//	                }
//	             else if(validDate.compareTo(LocaldateNow) > 0) {        //to compare to future
//	                    this.validDate = validDate;
//	                    result = 3;
//	            }
//	        	}catch (Exception e) {
//	        		 result = 0;
//				}
//	        }
//			return result;
//}
//	
//	

	public void vehicleDetails() {
		int VehicleCounter;
		double policyPremium = 0;
		int selector;
		boolean isCheck = false;

		try {
			do {
				System.out.println("\nHow many car(s) you want to add?");

				System.out.println("Enter: ");

				VehicleCounter = intValidator(VehicleCounter = 0);

				this.numberOfVehicle = VehicleCounter;

				vehicleArray = new Vehicle[VehicleCounter];
				try {
					for (int count = 0; count < VehicleCounter; count++) {

						System.out.println("Add Vehicle # " + (count + 1));

						System.out.println("Do you want to proceed?");
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

					}
				} catch (InputMismatchException e) {
					System.err.println("Action Cancelled..");
					PASAppDriver.MainMenu();
				}

				this.policy_premium = policyPremium;

				try {
					System.out.format("%5s %5s %12s", "Brand", "Model", "Vehicle Premium Charge" + "\n");
					for (Vehicle vehicle : vehicleArray) {
						System.out.println("=============================================================");
						vehicle.showVehicleDetails();

					}
					System.out.println("\nPolicy Premium: $" + String.format("%,.2f", +policyPremium));
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
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
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
							vehicleArray[index].saveVehicle();
							System.out.println("=====================================================================");
						}
						System.out.println("Policy Premium: $" + String.format("%,.2f", getPolicy_premium()));

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

	public void processPolicyQuery(int month, int day, int year) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(year, month, day);

		java.util.Date dateUtil = calendar.getTime();
		java.sql.Date date = new java.sql.Date(dateUtil.getTime());
		this.policy_effective_date = date.toString();

		calendar.set(year, month + 6, day);
		java.util.Date dateExpUtil = calendar.getTime();
		java.sql.Date dateExp = new java.sql.Date(dateExpUtil.getTime());
		this.policy_expiration_date = dateExp.toString();

//		String parseDateUtil = dateUtil.toString();
//		String parseDateExpUtil = dateExpUtil.toString();

		System.out.println("Effectivity Date: " + date);
		System.out.println("Expiration  Date: " + dateExp);
	}

	// #3 Cancel a specific policy
	public void cancelSpecificPolicy() {

		int selector;
		int policyNumSelector;

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			Statement stmt = conn.createStatement();

			int policyNumber;

			System.out.print("Enter Policy number: ");
			policyNumber = scan.nextInt();

			ResultSet rset = stmt.executeQuery("SELECT * from policy where policy_number = " + policyNumber + "");

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
				System.out.println("Account number not exisiting");
				PASAppDriver.MainMenu();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // end of cancelSpecificPolicy method

	public boolean editPolicyDate(int policyNumSelector) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
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
//					else {
//						String editSQLquery = "UPDATE policy SET policy_expiration_date = '" + newExpDate
//								+ "' WHERE policy_number = '" + policy_number + "'";
//						stmt.executeUpdate(editSQLquery);
//						isCheck = true;
//						
//					}

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
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			Statement stmt = conn.createStatement();

			System.out.println("Database connection successful!");
			System.out.println(
					"============================================================================================");

			int searchPolicy;
			System.out.println("Enter Policy ID: ");
			searchPolicy = scan.nextInt();

			ResultSet rset = stmt.executeQuery("SELECT * FROM policy where policy_number = " + searchPolicy);
			System.out.println("Result: ");
			if (rset.next()) {

				ResultSet result = stmt
						.executeQuery("SELECT * FROM policy where policy_number = '" + searchPolicy + "'");
				System.out.println(
						"=======================================Policy details===========================================");

				System.out.format("%2s %14s %12s %12s %12s %14s", "Policy ID", "Effective Date", "Expiration Date",
						"Policy owner", "Policy Premium", "Policy Status" + "\n");
				while (result.next()) {

					int policy_number = result.getInt("policy_number");
					String policy_effective_date = result.getString("policy_effective_date");
					String policy_expiration_date = result.getString("policy_expiration_date");
					int policy_owner = result.getInt("policy_owner");
					int policy_premium = result.getInt("policy_premium");
					String policy_status = result.getString("policy_status");

					System.out.format("%2s %18s %16s %18s %12s %16s", policy_number, policy_effective_date,
							policy_expiration_date, policy_owner, policy_premium, policy_status + "\n");

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

//	public int policyIDNumberGenerator() {
//		int start = 1;
//		int end = 999999;
//		end = (int) Math.ceil(end);
//		start = (int) Math.floor(start);
//
//		Integer result = (int) Math.floor(Math.random() * (start - end + 1) + start);
//		result = Math.abs(result);
//
//		return result;
//	}

	// checking of input if has number
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

			if (strChar.matches("[!@#%&*()'+,-.\\/:;<=>?[]^`{|}]]") || strChar.matches("[-//\\$]")) {
				output = true;
			}
//			else if(str.charAt(index) == ' ') {
//				output = true;
//			}

			else {
				output = false;
			}
		}
		return output;
	}

	// check if of the input has empty
//	public static boolean validateEmptyString(String str) {
//		Boolean result = false;
//		if (str.equals("") == false) {
//			System.out.println("has empty string");
//			result = true;
//		} else {
//			System.out.println("no empty string");
//			result = false;
//		}
//		return result;
//	}

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

//	public int validateChoice(int[] numArr, int choiceInt) { // validator for choices 1 or 2
//        Boolean choiceMatch = false;
//        do {
//        	
//            choiceInt = intValidator(choiceInt);
//                     
//            for (int index = 0; index < numArr.length; index++) {
//                if (choiceInt == numArr[index]) {
//                    choiceMatch = true;
//                } 
//            }
//            if (choiceMatch == false) {
//                System.err.println("Input out of range. Please try again.");
//            }
//        } while (!choiceMatch);
//
//        return choiceInt;
//    }

}
