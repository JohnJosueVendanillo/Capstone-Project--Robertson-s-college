package capstone;

import java.util.Calendar;

public class RatingEngine extends Policy {
	
	static Calendar calendar = Calendar.getInstance();
	final static int current_Year = calendar.get(Calendar.YEAR);
	
	public double RatingEngineProcessor(double vehiclePurchasedPrice,double vehiclePriceFactor,int driversLicenseIssuedDate) {
		double policyPremium=0;
		
		int driversLicenseAge = 2022 - driversLicenseIssuedDate;
		if(driversLicenseAge == 0) {
			driversLicenseAge =1;
		}
		
		System.out.println(driversLicenseAge);
		 
		double first = vehiclePurchasedPrice * vehiclePriceFactor;
		double second = ((vehiclePurchasedPrice/100)/driversLicenseAge);
		policyPremium = first + second;
				
		return policyPremium;
	}
}
