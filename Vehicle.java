package capstone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * Vehicle details created here
 * 
 * 
 * 
 */
public class Vehicle extends RatingEngine {
	Scanner scan = new Scanner(System.in);
	PolicyHolder policyholder = new PolicyHolder();
	Calendar calendar = Calendar.getInstance();

	public final int CURRENT_YEAR = calendar.get(Calendar.YEAR);

	private String make, model, color, vehicle_type, fuel_type;
	private int year, ageOfVehicle;
	private double purchase_price;

	public double vpf = 0;
	private double vehiclePremium;

	public Vehicle(String make, String model, String color, String vehicle_type, String fuel_type, int year,
			double purchase_price) {
		this.make = make;
		this.model = model;
		this.color = color;
		this.vehicle_type = vehicle_type;
		this.fuel_type = fuel_type;
		this.year = year;
		this.purchase_price = purchase_price;
	}

	public Vehicle() {

	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public String getFuel_type() {
		return fuel_type;
	}

	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}

	public int getVehicleYear() {
		return this.year;
	}

	public void setVehicleYear(int year) {
		this.year = year;
	}

	public double getVehiclePremium() {
		return vehiclePremium;
	}

	public double getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(double purchase_price) {
		this.purchase_price = purchase_price;
	}

	public double getVpf() {
		return vpf;
	}

	public void setVpf(double vpf) {
		this.vpf = vpf;
	}

	public void toLoad() {

		boolean isCheck = false;

		System.out.println("ENTER VEHICLE DETAILS");
		do {

			System.out.println("Make/Brand: ");
			String make = scan.nextLine().toUpperCase();

			if (validateEmptyString(make) == false) {
				System.err.println("Please do leave blank");
				isCheck = false;
			} else {
				isCheck = true;
			}

			this.make = make;

		} while (!(isCheck));

		isCheck = false;

		do {
			System.out.println("Model: ");
			String model = scan.nextLine().toUpperCase();

			if (validateEmptyString(model) == false) {
				System.out.println("Please do leave blank");
				isCheck = false;
			} else {
				isCheck = true;
			}
			this.model = model;
		} while (!(isCheck));

		isCheck = false;

		do {
			System.out.println("Color: ");
			String color = scan.next().toUpperCase();
			color += scan.nextLine();

			if (validateEmptyString(color) == false) {
				System.out.println("Please do not leave blank");
				isCheck = false;
			} else if (symbolValidator(color) == false) {
				System.out.println("Invalid Inputs");
				isCheck = false;
			} else {

				isCheck = true;
			}

			this.color = color;
		} while (!(isCheck));

		isCheck = false;

		do {

			System.out.println("Year (2XXX): ");
			String yearInput = scan.nextLine();
			int yearOfVehicle = Integer.parseInt(yearInput);
//			intValidator(yearOfVehicle);
			if (yearOfVehicle <= (CURRENT_YEAR - 40)) {
				System.err.println("Invalid input, must be not less than 40 years.\n");

				isCheck = false;

			} else if (yearOfVehicle > CURRENT_YEAR) {
				System.err.println("Invalid input, Issued Date is Invalid!");
				isCheck = false;
			} else {
				this.year = yearOfVehicle;
				isCheck = true;
			}

		} while (!(isCheck));

		isCheck = false;

		do {
			System.out.println("Vehicle Type: ");
			String vehicleType = scan.nextLine().toUpperCase();
			if (validateEmptyString(vehicleType) == false) {
				System.out.println("Please do leave blank");
				isCheck = false;
			} else {
				isCheck = true;
			}
			this.vehicle_type = vehicleType;
		} while (!(isCheck));

		isCheck = false;

		do {
			System.out.println("Fuel Type: ");
			String fuelType = scan.nextLine().toUpperCase();
			if (validateEmptyString(fuelType) == false) {
				System.out.println("Please do leave blank");
				isCheck = false;
			} else {
				isCheck = true;
			}

			this.fuel_type = fuelType;
		} while (!(isCheck));

		isCheck = false;

		do {
			System.out.println("Purchase Price: ");
			double purchasePrice = scan.nextDouble();
			if (purchasePrice > 0) {

				isCheck = true;
				this.purchase_price = purchasePrice;
			} else {
				System.err.println("Invalid input!, enter whole number only");
				isCheck = false;
			}
		} while (!(isCheck));
	}// end of toLoad(

	public void getVPF() {
		ageOfVehicle = CURRENT_YEAR - getVehicleYear();

		if (ageOfVehicle < 1 || ageOfVehicle == 0) {
			setVpf(vpf = 0.01);
		} else if (ageOfVehicle < 3) {
			setVpf(vpf = 0.008);
		} else if (ageOfVehicle < 5) {
			setVpf(vpf = 0.007);
		} else if (ageOfVehicle < 10) {
			setVpf(vpf = 0.006);
		} else if (ageOfVehicle < 15) {
			setVpf(vpf = 0.004);
		} else if (ageOfVehicle < 20) {
			setVpf(vpf = 0.002);
		} else if (ageOfVehicle < 40) {
			setVpf(vpf = 0.001);
		} else {
			System.out.println("Vehicle is not eligible for this insurance.");
		}
	}

	public void showVehicleDetails() {
		System.out.format("%6s %6s %12s", this.make, this.model, this.vehiclePremium + "\n");

	}

	public void setVehiclePremium(int issueDate) {

		this.vehiclePremium = RatingEngineProcessor(this.purchase_price, getVpf(), issueDate);
	}

	public void saveVehicle() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas_database", "root",
					"Password123!");
			Statement stmt = conn.createStatement();

			PreparedStatement preparedStatementAddVehicle = conn.prepareStatement(
					"INSERT INTO vehicles(make,model,color,year,vehicle_type,fuel_type,purchase_price) VALUES (?,?,?,?,?,?,?)");

			preparedStatementAddVehicle.setString(1, this.make);
			preparedStatementAddVehicle.setString(2, this.model);
			preparedStatementAddVehicle.setString(3, this.color);
			preparedStatementAddVehicle.setInt(4, this.year);
			preparedStatementAddVehicle.setString(5, this.vehicle_type);
			preparedStatementAddVehicle.setString(6, this.fuel_type);
			preparedStatementAddVehicle.setDouble(7, this.purchase_price);

			preparedStatementAddVehicle.execute();

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

			if (strChar.matches("[!@#%&*()'+,-.\\/:;<=>?[]^`{|}]]") || strChar.matches("[-\\$]")) {
				output = false;
			} else {
				output = true;
			}
		}
		return output;
	}

	// check if the input has symbol
	public boolean stringValidator(String str) {
		String strChar = str;
		boolean output = false;
		for (int index = 0; index < str.length(); index++) {
			strChar = Character.toString(str.charAt(index));

			if (strChar.matches("[a-zA-Z]")) {
				output = false;
			} else {
				output = true;
			}
		}
		return output;
	}

	// check if of the input has empty
	public boolean validateEmptyString(String str) {
		Boolean isNotEmpty = false;
		if (str.equals("")) {
			isNotEmpty = false;
		} else {
			isNotEmpty = true;
		}
		return isNotEmpty;
	}

}
