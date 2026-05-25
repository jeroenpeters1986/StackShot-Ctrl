package mtb.swamp.utes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;




public class UtesProps {

	public static void loadXMLProperties(Properties parameters, String filename) {
		File file = new File(filename);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(filename);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

		if (in == null)
			return;

		try {
			parameters.loadFromXML(in);
		} catch (InvalidPropertiesFormatException e) {
			System.out.println("props file empty");
			//e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	public static void saveXMLProperties(Properties parameters, String filename) {
		File file = new File(filename);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(filename);
		} catch (FileNotFoundException e2) {
			System.out.println("No props file");
		}

		if (out == null)
			return;

		try {
			parameters.storeToXML(out, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int propGetInt(Properties defprops, Properties userprops,
			String name, int defvalue, DecimalFormat nf) {
		String str = (defprops!=null?defprops.getProperty(name):null);
		String v = (userprops!=null?userprops.getProperty(name):null);
		if (v != null) {
			str=v;
		}
		if (str == null) {
			return defvalue;
		}

		int ret = defvalue;
		try {
			ret = nf.parse(str).intValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException in propGetInt1");
			e.printStackTrace();
		}

		return ret;
	}

	public static double propGetDouble(Properties defprops, Properties userprops,
			String name, double defvalue, DecimalFormat nf) {
		String str = (defprops!=null?defprops.getProperty(name):null);
		String v = (userprops!=null?userprops.getProperty(name):null);
		if (v != null) {
			str=v;
		}
		if (str == null) {
			return defvalue;
		}
		
		double ret=defvalue;
		try {
			ret = nf.parse(str).doubleValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException in propGetDouble2");
			e.printStackTrace();
		}

		return ret;
	}

	public static String propGetString(Properties defprops, Properties userprops,
			String name) {
		String ret = (defprops!=null?defprops.getProperty(name):null);
		if (userprops == null) {
			
			return ret;
		}
		String v = userprops.getProperty(name);
		if (v == null) {
			
			return ret;
		}
		
		return v;
	}

	public static void propGetArrayList(Properties defprops,
			Properties userprops, String name, ArrayList<String[]> valarray,
			int nbcolumns) {
		// vider le tableau
		valarray.clear();
		String ret = (defprops!=null?defprops.getProperty(name):null);
		
		String v = (userprops!=null?userprops.getProperty(name):null);
		if (v != null) {
			ret = v;
		}
		if (ret == null)
			return;
		String[] a = ret.split("\t\t");
		// System.out.println(a.length);
		String[] array = new String[nbcolumns - 1];
		for (int i = 0; i < a.length; i++) {
			array = a[i].split("\t");
			if (array.length != nbcolumns)
				break;
			valarray.add(array);
		}
		UtesArrays.sort(valarray);
	}

	public static void propPutInt(Properties parameters, String name,
			int value, DecimalFormat nf) {
		parameters.setProperty(name, nf.format(value));
	}

	public static void propPutDouble(Properties parameters, String name,
			double value, DecimalFormat nf) {

		parameters.setProperty(name, nf.format(value));
	}

	public static void propPutString(Properties parameters, String name,
			String value) {
		parameters.setProperty(name, value);
	}

	public static void propPutArrayList(Properties parameters, String name,
			ArrayList<String[]> valarray) {
		String Str = "";
		for (int i = 0; i < valarray.size(); i++) {
			for (int j = 0; j < valarray.get(i).length; j++) {
				Str += valarray.get(i)[j] + "\t";
				//System.out.println(valarray.get(i)[j]);
			}
			Str += "\t";
		}
		parameters.setProperty(name, Str);
	}
}
