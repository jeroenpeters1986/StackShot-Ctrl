package mtb.swamp.utes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class UtesArrays {
	public static byte[] concat(byte[]... arrays) {
		// Determine the length of the result array
		int totalLength = 0;
		for (int i = 0; i < arrays.length; i++) {
			totalLength += arrays[i].length;
		}

		// create the result array
		final byte[] result = new byte[totalLength];


		// copy the source arrays into the result array
		int currentIndex = 0;
		for (int i = 0; i < arrays.length; i++) {
			System.arraycopy(arrays[i], 0, result, currentIndex,
					arrays[i].length);
			currentIndex += arrays[i].length;
		}

		return result;
	}

	public static byte[] extract(byte[] array, int off, int len) {
		// Determine the length of the result array
		int arraylen = array.length;
		if (off > arraylen) {
			off = arraylen - 1;
		}

		if (len > arraylen - off) {
			len = arraylen - off;
		}

		// create the result array
		byte[] result = new byte[len];

		// copy the source arrays into the result array
		System.arraycopy(array, off, result, 0, len);

		return result;
	}

	public static final byte[] intTo1ByteArray(int value) {
		return new byte[] { (byte) ((value) & 0xff) };
	}

	public static final byte[] intTo2ByteArray(int value) {
		return new byte[] { (byte) ((value >> 8) & 0xff),
				(byte) ((value) & 0xff) };
	}

	public static final byte[] intTo2ByteArrayLittleEndian(int value) {
		return new byte[] { (byte) ((value) & 0xff),
				(byte) ((value >> 8) & 0xff) };
	}

	public static final byte[] floatToByteArray(float value) {
		int bits = Float.floatToIntBits(value);
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (bits & 0xff);
		bytes[1] = (byte) ((bits >> 8) & 0xff);
		bytes[2] = (byte) ((bits >> 16) & 0xff);
		bytes[3] = (byte) ((bits >> 24) & 0xff);
		return bytes;
	}

	public static final float byteArrayToFloat(byte[] value) {
		ByteBuffer buf = ByteBuffer.wrap(value);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		return buf.getFloat();
	}

	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public static String charArrayToHexString(char[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (char b : bytes) {
			sb.append(String.format("%02x", (byte)b));
		}
		return sb.toString();
	}
	

	//get a String[] constructed with an index of an ArrayList
	public static String[] listArrayList(ArrayList<String[]> tab) {
		return listArrayList(tab,0);
	}
	public static String[] listArrayList(ArrayList<String[]> tab, int index) {
		String[] list = new String[tab.size()];

		for (int i = 0; i < tab.size(); i++) {
			list[i] = tab.get(i)[index];
		}
		return list;
	}
	
	//sort an ArrayList on index
	public static void sort(ArrayList<String[]> tab) {
		sort(tab,0);
	}
	public static void sort(ArrayList<String[]> tab, final int index) {
		Collections.sort(tab, new Comparator<String[]>() {
			public int compare(String[] a, String[] b) {
				return Integer.signum(a[index].compareTo(b[index]));
			}
		});
	}
	

	//find index with value==String
	public static int findInArrayList(ArrayList<String[]> tab, String val) {
		return findInArrayList(tab, val, 0);
	}
	public static int findInArrayList(ArrayList<String[]> tab, String val, int index) {
		int res = -1;
		for (int i = 0; i < tab.size(); i++) {
			if (val.equals(tab.get(i)[index])) {
				res = i;
				break;
			}
		}
		return res;
	}
}
