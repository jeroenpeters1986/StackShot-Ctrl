package mtb.swamp.utes;

public class UtesNumbers {


	public static boolean isNumber(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			System.out.println("NumberFormatException in isNumber");
			return false;
		}
		return true;
	}
}
