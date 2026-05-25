package mtb.swamp.utes;

public class UtesStrings {
	/**
	 * Converts a bunch of bytes to a string. 
	 * This is a little different from new String( myBytes ) because
	 * byte-arrays received from C will be crazy long and just have a null-terminator
	 * somewhere in the middle. 
	 */
	public static String CStringtoString( byte bytes[] ){
		for( int i = 0; i < bytes.length; i++ ){
			if( bytes[i] == 0 ){
				return new String( bytes, 0, i ); 
			}
		}
		
		return new String( bytes ); 
	}
	
	public static String CStringtoString( char bytes[] ){
		for( int i = 0; i < bytes.length; i++ ){
			if( bytes[i] == 0 ){
				return new String( bytes, 0, i ); 
			}
		}
		
		return new String( bytes ); 
	}
	
	public static boolean contains(String chaineInitiale, String chaineATrouver) {
		   return (chaineInitiale.indexOf(chaineATrouver) >= 0);
		}
	
	 public static String Left(String text, int length)
     {
           return text.substring(0, length);
     }

     public static String Right(String text, int length)
     {
           return text.substring(text.length() - length, length);
     }  

     public static String Mid(String text, int start, int end)
     {
           return text.substring(start, end);
     }  

     public static String Mid(String text, int start)
     {
           return text.substring(start, text.length() - start);
     }
}
