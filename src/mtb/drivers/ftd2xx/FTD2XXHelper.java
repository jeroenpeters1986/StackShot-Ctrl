package mtb.drivers.ftd2xx;

import java.io.IOException;

import mtb.swamp.utes.UtesArrays;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;

import ftd2.Ftd2xx;
import ftd2.Ftd2xx.FT_HANDLE;

public class FTD2XXHelper {
	// préférences
	// Stackshot

	public static final int COMM_STATUS_FAILED = 0, COMM_STATUS_SUCCESS = 1, // The
																				// operation
																				// was
																				// successful
			COMM_STATUS_BUSY = 2, // The current operation could not begin
									// because it is already in progress
			COMM_STATUS_DATA_MISSING = 3, // Data was not fully read
			COMM_STATUS_BAD_SYNC = 4, // The sync-byte was missing in the
										// communiction stream
			COMM_STATUS_BUFFER_OVERRUN = 5, // The controller returned more data
											// than could be processed
			COMM_STATUS_NOT_EMPTY = 6, // The requested write operation could
										// not be performed because data was
										// already present
			COMM_STATUS_IO_ERROR = 7, // An input/output error occured (USB
										// unplugged, bad handle, etc)
			COMM_STATUS_BAD_PARAM = 8, // One of the parameters passed into the
										// function was rejected by the
										// controller
			COMM_STATUS_NOT_FOUND = 9; // The requested controller could not be
										// found via USB

	public static final int COMM_RAIL_AXIS_ANY = 0, // Use any available axis
													// when opening
			COMM_RAIL_AXIS_X = 1, // Only open the X axis
			COMM_RAIL_AXIS_Y = 2, // Only open the Y axis
			COMM_RAIL_AXIS_Z = 3, // Only open the Z axis
			COMM_RAIL_AXIS_UNDEFINED = 4; // The axis stored in the controller
											// is undefined

	public static final int CC_RAIL_MOVE = 0x1000, // Move the rail to the
													// specified position
			CC_RAIL_POSITION_TARGET = 0x1001, // Desired target position for the
												// rail
			CC_RAIL_POSITION_CURRENT = 0x1002, // Current position of the rail
			CC_RAIL_POSITION_ZERO = 0x1003, // Zero out the current position of
											// the rail
			CC_RAIL_SHUTTER_FIRE = 0x1004, // Fire the shutter control
			CC_RAIL_STATUS = 0x1005, // Retrieve the current controller status
			CC_RAIL_STOP = 0x1006, // Stop the rail from moving
			CC_RAIL_MOVE_AT_SPEED = 0x1007, // Move the rail at the specified
											// speed
			CC_RAIL_CONFIG_NAME = 0x1080, // The name for the current
											// configuration
			CC_RAIL_CONFIG_BACKLIGHT = 0x1081, // Backlighting configuration
			CC_RAIL_CONFIG_MODE = 0x1082, // Operating mode of the controller
			CC_RAIL_CONFIG_UNITS = 0x1083, // Units -- mm/mils/steps
			CC_RAIL_CONFIG_TORQUE = 0x1084, // Torque setting for the motor
			CC_RAIL_CONFIG_NUM_STEPS = 0x1085, // Number of steps to use for a
												// stack
			CC_RAIL_CONFIG_NUM_PULSES = 0x1086, // Number of pulses on the
												// shutter per step
			CC_RAIL_CONFIG_TOTAL_DISTANCE = 0x1087, // Total distance config
			CC_RAIL_CONFIG_DISTANCE_PER_STEP = 0x1088, // Distance to travel per
														// step
			CC_RAIL_CONFIG_SETTLE_TIME = 0x1089, // Settling time
			CC_RAIL_CONFIG_OFF_TIME = 0x108a, // Off time between shutter pulses
			CC_RAIL_CONFIG_SPEED = 0x108b, // Speed that the rail will move
			CC_RAIL_CONFIG_RAMP_TIME = 0x108c, // Ramp time for the rail
			CC_RAIL_CONFIG_DISTANCE_PER_REV = 0x108d, // Linear distance per
														// revolution of the
														// motor
			CC_RAIL_CONFIG_SHUTTER_DISABLE = 0x108e, // Shutter disable feature
														// enabled/disabled
			CC_RAIL_CONFIG_AUTO_RETURN = 0x108f, // Auto-return feature
													// enabled/disabled
			CC_RAIL_CONFIG_SAVE = 0x1090, // Save the current configuration
			CC_RAIL_CONFIG_LOAD = 0x1091, // Load the specified configuration
			CC_RAIL_CONFIG_AXIS = 0x1092, // Get/set the controllers configured
											// axis
			CC_RAIL_CONFIG_TIMELAPSE = 0x1093, // Time-lapse feature
												// enabled/disabled
			CC_RAIL_CONFIG_PULSE_TIME = 0x1094, // On time of the shutter pulse
			CC_RAIL_CONFIG_BACKLASH = 0x1095, // Rail backlash configuration
			CC_RAIL_CONFIG_HOLDING_TORQUE = 0x1096, CC_RESET = 0x1100, // Reset
																		// the
																		// controller
			CC_SOFTWARE_STRING = 0x1102, // Software string (human readable)
			CC_SOFTWARE_ID = 0x1103, // Sftware identifier
			CC_HARDWARE_ID = 0x1104, // Hardware identifer
			CC_BOOTLOADER_ID = 0x1105, // Bootloader identifer
			CC_SOFTWARE_CHECKSUM = 0x1106, // Software checksum
			CC_SERIAL_NUMBER = 0x1107; // The serial number of the device

	public static final int COMM_ACTION_MIN = 0,
			COMM_ACTION_READ = COMM_ACTION_MIN, // Read the specified command
			COMM_ACTION_WRITE = 1, // Write the specified command
			COMM_ACTION_RSP_OK = 2, // Controller responded with OK
			COMM_ACTION_BAD_PARAM = 3, // A bad parameter was passed to the
										// controller
			COMM_ACTION_UNSUPPORTED_ACTION = 4, // The action specified is
												// invalid
			COMM_ACTION_UNSUPPORTED_CMD = 5, // The command passed in is invalid
			COMM_ACTION_FAILED = 6, // The command failed (no further
									// information available)
			COMM_ACTION_NOT_EMPTY = 7, // The write operation was to a
										// write-once register and it is no
										// longer empty
			COMM_ACTION_BUSY = 8, // The controller is already performing the
									// specified action
			COMM_ACTION_MAX = 9;

	public static final int COMM_RAIL_DIR_MIN = 0,
			COMM_RAIL_DIR_FWD = COMM_RAIL_DIR_MIN, // Move the rail in the
													// forward direction
			COMM_RAIL_DIR_BACK = 1, // Move the rail in the backward direction
			COMM_RAIL_DIR_MAX = 2;

	public static final int COMM_RAIL_UNITS_MIN = 0,
			COMM_RAIL_UNITS_ENGLISH = COMM_RAIL_UNITS_MIN, // English/mils
			COMM_RAIL_UNITS_METRIC = 1, // Metric/mm
			COMM_RAIL_UNITS_STEPS = 2, // Motor steps
			COMM_RAIL_UNITS_MAX = 3;

	public static final int COMM_RAIL_MODE_MIN = 0,
			COMM_RAIL_MODE_AUTO_STEP = COMM_RAIL_MODE_MIN, // Automatic step
															// mode
			COMM_RAIL_MODE_AUTO_DIST = 1, // Automatic distance mode
			COMM_RAIL_MODE_TOTAL_DISTANCE = 2, // Total distance mode
			COMM_RAIL_MODE_DISTANCE_PER_STEP = 3, // distance per step mode
			COMM_RAIL_MODE_MANUAL = 4, // Manual mode
			COMM_RAIL_MODE_CONTINUOUS = 5, // Continuous mode
			COMM_RAIL_MODE_MAX = 6;

	public static final int COMM_BUFFER_SIZE = 40; // Buffer size used for
													// packet communications
	public static final int STACKSHOT_BAUD_RATE = 38400; // Baud-rate used for
															// the StackShot
															// interface
	public static final int RAIL_STATUS_IDLE = 0x00; // The rail is idle
	public static final int RAIL_STATUS_MOVING = 0x01; // The rail is currently
														// moving
	public static final int RAIL_STATUS_SHUTTER = 0x02; // The shutter is
														// currently firing
	public static final int RAIL_STATUS_USER_ABORTED = 0x04; // The user aborted
																// a move by
																// pressing a
																// button on
																// StackShot

	/** Device information */
	public static class DeviceInfo {
		public int index; // device index in info list
		public int flags; // device flags
		public int type; // device type
		public int id; // device ID
		public int location; // device location ID
		public String serial;
		public String description;
		public NativeLong handle; // device handle

		public String toString() {
			StringBuffer b = new StringBuffer();
			b.append("index: " + Integer.toString(index));
			b.append(", flags: 0x" + Integer.toHexString(flags));
			b.append(", type: 0x" + Integer.toHexString(type));
			b.append(", id: 0x" + Integer.toHexString(id));
			b.append(", location: 0x" + Integer.toHexString(location));
			b.append(", serial: " + serial);
			b.append(", description: " + description);
			// b.append(", handle: 0x" + handle.toString());
			return b.toString();
		}
	}

	public static final int FT_STOP_BITS_1 = 0, FT_STOP_BITS_1_5 = 1,
			FT_STOP_BITS_2 = 2;

	/* Parity */
	public static final int FT_PARITY_NONE = 0, FT_PARITY_ODD = 1,
			FT_PARITY_EVEN = 2, FT_PARITY_MARK = 3, FT_PARITY_SPACE = 4;

	public static final int FTRESP_NOK = -1;
	public static final int FTRESP_OK = 0;

	private static final boolean scan = false;

	

	private FT_HANDLE ftHandle = null;
	private Ftd2xx lib;


	public FTD2XXHelper() throws Exception {
		try {
			System.out.println(Ftd2xx.JNA_LIBRARY_NAME);	
		} catch (UnsatisfiedLinkError e) {
		      System.err.println("Native code library failed to load.\n" + e);
		      throw new Exception("No JNA library");
	    }
		try {
			lib = Ftd2xx.INSTANCE;
		} catch (NoClassDefFoundError e) {
			System.err.println(e);
			throw new Exception("No ClassDef");
		}
	}

	private int GET_LIBRARY_VERSION() throws IOException {
		IntByReference ver = new IntByReference();
		NativeLong status = lib.FT_GetLibraryVersion(ver);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return ver.getValue();
		} else {
			System.out.println("Exception in GET_LIBRARY_VERSION : "
					+ status.intValue());
			throw new IOException();
		}
	}

	private int CREATE_DEVICEINFO_LIST() throws IOException {
		IntByReference num = new IntByReference();
		NativeLong status = lib.FT_CreateDeviceInfoList(num);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return num.getValue();
		} else {
			System.out.println("Exception in CREATE_DEVICEINFO_LIST : "
					+ status.intValue());
			throw new IOException();
		}
	}

	private DeviceInfo GET_DEVICEINFO_DETAIL(int dn) throws IOException {
		NativeLong status;
		DeviceInfo di = new DeviceInfo();
		di.index = dn;

		NativeLong ftHandleTemp = new NativeLong();
		IntByReference flags = new IntByReference();
		IntByReference type = new IntByReference();
		IntByReference id = new IntByReference();
		IntByReference location = new IntByReference();

		Memory serialmem = new Memory(256);
		serialmem.clear();
		Memory descmem = new Memory(256);
		descmem.clear();
		status = lib.FT_GetDeviceInfoDetail(dn, flags, type, id, location,
				serialmem, descmem, ftHandleTemp);
		// System.out.println("status "+status.intValue());
		if (status.intValue() == Ftd2xx.FT_OK) {
			di.flags = flags.getValue(); // device flags
			di.type = type.getValue(); // device type
			di.id = id.getValue(); // device ID
			di.location = location.getValue(); // device location ID
			di.serial = serialmem.getString(0);
			di.description = descmem.getString(0);
			di.handle = ftHandleTemp; // device handle
			return di;
		} else {
			System.out.println("Exception in GET_DEVICEINFO_DETAIL : "
					+ status.intValue());
			throw new IOException();
		}
	}

	public int find() {
		int index = 0;
		int ver;
		try {
			ver = GET_LIBRARY_VERSION();
		} catch (IOException e1) {
			return FTRESP_NOK;
		}
		System.out.println("FTD2XX version : " + Integer.toHexString(ver));
		int num;
		try {
			num = CREATE_DEVICEINFO_LIST();
		} catch (IOException e1) {
			return FTRESP_NOK;
		}
		System.out.println("Number of FTD2XX Devices : " + num);
		for (int i = 0; i < num; ++i) {
			DeviceInfo di;
			try {
				di = GET_DEVICEINFO_DETAIL(i);
			} catch (IOException e) {
				return FTRESP_NOK;
			}
			System.out.println(di.toString());
			if (di.description.compareTo("StackShot") == 0) {
				index = i;
				if (((int) di.flags & (int) Ftd2xx.FT_FLAGS_OPENED) != 0) {
					System.out.println("Stackshot "+i+" already opened.");
					//return FTRESP_NOK;
				} else {
					return index;
				}
			}
		}
		return FTRESP_NOK;
	}

	public void OPEN(int dn) throws IOException {
		NativeLong status;

		FT_HANDLE[] ftHandleTemp = { null };

		status = lib.FT_Open(dn, ftHandleTemp);
		if (status.intValue() == Ftd2xx.FT_OK) {
			ftHandle = ftHandleTemp[0];
			return;
		} else {
			System.out.println("Exception in OPEN : " + status.intValue());
			throw new IOException();
		}
	}

	public void SET_BITMODE(int mask, int mode) throws IOException {
		NativeLong status;

		// IntByReference flags = new IntByReference();
		// flags.setValue(0);
		// status = lib.FT_GetBitMode(ftHandle, flags);
		// System.out.println("BITMODE : "+flags.getValue());
		status = lib.FT_SetBitMode(ftHandle, mask, mode);
		// flags.setValue(0);
		// status = lib.FT_GetBitMode(ftHandle, flags);
		// System.out.println("BITMODE : "+flags.getValue());
		if (status.intValue() == Ftd2xx.FT_OK) {
			return;
		} else {
			System.out.println("Exception in SET_BITMODE : "
					+ status.intValue());
			throw new IOException();
		}
	}

	public void SET_BAUDRATE(int baudRate) throws IOException {
		NativeLong status;

		status = lib.FT_SetBaudRate(ftHandle, baudRate);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return;
		} else {
			System.out.println("Exception in SET_BAUDRATE : "
					+ status.intValue());
			throw new IOException();
		}
	}

	public void SET_DATACHARACTERISTICS(int wordLength, int stopBits,
			int parity) throws IOException {
		NativeLong status;

		status = lib.FT_SetDataCharacteristics(ftHandle, wordLength, stopBits,
				parity);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return;
		} else {
			System.out.println("Exception in SET_DATACHARACTERISTICS : "
					+ status.intValue());
			throw new IOException();
		}

	}

	public void SET_FLOWCONTROL(int flowControl, int xonChar, int xoffChar)
			throws IOException {
		NativeLong status;

		status = lib
				.FT_SetFlowControl(ftHandle, flowControl, xonChar, xoffChar);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return;
		} else {
			System.out.println("Exception in SET_FLOWCONTROL : "
					+ status.intValue());
			throw new IOException();
		}

	}

	public void PURGE(int mask) throws IOException {
		NativeLong status;

		status = lib.FT_Purge(ftHandle, mask);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return;
		} else {
			System.out.println("Exception in PURGE : " + status.intValue());
			throw new IOException();
		}
	}

	private void SET_TIMEOUTS(int readTimeout, int writeTimeout)
			throws IOException {
		NativeLong status;

		status = lib.FT_SetTimeouts(ftHandle, readTimeout, writeTimeout);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return;
		} else {
			System.out.println("Exception in SET_TIMEOUTS : "
					+ status.intValue());
			throw new IOException();
		}

	}

	/** Write bytes to device helper function */
	private int WRITE(byte b[]) throws IOException {
		NativeLong status;
		IntByReference len = new IntByReference();

		status = lib.FT_Write(ftHandle, b, b.length, len);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return len.getValue();
		} else {
			System.out.println("Exception in write : " + status.intValue());
			throw new IOException();
		}

	}

	/** Read bytes from device helper function */
	private byte[] READ(int s) throws IOException {
		byte[] b = new byte[s];
		int r = READ(b);
		if (r == b.length)
			return b;
		else {
			byte[] c = new byte[r];
			System.arraycopy(b, 0, c, 0, r);
			return c;
		}
	}

	/** Read bytes from device helper function */
	private int READ(byte b[]) throws IOException {
		NativeLong status;
		IntByReference len = new IntByReference();

		status = lib.FT_Read(ftHandle, b, b.length, len);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return len.getValue();
		} else {
			System.out.println("Exception in read : " + status.intValue());
			throw new IOException();
		}
	}

	public synchronized byte[] WRITEREAD(int func, int action, byte[] values,
			int retlen) throws IOException {
		try {
			SET_TIMEOUTS(1000, 1000);
		} catch (IOException e1) {
			System.out.println("^^Exception in WRITEREAD");
			throw new IOException();
		}
		byte[] header = { 0x55 };
		byte[] footer = { 0 };
		byte[] cmd = UtesArrays.intTo2ByteArray(func);
		byte[] act = UtesArrays.intTo1ByteArray(action);
		byte[] len = { 0x00 };
		byte[] msg;
		if (values != null) {
			len[0] = (byte) values.length;
			msg = UtesArrays.concat(header, cmd, act, len, values, footer);
		} else {
			msg = UtesArrays.concat(header, cmd, act, len, footer);
		}

		if (scan)
			System.out.print("wr=" + UtesArrays.byteArrayToHexString(msg) + " ");

		int retval = WRITE(msg);
		if (retval != msg.length) {
			System.out.println("ERROR in write : msg length = " + msg.length
					+ " bytes sent = " + retval + " ");
		}

		byte[] rd = null;

		if (retlen != 0) {
			try {
				rd = READ(retlen);
				if (scan) {
					System.out.print("rd=" + UtesArrays.byteArrayToHexString(rd) + " ");
				}
				
			} catch (IOException e) {
				System.out.println("Exception in WRITEREAD-read1 ");
				if (!scan)
					e.printStackTrace();
				return null;
			}
		} else {
			byte[] rd1 = null;
			try {
				rd1 = READ(5);
				if (scan) {
					System.out.print(" " + rd1.length + " ");
					System.out.print("rd1=" + UtesArrays.byteArrayToHexString(rd1)
							+ " ");
				}
			} catch (IOException e) {
				System.out.println("Exception in WRITEREAD-read2 ");
				if (!scan)
					e.printStackTrace();
				return null;
			}

			byte[] rd2 = null;
			if (rd1.length != 5) {
				if (scan) {
					System.out.print("BAD LENGTH " + rd1.length + " ");
				} else {
					System.out.println("rd1 : BAD LENGTH " + rd1.length);
				}
				return null;
			}
			try {
				rd2 = READ(rd1[4] + 1);
				if (scan) {
					if (rd2 == null) {
						System.out.print("rd2=null ");
					} else {
						System.out.print("rd2=" + UtesArrays.byteArrayToHexString(rd2)
								+ " ");
					}
				}
			} catch (IOException e) {
				System.out.println("Exception in WRITEREAD-read3 ");
				if (!scan)
					e.printStackTrace();
			}
			rd = UtesArrays.concat(rd1, rd2);
		}
		if (rd.length == 0) {
			if (scan) {
				System.out.print("RESULT= BAD LENGTH ");
			} else {
				System.out.println("rd : BAD LENGTH ");
			}
			return null;
		}
		if (scan)
			System.out.print("RESULT=" + rd[3] + " ");

		if (rd[3] != COMM_ACTION_RSP_OK) {
			System.out.println("IOException in WRITEREAD-RSP_NOK" + rd[3]);
			throw new IOException();
		}

		byte[] ret = new byte[rd[4]];
		System.arraycopy(rd, 5, ret, 0, rd[4]);
		// System.out.println("PAYLOAD :"+gb.byteArrayToHexString(ret));
		return ret;

	}

	public void CLOSE() throws IOException {
		NativeLong status;

		status = lib.FT_Close(ftHandle);
		if (status.intValue() == Ftd2xx.FT_OK) {
			return;
		} else {
			System.out.println("Exception in CLOSE : " + status.intValue());
			throw new IOException();
		}
	}

	public String GET_SOFTWARE_STRING() throws Exception {
		// System.out.println("\nGET_SOFTWARE_STRING");
		byte[] payload = null;
		byte[] ret;
		try {
			ret = WRITEREAD(CC_SOFTWARE_STRING, COMM_ACTION_READ, payload, 0);
		} catch (IOException e1) {
			System.out.println("^^Exception in GET_SOFTWARE_STRING");
			throw new IOException();
		}
		String version = null;
		if (ret == null)
			return null;
		try {
			version = new String(ret, "US-ASCII");
		} catch (Exception e) {
			System.out.println("Exception in GET_SOFTWARE_STRING");
			throw new Exception();
		}
		return version;
	}

	public int SET_RAIL_CONFIG_DISTANCE_PER_REV_STEPS(double val)
			throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_DISTANCE_PER_REV_STEPS");
		// JOptionPane.showMessageDialog(null,"alert "+STEPS_PER_REV);
		byte[] value = UtesArrays.floatToByteArray((float) val);
		byte[] type = { COMM_RAIL_UNITS_STEPS };
		byte[] payload = UtesArrays.concat(type, value);
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_DISTANCE_PER_REV, COMM_ACTION_WRITE,
					payload, 6);
		} catch (IOException e) {
			System.out
					.println("^^Exception in SET_RAIL_CONFIG_DISTANCE_PER_REV_STEPS");
			throw new IOException();
		}
		if (ret != null)
			return FTRESP_OK;
		else
			return FTRESP_NOK;
	}

	public int SET_RAIL_CONFIG_DISTANCE_PER_REV_MM(double val)
			throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_DISTANCE_PER_REV_MM");
		byte[] value = UtesArrays.floatToByteArray((float) val);
		byte[] type = { COMM_RAIL_UNITS_METRIC };
		byte[] payload = UtesArrays.concat(type, value);
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_DISTANCE_PER_REV, COMM_ACTION_WRITE,
					payload, 6);
		} catch (IOException e) {
			System.out
					.println("^^Exception in SET_RAIL_CONFIG_DISTANCE_PER_REV_MM");
			throw new IOException();
		}
		if (ret != null)
			return FTRESP_OK;
		else
			return FTRESP_NOK;
	}

	public int SET_RAIL_CONFIG_HOLDING_TORQUE(int val) throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_HOLDING_TORQUE");
		byte[] payload = { (byte) val };
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_HOLDING_TORQUE, COMM_ACTION_WRITE,
					payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_CONFIG_HOLDING_TORQUE");
			throw new IOException();
		}
		if (ret != null)
			return FTRESP_OK;
		else
			return FTRESP_NOK;
	}

	public int SET_RAIL_CONFIG_BACKLIGHT(int val) throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_BACKLIGHT");
		byte[] payload = { (byte) val };
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_BACKLIGHT, COMM_ACTION_WRITE,
					payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_CONFIG_BACKLIGHT");
			throw new IOException();
		}
		if (ret != null)
			return FTRESP_OK;
		else
			return FTRESP_NOK;
	}

	public int GET_RAIL_STATUS() throws IOException {
		// System.out.println("\nGET_RAIL_STATUS");
		byte[] payload = null;
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_STATUS, COMM_ACTION_READ, payload, 10);
		} catch (IOException e) {
			System.out.println("^^Exception in GET_RAIL_STATUS");
			throw new IOException();
		}
		if (ret != null)
			return ret[0];
		else
			return FTRESP_NOK;
	}

	public int SET_RAIL_POSITION_ZERO() throws IOException {
		// System.out.println("\nSET_RAIL_POSITION_ZERO");
		byte[] payload = null;
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_POSITION_ZERO, COMM_ACTION_WRITE, payload,
					6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_POSITION_ZERO");
			throw new IOException();
		}
		if (ret != null) {
			return FTRESP_OK;
		} else
			return FTRESP_NOK;
	}

	public int SET_RAIL_CONFIG_BACKLASH(double val) throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_BACKLASH");
		byte[] value = UtesArrays.floatToByteArray((float) val);
		byte[] type = { COMM_RAIL_UNITS_STEPS };
		byte[] payload = UtesArrays.concat(type, value);

		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_BACKLASH, COMM_ACTION_WRITE,
					payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_CONFIG_BACKLASH");
			throw new IOException();
		}
		if (ret != null)
			return FTRESP_OK;
		else
			return FTRESP_NOK;
	}

	private double GET_RAIL_CONFIG_SPEED() throws IOException {
		// System.out.println("\nGET_RAIL_CONFIG_SPEED");
		byte[] payload = { COMM_RAIL_UNITS_STEPS };

		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_SPEED, COMM_ACTION_READ, payload, 11);
		} catch (IOException e) {
			System.out.println("^^Exception in GET_RAIL_CONFIG_SPEED");
			throw new IOException();
		}
		if (ret != null) {
			byte[] temp = UtesArrays.extract(ret, 1, 4);
			// System.out.println("FLOAT :"+gb.byteArrayToHexString(temp));
			return UtesArrays.byteArrayToFloat(temp);
		}

		else
			return FTRESP_NOK;
	}

	public int SET_RAIL_CONFIG_SPEED(double val) throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_SPEED");
		byte[] value = UtesArrays.floatToByteArray((float) val);
		byte[] type = { COMM_RAIL_UNITS_STEPS };
		byte[] payload = UtesArrays.concat(type, value);

		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_SPEED, COMM_ACTION_WRITE, payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_CONFIG_SPEED");
			throw new IOException();
		}
		if (ret != null)
			return FTRESP_OK;
		else
			return FTRESP_NOK;
	}

	private double GET_RAIL_CONFIG_RAMP_TIME() throws IOException {
		// System.out.println("\nGET_RAIL_CONFIG_RAMP_TIME");
		byte[] payload = null;

		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_RAMP_TIME, COMM_ACTION_READ,
					payload, 10);
		} catch (IOException e) {
			System.out.println("^^Exception in GET_RAIL_CONFIG_RAMP_TIME");
			throw new IOException();
		}
		if (ret != null) {
			// System.out.println("FLOAT :"+gb.byteArrayToHexString(ret));
			return UtesArrays.byteArrayToFloat(ret);
		}

		else
			return FTRESP_NOK;
	}

	public int SET_RAIL_CONFIG_RAMP_TIME(double val) throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_RAMP_TIME");
		byte[] payload = UtesArrays.floatToByteArray((float) val);

		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_RAMP_TIME, COMM_ACTION_WRITE,
					payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_CONFIG_RAMP_TIME");
			throw new IOException();
		}
		if (ret != null)
			return FTRESP_OK;
		else
			return FTRESP_NOK;
	}

	public int RAIL_MOVE(int dir, double val) throws IOException {
		// System.out.println("\nSET_RAIL_MOVE");
		// System.out.println("RAIL MOVE softval="+gb.stepsToMm(val)+" ofs="+gb.stepsToMm(gb.ofsPOSITION)+" hardval="+gb.stepsToMm(gb.getHardPosition(val)));
		if (Double.compare(val, 0) == 0)
			return -1;
		byte[] value = UtesArrays.floatToByteArray((float) val);
		byte[] type = { (byte) dir, COMM_RAIL_UNITS_STEPS, 1 }; // 1=backlash
																// compensation
		byte[] payload = UtesArrays.concat(type, value);
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_MOVE, COMM_ACTION_WRITE, payload, 6);
		} catch (IOException e1) {
			System.out.println("^^Exception in SET_RAIL_MOVE");
			throw new IOException();
		}
		if (ret != null) {
			// try {
			// Thread.sleep(50);
			// } catch (InterruptedException e) {
			// System.out.println("InterruptedException in RAIL MOVE");
			// }
			// System.out.println("SET_RAIL_MOVE 4");
			// EVALUATE_RAIL_STATUS();
			return FTRESP_OK;
		} else
			return FTRESP_NOK;
	}

	public int RAIL_SHUTTER_FIRE(int nb, double shutterduration,
			double shutterpause) throws IOException {
		// System.out.println("\nSET_RAIL_SHUTTER_FIRE");
		byte[] sd = UtesArrays.floatToByteArray((float) shutterduration);
		// gb.frame.statuslog("sd : "+gb.byteArrayToHexString(sd));
		byte[] sp = UtesArrays.floatToByteArray((float) shutterpause);
		// gb.frame.statuslog("sd : "+gb.byteArrayToHexString(sp));
		byte[] num = UtesArrays.intTo2ByteArrayLittleEndian(nb);
		// gb.frame.statuslog("sd : "+gb.byteArrayToHexString(num));
		byte[] payload = UtesArrays.concat(num, sd, sp);
		// gb.frame.statuslog("PAYLOAD : "+gb.byteArrayToHexString(payload));
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_SHUTTER_FIRE, COMM_ACTION_WRITE, payload, 6);
		} catch (IOException e1) {
			System.out.println("^^Exception in SET_RAIL_SHUTTER_FIRE");
			throw new IOException();
		}
		if (ret != null) {
			// try {
			// Thread.sleep(50);
			// } catch (InterruptedException e) {
			// System.out.println("InterruptedException in SHUTTER FIRE");
			// }
			// System.out.println("SET_RAIL_SHUTTER_FIRE");
			// EVALUATE_RAIL_STATUS();
			return FTRESP_OK;
		} else
			return FTRESP_NOK;
	}

	public double GET_RAIL_POSITION_CURRENT() throws IOException {
		// System.out.println("\nGET_RAIL_POSITION_CURRENT");
		byte[] payload = { COMM_RAIL_UNITS_STEPS };

		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_POSITION_CURRENT, COMM_ACTION_READ,
					payload, 11);
		} catch (IOException e) {
			System.out.println("^^Exception in GET_RAIL_POSITION_CURRENT");
			throw new IOException();
		}
		if (ret != null) {
			byte[] temp = UtesArrays.extract(ret, 1, 4);
			// System.out.println("FLOAT :"+gb.byteArrayToHexString(temp)+" f: "+gb.byteArrayToFloat(temp));
			double pos = UtesArrays.byteArrayToFloat(temp);
			return pos;
		}

		else
			return FTRESP_NOK;
	}

	public int RAIL_STOP() throws IOException {
		// System.out.println("\nSET_RAIL_STOP");
		byte[] payload = null;
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_STOP, COMM_ACTION_WRITE, payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_STOP");
			throw new IOException();
		}
		// System.out.println("\nSET_RAIL_STOP sent");
		if (ret != null) {
			// EVALUATE_RAIL_STATUS();
			return FTRESP_OK;
		} else
			return FTRESP_NOK;
	}

	private int SHUTTER_STOP() throws IOException {
		byte[] sd = UtesArrays.floatToByteArray((float) 0.001);
		byte[] sp = UtesArrays.floatToByteArray((float) 0.001);
		byte[] num = UtesArrays.intTo2ByteArrayLittleEndian(1);
		byte[] payload = UtesArrays.concat(num, sd, sp);
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_SHUTTER_FIRE, COMM_ACTION_WRITE, payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_SHUTTER_STOP");
			throw new IOException();
		}
		if (ret != null) {
			return FTRESP_OK;
		} else
			return FTRESP_NOK;
	}

	private int RAIL_CONFIG_SAVE(int val) throws IOException {
		// System.out.println("\nSET_RAIL_CONFIG_SAVE");
		byte[] payload = { (byte) val };
		byte[] ret;
		try {
			ret = WRITEREAD(CC_RAIL_CONFIG_SAVE, COMM_ACTION_WRITE, payload, 6);
		} catch (IOException e) {
			System.out.println("^^Exception in SET_RAIL_CONFIG_SAVE");
			throw new IOException();
		}
		if (ret != null) {
			return FTRESP_OK;
		} else
			return FTRESP_NOK;
	}

	

}
