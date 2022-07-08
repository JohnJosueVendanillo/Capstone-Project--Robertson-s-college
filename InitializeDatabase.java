package capstone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



/*
 * This codes will automatically create database.
 * If no database detected - will create.
 * if there's an existing policy - will use the existing policy.
 * 
 * Username & Password depends on your configured credentials of database.
 * 
 * 
 */

public class InitializeDatabase {
	final String DB_URL = "jdbc:mysql://localhost/";
	static final String DB_URL2 = "jdbc:mysql://localhost/PAS_DB";
	protected String USER = "";
	protected String PASS = "";
	

	public String getUSER() {
		return USER;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}

	public String getPASS() {
		return PASS;
	}

	public void setPASS(String pASS) {
		PASS = pASS;
	}

	
	// prompt for login credentials to access database
	public void loginDetails() {
		Scanner scan = new Scanner(System.in);

		System.out.println("PLEASE ENTER YOUR CREDENTIALS FOR DATABASE.");

				System.out.println("ENTER USERNAME: ");
				this.USER = scan.nextLine();
				System.out.println("ENTER PASSWORD: ");
				this.PASS = scan.nextLine();	
	}
	

// create database 
	public void createDatabase() {
		
		boolean isCheck = false;
		do {
			
		loginDetails();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();) {
			isCheck = true;
		
			String sql = "CREATE DATABASE IF NOT EXISTS  PAS_DB";
			stmt.executeUpdate(sql);
			System.out.println("Database created successfully...");
			createTable();
			
		} catch (Exception e) {
			System.err.println("INVALID CREDENTIALS, TRY AGAIN.");
		}
			
			} while (!isCheck);
		

		}
		

	
//create database tables
	public void createTable() {
		try (Connection conn = DriverManager.getConnection(DB_URL2, USER, PASS);
				Statement stmt = conn.createStatement();) {

			String sqlCreateCustomerTable = "CREATE TABLE IF NOT EXISTS `customers_create` (\r\n"
					+ "  `account_number` int(4) unsigned zerofill NOT NULL,\r\n"
					+ "  `first_name` varchar(60) NOT NULL,\r\n" + "  `last_name` varchar(60) NOT NULL,\r\n"
					+ "  `address` varchar(60) NOT NULL\r\n"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

			stmt.executeUpdate(sqlCreateCustomerTable);
			System.out.println("Created Customer Table.");

			String sqlCreatePolicyTable = "CREATE TABLE IF NOT EXISTS `policy` (\r\n"
					+ "  `policy_number` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `policy_effective_date` date NOT NULL,\r\n" + "  `policy_expiration_date` date NOT NULL,\r\n"
					+ "  `policy_owner` int(4) unsigned zerofill NOT NULL,\r\n"
					+ "  `policy_premium` double NOT NULL DEFAULT '0',\r\n"
					+ "  `policy_status` varchar(45) NOT NULL DEFAULT 'ACTIVE',\r\n"
					+ "  PRIMARY KEY (`policy_number`)\r\n"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=936299 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

			stmt.executeUpdate(sqlCreatePolicyTable);
			System.out.println("Created Policy Table.");

			String sqlCreateVehicleTable = "CREATE TABLE IF NOT EXISTS `vehicles` (\r\n"
					+ "  `vehicle_id` int NOT NULL AUTO_INCREMENT,\r\n" + "  `make` varchar(100) DEFAULT NULL,\r\n"
					+ "  `model` varchar(100) DEFAULT NULL,\r\n" + "  `color` varchar(100) DEFAULT NULL,\r\n"
					+ "  `year` int NOT NULL,\r\n" + "  `vehicle_type` varchar(100) DEFAULT NULL,\r\n"
					+ "  `fuel_type` varchar(100) DEFAULT NULL,\r\n" + "  `purchase_price` double NOT NULL,\r\n"
					+ "  PRIMARY KEY (`vehicle_id`)\r\n"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

			stmt.executeUpdate(sqlCreateVehicleTable);
			System.out.println("Created Vehicle Table.");

			String sqlCreatePolicyHolderTable = "CREATE TABLE IF NOT EXISTS `policyholders` (\r\n"
					+ "  `policy_holder_id` int NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `first_name` varchar(60) NOT NULL,\r\n" + "  `last_name` varchar(60) NOT NULL,\r\n"
					+ "  `birth_date` varchar(60) NOT NULL,\r\n" + "  `address` varchar(60) NOT NULL,\r\n"
					+ "  `driver_license_number` varchar(60) NOT NULL,\r\n"
					+ "  `driver_license_issued` varchar(60) NOT NULL,\r\n" + "  `policy_id` int NOT NULL,\r\n"
					+ "  `account_policy_owner` int NOT NULL,\r\n" + "  PRIMARY KEY (`policy_holder_id`)\r\n"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

			stmt.executeUpdate(sqlCreatePolicyHolderTable);
			System.out.println("Created Policy Holder Table.");

			String sqlCreateClaimsTable = "CREATE TABLE IF NOT EXISTS `claims` (\r\n"
					+ "  `claims_id` varchar(50) NOT NULL,\r\n" + "  `accident_date` varchar(60) NOT NULL,\r\n"
					+ "  `accident_location` varchar(60) NOT NULL,\r\n"
					+ "  `damage_description` varchar(100) NOT NULL,\r\n"
					+ "  `estimated_cost_of_repair` double NOT NULL DEFAULT '0',\r\n"
					+ "  `policy_id` int DEFAULT '0'\r\n"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

			stmt.executeUpdate(sqlCreateClaimsTable);
			System.out.println("Created Claims Table.");

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
