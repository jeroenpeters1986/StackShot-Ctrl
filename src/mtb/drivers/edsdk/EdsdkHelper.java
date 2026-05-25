package mtb.drivers.edsdk;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import mtb.swamp.utes.UtesStrings;
import mtb.swamp.utes.UtesTask;
import mtb.swamp.utes.UtesTasksScheduler;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;

import edsdk.EdsDeviceInfo;
import edsdk.EdsDirectoryItemInfo;
import edsdk.Edsdk;
import edsdk.Edsdk.EdsBaseRef;
import edsdk.Edsdk.EdsCameraRef;
import edsdk.Edsdk.EdsDataType;
import edsdk.Edsdk.EdsDirectoryItemRef;
import edsdk.Edsdk.EdsEvfImageRef;
import edsdk.Edsdk.EdsImageQuality;
import edsdk.Edsdk.EdsObjectEventHandler;
import edsdk.Edsdk.EdsStreamRef;

/**
 * Here are some great helpers. _All_ the functions in here are not thread save,
 * so you'll want to encapsulate them in a CanonTask and then send them to the
 * camera, like so for instance :
 * 
 * 
 * canonCamera.executeNow( new CanonTask<Boolean>(){ public void run(){
 * CanonUtils.doSomethingLikeDownloadOrWhatever(); } }
 * 
 * @author hansi
 * 
 */
public class EdsdkHelper implements EdsObjectEventHandler {
	public final static int Av_1 = 0x08;
	public final static int Av_1_1 = 0x0B;
	public final static int Av_1_2 = 0x0C;
	public final static int Av_1_2b = 0x0D;
	public final static int Av_1_4 = 0x10;
	public final static int Av_1_6 = 0x13;
	public final static int Av_1_8 = 0x14;
	public final static int Av_1_8b = 0x15;
	public final static int Av_2 = 0x18;
	public final static int Av_2_2 = 0x1B;
	public final static int Av_2_5 = 0x1C;
	public final static int Av_2_5b = 0x1D;
	public final static int Av_2_8 = 0x20;
	public final static int Av_3_2 = 0x23;
	public final static int Av_3_5 = 0x24;
	public final static int Av_3_5b = 0x25;
	public final static int Av_4 = 0x28;
	public final static int Av_4_5 = 0x2B;
	public final static int Av_4_5b = 0x2C;
	public final static int Av_5_0 = 0x2D;
	public final static int Av_5_6 = 0x30;
	public final static int Av_6_3 = 0x33;
	public final static int Av_6_7 = 0x34;
	public final static int Av_7_1 = 0x35;
	public final static int Av_8 = 0x38;
	public final static int Av_9 = 0x3B;
	public final static int Av_9_5 = 0x3C;
	public final static int Av_10 = 0x3D;
	public final static int Av_11 = 0x40;
	public final static int Av_13 = 0x43;
	public final static int Av_13_b = 0x44;
	public final static int Av_14 = 0x45;
	public final static int Av_16 = 0x48;
	public final static int Av_18 = 0x4B;
	public final static int Av_19 = 0x4C;
	public final static int Av_20 = 0x4D;
	public final static int Av_22 = 0x50;
	public final static int Av_25 = 0x53;
	public final static int Av_27 = 0x54;
	public final static int Av_29 = 0x55;
	public final static int Av_32 = 0x58;
	public final static int Av_36 = 0x5B;
	public final static int Av_38 = 0x5C;
	public final static int Av_40 = 0x5D;
	public final static int Av_45 = 0x60;
	public final static int Av_51 = 0x63;
	public final static int Av_54 = 0x64;
	public final static int Av_57 = 0x65;
	public final static int Av_64 = 0x68;
	public final static int Av_72 = 0x6B;
	public final static int Av_76 = 0x6C;
	public final static int Av_80 = 0x6D;
	public final static int Av_91 = 0x70;
	public final static int Av_invalid = 0xFFFFFFFF;

	public final static int Tv_BULB = 0x0C;
	public final static int Tv_30 = 0x10;
	public final static int Tv_25 = 0x13;
	public final static int Tv_20 = 0x14;
	public final static int Tv_20b = 0x15;
	public final static int Tv_15 = 0x18;
	public final static int Tv_13 = 0x1B;
	public final static int Tv_10 = 0x1C;
	public final static int Tv_10b = 0x1D;
	public final static int Tv_8 = 0x20;
	public final static int Tv_6 = 0x23;
	public final static int Tv_6b = 0x24;
	public final static int Tv_5 = 0x25;
	public final static int Tv_4 = 0x28;
	public final static int Tv_3_2 = 0x2B;
	public final static int Tv_3 = 0x2C;
	public final static int Tv_2_5 = 0x2D;
	public final static int Tv_2 = 0x30;
	public final static int Tv_1_6 = 0x33;
	public final static int Tv_1_5 = 0x34;
	public final static int Tv_1_3 = 0x35;
	public final static int Tv_1 = 0x38;
	public final static int Tv_0_8 = 0x3B;
	public final static int Tv_0_7 = 0x3C;
	public final static int Tv_0_6 = 0x3D;
	public final static int Tv_0_5 = 0x40;
	public final static int Tv_0_4 = 0x43;
	public final static int Tv_0_3 = 0x44;
	public final static int Tv_0_3b = 0x45;
	public final static int Tv_1by4 = 0x48;
	public final static int Tv_1by5 = 0x4B;
	public final static int Tv_1by6 = 0x4C;
	public final static int Tv_1by6b = 0x4D;
	public final static int Tv_1by8 = 0x50;
	public final static int Tv_1by10 = 0x53;
	public final static int Tv_1by10b = 0x54;
	public final static int Tv_1by25 = 0x5D;
	public final static int Tv_1by30 = 0x60;
	public final static int Tv_1by40 = 0x63;
	public final static int Tv_1by45 = 0x64;
	public final static int Tv_1by50 = 0x65;
	public final static int Tv_1by60 = 0x68;
	public final static int Tv_1by80 = 0x6B;
	public final static int Tv_1by90 = 0x6C;
	public final static int Tv_1by100 = 0x6D;
	public final static int Tv_1by125 = 0x70;
	public final static int Tv_1by160 = 0x73;
	public final static int Tv_1by180 = 0x74;
	public final static int Tv_1by200 = 0x75;
	public final static int Tv_1by250 = 0x78;
	public final static int Tv_1by320 = 0x7B;
	public final static int Tv_1by350 = 0x7C;
	public final static int Tv_1by400 = 0x7D;
	public final static int Tv_1by500 = 0x80;
	public final static int Tv_1by640 = 0x83;
	public final static int Tv_1by750 = 0x84;
	public final static int Tv_1by800 = 0x85;
	public final static int Tv_1by1000 = 0x88;
	public final static int Tv_1by1250 = 0x8B;
	public final static int Tv_1by1500 = 0x8C;
	public final static int Tv_1by1600 = 0x8D;
	public final static int Tv_1by2000 = 0x90;
	public final static int Tv_1by2500 = 0x93;
	public final static int Tv_1by3000 = 0x94;
	public final static int Tv_1by3200 = 0x95;
	public final static int Tv_1by4000 = 0x98;
	public final static int Tv_1by5000 = 0x9B;
	public final static int Tv_1by6000 = 0x9C;
	public final static int Tv_1by6400 = 0x9D;
	public final static int Tv_1by8000 = 0xA0;
	public final static int Tv_1by8000_invalid = 0xFFFFFFFF;

	public final static int ISO_6 = 0x28;
	public final static int ISO_12 = 0x30;
	public final static int ISO_25 = 0x38;
	public final static int ISO_50 = 0x40;
	public final static int ISO_100 = 0x48;
	public final static int ISO_125 = 0x4B;
	public final static int ISO_160 = 0x4D;
	public final static int ISO_200 = 0x50;
	public final static int ISO_250 = 0x53;
	public final static int ISO_320 = 0x55;
	public final static int ISO_400 = 0x58;
	public final static int ISO_500 = 0x5B;
	public final static int ISO_640 = 0x5D;
	public final static int ISO_800 = 0x60;
	public final static int ISO_1000 = 0x63;
	public final static int ISO_1250 = 0x65;
	public final static int ISO_1600 = 0x68;
	public final static int ISO_3200 = 0x70;
	public final static int ISO_6400 = 0x78;
	public final static int ISO_12800 = 0x80;
	public final static int ISO_25600 = 0x88;
	public final static int ISO_51200 = 0x90;
	public final static int ISO_102400 = 0x98;
	public final static int ISO_invalid = 0xFFFFFFFF;

	public final static int kEdsSaveTo_Camera = 1;
	public final static int kEdsSaveTo_Host = 2;
	public final static int kEdsSaveTo_Both = 3;

	private static Edsdk lib = null;
	private static boolean initialized = false;
	public final static String DRIVER = "EDSDK";
	public static ImageQuality imageQuality = null;
	private static UtesTasksScheduler taskScheduler = null;

	public class DeviceInfo {
		public String portName;
		public String deviceDescription;
		public String deviceSubType;
	}

	public class CameraRef {
		public EdsCameraRef edsCameraRef;
		public String productName;
		public String keyID;

		public CameraRef(EdsCameraRef ref, String name, String key) {
			this.edsCameraRef = ref;
			this.productName = name;
			this.keyID = key;
		}
	}

	class ImageQuality {
		ArrayList<ImageQualityDesc> list = new ArrayList<ImageQualityDesc>();

		class ImageQualityDesc {
			int edsImageQuality;
			String imageQualityName;

			ImageQualityDesc(int qual, String desc) {
				this.edsImageQuality = qual;
				this.imageQualityName = desc;
			}
		}

		ImageQuality() {
			// PTP Camera
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_LR,
					"RAW"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRLJF,
					"RAW + Large Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRMJF,
					"RAW + Middle Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRSJF,
					"RAW + Small Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRLJN,
					"RAW + Large Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRMJN,
					"RAW + Middle Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRSJN,
					"RAW + Small Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRS1JF,
					"RAW + Small1 Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRS1JN,
					"RAW + Small1 Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRS2JF, "RAW + Small2 Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRS3JF, "RAW + Small3 Jpeg"));

			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_LRLJ,
					"RAW + Large Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRM1J, "RAW + Middle1 Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_LRM2J, "RAW + Middle2 Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_LRSJ,
					"RAW + Small Jpeg"));

			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_MR,
					"SRAW1"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRLJF,
					"SRAW1 + Large Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRMJF,
					"SRAW1 + Middle Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRSJF,
					"SRAW1 + Small Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRLJN,
					"SRAW1 + Large Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRMJN,
					"SRAW1 + Middle Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRSJN,
					"SRAW1 + Small Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRS1JF,
					"SRAW1 + Small1 Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRS1JN,
					"SRAW1 + Small1 Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRS2JF,
					"SRAW1 + Small2 Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRS3JF,
					"SRAW1 + Small3 Jpeg"));

			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_MRLJ,
					"SRAW1 + Large Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRM1J,
					"SRAW1 + Middle1 Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_MRM2J,
					"SRAW1 + Middle2 Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_MRSJ,
					"SRAW1 + Small Jpeg"));

			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_SR,
					"SRAW2"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRLJF,
					"SRAW2 + Large Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRMJF,
					"SRAW2 + Middle Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRSJF,
					"SRAW2 + Small Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRLJN,
					"SRAW2 + Large Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRMJN,
					"SRAW2 + Middle Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRSJN,
					"SRAW2 + Small Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRS1JF,
					"SRAW2 + Small1 Fine Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRS1JN,
					"SRAW2 + Small1 Normal Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRS2JF,
					"SRAW2 + Small2 Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRS3JF,
					"SRAW2 + Small3 Jpeg"));

			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_SRLJ,
					"SRAW2 + Large Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRM1J,
					"SRAW2 + Middle1 Jpeg"));
			list.add(new ImageQualityDesc(
					EdsImageQuality.EdsImageQuality_SRM2J,
					"SRAW2 + Middle2 Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_SRSJ,
					"SRAW2 + Small Jpeg"));

			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_LJF,
					"Large Fine Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_LJN,
					"Large Normal Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_MJF,
					"Middle Fine Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_MJN,
					"Middle Normal Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_SJF,
					"Small Fine Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_SJN,
					"Small Normal Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_S1JF,
					"Small1 Fine Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_S1JN,
					"Small1 Normal Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_S2JF,
					"Small2 Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_S3JF,
					"Small3 Jpeg"));

			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_LJ,
					"Large Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_M1J,
					"Middle1 Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_M2J,
					"Middle2 Jpeg"));
			list.add(new ImageQualityDesc(EdsImageQuality.EdsImageQuality_SJ,
					"Small Jpeg"));

		}

		// get a String[] constructed with an index of an ArrayList
		public String[] listImageQuality() {
			String[] namelist = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {
				namelist[i] = list.get(i).imageQualityName;
			}
			return namelist;
		}

		public int getQualityRefByName(String name) {
			int res = -1;
			for (int i = 0; i < list.size(); i++) {
				if (name.equals(list.get(i).imageQualityName)) {
					res = list.get(i).edsImageQuality;
					break;
				}
			}
			return res;
		}

		public int getQualityIndexByName(String name) {
			int res = -1;
			for (int i = 0; i < list.size(); i++) {
				if (name.equals(list.get(i).imageQualityName)) {
					res = i;
					break;
				}
			}
			return res;
		}

	}

	public EdsdkHelper() throws Exception {

		if (initialized)
			return;

		try {
			System.out.println(Edsdk.JNA_LIBRARY_NAME);
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			// throw new Exception("No JNA library");
		}
		try {
			lib = Edsdk.INSTANCE;
		} catch (NoClassDefFoundError e) {
			System.err.println(e);
			throw new Exception("No ClassDef");
		}

		// TODO
		int result = 0;

		result = lib.EdsInitializeSDK().intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
			throw new Exception("Not initialized");
		}
		if (imageQuality == null)
			imageQuality = new ImageQuality();
		
		taskScheduler = new UtesTasksScheduler();
		initialized = true;
		// System.out.println("EdsInitializeSDK done");
	}

	
	
	public ArrayList<CameraRef> getCameraListTask() {
		return taskScheduler.executeNow(new GetCameraList());
	}
	
	
	public class GetCameraList extends UtesTask<ArrayList<CameraRef>> {

		GetCameraList() {
		}

		@Override
		public void run() {
			setResult(getCameraList());
		}

	}
	
	
	private ArrayList<CameraRef> getCameraList() {
		int result;
		result = lib.EdsTerminateSDK().intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
			initialized = false;
			return null;
		}
		result = lib.EdsInitializeSDK().intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
			initialized = false;
			return null;
		}

		EdsBaseRef[] list = new EdsBaseRef[1];

		result = lib.EdsGetCameraList(list).intValue();
		// System.out.println("EdsGetCameraList done");
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
		}

		NativeLongByReference outRef = new NativeLongByReference();
		result = lib.EdsGetChildCount(list[0], outRef).intValue();
		// System.out.println("EdsGetChildCount done");
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
		}
		System.out.println("Cameras: " + outRef.getValue().longValue());
		int numCams = outRef.getValue().intValue();
		ArrayList<CameraRef> ret = new ArrayList<CameraRef>();
		if (numCams == 0) {
			System.out.println("no camera found");
		}

		for (int i = 0; i < numCams; i++) {
			EdsCameraRef camera[] = new EdsCameraRef[1];

			result = lib.EdsGetChildAtIndex(list[0], new NativeLong(i), camera)
					.intValue();
			// System.out.println("EdsGetChildAtIndex done : " + i);
			if (result != Edsdk.EDS_ERR_OK) {
				System.out.println("Error: " + EdsdkHelper.errName(result));
			}
			openSession(camera[0]);
			String name = getPropertyString(camera[0],
					Edsdk.kEdsPropID_ProductName);
			String key = getPropertyString(camera[0],
					Edsdk.kEdsPropID_CurrentFolder);
			System.out.println("before key:" + key);
			if (key.equals("EDS_ERR_NOT_SUPPORTED")) {
				closeSession(camera[0]);
				openSession(camera[0]);
				key = getPropertyString(camera[0],
						Edsdk.kEdsPropID_CurrentFolder);
				System.out.println("rectified key:" + key);
			}

			ret.add(new CameraRef(camera[0], name, key));
			// System.out.println("\nkEdsPropID_ProductName");
			closeSession(camera[0]);
		}

		result = lib.EdsRelease(list[0]).intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
		}
		return ret;
	}

	public String getPropertyString(CameraRef ref, long property) {
		EdsBaseRef baseref = new EdsBaseRef(ref.edsCameraRef.getPointer());
		return getPropertyString(baseref, property);
	}

	public String getPropertyString(EdsCameraRef ref, long property) {
		EdsBaseRef baseref = new EdsBaseRef(ref.getPointer());
		return getPropertyString(baseref, property);
	}

	public String getPropertyString(EdsBaseRef ref, long property) {
		int result = Edsdk.EDS_ERR_OK;
		IntBuffer dataType = IntBuffer.allocate(1);
		dataType.put(0, EdsDataType.kEdsDataType_Unknown);
		NativeLongByReference dataSize = new NativeLongByReference(
				new NativeLong(0));
		String res = null;

		result = lib.EdsGetPropertySize(ref, new NativeLong(property),
				new NativeLong(0), dataType, dataSize).intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			// System.out.println("Error: " + EdsdkHelper.errName(result));
			res = EdsdkHelper.errName(result);
		}
		// System.out.println("DataSize: " + dataSize.getValue().intValue());

		if (result == Edsdk.EDS_ERR_OK) {
			if (dataType.get(0) == EdsDataType.kEdsDataType_String) {
				Memory str = new Memory(Edsdk.EDS_MAX_NAME);
				str.clear();

				result = lib.EdsGetPropertyData(ref, new NativeLong(property),
						new NativeLong(0), dataSize.getValue(), str).intValue();
				if (result != Edsdk.EDS_ERR_OK) {
					// System.out.println("Error: " +
					// EdsdkHelper.errName(result));
				}
				res = str.getString(0);
			} else {
				System.out.println("not str : " + dataType.get(0));
			}
		}

		// System.out.println("res=" + res);

		return res;
	}

	public int getPropertyNumber(CameraRef ref, long property) {
		EdsBaseRef baseref = new EdsBaseRef(ref.edsCameraRef.getPointer());
		return getPropertyNumber(baseref, property);
	}

	public int getPropertyNumber(EdsCameraRef ref, long property) {
		EdsBaseRef baseref = new EdsBaseRef(ref.getPointer());
		return getPropertyNumber(baseref, property);
	}

	public int getPropertyNumber(EdsBaseRef ref, long property) {
		int result = Edsdk.EDS_ERR_OK;
		IntBuffer dataType = IntBuffer.allocate(1);
		dataType.put(0, EdsDataType.kEdsDataType_Unknown);
		NativeLongByReference dataSize = new NativeLongByReference(
				new NativeLong(0));
		NativeLongByReference res = new NativeLongByReference();

		result = lib.EdsGetPropertySize(ref, new NativeLong(property),
				new NativeLong(0), dataType, dataSize).intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			// System.out.println("Error: " + EdsdkHelper.errName(result));
		}
		// System.out.println("DataSize: " + dataSize.getValue().intValue());

		if (result == Edsdk.EDS_ERR_OK) {
			if (dataType.get(0) == EdsDataType.kEdsDataType_UInt32
					|| dataType.get(0) == EdsDataType.kEdsDataType_Int32) {
				result = lib.EdsGetPropertyData(ref, new NativeLong(property),
						new NativeLong(0), dataSize.getValue(),
						res.getPointer()).intValue();
				if (result != Edsdk.EDS_ERR_OK) {
					// System.out.println("Error: " +
					// EdsdkHelper.errName(result));
				}
			} else {
				System.out.println("not int : " + dataType.get(0));
			}
		}

		// System.out.println("res=" + res.getValue().intValue());

		return res.getValue().intValue();
	}

	public DeviceInfo getDeviceInfo(CameraRef ref) {
		return getDeviceInfo(ref.edsCameraRef);
	}

	public DeviceInfo getDeviceInfo(EdsCameraRef ref) {
		EdsDeviceInfo edsdeviceInfo = new EdsDeviceInfo();
		int result;
		result = lib.EdsGetDeviceInfo(ref, edsdeviceInfo).intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
			edsdeviceInfo = null;
		}
		DeviceInfo deviceInfo = null;
		if (edsdeviceInfo != null) {
			deviceInfo = new DeviceInfo();
			deviceInfo.deviceDescription = UtesStrings
					.CStringtoString(edsdeviceInfo.szDeviceDescription);
			deviceInfo.portName = UtesStrings
					.CStringtoString(edsdeviceInfo.szPortName);
			deviceInfo.deviceSubType = edsdeviceInfo.deviceSubType.intValue() == 1 ? "PTP"
					: "nP";
		}
		return deviceInfo;
	}

	
	
	public int openSessionTask(CameraRef camera) {
		return taskScheduler.executeNow(new OpenSession(camera)).intValue();
	}
	
	
	public class OpenSession extends UtesTask<Integer> {
		private CameraRef cameraRef;

		OpenSession(CameraRef ref) {
			this.cameraRef = ref;
		}

		@Override
		public void run() {
			setResult(openSession(cameraRef.edsCameraRef));
		}

	}

	private int openSession(EdsCameraRef ref) {
		int result = Edsdk.EDS_ERR_OK;
		result = lib.EdsOpenSession(ref).intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + errName(result));
		}

		return result;
	}

	
	
	
	public int closeSessionTask(CameraRef camera) {
		return taskScheduler.executeNow(new CloseSession(camera)).intValue();
	}
	
	public class CloseSession extends UtesTask<Integer> {
		CameraRef cameraRef;

		CloseSession(CameraRef ref) {
			this.cameraRef = ref;
		}

		@Override
		public void run() {
			setResult(closeSession(cameraRef.edsCameraRef));
		}
	}

	private int closeSession(EdsCameraRef ref) {
		int result = Edsdk.EDS_ERR_OK;
		result = lib.EdsCloseSession(ref).intValue();
		if (result != Edsdk.EDS_ERR_OK) {
			System.out.println("Error: " + EdsdkHelper.errName(result));
		}

		return result;
	}

	/**
	 * Tries to find name of an error code.
	 * 
	 * @param errorCode
	 * @return
	 */
	public static String errName(int errorCode) {
		Field[] fields = Edsdk.class.getFields();

		for (Field field : fields) {
			try {
				if (field.getType().toString().equals("int")
						&& field.getInt(Edsdk.class) == errorCode) {
					if (field.getName().startsWith("EDS_")) {
						return field.getName();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "unknown error code";
	}

	public static String propertyIdToString(long property) {
		Field[] fields = Edsdk.class.getFields();

		for (Field field : fields) {
			try {
				if (field.getType().toString().equals("int")
						&& field.getInt(Edsdk.class) == property) {
					if (field.getName().startsWith("kEdsPropID_")) {
						return field.getName();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "unknown error code";
	}

	/**
	 * Finds the size of a class Use only with JNA stuff!
	 */
	public static int sizeof(Object o) {
		int size = 0;
		for (Field field : o.getClass().getDeclaredFields()) {
			Class<?> fieldtype = field.getType();
			if (fieldtype.equals(NativeLong.class)) {
				size += NativeLong.SIZE;
			} else {
				System.out.println("unknown field type: " + field);
			}
			// sofern nur char[] m�glich, keinerlei weitere Pr�fung,
			// ansonsten typenpr�fung anbauen
			// char[] sub =(char[]) field.get(o);
			// size+=sub.length;
		}

		return size;
	}

	/**
	 * Downloads an image and saves it somewhere
	 * 
	 * @param directoryItem
	 *            The item you want to download
	 * @param destination
	 *            A path in the filesystem where you want to save the file. Can
	 *            also be null or a directory. In case of null the temp
	 *            directory will be used, in case of a directory the file name
	 *            of the item will be used.
	 * @param deleteAfterDownload
	 *            Should the image be deleted right after successful download
	 * @return Either null, or the location the file was ultimately saved to on
	 *         success.
	 */
	public File download(EdsDirectoryItemRef directoryItem, File destination,
			boolean deleteAfterDownload) {
		int err = Edsdk.EDS_ERR_OK;
		// EdsStreamRef[] stream = new EdsStreamRef[1];
		EdsBaseRef[] stream = new EdsBaseRef[1];
		EdsDirectoryItemInfo dirItemInfo = new EdsDirectoryItemInfo();

		boolean success = false;

		long timeStart = System.currentTimeMillis();

		err = lib.EdsGetDirectoryItemInfo(directoryItem, dirItemInfo)
				.intValue();
		if (err == Edsdk.EDS_ERR_OK) {
			if (destination == null) {
				destination = new File(System.getProperty("java.io.tmpdir"));
			}
			if (destination.isDirectory()) {
				destination = new File(destination,
						UtesStrings.CStringtoString(dirItemInfo.szFileName));
			}

			destination.getParentFile().mkdirs();

			System.out.println("Downloading image "
					+ UtesStrings.CStringtoString(dirItemInfo.szFileName)
					+ " to " + destination.getAbsolutePath());

			err = lib
					.EdsCreateFileStream(
							ByteBuffer.wrap(Native.toByteArray(destination
									.getAbsolutePath())),
							Edsdk.EdsFileCreateDisposition.kEdsFileCreateDisposition_CreateAlways,
							Edsdk.EdsAccess.kEdsAccess_ReadWrite, stream)
					.intValue();
		}

		if (err == Edsdk.EDS_ERR_OK) {
			err = lib.EdsDownload(directoryItem, dirItemInfo.size, stream[0])
					.intValue();
		}

		if (err == Edsdk.EDS_ERR_OK) {
			System.out.println("Image downloaded in "
					+ (System.currentTimeMillis() - timeStart));

			err = lib.EdsDownloadComplete(directoryItem).intValue();
			if (deleteAfterDownload) {
				System.out.println("Image deleted");
				lib.EdsDeleteDirectoryItem(directoryItem);
			}

			success = true;
		}

		if (stream[0] != null) {
			lib.EdsRelease(stream[0]);
		}

		return success ? destination : null;
	}

	public int setPropertyData(EdsBaseRef ref, long property, long param,
			int size, Pointer data) {
		return lib.EdsSetPropertyData(ref, new NativeLong(property),
				new NativeLong(param), new NativeLong(size), data).intValue();
	}

	public int setPropertyData(CameraRef ref, long property, long param,
			int size, Pointer data) {
		EdsBaseRef baseref = new EdsBaseRef(ref.edsCameraRef.getPointer());
		return setPropertyData(baseref, property, param, size, data);
	}

	public int setPropertyData(EdsBaseRef ref, long property, long value) {
		NativeLongByReference number = new NativeLongByReference(
				new NativeLong(value));
		Pointer data = number.getPointer();

		return setPropertyData(ref, property, 0, NativeLong.SIZE, data);
	}

	public int setPropertyData(CameraRef ref, long property, long value) {
		EdsBaseRef baseref = new EdsBaseRef(ref.edsCameraRef.getPointer());
		return setPropertyData(baseref, property, value);
	}

	public int getPropertyData(EdsBaseRef ref, long property, long param,
			int size, Pointer data) {
		return lib.EdsGetPropertyData(ref, new NativeLong(property),
				new NativeLong(param), new NativeLong(size), data).intValue();
	}

	public int getPropertyData(CameraRef ref, long property, long param,
			int size, Pointer data) {
		EdsBaseRef baseref = new EdsBaseRef(ref.edsCameraRef.getPointer());
		return getPropertyData(baseref, property, param, size, data);
	}

	public int getPropertyData(EdsBaseRef ref, long property) {
		NativeLongByReference number = new NativeLongByReference(
				new NativeLong(1));
		Pointer data = number.getPointer();

		int res = getPropertyData(ref, property, 0, NativeLong.SIZE, data);
		System.out.println("res=" + res);

		return number.getValue().intValue();
	}

	public int getPropertyData(CameraRef ref, long property) {
		EdsBaseRef baseref = new EdsBaseRef(ref.edsCameraRef.getPointer());
		return getPropertyData(baseref, property);
	}

	
	
	
	
	
	
	
	public boolean beginLiveViewTask(CameraRef camera) {
		return taskScheduler.executeNow(new BeginLiveView(camera));
	}
	
	
	public class BeginLiveView extends UtesTask<Boolean> {
		private CameraRef cameraRef;

		BeginLiveView(CameraRef ref) {
			this.cameraRef = ref;
		}

		@Override
		public void run() {
			setResult(beginLiveView(cameraRef));
		}

	}
	
	
	private boolean beginLiveView(CameraRef camera) {
		int err = Edsdk.EDS_ERR_OK;

		NativeLongByReference number = new NativeLongByReference(
				new NativeLong(1));
		Pointer data = number.getPointer();
		err = setPropertyData(camera, Edsdk.kEdsPropID_Evf_Mode, 0,
				NativeLong.SIZE, data);
		if (err != Edsdk.EDS_ERR_OK) {
			System.err.println("Couldn't start live view, error=" + err + ", "
					+ errName(err));
			return false;
		}
		getPropertyData(camera, Edsdk.kEdsPropID_Evf_Mode, 0, NativeLong.SIZE,
				data);
		System.out.println("===" + number.getValue());

		// TODO:delete!

		number = new NativeLongByReference(new NativeLong(
				Edsdk.EdsEvfOutputDevice.kEdsEvfOutputDevice_PC));
		data = number.getPointer();
		err = setPropertyData(camera, Edsdk.kEdsPropID_Evf_OutputDevice, 0,
				NativeLong.SIZE, data);
		if (err != Edsdk.EDS_ERR_OK) {
			System.err.println("Couldn't start live view, error=" + err + ", "
					+ errName(err));
			return false;
		}

		return true;
	}

	
	public boolean endLiveViewTask(CameraRef camera) {
		return taskScheduler.executeNow(new EndLiveView(camera));
	}

	
	public class EndLiveView extends UtesTask<Boolean> {
		private CameraRef cameraRef;

		EndLiveView(CameraRef ref) {
			this.cameraRef = ref;
			taskScheduler.equals(this);
		}

		@Override
		public void run() {
			setResult(endLiveView(cameraRef));
		}

	}
	
	private boolean endLiveView(CameraRef camera) {
		int err = Edsdk.EDS_ERR_OK;

		NativeLongByReference number = new NativeLongByReference(
				new NativeLong(0));
		Pointer data = number.getPointer();
		err = setPropertyData(camera, Edsdk.kEdsPropID_Evf_Mode, 0,
				NativeLong.SIZE, data);
		if (err != Edsdk.EDS_ERR_OK) {
			System.err.println("Couldn't end live view, error=" + err + ", "
					+ errName(err));
			return false;
		}

		number = new NativeLongByReference(new NativeLong(
				Edsdk.EdsEvfOutputDevice.kEdsEvfOutputDevice_TFT));
		data = number.getPointer();
		err = setPropertyData(camera, Edsdk.kEdsPropID_Evf_OutputDevice, 0,
				NativeLong.SIZE, data);
		if (err != Edsdk.EDS_ERR_OK) {
			System.err.println("Couldn't end live view, error=" + err + ", "
					+ errName(err));
			return false;
		}

		return true;
	}

	
	
	public BufferedImage downloadLiveViewImageTask(CameraRef camera) {
		return taskScheduler.executeNow(new DownloadLiveViewImage(camera));
	}
	
	public class DownloadLiveViewImage extends UtesTask<BufferedImage> {
		private CameraRef cameraRef;

		DownloadLiveViewImage(CameraRef ref) {
			this.cameraRef = ref;
		}

		@Override
		public void run() {
			setResult(downloadLiveViewImage(cameraRef));
		}

	}
	private BufferedImage downloadLiveViewImage(CameraRef camera) {
		int err = Edsdk.EDS_ERR_OK;
		// EdsStreamRef stream = NULL;
		// EdsEvfImageRef = NULL;
		EdsStreamRef stream[] = new EdsStreamRef[1];
		EdsEvfImageRef image[] = new EdsEvfImageRef[1];

		// Create memory stream.
		err = lib.EdsCreateMemoryStream(new NativeLong(0), stream).intValue();
		if (err != Edsdk.EDS_ERR_OK) {
			System.err
					.println("Failed to download life view image, memory stream couldn't be created: code="
							+ err + ", " + errName(err));
			release(image[0]);
			release(stream[0]);
			return null;
		}

		err = lib.EdsCreateEvfImageRef(stream[0], image).intValue();
		if (err != Edsdk.EDS_ERR_OK) {
			System.err
					.println("Failed to download life view image, image ref couldn't be created: code="
							+ err + ", " + errName(err));
			release(image[0]);
			release(stream[0]);
			return null;
		}

		// Now try to follow the guidelines from
		// http://tech.groups.yahoo.com/group/CanonSDK/message/1225
		// instead of what the edsdk example has to offer!

		// Download live view image data.
		err = lib.EdsDownloadEvfImage(camera.edsCameraRef, image[0]).intValue();
		if (err != Edsdk.EDS_ERR_OK) {
			if( err != Edsdk.EDS_ERR_OBJECT_NOTREADY) {
				System.err.println("Failed to download life view image, code="
						+ err + ", " + errName(err));
			}
			release(image[0]);
			release(stream[0]);
			return null;
		}
		//
		// // Get the incidental data of the image.
		// NativeLongByReference zoom = new NativeLongByReference();
		// EdsVoid data = new EdsVoid();
		// err = getPropertyData( image[0],
		// CanonSDK.kEdsPropID_Evf_ZoomPosition, 0, NativeLong.SIZE, data );
		// if( err != CanonSDK.EDS_ERR_OK ){
		// System.err.println(
		// "Failed to download life view image, zoom value wasn't read: code=" +
		// err + ", " + toString( err ) );
		// return false;
		// }
		//
		// // Get the focus and zoom border position
		// EdsPoint point = new EdsPoint();
		// data = new EdsVoid( point.getPointer() );
		// err = getPropertyData( image[0],
		// CanonSDK.kEdsPropID_Evf_ZoomPosition, 0 , sizeof( point ), data );
		// if( err != CanonSDK.EDS_ERR_OK ){
		// System.err.println(
		// "Failed to download life view image, focus point wasn't read: code="
		// + err + ", " + toString( err ) );
		// return false;
		// }
		//
		// return true;

		NativeLongByReference length = new NativeLongByReference();
		err = lib.EdsGetLength(stream[0], length).intValue();
		if (err != Edsdk.EDS_ERR_OK) {
			System.err
					.println("Failed to download life view image, failed to read stream length: code="
							+ err + ", " + errName(err));
			release(image[0]);
			release(stream[0]);
			return null;
		}

		PointerByReference ref = new PointerByReference();
		err = lib.EdsGetPointer(stream[0], ref).intValue();

		long address = ref.getPointer().getNativeLong(0).longValue();
		Pointer pp = new Pointer(address);
		byte data[] = pp.getByteArray(0, length.getValue().intValue());
		try {
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
			// System.out.println(img.getWidth() + ",," + img.getHeight());
			return img;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			release(image[0]);
			release(stream[0]);
		}

		return null;
	}

	public static void release(EdsBaseRef ref) {
		lib.EdsRelease(ref);
	}

	public static void release(EdsStreamRef ref) {
		EdsBaseRef baseref = new EdsBaseRef(ref.getPointer());
		lib.EdsRelease(baseref);
	}

	public static void release(EdsEvfImageRef ref) {
		EdsBaseRef baseref = new EdsBaseRef(ref.getPointer());
		lib.EdsRelease(baseref);
	}

	// Object Event Handlers
	private static ArrayList<EdsObjectEventHandler> objectEventHandlers = new ArrayList<EdsObjectEventHandler>(
			10);

	@Override
	public NativeLong apply(NativeLong inEvent, EdsBaseRef inRef,
			Pointer inContext) {
		System.out.println("Event!!!" + inEvent.doubleValue() + ", "
				+ inContext);

		for (EdsObjectEventHandler handler : objectEventHandlers) {
			handler.apply(inEvent, inRef, inContext);
		}

		return new NativeLong(0);
	}

	/**
	 * Adds an object event handler
	 */
	public void addObjectEventHandler(EdsObjectEventHandler handler) {
		objectEventHandlers.add(handler);
	}

	/**
	 * Removes an object event handler
	 */
	public void removeObjectEventHandler(EdsObjectEventHandler handler) {
		objectEventHandlers.remove(handler);
	}

}
