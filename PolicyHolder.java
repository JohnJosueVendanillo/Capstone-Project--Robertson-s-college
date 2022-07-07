package capstone;

import java.sql.*;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * 
 * 
First name and last name
Date of birth
Address
Driver’s license number
Date on which driver’s license was first issued.
*
*	Policy Holder account created here
 */
public class PolicyHolder extends PASAppDriver {
	Scanner scan = new Scanner(System.in);

	Calendar calendar = Calendar.getInstance();
	final int currentYear = calendar.get(Calendar.YEAR);
	final int LEGAL_AGE = 17;

	int policy_holder_id, policy_id, account_policy_owner;
	String firstName, lastName, address, dateOfBirth, driversLicenseNumber;
	int month, day, year;
	Integer driversLicenseIssued;

	public PolicyHolder(int policy_holder_id, int policy_id, int account_policy_owner, String firstName,
			String lastName, String address, String dateOfBirth, String driversLicenseNumber,
			int driversLicenseIssued) {
		this.policy_holder_id = policy_holder_id;
		this.policy_id = policy_id;
		this.account_policy_owner = account_policy_owner;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.dateOfBirth = dateOfBirth;
		this.driversLicenseNumber = driversLicenseNumber;
		this.driversLicenseIssued = driversLicenseIssued;
	}

	public PolicyHolder() {
	}

	public int getPolicy_holder_id() {
		return policy_holder_id;
	}

	public void setPolicy_holder_id(int policy_holder_id) {
		this.policy_holder_id = policy_holder_id;
	}

	public int getPolicy_id() {
		return policy_id;
	}

	public void setPolicy_id(int policy_id) {
		this.policy_id = policy_id;
	}

	public int getAccount_policy_owner() {
		return account_policy_owner;
	}

	public void setAccount_policy_owner(int account_policy_owner) {
		this.account_policy_owner = account_policy_owner;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDriversLicenseNumber() {
		return driversLicenseNumber;
	}

	public void setDriversLicenseNumber(String driversLicenseNumber) {
		this.driversLicenseNumber = driversLicenseNumber;
	}

	public int getDriversLicenseIssued() {
		return driversLicenseIssued;
	}

	public void setDriversLicenseIssued(int driversLicenseIssued) {
		this.driversLicenseIssued = driversLicenseIssued;
	}

	boolean isNumber = false, isCheck = false;

	/**
	 * 
	 */
	/**
	 * 
	 */
	public void createPolicyHolder() {
		Scanner scanner = new Scanner(System.in);

		do {
			System.out.print("FirstName: ");
			firstName = scanner.nextLine();

			if ((strInputNumValidator(firstName) || symbolValidator(firstName)) == false
					|| validateEmptyString(firstName) == true) {
				System.err.println("Invalid input, Enter letters only.");
				isCheck = false;
			} else {
				isCheck = true;
			}
		} while (!isCheck);

		isCheck = false;

		do {
			System.out.print("LastName: ");
			lastName = scanner.nextLine();

			if ((symbolValidator(lastName) || strInputNumValidator(lastName)) == false
					|| validateEmptyString(lastName) == true) {
				System.err.println("Invalid input, Enter letters only.");
				isCheck = false;
			} else {
				isCheck = true;
			}
		} while (!isCheck);

		String birthMonthStr = null, birthDayStr = null, birthYearStr = null;
		isCheck = false;

		System.out.print("Date of Birth (MM-dd-YYYY): \n");
		do {
			try {
				System.out.println("Month: ");
				birthMonthStr = scanner.next();
				birthMonthStr += scanner.nextLine();
				if ((symbolValidator(birthMonthStr) || strInputNumValidator(birthMonthStr)) == true
						|| validateEmptyString(birthMonthStr) == true) {
			
					System.err.println(
							"RESULT: INVALID INPUT,can't enter blank or letters, Enter number only!.Please input number (1-12) to represent number of months.");

					isCheck = false;
				}

				else {
					try {
						this.month = Integer.parseInt(birthMonthStr);

						if (this.month > 12 || this.month < 1) {
							System.err.println("INVALID INPUT, month range 1-12");

							isCheck = false;
						} else {

							isCheck = true;
						}
					} catch (NumberFormatException e) {
					
						System.err.println(
								"RESULT: INVALID INPUT,can't enter blank or letters, Enter number only!.Please input number (1-12) to represent number of months.");
						isCheck = false;
//						scanner.nextLine();
					}

				}

			} catch (InputMismatchException e) {
			
				System.err.println(
						"RESULT: INVALID INPUT,can't enter blank or letters, Enter number only!.Please input number (1-12) to represent number of months.\n");
				isCheck = false;
//				scanner.nextLine();
			}

		} while (!isCheck);

		isCheck = false;

		do {
			try {

				System.out.println("Day: ");
				birthDayStr = scanner.next();
				birthDayStr += scanner.nextLine();
				if ((strInputNumValidator(birthDayStr) || symbolValidator(birthDayStr)) == true
						|| validateEmptyString(birthDayStr) == true) {
				
					System.err.println(
							"RESULT: INVALID INPUT,can't enter blank, Enter number only!. Please input number (1-31) to represent days in a month.");
					isNumber = false;
				}

				else {
					try {
						this.day = Integer.parseInt(birthDayStr);

						if (this.day > 31 || this.day < 1) {
							System.err.println("INVALID INPUT, date range 1-31");
							isCheck = false;
						} else {

							isCheck = true;
						}
					} catch (NumberFormatException e) {
				
						System.err.println(
								"RESULT: INVALID INPUT,can't enter blank or letters, Enter number only!.Please input number (1-31) to represent number of months.");
						isCheck = false;
//						scanner.nextLine();
					}

				}
			} catch (NumberFormatException e) {
			
				System.err.println(
						"RESULT: INVALID INPUT,can't enter blank or letters, Enter number only!.Please input number (1-31) to represent number of months.\n");

				isCheck = false;
//				scanner.nextLine();
			}

		} while (!isCheck);

		isCheck = false;

		do {
			try {
				System.out.println("Year: ");
				birthYearStr = scanner.next();
				birthYearStr += scanner.nextLine();
				if ((symbolValidator(birthYearStr) || strInputNumValidator(birthYearStr)) == true
						|| validateEmptyString(birthYearStr) == true) {
					System.err.println("Please input a number");
					isNumber = false;
				}

				else {
					try {
						this.year = Integer.parseInt(birthYearStr);
						int userAge = currentYear - this.year;
//						if (this.year != currentYear ) {
						if (this.year < 1900) {
							System.err.println("\nInvalid year Input!.");
							isCheck = false;
						} else if (userAge < LEGAL_AGE) {

							System.err.println("\nAge is: " + userAge + " years old.");
							System.err.println("\n17 Years old above only allowed to be policy holder .");
							System.out.println("\nReturning to main screen...");
							isCheck = false;
							PASAppDriver.MainMenu();
						} else {
							isCheck = true;
						}
					} catch (Exception e) {
						System.err.println("RESULT:");
						System.err.println("INVALID INPUT,can't enter blank or letters, Enter number only!.");
						System.err.println("Please input number (1-12) to represent number of months.");
						isCheck = false;

					}
				}
			} catch (NumberFormatException e) {
				System.err.println("RESULT:");
				System.err.println("INVALID INPUT,can't enter blank or letters, Enter number only!.");
				System.err.println("Please input valid year.");
				isCheck = false;
				scanner.nextLine();
			}

		} while (!isCheck);

		isCheck = false;

		dateOfBirth = birthMonthStr + "-" + birthDayStr + "-" + birthYearStr;

		//
		System.out.println("Date of Birth: " + dateOfBirth);
		System.out.println("==========================================================");

		isCheck = false;

		do {
			System.out.print("Address: ");
			address = scanner.next();
			address += scanner.nextLine();

			if (address == null || address.equals("") || address.isEmpty()) {
				System.err.println("Please do not leave blank");
				isCheck = false;
			} else {
				isCheck = true;
			}
		} while (isCheck = false);

		System.out.println("Address: " + address);
		isCheck = false;
		String licenseNumber;

		System.out.print("\nDriver's License Number : \n");
		do {
			System.out.print("Please input 12 digits Driver's license number (NO HYPEN!): ");
			licenseNumber = scan.nextLine();

			if (licenseNumber.length() == 12) {
				driversLicenseNumber = insertString(licenseNumber, "-").toUpperCase();
				isCheck = true;
			} else if (symbolValidator(licenseNumber) == false) {
				System.err.println("Invalid inputted symbol,");
				isCheck = false;
			} else {
				System.err.println("Out of range. 12 digits only");
				isCheck = false;
			}
		} while (!isCheck);

		System.out.println("License No. " + driversLicenseNumber);

		isCheck = false;

		do {
			try {
				System.out.print("Driver's License Issued Year ( YEAR ONLY! - YYYY): ");
				driversLicenseIssued = intValidator(driversLicenseIssued);

				if (licenseAgeChecker(driversLicenseIssued) == false) {
					isCheck = false;
					System.out.println(isCheck);
				} else {
					setDriversLicenseIssued(driversLicenseIssued);
					isCheck = true;
					System.out.println(isCheck);
				}

			} catch (InputMismatchException e) {

				System.err.println("\nInvalid Input.");
				scan.nextLine();
				isCheck = false;
			}

		} while (!isCheck);

	}// end of createPolicyHolder method

	/// validator methods

	// license Age validator
	public boolean licenseAgeChecker(int inputYear) {
		int legalAgeToGetLicense = 17;
		int userAge = currentYear - this.year;
		boolean checker = false;

		// do {
		try {
			if ((userAge < legalAgeToGetLicense) || (inputYear < this.year)) {
				System.err.println("Policy Holder's age is not applicable.");
				System.out.println("user age: " + userAge);
				System.out.println("inputYear: " + inputYear);
				System.out.println("Year: " + this.year);
				checker = false;
			} else {
				checker = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			scan.nextLine();
		}

		// }while(!checker);

		return checker;
	}

	// license number dissector
	public String insertString(String originalString, String stringToBeInserted) {
		String newString = originalString.substring(0, +3) + stringToBeInserted + originalString.substring(3, +6)
				+ stringToBeInserted + originalString.substring(6, originalString.length());
		// return the modified String
		return newString;
	}

	// checking of input if has number
	public boolean strInputNumValidator(String input) {
		String str = input;
		boolean flagStatus = false;
		for (int i = 0; i < str.length(); i++) {
			Boolean flag = Character.isDigit(str.charAt(i));
			if (!flag) {
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

			if (strChar.matches("[!@#%&*()'+,-.\\//:;<=>?[]^`{|}]]") || strChar.matches("[-//\\$]")) {
				output = true;
			} else if (strChar.contains("[!@#%&*()'+,-.\\//:;<=>?[]^`{|}]]") || strChar.contains("[-//\\$]")) {
				output = true;
			} else {
				output = false;
			}
		}
		return output;
	}

	// check if the input has string
	public boolean stringValidator(String str) {
		String strChar = str;
		boolean output = false;
		for (int index = 0; index < str.length(); index++) {
			strChar = Character.toString(str.charAt(index));

			if (strChar.matches("[a-zA-Z]")) {
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

}// end of policyholder class
