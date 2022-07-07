package capstone;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.*;

/*
ClaimID (Cxxxxxx)
PolicyID
AccountID
DateOfAcc
DescOfAcc
AddressOfAcc
DescOfDamage
EstimatedCost

 * 
 * 
 * 
 */

import java.util.Scanner;

public class Claims {
	Scanner scan = new Scanner(System.in);

	private String claims_id;
	private String dateOfAccident;
	private String accidentLocation;
	private String damageDescription;
	private double estimatedCostOfRepair;
	private int policyID; //
	
	public String getClaims_id() {
		return claims_id;
	}


	public void setClaims_id(String claims_id) {
		this.claims_id = claims_id;
	}
	
	public String getDateOfAccident() {
		return dateOfAccident;
	}


	public void setDateOfAccident(String dateOfAccident) {
		this.dateOfAccident = dateOfAccident;
	}


	public String getAccidentLocation() {
		return accidentLocation;
	}


	public void setAccidentLocation(String accidentLocation) {
		this.accidentLocation = accidentLocation;
	}


	public String getDamageDescription() {
		return damageDescription;
	}


	public void setDamageDescription(String damageDescription) {
		this.damageDescription = damageDescription;
	}


	public double getEstimatedCostOfRepair() {
		return estimatedCostOfRepair;
	}


	public void setEstimatedCostOfRepair(double estimatedCostOfRepair) {
		this.estimatedCostOfRepair = estimatedCostOfRepair;
	}




	public int getPolicyID() {
		return policyID;
	}


	public void setPolicyID(int policyID) {
		this.policyID = policyID;
	}




	boolean isCheck = false;

	int policyNum;
	String policyEffDate;
	String policyExpDate;
	String policyStatus;

	String policyIDNumber;
	
	

	public static PASAppDriver pas = new PASAppDriver();

	public void processClaims() {
		Scanner scanner = new Scanner(System.in);
		try {

			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
						"Password123!");
				Statement stmt = conn.createStatement();

				
//				policyIDNumber += scam
				
				do {
					System.out.print("Please enter Policy ID: ");
					policyIDNumber = scan.nextLine();

					if ((symbolValidator(policyIDNumber)|| strInputNumValidator(policyIDNumber)) == false
							|| validateEmptyString(policyIDNumber) == true) {
						System.out.println("INVALID INPUT,Enter number only!.");
						isCheck = false;
					} else {
						isCheck = true;
					}
				} while (!isCheck);

				ResultSet rset = stmt
						.executeQuery("SELECT * from policy where policy_number = '" + policyIDNumber + "'");

				
				if (rset.next()) {

					
					System.out.println("POLICY DETAILS: \n");
					System.out.format("%2s %12s %12s %12s", "POLICY ID", "Effectivity Date", "Expiration Date",
							"STATUS" + "\n");
					int policy_number = rset.getInt("policy_number");
					String policy_effective_date = rset.getString("policy_effective_date");
					String policy_expiration_date = rset.getString("policy_expiration_date");
					String policy_status = rset.getString("policy_status");

					this.policyNum = policy_number;
					this.policyEffDate = policy_effective_date;
					this.policyExpDate = policy_expiration_date;
					this.policyStatus = policy_status;

					System.out.format("%2s %17s %17s %16s \n", this.policyNum, this.policyEffDate, this.policyEffDate,
							this.policyStatus + "\n");
					if (policy_status.equals("EXPIRED")) {
						System.out.println("POLICY ID  is already expired");
					} else {
						
						System.out.println("POLICY ID  is ACTIVE. Please input details: \n");
						String claims_id = claimsIdNumberGenerator(); 
//						claims_id = claims_id.toString();
						
						System.out.println("Claim ID#: " + claims_id + "\n");
						System.out.print("Accident Date: ");
						String dateOfAccident = scanner.nextLine();
						System.out.print("Accident Location: ");
						String accidentLocation = scanner.nextLine();
						System.out.print("Damage Description: ");
						String damageDescription = scanner.nextLine();
						System.out.print("Estimated cost of repair: ");
						double estimatedCostOfRepair = scanner.nextDouble();

						this.claims_id = claims_id;
						this.dateOfAccident = dateOfAccident;
						this.accidentLocation = accidentLocation;
						this.damageDescription = damageDescription;
						this.estimatedCostOfRepair = estimatedCostOfRepair;
						this.policyID = this.policyNum;
						
						PreparedStatement updateStatus = conn.prepareStatement("UPDATE policy SET policy_status = 'EXPIRED'  where policy_number = '"+policyIDNumber+"'");
						updateStatus.executeUpdate();

						PreparedStatement addvehicleStatement = conn.prepareStatement(
								"INSERT INTO claims (claims_id,accident_date,accident_location,damage_description,estimated_cost_of_repair,policy_id) VALUES (?,?,?,?,?,?)");
						addvehicleStatement.setString(1, getClaims_id());
						addvehicleStatement.setString(2, getDateOfAccident());
						addvehicleStatement.setString(3, getAccidentLocation());
						addvehicleStatement.setString(4, getDamageDescription());
						addvehicleStatement.setDouble(5, getEstimatedCostOfRepair());
						addvehicleStatement.setInt(6, getPolicyID());
						addvehicleStatement.execute();

						printDetails();
					}
				} else if(!rset.next()){
					System.out.println("Result: Searched Not found");
					pas.MainMenu();
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input!");
				scan.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			scan.next();
		}
	}
	
	public String claimsIdNumberGenerator() {
		Random rand = new Random();
        Integer generateNum = rand.nextInt(999999);
        String claimNo = String.format("C%06d", generateNum); //generate random number for claim number
		return claimNo;
		
	
	}
	

//	//shows the latest created
//	ResultSet resultPolicyHolder = stmt.executeQuery("SELECT * FROM policyholders ORDER BY policy_holder_id DESC LIMIT 1");

	public void printDetails() {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			Statement stmt = conn.createStatement();

			PreparedStatement showQueryStatement = conn
					.prepareStatement("SELECT * FROM claims where policy_id = "+ policyNum +"");
			ResultSet rset = showQueryStatement.executeQuery();

			while (rset.next()) {
				int policy_id = rset.getInt("policy_id");
				String claims_id = rset.getString("claims_id");
				String accident_date = rset.getString("accident_date");
				String accident_location = rset.getString("accident_location");
				String damage_description = rset.getString("damage_description");
				double estimated_cost_of_repair = rset.getDouble("estimated_cost_of_repair");


				this.claims_id = claims_id;
				this.dateOfAccident = accident_date;
				this.accidentLocation = accident_location;
				this.damageDescription = damage_description;
				this.estimatedCostOfRepair = estimated_cost_of_repair;
				

				System.out.format("%2s %12s %12s %12s","POLICY ID","CLAIMS ID", "DATE OF ACCIDENT", "ACCIDENT LOCATION" + "\n");
				System.out.format("%2s %12s %16s %12s", policy_id,claims_id, accident_date, accident_location + "\n");
				System.out.format("%-2s", "DAMAGE DESCRIPTION" + "\n");
				System.out.format("%2s",damage_description + "\n");
				System.out.format("%2s", "ESTIMATED COST OF REPAIR: " + "\n");
				System.out.format("%2s", "$" + estimated_cost_of_repair + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void searchPolicyClaims() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			Statement stmt = conn.createStatement();

			String claimsIdNumber;
			
			
			do {
				System.out.print("Search for specific claims: ");
				claimsIdNumber = scan.nextLine();

				if ((symbolValidator(claimsIdNumber)|| strInputNumValidator(claimsIdNumber)) == false
						|| validateEmptyString(claimsIdNumber) == true) {
					System.out.println("INVALID INPUT,Enter number only!");
					isCheck = false;
				} else {
					isCheck = true;
				}
			} while (!isCheck);
			
			
			

			PreparedStatement preparedStmtShowClaims = conn
					.prepareStatement("SELECT * FROM claims where claims_id = '" + claimsIdNumber + "'");
			preparedStmtShowClaims.execute();
			ResultSet rset = preparedStmtShowClaims.executeQuery();


			if (rset.next()) {
				String claims_id = rset.getString("claims_id");
				String accident_date = rset.getString("accident_date");
				String accident_location = rset.getString("accident_location");
				String damage_description = rset.getString("damage_description");
				double estimated_cost_of_repair = rset.getDouble("estimated_cost_of_repair");
				String policy_id = rset.getString("policy_id");
				
				System.out.format("%2s %12s", "CLAIMS ID", "Policy ID" + "\n");
				System.out.format("%2s %12s", "C" + claims_id, policy_id + "\n");

				System.out.format("%-2s", "DAMAGE DESCRIPTION" + "\n");
				System.out.format("%-2s", damage_description + "\n");

				System.out.format("%-2s %12s", "DATE OF ACCIDENT", "\tACCIDENT LOCATION" + "\n");
				System.out.format("%-2s %16s", accident_date, "\t" + accident_location + "\n");

				System.out.format("%2s", "ESTIMATED COST OF REPAIR: " + "\n");
				System.out.format("%2s", "$" + estimated_cost_of_repair + "\n");
			}
			
			else {
				System.err.println("\nResult: \nNo Record Found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	
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

				if (strChar.matches("[!@#%&*()'+,-.\\/:;<=>?[]^`{|}]]") || strChar.matches("[-]")) {
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

		// Integer validator
		public int intValidator(int num) {
			boolean invalid;
			do {
				try {
					num = scan.nextInt();
					if (num <= 0) {
						System.err.println("Invalid Input");
						invalid = true;
					} else {
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
		
		public int validateChoice(int[] numArr, int choiceInt) { // validator for choices 1 or 2
	        Boolean choiceMatch = false;
	        do {
	            choiceInt = intValidator(choiceInt);
	            for (int index = 0; index < numArr.length; index++) {
	                if (choiceInt == numArr[index]) {
	                    choiceMatch = true;
	                }
	            }
	            if (choiceMatch == false) {
	                System.out.println("Input out of range. Please try again.");
	            }

	        } while (!choiceMatch);

	        return choiceInt;
	    }
}
